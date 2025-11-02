package models.heroes;

import models.attacks.ArcherAttack;
import models.attacks.AttackBehavior;

public class Archer extends Hero {

    public Archer() {
        super("ARCHER", 15, 1, new ArcherAttack());
    }
}
