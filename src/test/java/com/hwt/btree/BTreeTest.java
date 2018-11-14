package com.hwt.btree;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static com.hwt.btree.BTreeUtil.checkNode;

public class BTreeTest {
    private BTree tree;

    @Before
    public void before() {
        tree = new BTree(3);
    }

    @Test
    public void addTest() {
        boolean result = tree.add(10);
        checkNode(tree);
        assertTrue(result);
        result = tree.add(20);
        checkNode(tree);
        assertTrue(result);
        result = tree.add(30);
        checkNode(tree);
        assertTrue(result);
        result = tree.add(25);
        checkNode(tree);
        assertTrue(result);
        result = tree.add(100);
        checkNode(tree);
        assertTrue(result);
        result = tree.add(40);
        checkNode(tree);
        assertTrue(result);
        result = tree.add(30);
        checkNode(tree);
        assertFalse(result);
    }
}
