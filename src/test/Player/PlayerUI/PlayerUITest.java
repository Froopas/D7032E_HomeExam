package test.Player.PlayerUI;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import Player.PlayerUI.TerminalPlayerUI;

public class PlayerUITest {

    @Test
    public void testInput() {
        TerminalPlayerUI playerUi = new TerminalPlayerUI();
        String expected = "testar 1.2.3...";

        InputStream is = new ByteArrayInputStream(expected.getBytes());
        String value = "";
        try {
            value =  playerUi.getInput();
        } catch(IOException e)  {

        }

        assertEquals(expected, value);
    }

}
