package bearmaps.proj2d;

import java.util.LinkedList;
import java.util.List;

public class Trie {
    public TrieNode root;

    private class TrieNode {
        private char ch;
        private boolean isKey;
        private TrieNode[] next;

        private TrieNode(char c, boolean b) {
            ch = c;
            isKey = b;
            next = new TrieNode[27];
        }

        private void add(String s) {
            TrieNode n = root;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                int v = c - 97;
                if (c == 32) {
                    v = 26;
                }
                if (v < 27 && v >= 0) {
                    if (n.next[v] == null) {
                        if (i == s.length() - 1) {
                            n.next[v] = new TrieNode(c, true);
                        } else {
                            n.next[v] = new TrieNode(c, false);
                        }
                    }
                    n = n.next[v];
                    if (i == s.length() - 1) {
                        n.isKey = true;
                    }
                }
            }
        }
    }

    public Trie() {
        root = new TrieNode('/', false);
    }

    public void add(String s) {
        root.add(s);
    }

    public List<String> keysWithPrefix(String s) {
        TrieNode n = root;
        List<String> result = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            int j = s.charAt(i) - 97;
            if (s.charAt(i) == 32) {
                j = 26;
            }
            if (j < 27 && j >= 0) {
                n = n.next[j];
            }
        }
        collect(n, new StringBuilder(s), result);
        return result;
    }

    private void collect(TrieNode n, StringBuilder prefix, List<String> results) {
        if (n == null) {
            return;
        }
        if (n.isKey) {
            results.add(prefix.toString());
        }
        for (int c = 0; c < 27; c++) {
            if (n.next[c] != null) {
                String p = prefix.toString();
                TrieNode w = n.next[c];
                prefix.append(w.ch);
                collect(w, prefix, results);
                prefix = new StringBuilder(p);
            }
        }
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        t.add(" awls");
        t.add("apple");
        t.add("sad");
        t.add("sam");
        t.add("sap");
        t.add("same");
        List<String> s = t.keysWithPrefix("sam");
    }
}
