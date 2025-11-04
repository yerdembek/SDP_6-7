package service;

import com.bya.model.AttackConfirmationData;
import com.bya.model.ClientRequest;
import com.bya.model.GameOverData;
import com.bya.model.HeroAttackData;
import com.bya.model.ServerEvent;
import com.bya.model.VillainAttackData;
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

    public void sendReportHealth(String name, int heroHp) {
        if (!connected) return;
        try {
            HeroAttackData data = new HeroAttackData();
            data.characterName = name;
            data.heroHp = heroHp;
            data.attackDamage = 0;
            data.weaponType = null;

            ClientRequest req = new ClientRequest();
            req.action = "reportHealth";
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
        String dataAsJson = gson.toJson(event.data);
        for (GameEventListener listener : listeners) {
            switch (event.eventType) {
                case "attackConfirmation":
                    AttackConfirmationData attackConfirmData = gson.fromJson(dataAsJson, AttackConfirmationData.class);
                    listener.onAttackConfirmation(attackConfirmData.message, attackConfirmData.villainHp);
                    break;
                case "villainAttack":
                    VillainAttackData villainAttackData = gson.fromJson(dataAsJson, VillainAttackData.class);
                    listener.onVillainAttack(villainAttackData.damageDealt, villainAttackData.villainHp);
                    break;
                case "gameOver":
                    GameOverData gameOverData = gson.fromJson(dataAsJson, GameOverData.class);
                    listener.onGameOver(gameOverData.winner);
                    break;
                case "shockwaveImpact": // <-- ДОБАВЛЕНО
                    listener.onShockwaveImpact();
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