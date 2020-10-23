import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import Boggle.BoggleFactory;
import Boggle.BoggleMode.BoggleMode;
import Boggle.BoggleMode.StandardBoggle;
import Menu.Menu;
import Menu.MenuUI.AsciiMenuUI;
import Player.Player;
import Player.PlayerUI.TerminalPlayerUI;
import Player.PlayerUI.SocketPlayerUI;

public class VarietyBoggle {

    private static final long STARTUP_TIME = 10;

    BoggleMode boggle;

    ServerSocket aSocket;
    ArrayList<Socket> sockets = new ArrayList<Socket>();

    public void run() {
        JSONObject setting = BoggleFactory.getDefaultSettings("StandardBoggle");

        while (true) {
            // Prepare menu
            AsciiMenuUI menuUi = new AsciiMenuUI();
            menuUi.setInputStream(System.in);
            menuUi.setOutputStream(System.out);
            Menu menu = new Menu();
            menu.setVisual(menuUi);
            setting = menu.run(setting);

            if(setting == null) {
                break;
            }
            
            boggle = BoggleFactory.getGameMode(setting.getString("gamemode"));

            try {
                boggle.initialize(setting);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int numberOfPlayers = setting.getInt("numberOfPlayers");
            prepareGame(setting.getString("gamemode"), numberOfPlayers);

            boggle.startGame();

            playGame(numberOfPlayers, setting.getInt("gameTime"));

            boggle.finnishGame();
            boggle.broadcastMessage("CLOSE SOCKET", 0);

            try {
                for (Socket socket: sockets) {
                    socket.close();
                }
                aSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Prepares for the game, start sockets for all players and if needed find all possible words
    public void prepareGame(String gamemode, int numberOfPlayers) {
        if (!gamemode.equals("FoggleBoggle")) {
            ExecutorService startup = Executors.newFixedThreadPool(2);
            Runnable serverSetup = new Runnable() {
    
                @Override
                public void run() {
                    server(numberOfPlayers);
                    System.out.println("Server setup done");
                }
    
            };
    
            Runnable boggleSetup = new Runnable() {
                @Override
                public void run() {
                   ((StandardBoggle)boggle).searchAllWords();
                   System.out.println("Search done");
                }
            };
            startup.execute(serverSetup);
            startup.execute(boggleSetup);

            try {
                startup.awaitTermination(STARTUP_TIME, TimeUnit.SECONDS);
                startup.shutdownNow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            server(numberOfPlayers);
        }
    }

    // Start the game for all players
    public void playGame(int numberOfPlayers, int gameTime) {
        ExecutorService playThreads = Executors.newFixedThreadPool(numberOfPlayers);
        for(Player pl:boggle.getPlayers()) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    pl.run();
                }
            };
            playThreads.execute(task);
        }
        try {
            playThreads.awaitTermination(gameTime, TimeUnit.SECONDS);
            playThreads.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start sockets and create players for all users
    public void server(int numberOfPlayers) {
        boggle.addPlayer(createLocalPlayer());
        try {
            aSocket = new ServerSocket(2048);
            for (int i = 1; i < numberOfPlayers; i++) {
                Socket connSocket = aSocket.accept();
                boggle.addPlayer(createRemotePlayer(connSocket, i, numberOfPlayers));
            }
            boggle.broadcastMessage("All players have joined", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Player createLocalPlayer() {
        TerminalPlayerUI pUi = new TerminalPlayerUI();
        pUi.setInputStream(System.in);
        pUi.setOutputStream(System.out);
        Player locPlayer = new Player();
        locPlayer.setPlayerUI(pUi);
        locPlayer.setPlayerID(0);
        locPlayer.setBoggleMode(boggle);
        locPlayer.sendMessage("You are player 0\n");
        return locPlayer;
    }
 
    private Player createRemotePlayer(Socket sock, int playerID, int numOfPlayer) throws IOException {
        SocketPlayerUI pUi = new SocketPlayerUI();
        sockets.add(sock);
        pUi.setInputStream(sock.getInputStream());
        pUi.setOutputStream(sock.getOutputStream());
        Player remPlayer = new Player();
        remPlayer.setPlayerUI(pUi);
        remPlayer.setPlayerID(playerID);
        remPlayer.setBoggleMode(boggle);
        remPlayer.sendMessage(String.format("You are player %d \n", playerID));
        boggle.broadcastMessage(String.format("Waiting for players... \t %d/%d\n", playerID,numOfPlayer), -1);
        return remPlayer;
    }
}
