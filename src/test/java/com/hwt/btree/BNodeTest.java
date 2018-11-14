package com.hwt.btree;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BNodeTest {
    private BNode node;
    @Before
    public void before() {
        node = new BNode(10);
        node.addElement(20);
        node.children.add(new BNode(11, node, node.order));
        node.children.add(new BNode(12, node, node.order));
    }

    @Test
    public void childLenTest() {
        assertEquals("子节点长度应该为2", node.childrenLen(), 2);
    }

    @Test
    public void elemLenTest() {
        assertEquals("元素的长度应该为1", node.elemLen(), 1);
    }

    @Test
    public void addElement() {
        int pos = node.addElement(19);
        assertElem(node.elements, new int[]{19, 20});
        assertEquals("应该为0", 0, pos);
        pos = node.addElement(15);
        assertElem(node.elements, new int[]{15, 19, 20});
        assertEquals("应该为0", 0, pos);
        pos = node.addElement(50);
        assertElem(node.elements, new int[]{15, 19, 20, 50});
        assertEquals("应该为3", 3, pos);
        node.addElement(35);
        assertElem(node.elements, new int[]{15, 19, 20, 35, 50});
        assertEquals("应该为3", 3, pos);
    }

    private void assertElem(List<Integer> elem, int[] expect) {
        int elemLen = elem.size(), expectLen = expect.length;
        String message = "元素期望为:" + Arrays.toString(expect) + ", 实际为：" + elem.toString();
        if (elemLen != expectLen) {
            throw new RuntimeException(message);
        }
        for (int i = 0; i < elemLen; i++) {
            if (elem.get(i) != expect[i]) {
                throw new RuntimeException(message);
            }
        }
    }
}
