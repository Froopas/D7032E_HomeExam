package Player.PlayerUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import Boggle.Board;

public class SocketPlayerUI implements PlayerUI {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void setOutputStream(OutputStream os) {
        try {
            this.out = new ObjectOutputStream(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInputStream(InputStream is) {
        try {
            this.in = new ObjectInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        out.writeObject(boardString);
    }

    @Override
    public void renderMessage(String message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public String getInput() throws IOException {
        String input = "";
        try {
            input = (String) in.readObject();
        } catch (Exception e) {
        }
        return input.toUpperCase();
    }
}
