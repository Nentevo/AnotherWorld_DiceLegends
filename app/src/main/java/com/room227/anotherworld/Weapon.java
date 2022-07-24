package com.room227.anotherworld;

public class Weapon {
    public String name;
    public int imageId;
    public String description;
    public String ability;
    public int cost;

    public Weapon(String name, int imageId, String description, String ability, int cost){
        this.name = name;
        this.imageId = imageId;
        this.description = description;
        this.ability = ability;
        this.cost = cost;
    }


    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }

    public String getDescription(){
        return description;
    }

    public String getAbility(){
        return ability;
    }

    public int getCost(){
        return cost;
    }

    public Weapon findWeaponByName(String name){
        if(name.equals(this.name))
            return this;
        else
            return null;
    }
}
