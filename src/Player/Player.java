package Player;

import java.io.IOException;
import java.util.ArrayList;

import Boggle.BoggleMode.BoggleMode;
import Player.PlayerUI.PlayerUI;

public class Player {
    private PlayerUI visual;

    private ArrayList<String> acceptedInputs;

    private int score;
    private int playerID;

    private BoggleMode boggle;

    private boolean currentlyPlaying;

    public void run() {
        try {
            visual.renderBoard(boggle.getBoard());
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(currentlyPlaying) {
            String input = "";
            try {
                input = visual.getInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (currentlyPlaying) {
                String message = boggle.checkInput(input, playerID);
                try {
                    visual.renderMessage(message);
                    visual.renderBoard(boggle.getBoard());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void sendMessage(String message) {
        try {
            visual.renderMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setPlayerUI(PlayerUI ui) {
        this.visual = ui;
    }

    public void addAcceptedInputs(String input) {
        acceptedInputs.add(input);
    }

    public boolean isAccepted(String input) {
        return acceptedInputs.contains(input);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setBoggleMode(BoggleMode boggle) {
        this.boggle = boggle;
    }

    public void setPlaying(boolean shouldPlay) {
        this.currentlyPlaying = shouldPlay;
    }
}
