package test.Boggle.BoggleMode;

import org.json.JSONObject;
import org.junit.Test;

import Boggle.BoggleMode.StandardBoggle;

public class StandardBoggleTest {
    
    @Test
    public void standardBoggleInitializeTest() {
        StandardBoggle boggle = new StandardBoggle();
        JSONObject json = boggle.getSettings();
        try {
            boggle.initialize(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long start = System.currentTimeMillis();
        boggle.searchAllWords();
        Long fin = System.currentTimeMillis() - start;
        "hej".toString();
    }

}
