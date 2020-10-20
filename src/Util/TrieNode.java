package Util;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private final Map<String, TrieNode> children = new HashMap<String, TrieNode>();
    private boolean isWord;

    public Map<String, TrieNode> getChildren() {
        return children;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean isWord) {
        this.isWord = isWord;
    }
}
