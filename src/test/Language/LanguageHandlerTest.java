package test.Language;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Language.LanguageHandler;
import Language.LanguageHolder;
import Language.LanguageInfo;

public class LanguageHandlerTest {
    

    @Test
    public void getLanguagesTest() {
        List<String> expected = new ArrayList<String>();
        expected.add("English");

        LanguageHandler langHan = new LanguageHandler();
        List<String> lang = langHan.getLanguages();
        assertEquals(expected, lang);   
    }

    @Test
    public void getLanguageInfoTest() {
        LanguageHandler langHan = new LanguageHandler();
        langHan.getLanguages();
        LanguageInfo info = langHan.getLanguageInfo("English");

        "hej".toString();
    }

    @Test
    public void loadLanguageTest() {
        LanguageHandler langHan = new LanguageHandler();
        LanguageHolder lang = langHan.loadLanguage("English", "dic", "4x4");

        "hej".toString();
    }
}
