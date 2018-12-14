package com.hwt.newbtree;

import org.junit.Assert;
import org.junit.Test;

public class BNodeTest {
    @Test
    public void test() {
        BNode<Integer> node = new BNode<>(3);
        node.setKey(0, 1);
        node.setKey(1, 2);
        node.n = 2;
        Assert.assertFalse("测试isFull()", node.isFull());
        Assert.assertTrue("测试isHungry()", node.isHungry());

        node.setKey(2, 3);
        node.setKey(3, 4);
        node.setKey(4, 5);
        node.n = 5;
        Assert.assertTrue("测试isFull()", node.isFull());
        Assert.assertFalse("测试isHungry", node.isHungry());
    }
}
