// handlers/OutputHandler.java
package handlers;

import models.heroes.Hero;

import javax.swing.*;

public class OutputHandler {

    // Опциональный UI-логгер
    private static java.util.function.Consumer<String> uiLogger = null;

    public static void setUiLogger(java.util.function.Consumer<String> logger) {
        uiLogger = logger;
    }

    public static void logAttack(Hero attacker, Hero target) {
        String msg = attacker.getClass().getSimpleName() + " attacked " +
                target.getClass().getSimpleName() + "!";
        logMessage(msg);
    }

    public static void logMessage(String message) {
        // Всегда выводим в консоль (для отладки)
        System.out.println("[LOG] " + message);

        // И в UI, если задан
        if (uiLogger != null) {
            uiLogger.accept(message);
        }
    }
}