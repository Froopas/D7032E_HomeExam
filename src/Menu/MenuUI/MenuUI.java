package Menu.MenuUI;

import java.io.IOException;

import org.json.JSONObject;

public interface MenuUI {

    /**
     * Get the input from the user
     * @return user input
     * @throws IOException
     */
    public String getInput() throws IOException;

    /**
     * Render the setting whith the header
     * @param settings which is to be rendered
     * @param header the header of the setting
     * @throws IOException
     */
    public void renderSettings(JSONObject settings, String header) throws IOException;

    /**
     * Render a message to the user
     * @param message which is to be rendered
     * @throws IOException
     */
    public void renderMessage(String message) throws IOException;
    
}
