package com.hwt.newbtree;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PrintUtilsTest {

    private int degree = 3;
    @Test
    public void printHeadTest() {
        String head = BNodePrintUtils.createHead(10);
        Assert.assertEquals("头部长度应该为", " /--^---\\ ", head);
    }

    @Test
    public void createStringTest() {
        String str = BNodePrintUtils.createString("aa", 5);
        Assert.assertEquals("生成的字符串应该为", "aaaaaaaaaa", str);
    }

    @Test
    public void mergeChildTest() {
        List<String> child0 = Arrays.asList("abc", "efg");
        List<String> child1 = Arrays.asList("aaaa", "bbbb");
        List<String> child2 = Arrays.asList("bbbbb", "ccccc");
        List<List<String>> children = Arrays.asList(child0, child1, child2);
        List<String> result = Arrays.asList("abcaaaabbbbb", "efgbbbbccccc");
        Assert.assertArrayEquals("应该相等", result.toArray(), BNodePrintUtils.mergeChildString(children).toArray());
    }

    @Test
    public void toStringTest() {
        BNode<Integer> node = createNode(10, 100, 300);
        node.leaf = false;
        node.setChild(0, createNode(-10, -9));
        node.setChild(1, createNode(11, 23, 34));
        node.setChild(2, createNode(101, 102, 103, 104));
        node.setChild(3, createNode(310, 400, 100));
        BNodePrintUtils.print(node);
//        System.out.println();
    }

    private BNode<Integer> createNode(int ...ints) {
        BNode<Integer> node = new BNode<>(degree);
        node.leaf = true;
        for (int i = 0; i < ints.length; i++) {
            node.setKey(i, ints[i]);
        }
        node.n = ints.length;
        return node;
    }
}
