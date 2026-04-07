package com.itheima.domain;

import java.util.ArrayList;

public class HeroCharacter extends  Character{
    public int MP;//蓝量
    public int maxMP;//最大蓝量
    public  ArrayList<String> skillList;//我方技能

    public HeroCharacter() {
        super();
        skillList = new ArrayList<String>();
    }



    public HeroCharacter(String name, int HP, int maxHP, int attack, int defense, int MP, int maxMP) {
        super(name, HP, maxHP, attack, defense);
        this.MP = MP;
        this.maxMP = maxMP;
        skillList = new ArrayList<String>();
    }
    //展示人物技能
    public String showSkills() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < skillList.size(); i++) {
            sb.append(skillList.get(i));
            //如果不是最后一个元素，再添加空格逗号
            if (i != skillList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    //恢复蓝量
    public void recharge(int mp) {
        MP += mp;
        if (MP > maxMP) {
            MP = maxMP;
        }
    }
    //蓝量消耗，蓝量不够释放不了
    public void useMP(int mp) {
        if (MP >= mp) {
            MP -= mp;
        } else {
            System.out.println("蓝量不足！");
        }

    }
}
