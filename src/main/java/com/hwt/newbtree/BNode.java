package com.hwt.newbtree;


/**
 * B树的节点
 * @param <T>
 */
public class BNode<T extends Comparable<T>> {
    public int n;
    public boolean leaf;
    private T[] keys;
    private BNode<T>[] children;
    private int degree;

    @SuppressWarnings("unchecked")
    public BNode(int degree) {
        this.degree = degree;
        keys = (T[])new Comparable[2 * degree - 1];
        children = (BNode<T>[])new BNode[2 * degree];
    }

    public int getDegree() {
        return degree;
    }

    public BNode<T> getChild(int idx) {
        return children[idx];
    }

    public void setChild(int idx, BNode<T> child) {
        children[idx] = child;
    }

    public T getKey(int idx) {
        return keys[idx];
    }

    public void setKey(int idx, T key) {
        keys[idx] = key;
    }


    public boolean isFull() {
        return n >= (2 * degree - 1);
    }

    public boolean isHungry() {
        return n <= degree - 1;
    }
}
