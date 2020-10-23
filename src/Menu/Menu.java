package Menu;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Boggle.BoggleFactory;
import Language.LanguageHandler;
import Language.LanguageInfo;
import Menu.MenuUI.MenuUI;

public class Menu {

    private MenuUI visual;

    /**
     * Run the menu
     * @param baseSetting the default setting for the game
     * @return setting for the gamemode which was choosen through the menu
     */
    public JSONObject run(JSONObject baseSetting) {
        boolean shouldExit = false;

        ArrayList<String> gameModes = BoggleFactory.getGameModeNames();
        JSONObject setting = baseSetting;
        JSONObject returnSetting = setting;

        while (!shouldExit) {
            try {
                visual.renderSettings(setting, "Setting");
                visual.renderMessage("Write play to Play");
                String command = visual.getInput();
                switch (command.toLowerCase()) {
                    case ("gamemode"):
                        String gameMode = gamemodeMenu(returnSetting, gameModes);
                        if (gameMode != null) {
                            returnSetting = BoggleFactory.getDefaultSettings(gameMode);
                            setting = returnSetting;
                        }
                    break;
                    case ("generous boggle"):
                        returnSetting.put("generousBoggle", !returnSetting.getBoolean("generousBoggle"));
                        setting = returnSetting;
                    break;
                    case ("show solution"):
                        returnSetting.put("showSolution", !returnSetting.getBoolean("showSolution"));
                        setting = returnSetting;
                    break;
                    case ("language"):
                        returnSetting.put("language", languageMenu(returnSetting));
                        setting = returnSetting;
                    break;
                    case ("number of players"): 
                        visual.renderMessage("How many players? ");
                        String in = visual.getInput();
                        try {
                            int numOPlayer = Integer.parseInt(in);
                            if (numOPlayer < 2) {
                                visual.renderMessage("The number of players needs to be atleast 2");
                                continue;
                            }
                            returnSetting.put("numberOfPlayers", numOPlayer);
                            setting = returnSetting;
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    break;
                    case ("time to play"):
                        visual.renderMessage("For how many seconds should you play? ");
                        String in2 = visual.getInput();
                        try {
                            int numOPlayer = Integer.parseInt(in2);
                            returnSetting.put("gameTime", numOPlayer);
                            setting = returnSetting;
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    break;
                    case("play"):
                        return returnSetting;
                    case("quit"):
                        return null;
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnSetting;
    }

    // Helper menu for gamemode selection
    private String gamemodeMenu(JSONObject base, ArrayList<String> gameModes) {
        while(true) {
            JSONObject opt = new JSONObject();
            opt.put("value", base.getString("gamemode"));
            opt.put("options", gameModes);
            String input = "";
            try{
                visual.renderSettings(opt, "GameMode");
                visual.renderMessage("write back to go back");
                input = visual.getInput();
                if (gameModes.contains(input)) {
                    return input;
                } else if(input.equals("back")) {
                    return null;
                }
            } catch (Exception e) {

            }
        }
    }

    // helper menu for language selection
    private JSONObject languageMenu(JSONObject base) {
        JSONObject lang = base.getJSONObject("language");
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            String input = "";
            try {
                visual.renderSettings(lang, "Language Settings");
                visual.renderMessage("Write the option to change: ");
                input = visual.getInput();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LanguageHandler langhan = LanguageHandler.getInstance();
            JSONObject opt = null;
            LanguageInfo info = null;
            switch (input) {
                case ("name"):
                    opt = new JSONObject();
                    opt.put("value", lang.getString("name"));
                    opt.put("options", new JSONArray(langhan.getLanguages()));
                    try {
                        visual.renderSettings(opt, "Language");
                        String in = visual.getInput();
                        if (langhan.getLanguages().contains(in)) {
                            lang.put("name", in);
                            info = langhan.getLanguageInfo(in);
                            lang.put("size", info.getDimensions().get(0));
                            lang.put("dictionary", info.getDictionaries().get(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
                case ("size"):
                    opt = new JSONObject();
                    info = langhan.getLanguageInfo(lang.getString("name"));
                    opt.put("value", lang.getString("size"));
                    opt.put("options", new JSONArray(info.getDimensions()));
                    try {
                        visual.renderSettings(opt, "Size");
                        String in = visual.getInput();
                        if (info.getDimensions().contains(in)) {
                            lang.put("size", in);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
                case ("dictionary"):
                    opt = new JSONObject();
                    info = langhan.getLanguageInfo(lang.getString("name"));
                    opt.put("value", lang.getString("dictionary"));
                    opt.put("options", new JSONArray(info.getDictionaries()));
                    try {
                        visual.renderSettings(opt, "Dictionary");
                        String in = visual.getInput();
                        if (info.getDictionaries().contains(in)) {
                            lang.put("dictionary", in);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
                case ("back"):
                    return lang;
            }
        }
    }

    public void setVisual(MenuUI visual) {
        this.visual = visual;
    }
    
}
