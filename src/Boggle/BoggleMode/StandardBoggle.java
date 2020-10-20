package Boggle.BoggleMode;

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

    private boolean searchCompleted;

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
    public void initialize(JSONObject setting) {
        JSONObject language = setting.getJSONObject("language");

        generousBoggleOn = setting.getBoolean("generousBoggle");

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
    public String checkInput(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    private void generateBoard(Die dieSet, long seed) {
        if (seed == 0) {
            board = new Board(dieSet);
            return;
        }
        board = new Board(dieSet,seed);
    }

    public void searchAllWords() {
        int x = board.getDimension().getX();
        int y = board.getDimension().getY();
        boolean[][] processed = new boolean[y][x];

        for (int row = 0; row < y; row++) {
            for (int col = 0; col < x; col++) {
                String ch = board.getBoard(x, y);
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
                        searchBoard(entry.getValue(), x + rowOpt[k], y + colOpt[k], processed, path.concat(entry.getValue().toString()));
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
    
}
