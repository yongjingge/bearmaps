package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.List;

/**
 * TST
 * a Ternary Search Trie implementation
 */
public class TST implements TrieSet61B {
    private TrieNode root;
    private int n;

    private static class TrieNode {
        private char c;
        private TrieNode left, mid, right;
    }

    public TST() {
        this.clear();
    }

    @Override
    public void clear() {
        root = new TrieNode();
        n = 0;
    }

    /* number of strings in the trie */
    public int getSize() {
        return n;
    }

    @Override
    public boolean contains(String key) {
        return key != null && get(key) != null;
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
        } else { // if c == x.c && d == key.length()-1
            return x;
        }
    }

    @Override
    public void add(String key) {
        // check if key is empty!
        if (key == null || key.length() == 0) return;
        if (! contains(key)) {
            n += 1;
        }
        root = add(root, key, 0);
    }

    private TrieNode add(TrieNode x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new TrieNode();
            x.c = c;
        }
        if (c < x.c) {
            x.left = add(x.left, key, d);
        } else if (c > x.c) {
            x.right = add(x.right, key, d);
        } else if (d < key.length() - 1) {
            x.mid = add(x.mid, key, d + 1);
        }
        return x;
    }

    /* prefix matching */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            return null;
        }
        List<String> res = new ArrayList<>();
        TrieNode n = get(root, prefix, 0);
        if (n == null) {
            return res;
        }
        res.add(prefix);
        collect(root.mid, new StringBuilder(prefix), res);
        return res;
    }

    /* collect all keys in the sub-trie rooted at x with given prefix */
    private void collect(TrieNode x, StringBuilder prefix, List<String> res) {
        if (x == null) {
            return;
        }
        collect(x.left, prefix, res);
        res.add(prefix.toString() + x.c);
        collect(x.mid, prefix.append(x.c), res); // collect from x.mid, the prefix should append x.c
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, res);
    }

    @Override
    public String longestPrefixOf(String key) {
        if (key == null || key.length() == 0) {
            return null;
        }
        int len = 0;
        int i = 0;
        TrieNode x = root;
        while (x != null && i < key.length()) {
            char c = key.charAt(i);
            if (c < x.c) {
                x = x.left;
            } else if (c > x.c) {
                x = x.right;
            } else {
                i += 1; // i is used to traverse the given query key
                len = i;
                x = x.mid;
            }
        }
        return key.substring(0, len);
    }

}
