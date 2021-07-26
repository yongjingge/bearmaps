package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* Trie implementation (using HashMap) */
public class TrieSet implements TrieSet61B {

    private TrieNode root;

    private static class TrieNode{
        private boolean isKey;
        private HashMap<Character, TrieNode> ctmap;

        TrieNode() {
            this.isKey = false;
            ctmap = new HashMap<>();
        }

        public void setIsKey() {
            this.isKey = true;
        }
    }

    public TrieSet() {
        this.clear();
    }

    @Override
    public void clear() {
        root = new TrieNode();
    }

    private boolean isValid(String key) {
        return key != null && key.length() >= 1;
    }

    public TrieNode get(String key) {
        if (! isValid(key)) {
            return null;
        }
        TrieNode cur = root;
        for (int i = 0; i < key.length(); i += 1) {
            char c = key.charAt(i);
            if (! cur.ctmap.containsKey(c)) {
                return null;
            }
            cur = cur.ctmap.get(c);
        }
        return cur;
    }

    @Override
    public boolean contains(String key) {
        return isValid(key) && get(key) != null && get(key).isKey;
    }

    @Override
    public void add(String key) {
        if (! isValid(key)) {
            return;
        }
        TrieNode cur = root;
        char[] chars = key.toCharArray();
        for (char c : chars) {
            cur.ctmap.computeIfAbsent(c, k -> new TrieNode());
            cur = cur.ctmap.get(c);
        }
        cur.setIsKey(); // cur.isKey set to true after insertion!
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        if (! isValid(prefix)) {
            return null;
        }
        List<String> res = new ArrayList<>();
        TrieNode tn = get(prefix);
        collect(tn, prefix, res);
        return res;
    }

    private void collect(TrieNode cur, String prefix, List<String> res) {
        if (cur == null) {
            return;
        }
        if (cur.isKey) {
            res.add(prefix);
        }
        for (char c : cur.ctmap.keySet()) {
            collect(cur.ctmap.get(c), prefix + c, res);
        }
    }

    @Override
    public String longestPrefixOf(String prefix) {
        if (! isValid(prefix)) {
            return null;
        }
        int len = 0;
        int i = 0;
        TrieNode cur = root;
        while (cur != null && i < prefix.length()) {
            char c = prefix.charAt(i);
            if (cur.ctmap.containsKey(c) && cur.ctmap.get(c).isKey) {
                i += 1;
                len = i;
                cur = cur.ctmap.get(c);
            }
        }
        return prefix.substring(0, len);
    }


























//    private TrieNode root;
//
//    private static class TrieNode{
//        boolean isKey;
//        HashMap<Character, TrieNode> map;
//
//        TrieNode() {
//            isKey = false;
//            map = new HashMap<>();
//        }
//
//        public void setIsKey() {
//            isKey = true;
//        }
//    }
//
//    public TrieSet() {
//        root = new TrieNode();
//    }
//
//    public TrieNode get(String key) {
//        TrieNode cur = root;
//        for (int i = 0; i < key.length(); i += 1) {
//            char c = key.charAt(i);
//            if (! cur.map.containsKey(c)) {
//                return null;
//            }
//            cur = cur.map.get(c);
//        }
//        return cur;
//    }
//
//    public boolean contains(String key) {
//        if (key == null || key.length() == 0) {
//            return false;
//        }
//        TrieNode res = get(key);
//        return res != null && res.isKey == true;
//    }
//
//    public void add(String key) {
//        if (key == null || key.length() == 0) {
//            return;
//        }
//        TrieNode cur = root;
//        for (int i = 0; i < key.length(); i += 1) {
//            char c = key.charAt(i);
//            cur.map.computeIfAbsent(c, k -> new TrieNode());
//            cur = cur.map.get(c);
//        }
//        cur.setIsKey();
//    }
//
//    public List<String> keysWithPrefix(String prefix) {
//        List<String> keys = new ArrayList<>();
//        TrieNode cur = get(prefix);
//        collectHelper(cur, prefix, keys);
//        return keys;
//    }
//
//    private void collectHelper(TrieNode tn, String prefix, List<String> keys) {
//        if (tn == null) {
//            return;
//        }
//        if (tn.isKey) {
//            keys.add(prefix);
//        }
//        for (char c : tn.map.keySet()) {
//            collectHelper(tn.map.get(c), prefix + c, keys);
//        }
//    }
}
