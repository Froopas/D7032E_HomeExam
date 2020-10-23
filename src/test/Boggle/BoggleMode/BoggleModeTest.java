package test.Boggle.BoggleMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;

import Boggle.Board;
import Boggle.BoggleMode.BattleBoggle;
import Boggle.BoggleMode.FoggleBoggle;
import Boggle.BoggleMode.StandardBoggle;
import Language.LanguageHandler;
import Language.LanguageHolder;
import Player.Player;
import Player.PlayerUI.TerminalPlayerUI;

public class BoggleModeTest {

    // Test of req 1 for standard
    @Test(expected = Exception.class)
    public void standardBoggle1p() throws Exception {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject json = boggle.getSettings();
        json.put("numberOfPlayers", 1);
        boggle.initialize(json);
    }

    // Test of req 1 for standard
    @Test(expected = Exception.class)
    public void battleBoggle1p() throws Exception {
        BattleBoggle boggle = new BattleBoggle();
        JSONObject json = boggle.getSettings();
        json.put("numberOfPlayers", 1);
        boggle.initialize(json);
    }

    // Test of req 1 for standard
    @Test(expected = Exception.class)
    public void foggleBoggle1p() throws Exception {
        FoggleBoggle boggle = new FoggleBoggle();
        JSONObject json = boggle.getSettings();
        json.put("numberOfPlayers", 1);
        boggle.initialize(json);
    }
    
    // Test of rule 2
    @Test
    public void foggleBoggleBoardTest() {
        FoggleBoggle boggle = new FoggleBoggle();
        JSONObject json = boggle.getSettings();
        long seed = 12345L;
        json.put("seed", seed);
        try { 
            boggle.initialize(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Board board1 = boggle.getBoard();
        json.put("seed", seed + 12345L);
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

    // Test of rule 4
    @Test
    public void winnerAnnouncedTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(ps);
        pl1.setPlayerUI(ui1);
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        setting.put("seed", 1234);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        boggle.checkInput("MIRK",0);
        boggle.finnishGame();
        String output = baos.toString();
        boolean announced = output.contains("The winner of this game is player 0 with a score of 1");
        assertTrue(announced);
    }

    // Test of rule 5
    @Test
    public void pointsTest() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        setting.put("seed", 123456);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        ArrayList<String> words = boggle.getAllWords();
        int testLength = 2;
        for(String word: words) {
            if(word.length() == testLength) {
                int check = boggle.calculateScore(word);
                if (testLength < 3) {
                    assertEquals(check, 0);
                } else if (testLength == 3 || testLength == 4) {
                    assertEquals(check, 1);
                } else if (testLength > 4 && testLength < 7) {
                    assertEquals(check, testLength - 3);
                } else if (testLength == 7) {
                    assertEquals(check, 5);
                } else  if (testLength > 7) {
                    assertEquals(check, 11);
                }
                testLength++;
            }
        }
    }

    // Test of rule 6
    @Test
    public void quTest() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        ArrayList<String> words = boggle.getAllWords();
        for (String word: words) {
            if(word.contains("QU")) {
                "Test".toString();
            }
        }

        int check = boggle.calculateScore("SQUILLA");
        assertEquals(check,5);
        "hej".toString();
    }

    // Test of rule 7
    @Test
    public void multipleOfSameStandardTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(ps);
        pl1.setPlayerUI(ui1);
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        String check1 = boggle.checkInput("SQUILLA", 0);
        String check2 = boggle.checkInput("SQUILLA", 0);

        assertEquals("You have already submitted this word", check2);

    }

    // Test of rule 7 Battle Boggle (also tests rule 15)
    @Test
    public void multipleOfSameBattleTest() {
        Player pl1 = new Player();
        Player pl2 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(ps);
        pl1.setPlayerUI(ui1);
        pl2.setPlayerUI(ui1);
        BattleBoggle boggle = new BattleBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        boggle.addPlayer(pl2);
        String check1 = boggle.checkInput("SQUILLA", 0);
        String check2 = boggle.checkInput("SQUILLA", 1);

        assertEquals("This word has already been submitted", check2);
    }

    // Test of rule 8
    @Test
    public void dictionaryTest() {
        LanguageHandler langHan = LanguageHandler.getInstance();
        LanguageHolder lang = langHan.loadLanguage("English", "dic", "4x4");
        assertTrue(lang.getDictionary() != null);
    }

    // Test of rule 9
    @Test
    public void possibleWordListTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(ps);
        pl1.setPlayerUI(ui1);
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);

        boggle.finnishGame();

        String output = baos.toString();

        ArrayList<String> words = boggle.getAllWords();
        for (String word: words) {
            assertTrue(output.contains(word));
        }
    }

    // Test of rule 10 4x4
    @Test
    public void boardSize4x4Test() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        JSONObject language = setting.getJSONObject("language");
        language.put("size", "4x4");
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        Board board = boggle.getBoard();
        assertEquals(4, board.getDimension().getX());
        assertEquals(4, board.getDimension().getY());
    }

    // Test of rule 10 5x5
    @Test
    public void boardSize5x5Test() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        JSONObject language = setting.getJSONObject("language");
        language.put("size", "5x5");
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        Board board = boggle.getBoard();
        assertEquals(5, board.getDimension().getX());
        assertEquals(5, board.getDimension().getY());
    }

    // Test of rule 11
    @Test
    public void languageDiceTest() {
        LanguageHandler langHan = LanguageHandler.getInstance();
        LanguageHolder spanish = langHan.loadLanguage("Spanish", "DictButSpanish", "4x4");
        LanguageHolder english = langHan.loadLanguage("English", "dic", "4x4");
        ArrayList<ArrayList<String>> spanDie = spanish.getDice().getDiceArray();
        ArrayList<ArrayList<String>> engDie = english.getDice().getDiceArray();
        boolean same = true;
        if(spanDie.size()!=engDie.size()) {
            fail();
        }
        for (int i=0; i < spanDie.size(); i++) {
            if (spanDie.get(i).size()!=engDie.get(i).size()) {
                fail();
            }
            for (int j=0; j < spanDie.get(i).size(); j++) {
                if(!spanDie.get(i).get(j).equals(engDie.get(i).get(j))) {
                    same = false;
                }
            }
        }
        assertTrue(!same);
    }

    // Test of rule 12
    @Test
    public void standardBoggleChainTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(System.out);
        pl1.setPlayerUI(ui1);
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        try {
            ui1.renderBoard(boggle.getBoard());
        } catch (Exception e) {

        }
        String check = boggle.checkInput("SAGES", 0); // SAGES uses s twice 

        assertEquals("Not on Board", check);
    }

    // Test of rule 13 
    @Test
    public void standardBoggleGenerousChainTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(System.out);
        pl1.setPlayerUI(ui1);
        StandardBoggle boggle = new StandardBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        setting.put("generousBoggle", true);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        try {
            ui1.renderBoard(boggle.getBoard());
        } catch (Exception e) {

        }
        String check = boggle.checkInput("SAGES", 0); // SAGES uses s twice 

        assertEquals("OK", check);
    }

    // Test of rule 14 battle
    @Test
    public void battleBoggleGenerousChainTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(System.out);
        pl1.setPlayerUI(ui1);
        BattleBoggle boggle = new BattleBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        setting.put("generousBoggle", true);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        try {
            ui1.renderBoard(boggle.getBoard());
        } catch (Exception e) {

        }
        String check = boggle.checkInput("SAGES", 0); // SAGES uses s twice 

        assertEquals("OK", check);
    }

    // Test of rule 14 foggle
    @Test
    public void foggleBoggleGenerousChainTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(System.out);
        pl1.setPlayerUI(ui1);
        FoggleBoggle boggle = new FoggleBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        setting.put("generousBoggle", true);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }

        boggle.addPlayer(pl1);
        try {
            ui1.renderBoard(boggle.getBoard());
        } catch (Exception e) {

        }
        String check = boggle.checkInput("1+6=2+7-2", 0); // uses the same 2 twice 

        assertEquals("OK", check);
    }

    // Test of rule 16
    @Test
    public void battleBoggleAnnounceTest() {
        Player pl1 = new Player();
        Player pl2 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        TerminalPlayerUI ui2 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        PrintStream ps2 = new PrintStream(baos2);
        ui1.setOutputStream(ps);
        ui2.setOutputStream(ps2);
        pl1.setPlayerUI(ui1);
        pl2.setPlayerUI(ui2);
        pl2.setPlayerID(1);
        BattleBoggle boggle = new BattleBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }
        boggle.searchAllWords();

        boggle.addPlayer(pl1);
        boggle.addPlayer(pl2);
        String check1 = boggle.checkInput("SQUILLA", 0);

        String out2 = baos2.toString();

        assertTrue(out2.contains("Player 0 submitted word SQUILLA"));
    }

    // Test of rule 17
    @Test
    public void foggleMathEquationsTest() {
        Player pl1 = new Player();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TerminalPlayerUI ui1 = new TerminalPlayerUI();
        PrintStream ps = new PrintStream(baos);
        ui1.setOutputStream(System.out);
        pl1.setPlayerUI(ui1);
        FoggleBoggle boggle = new FoggleBoggle();
        JSONObject setting = boggle.getSettings();
        long seed = 1603459526904L; // Seed that contains Qu
        setting.put("seed", seed);
        setting.put("generousBoggle", true);
        try {
            boggle.initialize(setting);
        } catch (Exception e) {

        }

        boggle.addPlayer(pl1);
        try {
            ui1.renderBoard(boggle.getBoard());
        } catch (Exception e) {

        }
        String check = boggle.checkInput("4+6=4+3", 0); // uses the same 2 twice 

        assertEquals("NOT OK", check);
    }
}
