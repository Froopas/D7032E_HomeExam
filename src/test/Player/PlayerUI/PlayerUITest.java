package test.Player.PlayerUI;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import Player.PlayerUI.AsciiPlayerUI;

public class PlayerUITest {

    @Test
    public void testInput() {
        AsciiPlayerUI playerUi = new AsciiPlayerUI();
        String expected = "testar 1.2.3...";

        InputStream is = new ByteArrayInputStream(expected.getBytes());
        playerUi.setInputStream(is);
        String value = "";
        try {
            value =  playerUi.getInput();
        } catch(IOException e)  {

        }

        assertEquals(expected, value);
    }

}
