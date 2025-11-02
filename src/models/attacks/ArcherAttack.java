package models.attacks;

import models.heroes.Hero;

public class ArcherAttack implements AttackBehavior {
    @Override
    public void attack(Hero attacker, Hero target, Runnable onSendRequest) {
        // Здесь можно добавить спецэффекты (звук, визуал)
        // Но основное — отправить запрос
        onSendRequest.run();
    }
}
