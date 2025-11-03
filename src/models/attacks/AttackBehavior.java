package models.attacks;

import models.heroes.Hero;

public interface AttackBehavior {
    void attack(Hero attacker, Hero target, Runnable onSendRequest);
    int getRange();
    int getDamageMultiplier();
}