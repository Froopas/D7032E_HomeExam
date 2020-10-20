package Boggle.BoggleMode;

import org.json.JSONObject;

public interface BoggleMode {

    /**
     * Get the settings for this boggle mode
     * @return the required settings for the game mode
     */
    public JSONObject getSettings();

    /**
     * Initializes the gamemode with the settings
     * @param setting the settings with which to run the game mode
     */
    public void initialize(JSONObject setting);

    /**
     * Check if the given input is valid
     * @param input string to check
     * @return Message
     */
    public String checkInput(String input);
}
