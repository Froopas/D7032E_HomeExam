package Util;

public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        
        for (int i = 0; i< word.length(); i++) {
            String letter = Character.toString(word.charAt(i));
            if ((letter.equals("Q"))||(letter.equals("q"))) {
                letter = letter.concat("u");
                i++;
            }
            current = current.getChildren().computeIfAbsent(letter, c -> new TrieNode());
        }

        // for (char letter: word.toCharArray()) {
        //     if ((letter == 'Q') || (letter == 'q'))
            
        //     current = current.getChildren().computeIfAbsent(Character.toString(letter), c -> new TrieNode());
        // }
        current.setWord(true);
    }

    public boolean delete(String word) {
        return delete(root, word, 0);
    }

    public boolean containsNode(String word) {
        TrieNode current = root;

        for (int i=0; i< word.length(); i++) {
            String ch = Character.toString(word.charAt(i));
            if ((ch.equals("Q"))||(ch.equals("q"))) {
                ch = ch.concat("u");
                i++;
            }
            TrieNode node = current.getChildren().get(ch.toString());
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
        String ch = Character.toString(word.charAt(index));
        if ((ch.equals("Q"))||(ch.equals("q"))) {
            ch = ch.concat("u");
            index++;
        }
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
