package Util;

/**
 * This class is for fast search through a list of words.
 * Used for the dictionary
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        
        for (char letter: word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(letter, c -> new TrieNode());
        }
        current.setWord(true);
    }

    public boolean delete(String word) {
        return delete(root, word, 0);
    }

    public int getWordCount(TrieNode node) {
        int result = 0;

        if (node.isWord()) {
            result += 1;
        }
        
        for (var child: node.getChildren().entrySet()) {
            if (child != null) {
                result += getWordCount(child.getValue());
            }
        }
        return result;
    }

    public boolean containsNode(String word) {
        TrieNode current = root;

        for (int i=0; i< word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }

        return current.isWord();
    }

    public boolean isEmpty() {
        return root == null;
    }

    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if(!current.isWord()) {
                return false;
            }
            current.setWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isWord();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }

    public TrieNode getRoot() {
        return this.root;
    }
}
