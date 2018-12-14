package com.hwt.newbtree;

import com.hwt.btree.ElemPos;
import org.junit.Assert;
import org.junit.Test;

public class BTreeUtilsTest {
    private int id = 0;
    private int degree = 3;
    @Test
    public void splitTest() {
        BNode<Integer> root = createNode(10, 20);
        root.leaf = false;
        root.setChild(0, createNode(1, 2, 3, 4, 5));
        root.setChild(1, createNode(11, 12));
        root.setChild(2, createNode(21, 22));
        BTreeUtils.splitChildNode(root, 0);
        checkNode(root, 3, 3, 10, 20);
        checkNode(root.getChild(0), 2, 1, 2);
        checkNode(root.getChild(1), 2, 4, 5);
    }

    @Test
    public void mergeToTest() {
        BNode<Integer> node1 = createNode(1, 2);
        BNode<Integer> node2 = createNode(5, 6);
        BTreeUtils.mergeTo(node1, 3, node2);
        checkNode(node1, 5, 1, 2, 3, 5, 6);
    }

    @Test
    public void deleteKeyAndChildTest() {
        BNode<Integer> root = createNode(10, 20, 30, 40, 50);
        root.leaf = false;
        root.setChild(0, createNode(1, 2, 3, 4, 5));
        root.setChild(1, createNode(11, 12));
        root.setChild(2, createNode(21, 22));
        root.setChild(3, createNode(31, 32));
        root.setChild(4, createNode(41, 42));
        root.setChild(5, createNode(51, 52));
        BNode<Integer> child0 = root.getChild(0);
        BNode<Integer> child1 = root.getChild(1);
        BNode<Integer> child2 = root.getChild(2);
        BNode<Integer> child3 = root.getChild(3);
        BNode<Integer> child4 = root.getChild(4);
        BNode<Integer> child5 = root.getChild(5);
        BTreeUtils.deleteKeyAndChild(root, 0);
        checkNode(root, 4, 20, 30, 40, 50);
        Assert.assertEquals("第0个节点应该与之前的第0个节点相同", child0, root.getChild(0));
        Assert.assertEquals("第1个节点应该与之前的第2个节点相同", child2, root.getChild(1));
        Assert.assertEquals("第2个节点应该与之前的第3个节点相同", child3, root.getChild(2));
        Assert.assertEquals("第3个节点应该与之前的第4个节点相同", child4, root.getChild(3));
        Assert.assertEquals("第4个节点应该与之前的第5个节点相同", child5, root.getChild(4));

        BTreeUtils.deleteKeyAndChild(root, 3);
        checkNode(root, 3, 20, 30, 40);
        Assert.assertEquals("第0个节点应该与之前的第0个节点相同", child0, root.getChild(0));
        Assert.assertEquals("第1个节点应该与之前的第2个节点相同", child2, root.getChild(1));
        Assert.assertEquals("第2个节点应该与之前的第3个节点相同", child3, root.getChild(2));
        Assert.assertEquals("第3个节点应该与之前的第4个节点相同", child4, root.getChild(3));

        BTreeUtils.deleteKeyAndChild(root, 1);
        checkNode(root, 2, 20, 40);
        Assert.assertEquals("第0个节点应该与之前的第0个节点相同", child0, root.getChild(0));
        Assert.assertEquals("第1个节点应该与之前的第2个节点相同", child2, root.getChild(1));
        Assert.assertEquals("第2个节点应该与之前的第4个节点相同", child4, root.getChild(2));
    }

    @Test
    public void deleteKeyAndChildTest2() {
        BNode<Integer> root = createNode(10, 20, 30, 40, 50);
        root.leaf = false;
        root.setChild(0, createNode(1, 2, 3, 4, 5));
        root.setChild(1, createNode(11, 12));
        root.setChild(2, createNode(21, 22));
        root.setChild(3, createNode(31, 32));
        root.setChild(4, createNode(41, 42));
        root.setChild(5, createNode(51, 52));
        BNode<Integer> child0 = root.getChild(0);
        BNode<Integer> child1 = root.getChild(1);
        BNode<Integer> child2 = root.getChild(2);
        BNode<Integer> child3 = root.getChild(3);
        BNode<Integer> child4 = root.getChild(4);
        BNode<Integer> child5 = root.getChild(5);
        BTreeUtils.deleteKeyAndChild2(root, 0);
        checkNode(root, 4, 20, 30, 40, 50);
        Assert.assertEquals("第0个节点应该与之前的第1个节点相同", child1, root.getChild(0));
        Assert.assertEquals("第1个节点应该与之前的第2个节点相同", child2, root.getChild(1));
        Assert.assertEquals("第2个节点应该与之前的第3个节点相同", child3, root.getChild(2));
        Assert.assertEquals("第3个节点应该与之前的第4个节点相同", child4, root.getChild(3));
        Assert.assertEquals("第4个节点应该与之前的第5个节点相同", child5, root.getChild(4));

        BTreeUtils.deleteKeyAndChild2(root, 3);
        checkNode(root, 3, 20, 30, 40);
        Assert.assertEquals("第0个节点应该与之前的第1个节点相同", child1, root.getChild(0));
        Assert.assertEquals("第1个节点应该与之前的第2个节点相同", child2, root.getChild(1));
        Assert.assertEquals("第2个节点应该与之前的第3个节点相同", child3, root.getChild(2));
        Assert.assertEquals("第3个节点应该与之前的第5个节点相同", child5, root.getChild(3));

        BTreeUtils.deleteKeyAndChild2(root, 1);
        checkNode(root, 2, 20, 40);
        Assert.assertEquals("第0个节点应该与之前的第1个节点相同", child1, root.getChild(0));
        Assert.assertEquals("第1个节点应该与之前的第3个节点相同", child3, root.getChild(1));
        Assert.assertEquals("第2个节点应该与之前的第5个节点相同", child5, root.getChild(2));
    }

    @Test
    public void insertKeyAndChildTest() {
        BNode<Integer> root = createNode(10, 20);
        root.leaf = false;
        root.setChild(0, createNode(1, 2, 3, 4));
        root.setChild(1, createNode(11, 12));
        root.setChild(2, createNode(21, 22));

        BNode<Integer> child0 = root.getChild(0);
        BNode<Integer> child1 = root.getChild(1);
        BNode<Integer> child2 = root.getChild(2);

        BNode<Integer> child3 = createNode(-1, -2);

        BTreeUtils.insertKeyAndChild(root, 0, 0, child3);
        checkNode(root, 3, 0, 10, 20);
        checkNodeChild(root, child0, child3,  child1, child2);

        BNode<Integer> child4 = createNode(31, 32);

        BTreeUtils.insertKeyAndChild(root, 3, 30, child4);
        checkNode(root, 4, 0, 10, 20, 30);
        checkNodeChild(root, child0, child3, child1, child2, child4);

        BNode<Integer> child5 = createNode(28, 29);
        BTreeUtils.insertKeyAndChild(root, 3, 27, child5);
        checkNode(root, 5, 0, 10, 20, 27, 30);
        checkNodeChild(root, child0, child3, child1, child2, child5, child4);
    }

    @Test
    public void insertKeyToLeafTest() {
        BNode<Integer> node = createNode(10, 11, 14);
        BTreeUtils.insertKeyToLeaf(node, 12);
        checkNode(node, 4, 10 ,11, 12, 14);
        BTreeUtils.insertKeyToLeaf(node, 100);
        checkNode(node, 5, 10 ,11, 12, 14, 100);
    }

    @Test
    public void fillChildNodeTest() {
        BNode<Integer> root = createNode(10, 20, 30);
        root.leaf = false;
        BNode<Integer> child0 = createNode(1, 2);
        BNode<Integer> child1 = createNode(11, 12, 13);
        BNode<Integer> child2 = createNode(21, 22, 23);
        BNode<Integer> child3 = createNode(33, 34);
        root.setChild(0, child0);
        root.setChild(1, child1);
        root.setChild(2, child2);
        root.setChild(3, child3);

        BTreeUtils.fillChildNode(root, 0);
        checkNode(child0, 3, 1, 2, 10);
        checkNode(root, 3, 11, 20, 30);
        checkNode(child1, 2, 12, 13);

        BTreeUtils.fillChildNode(root, 3);
        checkNode(root, 3, 11, 20, 23);
        checkNode(child3, 3, 30 , 33, 34);
        checkNode(child2, 2, 21, 22);
    }

    @Test
    public void fillChildNodeTest2() {
        BNode<Integer> root = createNode(10, 20, 30, 40);
        root.leaf = false;
        BNode<Integer> child0 = createNode(1, 2);
        BNode<Integer> child1 = createNode(11, 12);
        BNode<Integer> child2 = createNode(21, 22);
        BNode<Integer> child3 = createNode(33, 34);
        BNode<Integer> child4 = createNode(41, 42);
        root.setChild(0, child0);
        root.setChild(1, child1);
        root.setChild(2, child2);
        root.setChild(3, child3);
        root.setChild(4, child4);

        BTreeUtils.fillChildNode(root, 0);
        checkNode(root, 3, 20, 30, 40);
        checkNode(child0, 5, 1, 2, 10, 11, 12);
        checkNodeChild(root, child0, child2, child3, child4);

        BTreeUtils.fillChildNode(root, 3);
        checkNode(root, 2, 20, 30);
        checkNode(child3, 5, 33 , 34, 40, 41, 42);
        checkNodeChild(root, child0, child2, child3);
    }




    @Test
    public void insertTest() {
        BNode<Integer> node = createNode(10);
        BTreeUtils.insert(node, 20);
        checkNode(node, 2, 10, 20);
        BTreeUtils.insert(node, 50);
        checkNode(node, 3, 10, 20, 50);
        BTreeUtils.insert(node, 30);
        checkNode(node, 4, 10 ,20, 30, 50);
    }

    @Test
    public void insertTest2() {
        BNode<Integer> root = createNode(10, 20);
        root.leaf = false;
        BNode<Integer> child0 = createNode(1, 2, 3, 4, 9);
        BNode<Integer> child1 = createNode(11, 12);
        BNode<Integer> child2 = createNode(22, 23);

        root.setChild(0, child0);
        root.setChild(1, child1);
        root.setChild(2, child2);

        BTreeUtils.insert(root, 7);
        checkNode(root, 3, 3, 10, 20);
        checkNode(root.getChild(0), 2, 1, 2);
        checkNode(root.getChild(1), 3, 4, 7, 9);
        checkNodeChild(root, child0, root.getChild(1), child1, child2);
    }

    @Test
    public void deleteFromNodeTest() {
        BNode<Integer> root = createNode(10, 20, 30, 50);
        root.leaf = true;
        BTreeUtils.deleteFromNode(root, 0);
        checkNode(root, 3, 20, 30, 50);
        BTreeUtils.deleteFromNode(root, 1);
        checkNode(root, 2, 20, 50);
    }

    @Test
    public void deleteFromNodeTest2() {
        BNode<Integer> root = createNode(10, 20, 30);
        root.leaf = false;
        BNode<Integer> child0 = createNode(1, 2);
        BNode<Integer> child1 = createNode(11, 12, 13);
        BNode<Integer> child2 = createNode(22, 23, 24);
        BNode<Integer> child3 = createNode(31, 32);

        root.setChild(0, child0);
        root.setChild(1, child1);
        root.setChild(2, child2);
        root.setChild(3, child3);

        BTreeUtils.deleteFromNode(root, 0);
        checkNode(root, 3, 11, 20, 30);
        checkNodeChild(root, child0, child1, child2, child3);
        checkNode(child1, 2, 12, 13);

        BTreeUtils.deleteFromNode(root, 2);
        checkNode(root, 3, 11, 20, 24);
        checkNodeChild(root, child0, child1, child2, child3);
        checkNode(child2, 2, 22, 23);
    }

    @Test
    public void deleteFromNodeTest3() {
        BNode<Integer> root = createNode(10, 20, 30);
        root.leaf = false;
        BNode<Integer> child0 = createNode(1, 2);
        BNode<Integer> child1 = createNode(11, 12);
        BNode<Integer> child2 = createNode(22, 23, 24);
        BNode<Integer> child3 = createNode(31, 32);

        root.setChild(0, child0);
        root.setChild(1, child1);
        root.setChild(2, child2);
        root.setChild(3, child3);

        BTreeUtils.deleteFromNode(root, 0);
        checkNode(root, 2, 20, 30);
        checkNodeChild(root, child0, child2, child3);
        checkNode(child0, 4, 1, 2, 11, 12);
    }

    @Test
    public void findTest() {
        BNode<Integer> root = createNode(10, 20, 30, 40);
        KeyPos<Integer> pos = BTreeUtils.find(root, 20);
        Assert.assertEquals("位置应该找到", new KeyPos<>(root, 1), pos);
        pos = BTreeUtils.find(root, 45);
        Assert.assertNull("找不到位置，返回null", pos);

        root.leaf = false;
        BNode<Integer> child0 = createNode(1, 2);
        BNode<Integer> child1 = createNode(11, 12);
        BNode<Integer> child2 = createNode(22, 23, 24);
        BNode<Integer> child3 = createNode(31, 32);
        BNode<Integer> child4 = createNode(41, 42);

        root.setChild(0, child0);
        root.setChild(1, child1);
        root.setChild(2, child2);
        root.setChild(3, child3);
        root.setChild(4, child4);

        pos = BTreeUtils.find(root, 41);
        Assert.assertEquals("位置应该找到", new KeyPos<>(child4, 0), pos);
        pos = BTreeUtils.find(root, 45);
        Assert.assertNull("找不到位置，返回null", pos);
    }



    private void checkNode(BNode<Integer> node, int n, int ...keys) {
        Assert.assertEquals("节点的大小应该为" + n, n, node.n);
        for (int i = 0; i < n; i++) {
            Assert.assertEquals("节点的第" + i + "关键字应该为" + keys[i], keys[i], (int)node.getKey(i));
        }
    }
    @SafeVarargs
    private final void checkNodeChild(BNode<Integer> node, BNode<Integer> ...children) {
        Assert.assertFalse("节点应该为非叶子节点", node.leaf);
        Assert.assertEquals("节点的子节点个数应该为" + children.length, children.length, node.n + 1);
        for (int i = 0; i <= node.n; i++) {
            Assert.assertEquals("第" + i + "个节点应该相同", children[i], node.getChild(i));
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
}
