import models.attacks.WarriorAttack;
import models.heroes.Archer;
import models.heroes.Hero;
import models.heroes.Warrior;
import service.GameClient;
import service.GameController;
import ui.ConsoleGameView;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String SERVER_IP = "10.28.23.252";
        int PORT = 12345;

        try {
            GameClient client = new GameClient(SERVER_IP, PORT);
            client.connect();



            Hero hero = new Warrior();
            hero.setAttackBehavior(new WarriorAttack());
            GameController game = new GameController(hero, client);
            ConsoleGameView view = new ConsoleGameView(game);

            view.run();

            client.disconnect();
        } catch (IOException e) {

            System.err.println("Error: " + e.getMessage());
        }
    }
}