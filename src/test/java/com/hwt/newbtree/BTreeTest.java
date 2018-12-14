package com.hwt.newbtree;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BTreeTest {
    private int degree = 3;
    @Test
    public void insertTest() {
        BNode<Integer> root = createNode(10, 20, 30);
        BTree<Integer> tree = new BTree<>(degree);
        tree.setRoot(root);

        tree.insert(44);
        tree.insert(100);
        tree.insert(200);
        Assert.assertEquals("树应该分裂", tree.getRoot().n, 1);
        tree.insert(2343);
    }

    @Test
    public void insertTest2() {
        BNode<Integer> root = createNode(10, 20, 30);
        BTree<Integer> tree = new BTree<>(degree);
        tree.setRoot(root);
        int count = 100;
        Random random = new Random(47);
        while (count-- > 0) {
            tree.insert(random.nextInt(100000));
        }
    }

    @Test
    public void deleteTest() {
        BTree<Integer> tree = new BTree<>(degree);
        int count = 49;
        Random rand = new Random(47);
        int bound = 10000;
        List<Integer> all = new LinkedList<>();
        int num;
        while (count-- >0) {
            num = rand.nextInt(bound);
            if (tree.insert(num)) {
                checkNode2(tree.getRoot());
                all.add(num);
            }
        }

        System.out.println(all);
        for (int i = 0; i < all.size(); i++) {
            int item = all.get(i);
            if (item == 6207) {
                int a = 1;
            }
            System.out.println("删除关键字【" + item + "】");
            Assert.assertTrue("删除【" + item + "】应该返回true", tree.delete(item));
            BNodePrintUtils.print(tree);
            if (tree.getRoot().n > 0) {
                checkNode2(tree.getRoot());
            }
            for (int j = i + 1; j < all.size(); j++) {
                List<Integer> lostKeys = new LinkedList<>();
                if (tree.find(all.get(j)) == null) {
                    lostKeys.add(all.get(j));
                }
                if (!lostKeys.isEmpty()) {
                    throw new RuntimeException("删除【" + item + "】时，导致【" + lostKeys + "】被删除");
                }
            }
        }
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

    private int[] checkNode2(BNode<Integer> node) {
        int n = node.n;
        for (int i = 0; i < n - 1; i++) {
            if (node.getKey(i) >= node.getKey(i + 1)) {
                throw new RuntimeException("排序不均匀");
            }
        }

        if (node.leaf) {
            return  new int[]{node.getKey(0), node.getKey(n - 1)};
        }
        int min = 0, max = 0;
        for (int i = 0; i <= n; i++) {
            BNode<Integer> child = node.getChild(i);
            int[] range = checkNode2(child);
            if (i == 0) {
                min = range[0];
            }
            if (i == n) {
                max = range[1];
            }
            if (i > 0) {
                if (range[0] <= node.getKey(i - 1)) {
                    throw new RuntimeException("节点的左节点大");
                }
            }
            if (i < n) {
                if (range[1] >= node.getKey(i)) {
                    throw new RuntimeException("节点的左节点大");
                }
            }
        }
        return new int[]{min, max};
    }
}
