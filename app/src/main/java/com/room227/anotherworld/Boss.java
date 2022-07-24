package com.room227.anotherworld;

public class Boss extends Character{
//    public Boss(String boss_name, int boss_hp, int boss_attack_power, int boss_defense_power, int boss_speed, boolean boss_alive)
//    {
//        this.name = boss_name;
//        this.hp = boss_hp;
//        this.attack_power = boss_attack_power;
//        this.defense_power = boss_defense_power;
//        this.speed = boss_speed;
//        this.alive = boss_alive;
//    }

    //混沌弥散，效果是生命值减半
    int ChaoticDispersion()
    {
        Dice d20 = new Dice();
        d20.dice_result = d20.D20_double();
        return d20.dice_result;
    }

    //诅咒法令，效果是我方速度降为0
    int DamningEdict()
    {
        Dice d20 = new Dice();
        d20.dice_result = d20.D20_double();
        return d20.dice_result;
    }


}
