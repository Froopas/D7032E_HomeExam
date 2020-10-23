package Menu.MenuUI;

import java.io.IOException;

import org.json.JSONObject;

public interface MenuUI {

    public String getInput() throws IOException;

    public void renderSettings(JSONObject settings, String header) throws IOException;

    public void renderMessage(String message) throws IOException;
    
}
