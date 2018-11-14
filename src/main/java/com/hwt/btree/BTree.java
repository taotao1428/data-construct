package com.hwt.btree;

import java.util.LinkedList;
import java.util.List;

import static com.hwt.btree.BTreeUtil.*;

public class BTree extends BNode {
    BTree(int elem, int order) {
        super(elem, null, order);
    }
    BTree(int order) {
        super(order);
    }

    /**
     * 向树中添加一个元素
     * @param elem
     * @return 添加成功，返回true，否者返回false
     */
    public boolean add(int elem) {
        BNode curNode = this;
        if (curNode.contains(elem)) { return false; } // 如果已经存在，就不添加
        while (curNode.hasChildren()) {
            curNode = findChildToAdd(curNode, elem);
            if (curNode.contains(elem)) { return false; }
        }
        addElemToNode(curNode, elem);
        return true;
    }

    /**
     * 删除树中的某个元素
     * @param elem
     * @return 删除成功返回true，否者返回false
     */
    public boolean delete(int elem) {
        ElemPos pos = findPos(this, elem); // 找到元素的位置
        if (pos == null) { return false; }
        BNode node = pos.node;
        boolean canDelElem = canDelElem(node); // 是否可以删除元素
        List<ElemPos> bestWay;
        if (!node.hasChildren()) { // 如果没有子节点
            if (canDelElem) {
                node.elements.remove(pos.idx);
                return true;
            } else {
                BNode parent = node.parent;
                int idxParent = parent.childIdx(node);
                bestWay = findPosToDelete2(node, idxParent,
                        createWay(node, pos.idx - 1, -1),
                        createWay(node, pos.idx + 1, node.elemLen()));
            }
        } else {
            List<ElemPos> leftWay = new LinkedList<>();
            boolean leftFound = findPosToDelete(node.getChild(pos.idx), leftWay, true);
            if (!leftFound) { // 如果左边为空，则从左边切开查找最优的元素位置
                bestWay = findPosToDelete2(node, pos.idx, leftWay, new LinkedList<>());
                //////////////////////////////////////////////////////////
                // 因为上句代码的问题，可能导致右边的路径在第一个位置会出现起点 //
                // 所以可能导致bestWay中也会多出，所以这里将它删除           //
                //////////////////////////////////////////////////////////
                if (pos.equals(bestWay.get(0))) {
                    bestWay.remove(0);
                }
            } else {
                List<ElemPos> rightWay = new LinkedList<>();
                boolean rightFound = findPosToDelete(node.getChild(pos.idx + 1), rightWay, false);
                if (!rightFound) { // 如果右边为空，直接将leftPos作为最好的位置
                    bestWay = leftWay;
                } else { // 如果两边都不为空，则选择其中更近的位置，作为最优的位置
                    bestWay = leftWay.size() <= rightWay.size() ? leftWay : rightWay;
                }
            }
        }

        int wayLen = bestWay.size();
        if (wayLen > 0) { // 如果路径超过长度大于0，即需要移动元素
            moveElem(pos, bestWay);
            deleteElem(bestWay.get(wayLen - 1));
        } else {
            deleteElem(pos); // 如果不需要移动元素，说明直接删除元素就可以
        }


        return true;
    }

    /**
     * 找到元素对应的节点
     * @param elem 需要找到的元素
     * @return 如果没有找到返回null
     */
    public ElemPos find(int elem) {
        return findPos(this, elem);
    }
}
