package com.hwt.newbtree;


/**
 * 由于B树的元素较多，所以使用这个类储存元素的位置
 */
public class KeyPos<T extends Comparable<T>> {
    private BNode<T> node; // 元素所在节点
    private int idx; // 元素所在位置
    KeyPos(BNode<T> node, int idx) {
        this.node = node;
        this.idx = idx;
    }

    public BNode<T> getNode() {
        return node;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KeyPos) {
            KeyPos another = (KeyPos)obj;
            return another.node == node && another.idx == idx;
        }
        return false;
    }
}
