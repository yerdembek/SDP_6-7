package models.heroes;

import models.attacks.AttackBehavior;

public abstract class Hero {
    protected String name;
    private static int nextId = 1;
    protected int id;
    protected int damage;
    protected int health;
    protected int maxHealth;
    protected int maxDamage;

    protected AttackBehavior attackBehavior;

    private long lastAttackRequestTime = 0;
    private long attackCooldownMs = 1000;

    public void setAttackBehavior(AttackBehavior behavior) {
        this.attackBehavior = behavior;
    }

    public Hero(String name, int maxHealth, int maxDamage) {
        this.id = nextId++;
        this.maxHealth = maxHealth;
        this.maxDamage = maxDamage;
        health = maxHealth;
        damage = maxDamage;
        this.name = name;
    }

    public String getAttackBehavior(){
        return attackBehavior.toString();
    }

    public boolean attack(Hero target, Runnable onSendRequest) {
        if (target == null) return false;

        long now = System.currentTimeMillis();
        if (now - lastAttackRequestTime < attackCooldownMs) {
            //I am in cooldown
            return false;
        }

        if (attackBehavior != null) {
            attackBehavior.attack(this, target, onSendRequest);
        }

        lastAttackRequestTime = now;
        return true;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
    }

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public int getHealth() { return health; }

}
