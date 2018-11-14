package com.hwt.btree;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static com.hwt.btree.BTreeUtil.checkNode;

public class BTreeTest2 {
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
    public void findTest() {
        for (int e : elem) {
            ElemPos pos = tree.find(e);
            assertNotNull(pos);
        }
    }

    @Test
    public void findTest2() {
        ElemPos pos = tree.find(12);
        assertNull(pos);
    }
}