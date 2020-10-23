package test.Boggle.BoggleMode;

import org.json.JSONObject;
import org.junit.Test;

import Boggle.BoggleMode.FoggleBoggle;
import Player.Player;
import Player.PlayerUI.TerminalPlayerUI;

public class FoggleBoggleTest {

    @Test
    public void FoggleInputTest() {
        FoggleBoggle boggle = new FoggleBoggle();
        JSONObject json = boggle.getSettings();
        long seed = 1603452571556L;
        json.put("seed", seed);

        try {
            boggle.initialize(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TerminalPlayerUI ui = new TerminalPlayerUI();
        ui.setOutputStream(System.out);
        ui.setInputStream(System.in);
        try {
            ui.renderBoard(boggle.getBoard());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Player first = new Player();
        first.setBoggleMode(boggle);
        boggle.addPlayer(first);
        
        String test = boggle.checkInput("4+8=1+2+2+2+5", 0);
        test = boggle.checkInput("2+6=9-1", 0);
        test = boggle.checkInput("2+2+7=3+0+6+3-1", 0);
        "hej".toString();
    }
    
}