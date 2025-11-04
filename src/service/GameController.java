package service;

import models.heroes.Hero;
import ui.GameViewListener;

public class GameController {
    private Hero hero;
    private GameClient client;
    private boolean gameOver = false;
    private GameViewListener viewListener;

    public void setViewListener(GameViewListener listener) {
        this.viewListener = listener;
    }

    public GameController(Hero hero, GameClient client) {
        this.hero = hero;
        this.client = client;
        client.addListener(new GameEventListener() {
            @Override
            public void onVillainAttack(int damage, int ignored) {
                if (viewListener != null) {
                    viewListener.onVillainAttackReceived(damage);
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

            @Override
            public void onShockwaveImpact() {
                if (viewListener != null) {
                    viewListener.onShowShockwave();
                }
            }
        });
    }

    public void reportCurrentHealth() {
        client.sendReportHealth(hero.getName(), hero.getHealth());
    }

    public void onAttack() {
        if (gameOver || hero.getHealth() <= 0) return;
        client.sendAttack(hero.getName(), hero.getDamage(), hero.getAttackBehavior(), hero.getHealth());
    }

    public Hero getHero() { return hero; }
    public boolean isGameOver() { return gameOver; }
}