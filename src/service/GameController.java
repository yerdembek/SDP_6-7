package service;

import models.attacks.ArcherAttack;
import models.heroes.Hero;

public class GameController {
    private Hero hero;
    private GameClient client;
    private boolean gameOver = false;

    public GameController(Hero hero, GameClient client) {
        this.hero = hero;
        this.client = client;
        client.addListener(new GameEventListener() {
            @Override
            public void onVillainAttack(int damage, int ignored) {
                hero.takeDamage(damage);
                if (hero.getHealth() <= 0) {
                    System.out.println("[💀] Вы погибли!");
                }
            }

            @Override
            public void onGameOver(String winner) {
                gameOver = true;
                System.out.println("🏆 Победитель: " + winner);
            }

            @Override
            public void onAttackConfirmation(String message, int villainHp) {
                System.out.println("[✓] " + message + " Злодей HP: " + villainHp);
            }
        });
    }

    public void onCreate(){
        
    }

    public void onAttack() {
        if (gameOver || hero.getHealth() <= 0) return;
        client.sendAttack(hero.getName(), hero.getDamage(), hero.getAttackBehavior(), hero.getHealth());
    }

    public Hero getHero() { return hero; }
    public boolean isGameOver() { return gameOver; }
}