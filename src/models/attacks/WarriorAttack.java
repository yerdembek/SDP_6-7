package models.attacks;

import models.heroes.Hero;

public class WarriorAttack implements AttackBehavior{

    @Override
    public void attack(Hero attacker, Hero target, Runnable onSendRequest) {
        // Здесь можно добавить спецэффекты (звук, визуал)
        // Но основное — отправить запрос
        onSendRequest.run();
    }

    @Override
    public int getRange() {
        return 2;
    }


    @Override
    public String toString() {
        return "Staff";
    }
}
