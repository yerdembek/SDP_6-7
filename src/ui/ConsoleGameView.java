package ui;

import service.GameController;

import java.util.Scanner;

public class ConsoleGameView {
    private final GameController controller;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleGameView(GameController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println("=== Archer Game ===");
        while (!controller.isGameOver()) {
            System.out.println("HP: " + controller.getHero().getHealth() + " | Введите 'a' для атаки, 'q' для выхода");
            String cmd = scanner.nextLine().trim().toLowerCase();
            if ("a".equals(cmd)) {
                controller.onAttack();
            } else if ("q".equals(cmd)) {
                break;
            }
        }
        scanner.close();
    }
}
