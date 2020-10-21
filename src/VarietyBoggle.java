import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import Boggle.BoggleMode.BoggleMode;
import Boggle.BoggleMode.StandardBoggle;
import Player.Player;
import Player.PlayerUI.AsciiPlayerUI;

public class VarietyBoggle {

    private static final long STARTUP_TIME = 3;

    BoggleMode boggle;

    ServerSocket aSocket;

    public void run() {
        // run menu
        // get settings from menu
        boggle = new StandardBoggle();
        
        JSONObject setting = boggle.getSettings();
        try {
            boggle.initialize(setting);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (setting.getJSONObject("language").getString("name").equals("foggle")) {
            ExecutorService startup = Executors.newFixedThreadPool(2);
            Runnable serverSetup = new Runnable() {
    
                @Override
                public void run() {
                    server(setting.getInt("numberOfPlayers"));
                }
    
            };
    
            Runnable boggleSetup = new Runnable() {
                @Override
                public void run() {
                   ((StandardBoggle)boggle).searchAllWords();
                }
            };
            startup.execute(serverSetup);
            startup.execute(boggleSetup);

            try {
                startup.awaitTermination(STARTUP_TIME, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            server(setting.getInt("numberOfPlayers"));
        }

    }

    public void server(int numberOfPlayers) {
        boggle.addPlayer(createLocalPlayer());
        try {
            aSocket = new ServerSocket(2048);
            for (int i = 1; i < numberOfPlayers; i++) {
                Socket connSocket = aSocket.accept();
                boggle.addPlayer(createRemotePlayer(connSocket, i));
            }
            boggle.broadcastMessage("All players have joined", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Player createLocalPlayer() {
        AsciiPlayerUI pUi = new AsciiPlayerUI();
        pUi.setInputStream(System.in);
        pUi.setOutputStream(System.out);
        Player locPlayer = new Player();
        locPlayer.setPlayerUI(pUi);
        locPlayer.setPlayerID(0);
        locPlayer.sendMessage("You are player 0");
        return locPlayer;
    }
 
    private Player createRemotePlayer(Socket sock, int playerID) throws IOException {
        AsciiPlayerUI pUi = new AsciiPlayerUI();
        pUi.setInputStream(sock.getInputStream());
        pUi.setOutputStream(sock.getOutputStream());
        Player remPlayer = new Player();
        remPlayer.setPlayerUI(pUi);
        remPlayer.setPlayerID(playerID);
        remPlayer.sendMessage(String.format("You are player %d", playerID));
        return remPlayer;
    }
}
