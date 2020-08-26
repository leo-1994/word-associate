package com.leo.word.trie;


import com.leo.word.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @description tree
 * @version: 1.0.0
 * @create: 2020-04-21 16:46
 * @author: xingzhi
 */
public class TrieTree {

    private static final int maxStr = 200;
    private Node root;

    public TrieTree() {
        root = new Node();
    }

    public void insert(String words) {
        insert(this.root, words);
    }

    public void insert(List<String> words) {
        if (CollectionUtils.isEmpty(words)) {
            return;
        }
        for (String word : words) {
            insert(this.root, word);
        }
    }

    private void insert(Node root, String words) {
        if (null == words || "".equals(words)) return;
        char[] chars = words.toLowerCase().toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            if (!root.getChildMap().containsKey(chars[i]))
                root.getChildMap().put(chars[i], new Node());
            if (i == length - 1) {
                root.getChildMap().get(chars[i]).setLeaf(true);
            }
            root = root.getChildMap().get(chars[i]);
        }
    }

    private HashSet<String> preTraversal(Node root, String preStr) {
        HashSet<String> set = new HashSet<>();
        if (root != null) {
            if (root.isLeaf()) {
                set.add(preStr);
            }
            for (Map.Entry<Character, Node> chr : root.getChildMap().entrySet()) {
                String tempStr = preStr + chr.getKey();
                if (set.size() > maxStr) return set;
                set.addAll(preTraversal(chr.getValue(), tempStr));
            }
        }
        return set;
    }

    public HashSet<String> getWordsForPrefix(String word) {
        return getWordsForTrie(this.root, word);
    }

    private HashSet<String> getWordsForTrie(Node root, String word) {
        char[] chars = word.toLowerCase().toCharArray();
        for (char aChar : chars) {
            if (!root.getChildMap().containsKey(aChar)) {
                return new HashSet<>();
            }
            root = root.getChildMap().get(aChar);
        }
        return preTraversal(root, word);
    }


}
