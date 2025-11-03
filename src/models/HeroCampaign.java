package models;

import models.heroes.*;

public class HeroCampaign {
    public static Hero createHero(String type) {
        switch (type.toLowerCase()) {
            case "mage": return new Mage();
            case "archer": return new Archer();
            case "warrior": return new Warrior();
            default: throw new IllegalArgumentException("Unknown hero type: " + type);
        }
    }
}
