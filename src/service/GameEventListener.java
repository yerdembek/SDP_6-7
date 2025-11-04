package service;

public interface GameEventListener {
    void onAttackConfirmation(String message, int villainHp);
    void onVillainAttack(int damage, int newHeroHp);
    void onGameOver(String winner);
    void onShockwaveImpact();
}