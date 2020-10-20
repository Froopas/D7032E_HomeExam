package Boggle.BoggleMode;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import Boggle.Board;
import Language.Die;
import Language.LanguageHandler;
import Language.LanguageHolder;
import Util.Trie;
import Util.TrieNode;

public class StandardBoggle implements BoggleMode {

    private Board board;

    private boolean generousBoggleOn;

    private Trie dictionary;

    private Trie foundWords;

    private ArrayList<String> foundWordsList;

    private boolean searchCompleted;

    private ArrayList<Player> players;

    @Override
    public JSONObject getSettings() {
        JSONObject body = new JSONObject();

        body.put("gamemode", "standardBoggle");

        body.put("generousBoggle", false);
        
        JSONObject language = new JSONObject();
        language.put("name", "English");
        language.put("size", "4x4");
        language.put("dictionary", "dic");
        body.put("language", language);
        body.put("numberOfPlayers", 2);
        body.put("timeToPlay", 60);
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
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player());
        }

        String langName = language.getString("name");
        String dimension = language.getString("size");
        String dictName = language.getString("dictionary");

        LanguageHandler langHan = new LanguageHandler();
        LanguageHolder lang = langHan.loadLanguage(langName, dictName, dimension);

        long seed = setting.optLong("seed", 0);

        dictionary = lang.getDictionary();

        generateBoard(lang.getDice(), seed);
    }

    @Override
    public String checkInput(String input, int playerID) {
        Player player = players.get(playerID);
        if (searchCompleted) {
            if (foundWords.containsNode(input)) {
                if (player.words.contains(input)) {
                    return "You have already submitted this word";
                }
                player.score += calculateScore(input); 
                player.words.add(input);
                return "OK";
            }
        }
        return "Search is not completed";
    }

    private int calculateScore(String input) {
        int length = input.length();
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

    private void generateBoard(Die dieSet, long seed) {
        board = new Board();
        if (seed == 0) {
            board.initialize(dieSet);
            return;
        }
    }

    public void searchAllWords() {
        this.foundWords = new Trie();
        this.foundWordsList = new ArrayList<String>();
        int x = board.getDimension().getX();
        int y = board.getDimension().getY();
        boolean[][] processed = new boolean[y][x];

        for (int row = 0; row < y; row++) {
            for (int col = 0; col < x; col++) {
                String ch = board.getBoard(col, row);
                searchBoard(dictionary.getRoot(), col, row, processed, ch);
            }
        }
        searchCompleted = true;
    }


    private static int[] rowOpt = {-1,-1,-1, 0, 0, 1, 1, 1}; // all possible moves in a boggle board
    private static int[] colOpt = {-1, 0, 1,-1, 1,-1, 0, 1};

    private void searchBoard(TrieNode node, int x, int y, boolean[][] processed, String path) {
        if (node.isWord()) {
            foundWords.insert(path);
            foundWordsList.add(path);
        }

        processed[y][x] = true;

        for (var entry: node.getChildren().entrySet()) {
            for (int k = 0; k < 8; k++) {
                if(isValid(y + colOpt[k], x + rowOpt[k], processed, entry.getKey())) {
                    // Check the special case of Qu on the board
                    if (board.getBoard(x + rowOpt[k], y + colOpt[k]).equals("Qu") && entry.getKey().toString().equals("Q") ) {
                        TrieNode entryChild = entry.getValue().getChildren().get('U');
                        if (entryChild == null) {
                            continue;
                        }
                        searchBoard(entryChild, x + rowOpt[k], y + colOpt[k], processed, path.concat("QU")); 
                    // See if the child of the trie has the same char as the board
                    } else if (board.getBoard(x + rowOpt[k], y + colOpt[k]).equals(entry.getKey().toString())) {
                        searchBoard(entry.getValue(), x + rowOpt[k], y + colOpt[k], processed, path.concat(entry.getKey().toString()));
                    }
                }
            }
        }

        processed[y][x]= false;
    }

    private boolean isValid(int x, int y, boolean[][] processed, Character ch) {
        return  (x>= 0 && x < board.getDimension().getX()) && // see if index is out of bounds
                (y>= 0 && y < board.getDimension().getX()) &&
                (!processed[y][x] || generousBoggleOn); // see if the tile is not processed or generous boggle is on
    }

    private class Player {
        ArrayList<String> words = new ArrayList<String>();
        int score = 0;
    }
    
}
