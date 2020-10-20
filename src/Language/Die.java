package Language;

import java.util.ArrayList;

import Util.Dimension;

public class Die {

    private Dimension dimension;
    private ArrayList<ArrayList<String>> diceArray;

    public Die(Dimension dimension, ArrayList<ArrayList<String>> diceArray) {
        this.dimension = dimension;
        this.diceArray = diceArray;
    }

    public Dimension getDimenstion() {
        return dimension;
    }

    public ArrayList<ArrayList<String>> getDiceArray() {
        return diceArray;
    }

    public void setDiceArray(ArrayList<ArrayList<String>> diceArray) {
        this.diceArray = diceArray;
    }

    public void setDimenstion(Dimension dimenstion) {
        this.dimension = dimenstion;
    }   
}
