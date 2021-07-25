package bearmaps.proj2c;

import java.util.ArrayDeque;
import java.util.Queue;

public class TST {
    private TrieNode root;
    private int n;

    private static class TrieNode {
        private char c;
        private TrieNode left, mid, right;
    }

    public TST() {}

    public int getSize() {
        return n;
    }

    public boolean containsKey(String key) {
        return key == null ? false : get(key) != null;
    }

    private TrieNode get(String key) {
        return get(root, key, 0);
    }

    private TrieNode get(TrieNode x, String key, int d) {
        if (x == null) {
            return null;
        }
        char c = key.charAt(d);
        if (c < x.c) {
            return get(x.left, key, d);
        } else if (c > x.c) {
            return get(x.right, key, d);
        } else if (d < key.length() - 1) {
            return get(x.mid, key, d + 1);
        } else {
            return x;
        }
    }

    public void put(String key) {
        // check if key is empty!
        if (key == null || key.length() == 0) return;
        if (! containsKey(key)) {
            n += 1;
        }
        root = put(root, key, 0);
    }

    private TrieNode put(TrieNode x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new TrieNode();
            x.c = c;
        }
        if (c < x.c) {
            x.left = put(x.left, key, d);
        } else if (c > x.c) {
            x.right = put(x.right, key, d);
        } else if (d < key.length() - 1) {
            x.mid = put(x.mid, key, d + 1);
        }
        return x;
    }

    /* prefix matching */
    public Queue<String> keyWithPrefix(String prefix) {
        if (prefix == null) {
            return null;
        }
        Queue<String> res = new ArrayDeque<String>();
        TrieNode n = get(root, prefix, 0);
        if (n == null) {
            return res;
        }
        res.add(prefix);
        collect(root.mid, new StringBuilder(prefix), res);
        return res;
    }

    /* collect all keys in the sub-trie rooted at x with given prefix */
    private void collect(TrieNode x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) {
            return;
        }
        collect(x.left, prefix, queue);
        queue.add(prefix.toString() + x.c);
        collect(x.mid, prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }

}
