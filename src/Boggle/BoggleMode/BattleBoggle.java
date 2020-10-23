package Boggle.BoggleMode;

import java.util.ArrayList;

import org.json.JSONObject;

import Player.Player;

public class BattleBoggle extends StandardBoggle {

    ArrayList<String> battleWords = new ArrayList<String>();
    
    @Override
    public JSONObject getSettings() {
        JSONObject body = super.getSettings();
        body.put("gamemode","BattleBoggle");
        return body;
    }

    @Override
    public String checkInput(String input, int playerID) {
        // Use the standard boggle check input
        String check = super.checkInput(input, playerID);
        if (check.equals("OK")) {
            if(battleWords.contains(input)) {
                Player player = players.get(playerID);
                // As standard boggles check input adds the score if it is correct
                // we need to remove the score since the input was "falsely" accepted
                player.setScore(player.getScore() - calculateScore(input));
                return "This word has already been submitted";
            } else {
                broadcastMessage(String.format("Player %d submitted word %s ", playerID, input), playerID);
            }
            battleWords.add(input);
        }
        return check;
    } 
}
