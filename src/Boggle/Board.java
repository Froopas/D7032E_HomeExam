package Boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Language.Die;
import Util.Dimension;

public class Board {

    private String[][] board;

    private Dimension dim;

    public Dimension getDimension() { 
        return dim; 
    }
    
    public String[][] getBoard() {
        return board;
    }

    public String getBoard(int x, int y) {
        return board[y][x];
    }

    public void initialize(Die dieSet) {
        initialize(dieSet, System.currentTimeMillis());
    }

    public void initialize(Die dieSet, long seed) {
        dim = dieSet.getDimenstion();
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
