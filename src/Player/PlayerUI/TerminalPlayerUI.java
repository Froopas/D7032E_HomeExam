package Player.PlayerUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;


import Boggle.Board;

public class TerminalPlayerUI implements PlayerUI {

    Scanner in = null;
    PrintStream out;

    public void setInputStream(InputStream in) {
        this.in = new Scanner(in);
    } 

    public void setOutputStream(PrintStream out) {
        this.out = out;
    } 

    @Override
    public void renderBoard(Board board) throws IOException {
        int x = board.getDimension().getX();
        int y = board.getDimension().getY();
        String boardString = "";
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                String cha = board.getBoard(j, i);
                String seperator = "  ";
                if (cha.equals("Qu")) {
                    seperator = " ";
                }
                boardString = boardString.concat(cha).concat(seperator);
            }
            boardString = boardString.concat("\n");
        }

        out.println(boardString);
    }

    @Override
    public void renderMessage(String message) throws IOException {
        out.println(message);
    }

    @Override
    public String getInput() throws IOException {
        String input = in.nextLine();
        return input.toUpperCase();
    }
    
}
