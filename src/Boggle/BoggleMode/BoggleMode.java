package Boggle.BoggleMode;

import java.util.ArrayList;

import org.json.JSONObject;

import Boggle.Board;
import Player.Player;

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
    public void initialize(JSONObject setting) throws Exception;

    /**
     * Fetches the current board
     * @return current board
     */
    public Board getBoard();

    /**
     * Adds a player to the gamemode
     * @param player to add
     */
    public void addPlayer(Player player);

    /**
     * Get the list of current players
     * @return 
     */
    public ArrayList<Player> getPlayers();

    /**
     * Starts the game for players to active and broadcasts that the game is starting
     */
    public void startGame();

    /**
     * Writes message to all players excluding exclude Player
     * @param message to be broadcasted
     * @param excludePlayer player which the broadcast should exclude
     */
    public void broadcastMessage(String message, int excludePlayer);

    /**
     * Stops the game for all players and broadcasts the winning player
     */
    public void finnishGame();

    /**
     * Check if the given input is valid
     * @param input string to check
     * @return Message
     */
    public String checkInput(String input, int playerID);
}
