package Boggle.BoggleMode;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.json.JSONObject;

import Boggle.Board;
import Language.Die;
import Language.LanguageHandler;
import Language.LanguageHolder;
import Language.LanguageInfo;
import Player.Player;

public class FoggleBoggle implements BoggleMode {

    private Board board;

    private boolean generousBoggleOn;

    private ScriptEngineManager mgr  = new ScriptEngineManager();
    private ScriptEngine javaScriptEngine = mgr.getEngineByName("JavaScript");

    ArrayList<Player> players;

    @Override
    public JSONObject getSettings() {
        JSONObject body = new JSONObject();

        body.put("gamemode", "FoggleBoggle");

        body.put("generousBoggle", false);
        
        JSONObject language = new JSONObject();
        LanguageHandler langHan = LanguageHandler.getInstance();
        String languageName = "Foggle";
        LanguageInfo info = langHan.getLanguageInfo(languageName);
        String boardSize = info.getDimensions().get(0);
        String dict = "Foggle";


        language.put("name", languageName);
        language.put("size", boardSize);
        language.put("dictionary", dict);
        body.put("language", language);
        body.put("numberOfPlayers", 2);
        body.put("gameTime", 60);
        body.put("showSolution",false);
        return body;
    }

    @Override
    public void initialize(JSONObject setting) throws Exception {
        JSONObject language = setting.getJSONObject("language");

        generousBoggleOn = setting.getBoolean("generousBoggle");

        int numberOfPlayers = setting.getInt("numberOfPlayers");
        if (numberOfPlayers < 2) {
            throw new Exception("The amount of players are too low");
        }
        players = new ArrayList<Player>();

        String langName = "Foggle";
        String dimension = language.getString("size");
        String dictName = "Foggle";

        LanguageHandler langHan = LanguageHandler.getInstance();
        LanguageHolder lang = langHan.loadLanguage(langName, dictName, dimension);

        long seed = setting.optLong("seed", 0);

        generateBoard(lang.getDice(), seed);
    }

    private void generateBoard(Die dieSet, long seed) {
        board = new Board();
        if (seed == 0) {
            board.initialize(dieSet);
            return;
        }
        board.initialize(dieSet, seed);
    }

    public int calculateScore(String input) {
        int length = input.replace("[^0-9]", "").length();
        if (length >= 3 && length <= 4) {
            return 1;
        } else if (length > 4 && length <7 ) {
            return length - 3;
        } else if (length == 7) {
            return 5;
        } else if (length > 7) {
            return 11;
        } else {
            return 0;
        }
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public void startGame() {
        for (Player pl: players) {
            pl.setPlaying(true);
        }
        this.broadcastMessage("Lets play!!\n", -1);
    }

    @Override
    public void broadcastMessage(String message, int excludePlayer) {
        for(Player player: players) {
            if (player.getPlayerID() == excludePlayer) {
                continue;
            }
            player.sendMessage(message);
        }

    }

    @Override
    public void finnishGame() {
        Player winner = null;
        for (Player pl: players) {
            if (winner == null) {
                winner = pl;
            } else if(winner.getScore() < pl.getScore()) {
                winner = pl;
            }
            pl.sendMessage(String.format("You got a score of %d\n", pl.getScore()));
            pl.setPlaying(false);
        }
        broadcastMessage(String.format("The winner of this game is player %d with a score of %d\n", winner.getPlayerID(), winner.getScore()), -1);
        winner.sendMessage("Congratualations you won!!\n");
    }

    @Override
    public String checkInput(String input, int playerID) {
        String[] expressions = input.split("=");
        boolean validInput = false;
        Player pl = players.get(playerID);
        foundOnBoard = false;
        try {
            validInput = (javaScriptEngine.eval(expressions[0]) == javaScriptEngine.eval(expressions[1]));
            validInput = validInput && !pl.isAccepted(input);
        } catch(Exception e) {
        }
        if (validInput) {
            String nums = input.replaceAll("[^a-zA-Z0-9]", "");
            int x = board.getDimension().getX();
            int y = board.getDimension().getY();
            boolean[][] processed = new boolean[y][x];
            boolean found = false;

            for (int row = 0; row < y; row++) {
                for (int col = 0; col < x; col++) {
                    if (board.getBoard(col, row).startsWith(nums.substring(0,1))) {
                        found = searchBoard(nums, col, row, 1, processed);
                    }
                }
            }
            if(found) {
                pl.addAcceptedInputs(input);
                pl.setScore(pl.getScore() + calculateScore(input.replaceAll("[^0-9]", "")));
                return "OK";
            }
            return "NOT OK";
        }
        return "NOT OK";
    }

    // All possible movements from a position, x represents the move 0 is current
    /*   x,x,x
     *   x,0,x
     *   x,x,x
     */
    private static int[] rowOpt = {-1,-1,-1, 0, 0, 1, 1, 1};
    private static int[] colOpt = {-1, 0, 1,-1, 1,-1, 0, 1};

    private boolean foundOnBoard = false;

    // Helper function for looking through the board
    private boolean searchBoard(String input, int x, int y, int matches, boolean[][] processed) {
        if (matches >= input.length()) {
            foundOnBoard = true;
            return true;
        }

        processed[y][x] = true;

        for(int k = 0; k < 8; k++) {
            if (isValid(x + colOpt[k], y + rowOpt[k], processed)) {
                if (input.substring(matches, matches+1).equals(board.getBoard(x + colOpt[k], y + rowOpt[k]))) {
                    searchBoard(input, x + colOpt[k], y + rowOpt[k], matches + 1, processed);
                }
            }
            if (foundOnBoard) {
                return true;
            }
        }

        processed[y][x] = false;
        return false;
    }

    // checks if the position x,y is within the board and if the position have been processed
    private boolean isValid(int x, int y, boolean[][] processed) {
        return  (x >= 0 && x < board.getDimension().getX()) &&
                (y >= 0 && y < board.getDimension().getY()) &&
                (!processed[y][x] || generousBoggleOn);
    }


    
}
