

import models.attacks.ArcherAttack;
import models.heroes.Archer;
import models.heroes.Hero;
import service.GameClient;
import service.GameController;
import ui.GameFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String SERVER_IP = "10.28.23.121";
        int PORT = 12345;

        try {
            GameClient client = new GameClient(SERVER_IP, PORT);
            client.connect();

            Hero hero = new Archer();
            hero.setAttackBehavior(new ArcherAttack());
            GameController game = new GameController(hero, client);

            SwingUtilities.invokeLater(() -> {
                GameFrame frame = new GameFrame(game);
                game.setViewListener(frame); // <-- ДОБАВЛЕНО
            });

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка подключения к серверу: " + e.getMessage(),
                    "Ошибка сети",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("Error: " + e.getMessage());
        }
    }
}