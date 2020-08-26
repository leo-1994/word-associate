package com.leo.word.trie;

import java.util.HashMap;

/**
 * @author leo
 * @version 1.0.0
 * @description
 * @create 2020/8/26 15:16
 */
public class Node {

    private HashMap<Character, Node> childMap;
    private boolean isLeaf;

    public Node() {
        setLeaf(false);
        setChildMap(new HashMap<>());
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public HashMap<Character, Node> getChildMap() {
        return childMap;
    }

    public void setChildMap(HashMap<Character, Node> childMap) {
        this.childMap = childMap;
    }
}
