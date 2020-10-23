package Boggle;

import java.util.ArrayList;

import org.json.JSONObject;

import Boggle.BoggleMode.*;

public class BoggleFactory {

    private static String[] modes = {"StandardBoggle", "BattleBoggle", "FoggleBoggle"};

    private static BoggleMode currentGameMode;

    /**
     * Get the default JSONObject setting of the gamemode
     * @param gameMode of which to get the settings of
     * @return setting
     */
    public static JSONObject getDefaultSettings(String gameMode) {
        getGameMode(gameMode);
        return currentGameMode.getSettings();
    }

    /**
     * Get possible gamemodes
     * @return
     */
    public static ArrayList<String> getGameModeNames() {
        ArrayList<String> modeList = new ArrayList<String>();
        for (String mode: modes) {
            modeList.add(mode);
        }
        return modeList;
    }

    /**
     * Get the gamemode
     * @param gameMode which is to be instantiated
     * @return BoggleMode
     */
    public static BoggleMode getGameMode(String gameMode) {
        try {
            currentGameMode = (BoggleMode) Class.forName("Boggle.BoggleMode.".concat(gameMode)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentGameMode;
    }
}
