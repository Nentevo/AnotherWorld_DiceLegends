package com.room227.anotherworld;

public class Character {
    String name;
    int hp;
    int attack_power;
    int defense_power;
    int speed;
    int exp;
    int level;
    int money;
    boolean alive;

    public int attacked_by(Character c1)
    {
        int damage;
        if(this.defense_power >= c1.attack_power)
        {
            return 1;
        }
        else
        {
            damage = c1.attack_power - this.defense_power;
            return damage;
        }
    }

    public boolean isUpgrade()
    {
        if(this.exp >= 100)
        {
            this.exp = this.exp - 100;
            this.level = this.level + 1;
            this.attack_power = this.attack_power + 5;
            this.defense_power = this.defense_power + 5;
            return true;
        }
        return false;
    }
}
