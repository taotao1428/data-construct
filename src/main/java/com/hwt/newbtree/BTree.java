package com.hwt.newbtree;

/**
 * B树
 * @param <T> 关键字类型
 */
public class BTree<T extends Comparable<T>> {
    private BNode<T> root;
    private int degree;
    public BTree(int degree) {
        this.degree = degree;
        this.root = new BNode<>(degree);
        this.root.leaf = true;
    }

    public BNode<T> getRoot() {
        return root;
    }

    public void setRoot(BNode<T> root) {
        this.root = root;
    }

    public boolean insert(T key) {
        if (find(key) != null) {
            return false;
        }
        if (root.isFull()) { // 如果根节点满了，分割根节点
            BNode<T> newRoot = new BNode<>(root.getDegree());
            newRoot.n = 0;
            newRoot.setChild(0, root);
            root = newRoot;
            BTreeUtils.splitChildNode(root, 0);
        }
        BTreeUtils.insert(root, key);
        return true;
    }

    /**
     * 在树中搜索关键字的位置
     * @param key 关键字
     * @return 关键字的位置
     */
    public KeyPos<T> find(T key) {
        return BTreeUtils.find(root, key);
    }

    public boolean delete(T key) {
        if (find(key) == null) {
            return false;
        }
        BTreeUtils.delete(root, key);
        if (root.n == 0 && !root.leaf) { // 如果根节点没有关键字，直接使用它的子节点替代它
            root = root.getChild(0);
        }
        return true;
    }
}
