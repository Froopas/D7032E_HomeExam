package Player.PlayerUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import Boggle.Board;

public class AsciiPlayerUI implements PlayerUI {

    private OutputStream out;
    private InputStream in;

    public void setOutputStream(OutputStream os) {
        this.out = os;
    }

    public void setInputStream(InputStream is) {
        this.in = is;
    }

    @Override
    public void renderBoard(Board board) throws IOException {
        int x = board.getDimension().getX();
        int y = board.getDimension().getY();
        String boardString = "";
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                String cha = board.getBoard(i, j);
                boardString.concat(cha).concat("\t");
            }
            boardString.concat("\n");
        }

        out.write(boardString.getBytes());
    }

    @Override
    public void renderMessage(String message) throws IOException {
        out.write(message.getBytes());
    }

    @Override
    public String getInput() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        // read from the inputStream and write it to buffe
        int nread;
        byte[] data = new byte[1024];
        while((nread = in.read(data,0,data.length)) != -1) {
            buffer.write(data,0, nread);
        }
        buffer.flush();

        // convert buffer to string
        String input = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        return input;
    }
    
}
