package Boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Language.Die;
import Util.Dimension;

public class Board {

    private String[][] board;

    private long seed;

    private Dimension dim;

    /**
     * get the dimension of the board
     * @return dimension of the board
     */
    public Dimension getDimension() { 
        return dim; 
    }
    
    /**
     * get the whole board
     * @return the board
     */
    public String[][] getBoard() {
        return board;
    }

    /**
     * Get the string at position x,y in the board
     * @param x index
     * @param y index
     * @return String at position x,y
     */
    public String getBoard(int x, int y) {
        return board[y][x];
    }

    /**
     * Randomly distributes the dieset over the board with System time seed
     * @param dieSet to distribute
     */
    public void initialize(Die dieSet) {
        initialize(dieSet, System.currentTimeMillis());
    }

    /**
     * Randomly distribues the dieset over the board with seed
     * @param dieSet to distribute
     * @param seed of which RNG is defined
     */
    public void initialize(Die dieSet, long seed) {
        this.seed = seed;
        dim = dieSet.getDimension();
        int x = dim.getX();
        int y = dim.getY();

        board = new String[y][x];

        int colindex = 0;
        int rowindex = 0;

        Random rnd = new Random(seed);
        ArrayList<ArrayList<String>> dice = dieSet.getDiceArray();
        ArrayList<ArrayList<String>> rows = new ArrayList<>(dice);
        Collections.shuffle(rows, rnd);
        for (ArrayList<String> row: rows) {
            board[rowindex][colindex] = row.get(rnd.nextInt(row.size()));
            colindex = (colindex + 1) % x;
            rowindex = colindex == 0?rowindex + 1: rowindex;
        }
    }
}
