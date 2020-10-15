package Player.PlayerUI;

import java.io.IOException;

import Boggle.Board;

public interface PlayerUI {
    /**
     * Gives a visual representation of the current board
     * @param board to represent
     */ 
    public void renderBoard(Board board) throws IOException;

    /**
     * Gives a visual representation of a message
     * @param message to represent
     */
    public void renderMessage(String message) throws IOException;

    /**
     * Get input from player
     * @return the string that the player wrote
     */
    public String getInput() throws IOException;
}
