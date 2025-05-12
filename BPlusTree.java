package project2;

import java.io.*;
import java.util.*;

public class BPlusTree implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int maxDegree;
    private Node root;

    public BPlusTree(int degree) {
        if (degree < 3) throw new IllegalArgumentException("Degree must be at least 3.");
        this.maxDegree = degree;
        this.root = new Node(true);
    }

    public static class Node implements Serializable {
        private static final long serialVersionUID = 1L;
        boolean isLeaf;
        List<String> keys;
        List<Node> children;
        List<Integer> values;

        Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            this.children = isLeaf ? null : new ArrayList<>();
            this.values = isLeaf ? new ArrayList<>() : null;
        }
    }

    public Node getRoot() {
        return root;
    }

    public void insert(String key, int value) {
        Node r = root;
        if (r.keys.size() == maxDegree - 1) {
            Node s = new Node(false);
            s.children.add(r);
            splitChild(s, 0, r);
            root = s;
        }
        insertNonFull(root, key, value);
    }

    private void insertNonFull(Node node, String key, int value) {
        int i = Collections.binarySearch(node.keys, key);
        if (i >= 0) return;
        i = -i - 1;

        if (node.isLeaf) {
            node.keys.add(i, key);
            node.values.add(i, value);
        } else {
            Node child = node.children.get(i);
            if (child.keys.size() == maxDegree - 1) {
                splitChild(node, i, child);
                if (key.compareTo(node.keys.get(i)) > 0) i++;
            }
            insertNonFull(node.children.get(i), key, value);
        }
    }

    private void splitChild(Node parent, int index, Node child) {
        int mid = maxDegree / 2;
        Node sibling = new Node(child.isLeaf);

        parent.keys.add(index, child.keys.get(mid));
        parent.children.add(index + 1, sibling);

        sibling.keys.addAll(child.keys.subList(mid + 1, child.keys.size()));
        child.keys.subList(mid, child.keys.size()).clear();

        if (child.isLeaf) {
            sibling.values.addAll(child.values.subList(mid + 1, child.values.size()));
            child.values.subList(mid, child.values.size()).clear();
        } else {
            sibling.children.addAll(child.children.subList(mid + 1, child.children.size()));
            child.children.subList(mid + 1, child.children.size()).clear();
        }
    }

    public Integer search(String key) {
        return search(root, key);
    }

    private Integer search(Node node, String key) {
        int i = Collections.binarySearch(node.keys, key);
        if (node.isLeaf) {
            return i >= 0 ? node.values.get(i) : null;
        } else {
            i = i >= 0 ? i + 1 : -i - 1;
            return search(node.children.get(i), key);
        }
    }
}
