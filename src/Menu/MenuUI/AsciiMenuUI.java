package Menu.MenuUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

// Used for terminal Menu
public class AsciiMenuUI implements MenuUI {

    private PrintStream out;
    private Scanner in;

    public void setOutputStream(PrintStream os) {
        this.out = os;
    }

    public void setInputStream(InputStream is) {
        this.in = new Scanner(is);
    }

    @Override
    public String getInput() throws IOException {
        String input = in.nextLine();
        return input;
    }

    @Override
    public void renderSettings(JSONObject setting, String header) throws IOException {
        String menuString = "\n".concat(header).concat("\n\n");
        if (header.equals("Setting")) {
            menuString = menuString.concat("GameMode: ").concat(setting.getString("gamemode")).concat("\n");
            menuString = menuString.concat("Language: ").concat(setting.getJSONObject("language").getString("name")).concat("\n");
            menuString = menuString.concat("\t  BoardSize: ").concat(setting.getJSONObject("language").getString("size")).concat(" (depends on Language)\n");
            menuString = menuString.concat("\t  Dictionary: ").concat(setting.getJSONObject("language").getString("dictionary")).concat(" (depends on Language)\n");
            menuString = menuString.concat("Show Solution: ").concat(String.valueOf(setting.getBoolean("showSolution"))).concat("\n");
            menuString = menuString.concat("Number of Players: ").concat(String.valueOf(setting.getInt("numberOfPlayers"))).concat("\n");
            menuString = menuString.concat("Time to Play: ").concat(String.valueOf(setting.getInt("gameTime"))).concat(" seconds\n");
            menuString = menuString.concat("Generous Boggle: ").concat(String.valueOf(setting.getBoolean("generousBoggle"))).concat("\n");

        } else if (header.equals("GameMode") || header.equals("Language") || header.equals("Size") || header.equals("Dictionary")) {
            menuString = menuString.concat("Current ").concat(header).concat(": ").concat(setting.getString("value")).concat("\n");
            menuString = menuString.concat("Available ").concat(header).concat("s: [");
            JSONArray options = setting.getJSONArray("options");
            menuString = menuString.concat(options.getString(0));
            for (int i=1; i< options.length(); i++) {
                menuString = menuString.concat(", ").concat(options.getString(i));
            }
            menuString = menuString.concat("]\n");
        } else if (header.equals("Language Settings")) {
            menuString = menuString.concat("Language: ").concat(setting.getString("name")).concat("\n");
            menuString = menuString.concat("Dictionary: ").concat(setting.getString("dictionary")).concat(" (depends on Language)\n");
            menuString = menuString.concat("BoardSize: ").concat(setting.getString("size")).concat(" (depends on Language)\n");
        }
        out.print(menuString);
    }

    @Override
    public void renderMessage(String message) throws IOException {
        out.println(message);

    }
}