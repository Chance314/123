package com.itheima.domain;

public class EnemyCharacter extends Character{
    public String skill;//技能
    public boolean defending;//减伤buff

    public EnemyCharacter() {
        super();
    }
    public EnemyCharacter(String name, int HP, int maxHP, int attack, int defense, String skill) {
        super(name, HP, maxHP, attack, defense);
        this.skill = skill;
    }

    //重写受伤方法，因为敌方有一个减伤buff
    @Override
    public void takeDamage(int damage) {
        if(defending) {//当前处于防御状态，伤害减半
           damage = damage / 2 > 1 ? damage / 2 : 1;
           defending = false;//防御状态只能持续一回合
        }
        //调用父类的方法扣血
        super.takeDamage(damage);
    }
}
