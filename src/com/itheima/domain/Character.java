package com.itheima.domain;

public class Character {

    //name,HP,maxHP,attack,defense
    public String name;//名字
    public int HP;//当前生命
    public int maxHP;//最大生命
    public int attack;//攻击力
    public int defense;//防御力

    public Character() {
    }

    public Character(String name, int HP, int maxHP, int attack, int defense) {
        this.name = name;
        this.HP = HP;
        this.maxHP = maxHP;
        this.attack = attack;
        this.defense = defense;
    }

    //判断当前人物是否存活
    public boolean isAlive() {
        return HP > 0;
    }
    //恢复血量
    public void heal(int hp) {
        HP += hp;
        if (HP > maxHP) {
            HP = maxHP;
        }
    }

    //受到伤害，收到伤害后剩多少血
    public void takeDamage(int damage) {
        HP -= damage;
        if (HP < 0) {
            HP = 0;
        }
    }
    //展示人物属性
    public void show() {
        System.out.println(name+"[当前生命：" +HP+",攻击："+attack+",防御："+defense+"]");
    }


}
