package Boggle;

import Util.Dimension;

public class Board {

    private String[][] board;

    private Dimension dim;

    public Dimension getDimension(){ return dim; }
    
    public String[][] getBoard() {
        return board;
    }

    public String getBoard(int x, int y) {
        return board[y][x];
    }
}
