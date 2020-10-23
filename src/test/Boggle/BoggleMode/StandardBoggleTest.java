package test.Boggle.BoggleMode;

import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

import Boggle.Board;
import Boggle.BoggleMode.StandardBoggle;

public class StandardBoggleTest {
    
    @Test
    public void standardBoggleInitializeTest() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject json = boggle.getSettings();
        json.put("seed", 0);
        json.put("generousBoggle", false);
        Long start = System.currentTimeMillis();
        try {
            boggle.initialize(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boggle.searchAllWords();
        Long fin = System.currentTimeMillis() - start;
        "hej".toString();
    }

    // Test of rule 1
    @Test(expected = Exception.class)
    public void standardBogglePlayerTest() throws Exception {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject json = boggle.getSettings();
        json.put("numberOfPlayers", 1);
        boggle.initialize(json);
    }

    // Test of rule 2
    @Test
    public void standardBoggleBoardTest() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject json = boggle.getSettings();
        try { 
            boggle.initialize(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Board board1 = boggle.getBoard();
        try { 
            boggle.initialize(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Board board2 = boggle.getBoard();
        boolean boardSame = true;
        for (int y = 0; y < board1.getDimension().getY(); y++) {
            for( int x = 0; x < board1.getDimension().getX(); x++) {
                if(!board1.getBoard(x, y).equals(board2.getBoard(x, y))) {
                    boardSame = false;
                }
            }
        }
        assertTrue(!boardSame);
    }

}
