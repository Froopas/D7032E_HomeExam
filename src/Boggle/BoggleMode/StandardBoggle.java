package Boggle.BoggleMode;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Boggle.Board;
import Language.Die;
import Language.LanguageHandler;
import Language.LanguageHolder;
import Language.LanguageInfo;
import Player.Player;
import Util.Trie;
import Util.TrieNode;

public class StandardBoggle implements BoggleMode {

    private Board board;

    private boolean generousBoggleOn;
    private boolean showSolution;

    private Trie dictionary;

    private Trie foundWords;

    private ArrayList<String> foundWordsList;

    private boolean searchCompleted;

    ArrayList<Player> players;

    @Override
    public JSONObject getSettings() {
        JSONObject body = new JSONObject();

        body.put("gamemode", "StandardBoggle");

        body.put("generousBoggle", false);
        
        JSONObject language = new JSONObject();
        LanguageHandler langHan = LanguageHandler.getInstance();
        String languageName = langHan.getLanguages().get(0);
        LanguageInfo info = langHan.getLanguageInfo(languageName);
        String boardSize = info.getDimensions().get(0);
        String dict = info.getDictionaries().get(0);


        language.put("name", "English");
        language.put("size", "4x4");
        language.put("dictionary", "dic");
        body.put("language", language);
        body.put("numberOfPlayers", 2);
        body.put("gameTime", 60);
        body.put("showSolution",true);
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

        showSolution = setting.getBoolean("showSolution");

        String langName = language.getString("name");
        String dimension = language.getString("size");
        String dictName = language.getString("dictionary");

        LanguageHandler langHan = LanguageHandler.getInstance();
        LanguageHolder lang = langHan.loadLanguage(langName, dictName, dimension);

        long seed = setting.optLong("seed", 0);

        dictionary = lang.getDictionary();

        generateBoard(lang.getDice(), seed);
    }

    @Override
    public String checkInput(String input, int playerID) {
        Player player = players.get(playerID);
        if (searchCompleted) {
            if (dictionary.containsNode(input)) { 
                if (foundWords.containsNode(input)) {
                    if (player.isAccepted(input)) {
                        return "You have already submitted this word";
                    }
                    player.setScore(player.getScore() + calculateScore(input)); 
                    player.addAcceptedInputs(input);
                    return "OK";
                }
                return "Not on Board";
            }
            return "Not in dictionary";
        }
        return "Search is not completed";
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

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public int calculateScore(String input) {
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
        board.initialize(dieSet, seed);
    }

    public ArrayList<String> getAllWords() {
        if(searchCompleted) {
            return foundWordsList;
        }
        return null;
    }


    private static int[] rowOpt = {-1,-1,-1, 0, 0, 1, 1, 1}; // all possible moves in a boggle board
    private static int[] colOpt = {-1, 0, 1,-1, 1,-1, 0, 1};

    public void searchAllWords() {
        this.foundWords = new Trie();
        this.foundWordsList = new ArrayList<String>();
        int x = board.getDimension().getX();
        int y = board.getDimension().getY();
        boolean[][] processed = new boolean[y][x];

        for (int row = 0; row < y; row++) {
            for (int col = 0; col < x; col++) {
                String ch = board.getBoard(col, row);
                TrieNode firstChar = null;
                if (ch.equals("Qu")) {
                    TrieNode q = dictionary.getRoot().getChildren().get('Q');
                    if (q == null) {
                        continue;
                    }
                    firstChar = q.getChildren().get('U');
                } else {
                    firstChar = dictionary.getRoot().getChildren().get(ch.toCharArray()[0]);
                }
                if (firstChar != null) {
                    searchBoard(firstChar, col, row, processed, ch);
                }
            }
        }
        searchCompleted = true;
    }

    private void searchBoard(TrieNode node, int x, int y, boolean[][] processed, String path) {
        if (node.isWord()) {
            foundWords.insert(path);
            foundWordsList.add(path);
        }

        processed[y][x] = true;

        for (var entry: node.getChildren().entrySet()) {
            for (int k = 0; k < 8; k++) {
                if(isValid(x + colOpt[k], y + rowOpt[k], processed, entry.getKey())) {
                    // Check the special case of Qu on the board
                    String ch = board.getBoard(x + colOpt[k], y + rowOpt[k]);
                    if (ch.equals("Qu") && entry.getKey().toString().equals("Q") ) {
                        TrieNode entryChild = entry.getValue().getChildren().get('U');
                        if (entryChild == null) {
                            continue;
                        }
                        searchBoard(entryChild, x + colOpt[k], y + rowOpt[k], processed, path.concat("QU")); 
                    // See if the child of the trie has the same char as the board
                    } else if (ch.equals(entry.getKey().toString())) {
                        searchBoard(entry.getValue(), x + colOpt[k], y + rowOpt[k], processed, path.concat(entry.getKey().toString()));
                    }
                }
            }
        }

        processed[y][x]= false;
    }

    private boolean isValid(int x, int y, boolean[][] processed, Character ch) {
        return  (x>= 0 && x < board.getDimension().getX()) && // see if index is out of bounds
                (y>= 0 && y < board.getDimension().getY()) &&
                (!processed[y][x] || generousBoggleOn); // see if the tile is not processed or generous boggle is on
    }

    @Override
    public Board getBoard() {
        return board;
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

        if (showSolution) {
            String message = "These are the possible words\n";
            StringBuffer sb = new StringBuffer();
            for (String word: foundWordsList) {
                sb.append(word.concat(","));
            }
            message = message.concat(sb.toString());
            for (Player pl: players) {
                pl.sendMessage(message);
            }
        }
    }
}
