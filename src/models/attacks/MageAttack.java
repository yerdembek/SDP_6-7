package models.attacks;

import models.heroes.Hero;

public class MageAttack implements AttackBehavior{

    @Override
    public void attack(Hero attacker, Hero target, Runnable onSendRequest) {
        // Здесь можно добавить спецэффекты (звук, визуал)
        // Но основное — отправить запрос
        onSendRequest.run();
    }

    @Override
    public int getRange() {
        return 3;
    }



    @Override
    public String toString() {
        return "Staff";
    }
}
