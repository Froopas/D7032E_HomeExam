package Language;

import Util.Trie;

public class LanguageHolder {
    private String language;
    private Die dice;
    private Trie dictionary;

    public LanguageHolder(String language, Die dice, Trie dictionary) {
        this.language = language;
        this.dice = dice;
        this.dictionary = dictionary;
    }

    public String getLanguage() {
        return language;
    }

    public Trie getDictionary() {
        return dictionary;
    }

    public void setDictionary(Trie dictionary) {
        this.dictionary = dictionary;
    }

    public Die getDice() {
        return dice;
    }

    public void setDice(Die dice) {
        this.dice = dice;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
