package com.hwt.btree;

/**
 * 由于B树的元素较多，所以使用这个类储存元素的位置
 */
public class ElemPos {
    BNode node; // 元素所在节点
    int idx; // 元素所在位置
    ElemPos (BNode node, int idx) {
        this.node = node;
        this.idx = idx;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ElemPos) {
            ElemPos another = (ElemPos)obj;
            return another.node == node && another.idx == idx;
        }
        return false;
    }
}
