import models.heroes.Archer;
import models.heroes.Hero;
import service.GameClient;
import service.GameController;
import ui.ConsoleGameView;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String SERVER_IP = "169.254.37.94";
        int PORT = 12345;

        try {
            GameClient client = new GameClient(SERVER_IP, PORT);
            client.connect();

            Hero hero = new Archer();
            GameController game = new GameController(hero, client);
            ConsoleGameView view = new ConsoleGameView(game);

            view.run();

            client.disconnect();
        } catch (IOException e) {

            System.err.println("Error: " + e.getMessage());
        }
    }
}