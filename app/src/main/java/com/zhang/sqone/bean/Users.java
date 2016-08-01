package com.zhang.sqone.bean;

/**
 * Created by mac on 16/7/8.
 */
public class Users {
    public String name;
    public int power;

    public Users(String name, int i) {
        this.name=name;
        this.power=i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
