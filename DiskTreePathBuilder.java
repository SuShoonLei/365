package project2;

import java.util.*;

public class DiskTreePathBuilder {
    public static String getPath(BPlusTree tree, String key) {
        List<String> path = new ArrayList<>();
        BPlusTree.Node current = tree.getRoot();

        while (!current.isLeaf) {
            int i = Collections.binarySearch(current.keys, key);
            i = i >= 0 ? i + 1 : -i - 1;
            path.add(String.format("%07d", i));  // Format to 7-digit zero-padded string
            current = current.children.get(i);
        }

        return String.join("", path);  // Flattened path without "/" separator
    }
}
