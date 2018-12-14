package com.hwt.newbtree;

import java.util.LinkedList;
import java.util.List;

public class BNodePrintUtils {
    public static void print(BTree<?> tree) {
        BNode<?> root = tree.getRoot();
        if (root.n == 0) {
            System.out.println("[emptyTree]");
        } else {
            print(root);
        }
    }
    public static void print(BNode<?> node) {
        List<String> strings = toString(node);
        for (String item : strings) {
            System.out.println(item);
        }
    }

    public static List<String> toString(BNode<?> node) {
        List<String> lines = new LinkedList<>();
        if (node.leaf) {
            if (node.n == 0) {
                lines.add("[empty]");
            } else {
                StringBuilder sb = new StringBuilder(" ");
                for (int i = 0; i < node.n; i++) {
                    sb.append(node.getKey(i));
                    sb.append(" ");
                }
                ensureSize(sb);
                int len = sb.length();
                lines.add(createHead(len));
                lines.add(sb.toString());
            }
        } else {
            List<List<String>> childString = new LinkedList<>();
            int n = node.n;
            for (int i = 0; i <= n; i++) {
                childString.add(toString(node.getChild(i)));
            }
            List<String> mergedChildString = mergeChildString(childString);
            lines.addAll(mergedChildString);
            String items = "";
            int count = 0;
            for (int i = 0; i < n; i++) {
                List<String> childList = childString.get(i);
                int l = childList.get(0).length();
                count += l;
                String key = node.getKey(i).toString();
                items += createString(" ", count - items.length() - (key.length()) / 2);
                items += key;
            }
            int firstLen = childString.get(0).get(0).length();
            int lastLen = childString.get(n).get(0).length();
            count += lastLen;
            items += createString(" ", count - items.length());
            lines.add(0, items);
            String head;
            if (n == 1) {
                head = createString(" ", firstLen - 2) + createHead(5)
                        + createString(" ", lastLen - 3);
            } else {
                head = createString(" ", firstLen) + createHead(count - firstLen - lastLen)
                        + createString(" ", lastLen);
            }

            lines.add(0, head);
        }

        return lines;
    }

    static String createHead(int len) {
        StringBuilder sb = new StringBuilder(" /");
        int size = (len - 5) / 2;
        sb.append(createString("-", size));
        sb.append("^");
        sb.append(createString("-", len - 5 - size));
        sb.append("\\ ");
        return sb.toString();
    }

    static String createString(String item, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(item);
        }
        return sb.toString();
    }

    static void ensureSize(StringBuilder sb) {
        int len = sb.length();
        if (len < 5) {
            int size = (5 - len) / 2;
            sb.insert(0, createString(" ", size));
            sb.append(createString(" ", 5 - len - size));
        }
    }

    static List<String> mergeChildString(List<List<String>> childString) {
        int deep = childString.get(0).size();
        List<String> merged = new LinkedList<>();
        for (int i = 0; i < deep; i++) {
            StringBuilder sb = new StringBuilder();
            for (List<String> child : childString) {
                sb.append(child.get(i));
            }
            merged.add(sb.toString());
        }
        return merged;
    }
}
