package com.hwt.btree;

import java.util.LinkedList;
import java.util.List;

public class BTreeUtil {
    /**
     * 查找一个子节点用于添加元素
     * @param node
     * @param elem
     * @return
     */
    public static BNode findChildToAdd(BNode node, int elem) {
        int elemLen = node.elemLen();
        for (int i = 0; i < elemLen; i++) {
            if (elem < node.elements.get(i)) {
                return node.children.get(i);
            }
        }
        return node.children.get(elemLen);
    }

    /**
     * 添加元素到叶节点
     * @param node 叶节点
     * @param elem 元素
     */
    public static void addElemToNode(BNode node, int elem) {
        node.addElement(elem);
        if (isOverSize(node)) { // 如果节点过大
            decompose(node);
        }
    }

    /**
     * 如果节点过大，将节点拆开，然后放入父级，如果父级过大，再将父级拆开
     * @param node 需要拆开的节点
     */
    public static void decompose(BNode node) {
        Decompose decompose = Decompose.decompose(node);
        if (isRoot(node)) { // 根节点拆开之后，依然放在根节点中
            node.elements.clear();
            node.children.clear();
            addDecomposeToParent(node, decompose, 0);
        } else {
            BNode parent = node.parent;
            int idxInParent = parent.childIdx(node);
            parent.removeChild(idxInParent); // 先将节点从父节点移除再添加新的节点
            addDecomposeToParent(parent, decompose, idxInParent);
            if (isOverSize(parent)) {
                decompose(parent);
            }
        }
    }

    /**
     * 将分解后的节点与元素，添加到父节点中
     * @param parent
     * @param decompose
     * @param idx
     */
    private static void addDecomposeToParent(BNode parent, Decompose decompose, int idx) {
        parent.elements.add(idx, decompose.element);
        decompose.minNode.parent = parent;
        decompose.maxNode.parent = parent;
        parent.children.add(idx, decompose.minNode);
        parent.children.add(idx + 1, decompose.maxNode);
    }

    public static boolean isRoot(BNode node) {
        return node instanceof BTree;
    }

    /**
     * 检查节点是否过大
     * @param node
     * @return
     */
    public static boolean isOverSize(BNode node) {
        return node.elemLen() >= node.order;
    }

    /**
     * 将一个已经满了的节点与需要的元素，分解成 minElements, midElement, maxElements;
     * 然后由minElements 组成节点minNode，maxElements组成节点maxNode
     */
    private static class Decompose{
        BNode minNode;
        int element;
        BNode maxNode;
        Decompose(BNode minNode, int element, BNode maxNode) {
            this.minNode = minNode;
            this.element = element;
            this.maxNode = maxNode;
        }

        /**
         * 将一个过大的节点从中间分开，使成为两个节点
         * @param node
         * @return
         */
        public static Decompose decompose(BNode node) {
            int elemLen = node.elemLen();
            int mid = elemLen / 2;
            BNode minNode = new BNode(node.order), maxNode = new BNode(node.order), child;
            minNode.elements.addAll(node.elements.subList(0, mid));
            maxNode.elements.addAll(node.elements.subList(mid + 1, elemLen));
            if (node.hasChildren()) {
                for (int i = 0; i <= mid; i++) {
                    child = node.children.get(i);
                    child.parent = minNode;
                    minNode.children.add(child);
                }
                for (int i = mid+1; i <= elemLen; i++) {
                    child = node.children.get(i);
                    child.parent = maxNode;
                    maxNode.children.add(child);
                }
            }
            return new Decompose(minNode, node.elements.get(mid), maxNode);
        }
    }

    /**
     * 找到元素所在的位置
     * @param node 搜索的节点
     * @param elem 元素
     * @return 如果没有找到返回null
     */
    public static ElemPos findPos(BNode node, int elem) {
        int elemLen = node.elemLen();
        int val;
        if (node.hasChildren()) {
            for (int i = 0; i < elemLen; i++) {
                val = node.elements.get(i);
                if (val == elem) {
                    return new ElemPos(node, i);
                } else if (elem < val) {
                    return findPos(node.children.get(i), elem);
                }
            }
            return findPos(node.children.get(elemLen), elem);
        } else {
            for (int i = 0; i < elemLen; i++) {
                val = node.elements.get(i);
                if (val == elem) {
                    return new ElemPos(node, i);
                }
            }
            return null;
        }
    }

    /**
     * 判断节点中元素是否可以被删除
     * @param node 节点是否可以被删除
     * @return
     */
    public static boolean canDelElem(BNode node) {
        return isRoot(node) || node.elemLen() > ((node.order + 1) / 2 - 1);
    }

    /**
     * 用于不断向父级搜索可删除元素位置
     * @param node 当前搜索的节点
     * @param divide 子节点的位置，用于将当前节点分开
     * @param leftWay 左边的搜索路径
     * @param rightWay 右边的搜索路径
     * @return 最短的路径
     */
    public static List<ElemPos> findPosToDelete2(BNode node, int divide,
                                                 List<ElemPos> leftWay, List<ElemPos> rightWay) {
        int childLen = node.childrenLen();
        boolean canDelElem = canDelElem(node); // 是否元素可以删除
        boolean leftFound = false, rightFound = false;
        List<ElemPos> childWay = new LinkedList<>();

        if (divide > 0) {
            int i = divide - 1;
            leftWay.add(new ElemPos(node, i));
            leftFound = findPosToDelete(node.getChild(i), childWay, true);
            if (!leftFound) {
                if (canDelElem) {
                    leftFound = true;
                } else {
                    leftWay.addAll(childWay);
                    while (!leftFound && --i >= 0) {
                        leftWay.add(new ElemPos(node, i));
                        leftFound = findPosToDelete(node.getChild(i), leftWay, true);
                    }
                }
            } else {
                leftWay.addAll(childWay);
            }
        }
        childWay.clear(); // 上面可能使用了childWay，将其清空
        if (divide < (childLen - 1)) {
            int i = divide + 1;
            rightWay.add(new ElemPos(node, i - 1));
            rightFound = findPosToDelete(node.getChild(i), childWay, false);
            if (!rightFound) {
                if (canDelElem) {
                    rightFound = true;
                } else {
                    rightWay.addAll(childWay);
                    while (!rightFound && ++i < childLen) {
                        rightWay.add(new ElemPos(node, i - 1));
                        rightFound = findPosToDelete(node.getChild(i), rightWay, false);
                    }
                }
            } else {
                rightWay.addAll(childWay);
            }
        }

        // 如果找到一个元素可以删除，在leftWay或rightWay中选择一个较短路径返回
        if (leftFound) {
            if (!rightFound) { return leftWay; }
            return leftWay.size() <= rightWay.size() ? leftWay : rightWay;
        } else if (rightFound) {
            return rightWay;
        }

        // 如果没有找到，再向父级搜索
        BNode parent = node.parent;
        int idxInParent = parent.childIdx(node);
        return findPosToDelete2(parent, idxInParent, leftWay, rightWay);
    }

    /**
     * 从节点以及子节点中寻找一个可以删除的元素
     * @param node 节点
     * @param way 到达该节点的路径，搜索的路径会继续添加到该节点
     * @param isLeft 搜索的方向
     * @return 是否找到一个可以删除的元素
     */
    public static boolean findPosToDelete(BNode node, List<ElemPos> way, boolean isLeft) {
        int childLen = node.childrenLen(), elemLen = node.elemLen();
        boolean canDelElem = canDelElem(node);
        if (childLen == 0) { // 叶节点
            if (canDelElem) { // 如果可以删除
                way.add(new ElemPos(node, isLeft ? elemLen - 1 : 0));
                return true;
            }
            if (isLeft) {
                way.addAll(createWay(node, elemLen - 1, -1));
            } else {
                way.addAll(createWay(node, 0, elemLen));
            }
            return false;
        }
        // 如果不是叶节点，先搜索子节点
        List<ElemPos> childWay = new LinkedList<>();
        boolean found;
        if (isLeft) {
            found = findPosToDelete(node.getChild(childLen - 1), way, isLeft);
            if (found) { return true; }
            way.add(new ElemPos(node, elemLen - 1));
            found = findPosToDelete(node.getChild(childLen - 2), childWay, isLeft);
            if (found) { // 如果在倒数第二个子节点找到
                way.addAll(childWay);
                return true;
            }
            if (canDelElem) { // 如果在倒数第一个子节点与倒数第二个子节点都没有找到，并该节点可以删除元素
                return true;
            }
            way.addAll(childWay);
            for (int i = childLen - 3; i >= 0; i--) {
                way.add(new ElemPos(node, i));
                found = findPosToDelete(node.getChild(i), way, isLeft);
                if (found) {
                    return true;
                }
            }
        } else {
            found = findPosToDelete(node.getChild(0), way, isLeft);
            if (found) { return true; }
            way.add(new ElemPos(node, 0));
            found = findPosToDelete(node.getChild(1), childWay, isLeft);
            if (found) { // 如果在第二个子节点找到
                way.addAll(childWay);
                return true;
            }
            if (canDelElem) { // 如果在第一个子节点与第二个子节点都没有找到，并该节点可以删除元素
                return true;
            }
            way.addAll(childWay);
            for (int i = 2; i < childLen; i++) {
                way.add(new ElemPos(node, i - 1));
                found = findPosToDelete(node.getChild(i), way, isLeft);
                if (found) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 创建一个路径，主要是为叶节点创建路径。由于路径的可能是向左或则向右，所以start可能比end大
     * @param node 叶节点
     * @param start 起始元素位置
     * @param end 结束元素的位置（不包含）
     * @return
     */
    public static List<ElemPos> createWay(BNode node, int start, int end) {
        List<ElemPos> way = new LinkedList<>();
        if (start < end) {
            for (int i = start; i < end; i++) {
                way.add(new ElemPos(node, i));
            }
        } else if (start > end) {
            for (int i = start; i > end; i--) {
                way.add(new ElemPos(node, i));
            }
        }
        return way;
    }

    /**
     * 将way中的点向startPos移动一个位置
     * @param startPos 起点
     * @param way 整条路径
     */
    public static void moveElem(ElemPos startPos, List<ElemPos> way) {
        ElemPos curPos = startPos;
        for (ElemPos pos : way) {
            curPos.node.elements.set(curPos.idx, pos.node.elements.get(pos.idx));
            curPos = pos;
        }
    }

    /**
     * 通过聚合元素的两个子节点的方式达到删除元素的目的
     * @param pos 元素位置
     */
    public static void deleteElem(ElemPos pos) {
        BNode node = pos.node;
        int idx = pos.idx;
        node.elements.remove(idx); // 先将元素删除
        if (node.hasChildren()) { // 如果元素有子节点，聚合元素左右两个节点
            mergeTo(node.removeChild(idx + 1), node.getChild(idx)); // 将两个分支合并
            if (isRoot(node)) { // 如果是根节点
                removeRootIfEmpty(node);
            }
        }
    }

    /**
     * 将other节点聚合到main节点上
     * @param other 被聚合的节点
     * @param main
     */
    public static void mergeTo(BNode other, BNode main) {
        main.elements.addAll(other.elements); // 将other的元素先添加到main中
        int mainLen = main.childrenLen(), otherLen = other.childrenLen();
        if (mainLen > 0 && otherLen > 0) { // 如果还有子节点
            mergeTo(other.getChild(0), main.getChild(mainLen - 1)); // 将other的第一个子节点添加到main的最后一个子节点上
            BNode child;
            for (int i = 1; i < otherLen; i++) { // 将other其余的子节点添加到main中
                child = other.getChild(i);
                child.parent = main; // 修改父级节点
                main.children.add(child);
            }
        } else if (mainLen != 0 || otherLen != 0) {
            throw new RuntimeException("两个merge的节点深度不一样");
        }
    }

    /**
     * 由于删除元素的原因，根节点可能为空；如果根节点为空，将其清除
     * @param root
     */
    public static void removeRootIfEmpty(BNode root) {
        int elemLen = root.elemLen();
        if (elemLen == 0) { // 如果为空
            BNode child = root.removeChild(0); // 将根节点的的唯一子节点的元素以及子节点放在根节点中
            root.elements.addAll(child.elements);
            for (BNode node : child.children) {
                node.parent = root;
                root.children.add(node);
            }
        }
    }

    /**
     * 检查BTree是否结构合理
     * @param node
     * @return 节点的深度
     */
    public static int checkNode(BNode node) {
        int elemLen = node.elemLen();
        int childLen = node.childrenLen();
        boolean isRoot = isRoot(node);
        if (elemLen >= node.order) {
            throw new IllegalStateException("节点的元素个数过大");
        }
        if (elemLen < (node.order + 1) / 2 - 1 && !isRoot) {
            throw new IllegalStateException("节点的元素过少");
        }

        if (childLen == 0) { // 如果没有子节点，直接返回
            return 1;
        }

        if (elemLen != childLen - 1) {
            throw new IllegalStateException("节点的子节点数与元素数量不匹配");
        }

        if (elemLen == 0 && childLen == 1) {
            throw new IllegalStateException("不应该出现空节点");
        }

        int deep = 0;
        boolean isFirst = true;
        for (BNode child : node.children) {
            if (child.parent != node) {
                throw new RuntimeException("子节点的parent不正确");
            }
            if (isFirst) {
                deep = checkNode(child);
                isFirst = false;
            } else if(deep != checkNode(child)) {
                throw new IllegalStateException("节点的子节点深度不一样");
            }
        }
        return deep + 1;
    }
}
