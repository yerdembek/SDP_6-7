package service;

import com.bya.model.ClientRequest;
import com.bya.model.HeroAttackData;
import com.bya.model.ServerEvent;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameClient {
    private final String serverIp;
    private final int serverPort;
    private final Gson gson = new Gson();
    private Socket socket;
    private volatile boolean connected = false;
    private List<GameEventListener> listeners = new ArrayList<>();

    public GameClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void addListener(GameEventListener listener) {
        this.listeners.add(listener);
    }

    public void connect() throws IOException {
        socket = new Socket(serverIp, serverPort);
        connected = true;
        startListening();
    }

    public void sendAttack(String name, int damage, String weapon, int heroHp) {
        if (!connected) return;
        try {
            HeroAttackData data = new HeroAttackData();
            data.characterName = name;
            data.attackDamage = damage;
            data.weaponType = weapon;
            data.heroHp = heroHp;

            ClientRequest req = new ClientRequest();
            req.action = "heroAttack";
            req.data = data;

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(gson.toJson(req));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void startListening() {
        Thread t = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))) {
                String line;
                while (connected && (line = in.readLine()) != null) {
                    ServerEvent event = gson.fromJson(line, ServerEvent.class);
                    notifyListeners(event);
                }
            } catch (Exception e) {
                // disconnected
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void notifyListeners(ServerEvent event) {
        for (GameEventListener listener : listeners) {
            switch (event.eventType) {
                case "attackConfirmation":
                    int vHp = ((Double) event.data.get("villainHp")).intValue();
                    listener.onAttackConfirmation((String) event.data.get("message"), vHp);
                    break;
                case "villainAttack":
                    int dmg = ((Double) event.data.get("damageDealt")).intValue();

                    listener.onVillainAttack(dmg, -1);
                    break;
                case "gameOver":
                    listener.onGameOver((String) event.data.get("winner"));
                    break;
            }
        }
    }

    public void disconnect() {
        connected = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
