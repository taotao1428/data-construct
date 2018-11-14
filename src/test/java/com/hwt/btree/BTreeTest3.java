package com.hwt.btree;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.hwt.btree.BTreeUtil.*;
import static com.hwt.btree.BTreeUtil.createWay;
import static org.junit.Assert.*;

public class BTreeTest3 {
    private BTree tree;
    private int[] elem = new int[]{10, 34, 11, 35, 100, 67, 37, 76, 93, 222};
    @Before
    public void before() {
        tree = new BTree(3);
        for (int e : elem) {
            tree.add(e);
        }
    }

    @Test
    public void deleteTest() {
//        ElemPos pos;
//        pos = findPos(tree, 10);

//        List<ElemPos> way = new LinkedList<>();
//        boolean found = findPosToDelete(findPos(tree, 10).node, way, false);
//        assertTrue(found);

//        List<ElemPos> leftWay = new LinkedList<>();
//        List<ElemPos> rightWay = new LinkedList<>();
//
//        List<ElemPos> bestWay = findPosToDelete2(pos.node.parent, 0, leftWay, rightWay);
//
//        int a = 1;
//        bestWay = findPosToDelete2(tree, idxParent,
//                createWay(node, pos.idx - 1, -1),
//                createWay(node, pos.idx + 1, node.elemLen()));
//        System.out.println(result);
        boolean result;
        for(int e : elem) {
            System.out.println(e);
            result = tree.delete(e);
            checkNode(tree);
            assertTrue(result);
        }
    }
}
