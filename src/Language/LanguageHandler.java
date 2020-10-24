package Language;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Util.Dimension;
import Util.Trie;

public class LanguageHandler {

    private static LanguageHandler langHan = null;

    private String LANGUAGE_DIR = "/home/frappe/document/D7032E/home_exam/Languages";
    private String DIE_FILE_END = ".csv";
    private String DICTIONARY_FILE_END = ".txt";

    private Map<String,File> languageDirs;

    public static LanguageHandler getInstance() {
        if (langHan == null) {
            langHan = new LanguageHandler();
        }
        return langHan;
    }

    public LanguageHandler() {
        languageDirs = new HashMap<String,File>();
        String currPath = "";
        try {
            currPath = new File("").getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File currDir = new File(currPath);
        File projDir = currDir.getParentFile().getParentFile().getParentFile();
        File[] projDirs = projDir.listFiles();
        for (File dir: projDirs) {
            if (dir.getName().equals("Languages")) {
                LANGUAGE_DIR = dir.getAbsolutePath();
                break;
            }
        }
        getLanguages();
    }

    /**
     * Searches the Language folder for languages
     * @return a list of available languages
     */
    public ArrayList<String> getLanguages() {
        File langDir = new File(LANGUAGE_DIR);
        File[] languages = langDir.listFiles(File::isDirectory);

        ArrayList<String> dirNames = new ArrayList<String>();
        for (File dir :languages) {
            // might want to check if dir has atleas one csv and one txt file in the dir.
            if (dir.getName().equals("Foggle")) {
                languageDirs.put(dir.getName(), dir);
                continue;
            }
            dirNames.add(dir.getName());
            languageDirs.put(dir.getName(), dir);
        }
        return dirNames;
    }

    /**
     * Gets available die configuration and dictionaries for the language
     * @param language to get Language info from
     * @return Language info about that language
     */
    public LanguageInfo getLanguageInfo(String language) {
        File langDir = languageDirs.get(language);
        
        File[] files = langDir.listFiles();
        ArrayList<String> dictionaries = new ArrayList<String>();
        ArrayList<String> dieSets = new ArrayList<String>();
        for (File file: files) {
            String fileName = file.getName();
            if (fileName.endsWith(DIE_FILE_END)) {
                dieSets.add(fileName.replace(DIE_FILE_END, ""));
            }
            if (fileName.endsWith(DICTIONARY_FILE_END)) {
                dictionaries.add(fileName.replace(DICTIONARY_FILE_END, ""));
            }
        }

        return new LanguageInfo(dictionaries, dieSets);
    }

    /**
     * Parses csv files and return dimension
     * Example: "4x3.csv" returns a dimension with x:4 and y:3
     * @param filename filename to parse
     * @return Dimension that corresponds to the name
     */
    private Dimension parseDieFileName(String filename) {
        if (!filename.endsWith(DIE_FILE_END)) {
            return null;
        }
        String dimension = filename.replace(DIE_FILE_END, "");
        String[] dim = dimension.split("x");
        int x = Integer.parseInt(dim[0]);
        int y = Integer.parseInt(dim[1]);

        return new Dimension(x, y);
    }

    /**
     * Loads languages from files
     * @param language Which language to load
     * @param dictionary which dictionary in the specified language folder
     * @param dieSet which die set to load from the specified language folder
     * @return A structure with the language selected
     */
    public LanguageHolder loadLanguage(String language, String dictionary, String dieSet) {
        File langDir = languageDirs.get(language);
        Trie dict = null;
        Die die = null;
        File[] files = langDir.listFiles();
        for (File file: files) {
            String fileName = file.getName();
            if (fileName.startsWith(dictionary)) {
                dict = loadDictionary(file);
            }
            if (fileName.startsWith(dieSet)) {
                die = loadDieSet(file);
            }
        }
        return new LanguageHolder(language, die, dict);
    }

    private Trie loadDictionary(File dictionaryFile) {
        Trie dictionary = new Trie();
        try {
            Scanner scan = new Scanner(dictionaryFile);
            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                dictionary.insert(line.toUpperCase());
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    private Die loadDieSet(File dieFile) {
        Die dice;
        ArrayList<ArrayList<String>> dieSet = new ArrayList<ArrayList<String>>();
        try {
            Scanner scan = new Scanner(dieFile);
            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] faces = line.split(",");
                ArrayList<String> die = new ArrayList<String>();
                for (String face: faces){
                    die.add(face);
                }
                dieSet.add(die);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Dimension dim = parseDieFileName(dieFile.getName());
        dice = new Die(dim,dieSet);
        return dice;
    }
}
