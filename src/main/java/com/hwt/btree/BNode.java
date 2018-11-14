package com.hwt.btree;

import java.util.ArrayList;
import java.util.List;

/**
 * B树的节点
 */
public class BNode {
    List<Integer> elements = new ArrayList<>(); // 用于保存元素（关键字）
    List<BNode> children = new ArrayList<>(); // 用于保存子节点
    BNode parent; // 父级节点
    int order; // 节点的度
    public BNode(int order) {
        this.order = order;
    }

    public BNode(BNode parent, int order) {
        this(order);
        this.parent = parent;
    }

    /**
     * 初始化
     * @param element
     */
    public BNode (int element, BNode parent, int order) {
        this(parent, order);
        elements.add(element);
    }

    /**
     * 获得指定子节点
     * @param idx
     * @return
     */
    public BNode getChild(int idx) {
        return children.get(idx);
    }

    /**
     * 获得指定子节点的位置
     * @param child 子节点
     * @return 子节点的位置
     */
    public int childIdx(BNode child) {
        return children.indexOf(child);
    }

    /**
     * 移除并返回子节点
     * @param idx 子节点的位置
     * @return 被移除的子节点
     */
    public BNode removeChild(int idx) {
        return children.remove(idx);
    }

    /**
     * 获得子节点的个数
     * @return
     */
    public int childrenLen() {
        return children.size();
    }


    /**
     * 获得指定元素
     * @param idx 位置
     * @return
     */
    public int getElem(int idx) {
        return elements.get(idx);
    }

    /**
     * 获得指定元素的位置
     * @param elem 元素
     * @return
     */
    public int elemIdx(int elem) {
        return elements.indexOf(elem);
    }

    /**
     * 获得元素的长度
     * @return
     */
    public int elemLen() {
        return elements.size();
    }


    /**
     * 判断节点是否存在子节点
     * @return
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * 检查节点是否拥有相同元素
     * @param element
     * @return
     */
    public boolean contains(Integer element) {
        return elements.contains(element);
    }

    /**
     * 节点拥有元素的个数
     * @return
     */
    public int size() {
        return elements.size();
    }

    /**
     * 节点添加元素，需要排序
     * @param element
     * @return 返回元素添加的位置
     */
    public int addElement(Integer element) {
        for (int i=0; i<size(); i++) {
            if (element < elements.get(i)){
                elements.add(i, element);
                return i;
            }
        }
        elements.add(element);
        return size() - 1;
    }

    public String toString() {
        return "BNode" + elements.toString();
    }

    public List<Integer> getElements() {
        return elements;
    }

    public List<BNode> getChildren() {
        return children;
    }

    public BNode getParent() {
        return parent;
    }

    public int getOrder() {
        return order;
    }
}
