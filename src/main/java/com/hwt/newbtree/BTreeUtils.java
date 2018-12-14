package com.hwt.newbtree;

public class BTreeUtils {
    public static <T extends Comparable<T>> KeyPos<T> find(BNode<T> node, T key) {
        int n = node.n;
        int result, i;
        for (i = 0; i < n; i++) {
            result = key.compareTo(node.getKey(i));
            if (result == 0) {
                return new KeyPos<>(node, i);
            } else if (result < 0) {
                break;
            }
        }
        if (node.leaf) {
            return null;
        }
        return find(node.getChild(i), key);
    }

    public static <T extends Comparable<T>> void insert(BNode<T> node, T key) {
        int n = node.n;
        int result, i;
        if (node.leaf) { // 如果是叶节点，直接插入
            insertKeyToLeaf(node, key);
        } else {
            for (i = 0; i < n; i++) {
                result = key.compareTo(node.getKey(i));
                if (result == 0) {
                    throw new IllegalStateException("节点中存在相同的元素【" + key + "】，无法插入");
                } if (result < 0) {
                    break;
                }
            }
            BNode<T> child = node.getChild(i);
            if (child.isFull()) { // 如果子节点满了，将其分割成两个节点
                splitChildNode(node, i);
                if (key.compareTo(node.getKey(i)) > 0) { // 如果大于当前第i个关键字
                    i++;
                }
            }
            insert(node.getChild(i), key);
        }
    }

    public static <T extends Comparable<T>> void splitChildNode(BNode<T> node, int idx) {
        BNode<T> child = node.getChild(idx);
        if (!child.isFull()) {
            throw new IllegalArgumentException("节点node的子节点非满的，所以不需要分离");
        }
        int degree = node.getDegree();
        // 填充新的子节点
        BNode<T> newNode = new BNode<>(node.getDegree());
        if (node.leaf) {
            for (int i = 2 * degree - 2; i >= degree; i--) {
                newNode.setKey(i - degree, child.getKey(i));
            }
        } else {
            for (int i = 2 * degree - 2; i >= degree; i--) {
                newNode.setKey(i - degree, child.getKey(i));
                newNode.setChild(i - degree + 1, child.getChild(i + 1));
            }
            newNode.setChild(0, child.getChild(degree));
        }
        newNode.n = degree - 1;
        newNode.leaf = child.leaf;

        // 用于插入到node中的key
        T key = child.getKey(degree - 1);

        // 更新child
        child.n = degree - 1;

        // 将子女的子节点放在父节点上
        insertKeyAndChild(node, idx, key, newNode);
    }

    public static <T extends Comparable<T>> void delete(BNode<T> node, T key) {
        int n = node.n, i, result;
        for (i = 0; i < n; i++) {
            result = key.compareTo(node.getKey(i));
            if (result == 0) {
                deleteFromNode(node, i);
                return;
            } else if (result < 0) {
                break;
            }
        }
        if (node.leaf) { // 如果是叶节点，没有子节点可以搜索
            return;
        }
        BNode<T> child = node.getChild(i);
        if (child.isHungry()) {
            fillChildNode(node, i);
        }
        if (i == node.n + 1) { // 可能由于子节点合并，最右边子节点被合并到左边的兄弟节点中
            i = node.n;
        }
        delete(node.getChild(i), key);
    }

    public static <T extends Comparable<T>> void deleteFromNode(BNode<T> node, int idx) {
        int n = node.n, i;
        T key = node.getKey(idx);
        if (node.leaf) {
            for (i = idx; i < (n - 1); i++) {
                node.setKey(i, node.getKey(i + 1));
            }
            node.n--;
        } else {
            BNode<T> leftChild = node.getChild(idx);
            if (!leftChild.isHungry()) {
                // 找到在左子节点的后驱关键字，然后交换
                BNode<T> leafChild = leftChild;
                while (!leafChild.leaf) {
                    leafChild = leafChild.getChild(leafChild.n);
                }
                node.setKey(idx, leafChild.getKey(leafChild.n - 1));
                leafChild.setKey(leafChild.n - 1, key);
                delete(leftChild, key); // 再从左子节点删除
                return;
            }
            BNode<T> rightChild = node.getChild(idx + 1);
            if (!rightChild.isHungry()) {
                // 找到在右子节点的前驱关键字，然后交换
                BNode<T> leafChild = rightChild;
                while (!leafChild.leaf) {
                    leafChild = leafChild.getChild(0);
                }
                node.setKey(idx, leafChild.getKey(0));
                leafChild.setKey(0, key);
                delete(rightChild, key); // 再从右子节点删除
                return;
            }
            // 将关键字与右子节点删除
            deleteKeyAndChild(node, idx);
            int leftChildN = leftChild.n; // 也是合并之后关键字在左子节点的位置
            // 将关键字与右子节点合并到左子节点中
            mergeTo(leftChild, key, rightChild);
            deleteFromNode(leftChild, leftChildN);
        }
    }

    public static <T extends Comparable<T>> void mergeTo(BNode<T> node, T key, BNode<T> other) {
        int n = node.n;
        int otherN = other.n;
        node.n = n + otherN + 1;
        node.setKey(n, key);
        for (int i = 0; i < otherN; i++) {
            node.setKey(n + i + 1, other.getKey(i));
            node.setChild(n + i + 1, other.getChild(i));
        }
        node.setChild(n + otherN + 1, other.getChild(otherN));
    }


    public static <T extends Comparable<T>> void fillChildNode(BNode<T> node, int idx) {
        BNode<T> child = node.getChild(idx);
        BNode<T> leftChild, rightChild;
        if (idx > 0) {
            leftChild = node.getChild(idx - 1);
            if (!leftChild.isHungry()) { // 将左子节点的最右子节点放到需要填充的子节点中
                insertKeyAndChild2(child, 0, node.getKey(idx - 1), leftChild.getChild(leftChild.n));
                node.setKey(idx - 1, leftChild.getKey(leftChild.n - 1));
                leftChild.n--;
                return;
            }
        }
        if (idx < node.n) {
            rightChild = node.getChild(idx + 1);
            if (!rightChild.isHungry()) {
                child.n++;
                child.setKey(child.n - 1, node.getKey(idx));
                child.setChild(child.n, rightChild.getChild(0));
                node.setKey(idx, rightChild.getKey(0));
                deleteKeyAndChild2(rightChild, 0);
                return;
            }
        }

        if (idx < node.n) {
            rightChild = node.getChild(idx + 1);
            mergeTo(child, node.getKey(idx), rightChild); // 将右节点合并到左节点中
            deleteKeyAndChild(node, idx);
        } else {
            leftChild = node.getChild(idx - 1);
            mergeTo(leftChild, node.getKey(node.n - 1), child); // 说明是最右子节点，只能合并到左节点了
            node.n--;
        }
    }

    public static <T extends Comparable<T>> void deleteKeyAndChild(BNode<T> node, int idx) {
        int n = node.n;
        for (int i = idx; i < n - 1; i++) {
            node.setKey(i, node.getKey(i + 1));
            node.setChild(i + 1, node.getChild(i + 2));
        }
        node.n--;
    }

    public static <T extends Comparable<T>> void deleteKeyAndChild2(BNode<T> node, int idx) {
        int n = node.n;
        for (int i = idx; i < n - 1; i++) {
            node.setKey(i, node.getKey(i + 1));
            node.setChild(i, node.getChild(i + 1));
        }
        node.setChild(n - 1, node.getChild(n));
        node.n--;
    }

    public static <T extends Comparable<T>> void insertKeyAndChild(BNode<T> node, int idx, T key, BNode<T> child) {
        node.n++;
        int n = node.n;
        for (int i = n - 1; i > idx; i--) {
            node.setKey(i, node.getKey(i - 1));
            node.setChild(i + 1, node.getChild(i));
        }
        node.setKey(idx, key);
        node.setChild(idx + 1, child);
    }

    public static <T extends Comparable<T>> void insertKeyAndChild2(BNode<T> node, int idx, T key, BNode<T> child) {
        node.n++;
        int n = node.n;
        for (int i = n - 1; i > idx; i--) {
            node.setKey(i, node.getKey(i - 1));
            node.setChild(i + 1, node.getChild(i));
        }
        node.setChild(idx + 1, node.getChild(idx));
        node.setKey(idx, key);
        node.setChild(idx, child);
    }


    public static <T extends Comparable<T>> void insertKeyToLeaf(BNode<T> node, T key) {
        if (!node.leaf) {
            throw new IllegalArgumentException("node不为叶节点");
        }
        int i = 0, result, j;
        // 找到插入的位置，并检查是否存在相同的关键字
        for (; i < node.n; i++) {
            result = key.compareTo(node.getKey(i));
            if (result < 0) {
                break;
            } else if (result == 0) {
                throw new IllegalArgumentException("节点node中已存在关键字【" + key + "】");
            }
        }
        node.n++;
        for (j = node.n - 1; j > i; j--) {
            node.setKey(j, node.getKey(j - 1));
        }
        node.setKey(i, key);
    }
}
