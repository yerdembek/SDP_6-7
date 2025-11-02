// handlers/InputHandler.java
package handlers;

import models.heroes.Hero;

public class InputHandler {

    public static void handleAttackCommand(Hero attacker, Hero target, Runnable onSendRequest) {
        if (attacker == null || target == null) {
            OutputHandler.logMessage("Invalid attack command: null hero.");
            return;
        }

        boolean wasSent = attacker.attack(target, onSendRequest);

        if (wasSent) {
            OutputHandler.logAttack(attacker, target);
        }
    }
}