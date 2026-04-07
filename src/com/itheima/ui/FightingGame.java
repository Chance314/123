package com.itheima.ui;

import com.itheima.domain.EnemyCharacter;
import com.itheima.domain.HeroCharacter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightingGame {

    //启动游戏
    public void gameStart(String username) {
        //1.测试游戏标题
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("    🎮" + username + "欢迎来到文字格斗游戏 🎮 ");
        System.out.println("╚════════════════════════════════════════╝");
        //创建玩家角色（名字＋属性分配）
        HeroCharacter player = createPlayerCharacter(username);
        System.out.println("角色创建成功");
        //显示角色属性
        player.show();
        //角色技能
        System.out.println("拥有的技能：" + player.showSkills());
        //创建敌人列表
//  | 敌人名称 | 生命值 | 攻击力 | 防御力 | 技能（变量）                                           |
//| -------- | ------ | ------ | ------ | ------------------------------------------------------ |
//  | 初级战士 | 80     | 15     | 10     | 猛击（150%伤害）                                       |
//| 敏捷刺客 | 60     | 20     | 5      | 快速攻击（2次50%伤害）                                 |
//| 重装坦克 | 120    | 10     | 20     | 防御姿态（下回合伤害减半） buff（ boolean defendding） |
//| 神秘法师 | 70     | 25     | 8      | 火球术（180%伤害）
        ArrayList<EnemyCharacter> enemyList = new ArrayList<>();
        enemyList.add(new EnemyCharacter("初级战士", 80, 80, 15, 10, "猛击"));
        enemyList.add(new EnemyCharacter("敏捷刺客", 60, 60, 20, 5, "快速攻击"));
        enemyList.add(new EnemyCharacter("重装坦克", 120, 120, 10, 20, "防御姿态"));
        enemyList.add(new EnemyCharacter("神秘法师", 70, 70, 25, 8, "火球术"));

        //准备战斗
        int count = 1;//记录战斗次数
        int wins = 0;//统计战斗胜利次数
        while (player.isAlive()) {
            //进入战斗
            // 第一场战斗：怪物是基础属性玩家每连胜一场：怪物属性增加HP+10, ATK+3, DEF+2
            if (count > 1) {
                //获取每一个敌人的信息，进行属性点增加
                for (int i = 0; i < enemyList.size(); i++) {
                    EnemyCharacter enemy = enemyList.get(i);
                    enemy.HP += 10;
                    enemy.maxHP += 3;
                    enemy.attack += 3;
                    enemy.defense += 2;
                    enemy.defending = false;//重置防御状态
                }
            }
            //随机选择敌人
            Random r = new Random();
            int index = r.nextInt(enemyList.size());
            EnemyCharacter enemy = enemyList.get(index);
            enemy.show();
            //开始跟敌人战斗（结算，回血）
            System.out.println("═══════════════════════════════════════");
            //第几场战斗开始，敌人是
            System.out.println("⚔️ 第 " + count + "场战斗开始！对手:" + enemy.name);//战斗结束count + 1了
            int round = 1;
            //第i场战斗
            while (player.isAlive()) {
                //显示双方状态
                System.out.println("═══════════════════════════════════════");
                System.out.println("⚔️ 第 " + round + "回合开始");
                //打印敌我双方血条
                //zhangsan: [████████████████████] 100/100 HP
                //初级战士: [████████████████████] 80/80 HP
                getHealthBar(player.name, player.HP, player.maxHP);
                getHealthBar(enemy.name, enemy.HP, enemy.maxHP);
                //===== 你的回合 =====
                //1. 普通攻击
                //2. 强力一击 (消耗10MP)
                //3. 生命汲取 (消耗10MP，恢复生命)
                //选择行动 (1-3): 2
                playerTurn(player, enemy);//玩家回合

                //判断敌人是否被击败
                if (!enemy.isAlive()) {
                    //战斗胜利，wins + 1
                    wins++;
                    //结束战斗
                    break;
                }
                //敌人回合
                enemyTurn(player, enemy);
                //判断玩家是否被击败
                if (!player.isAlive()){
                    System.out.println("你被" + enemy.name + "击败了");
                    //战斗结束
                    break;
                }
                //这一回合结束
                round++;
            }
            //战斗结束,玩家胜利时恢复20-40点HP，胜场数+1，每三胜获得属性提升，失败时游结束
            //🎉 你击败了 敏捷刺客！
            //💚 战斗结束！你恢复了 36 点生命值
            //🏆 当前胜场: 1
            if (player.isAlive()) {
                System.out.println("🎉 你击败了" + enemy.name + "！");
                //恢复20-40点HP
                int healHP = r.nextInt(20,41);
                //击败一个敌人恢复最大蓝量的30%
                int healMp = player.maxMP* 30 / 100;
                player.recharge(healMp);
                player.heal(healHP);
                System.out.println("💚 战斗结束！你恢复了" + healHP + "点生命值,"+healMp+"点蓝量");
                System.out.println("🏆 当前胜场: " + wins);
                System.out.println("===============================");
                if (wins % 3 == 0){
                    System.out.println("🎉 恭喜你获得属性提升");
                    player.maxHP += 30;//最大生命
                    player.attack += 5;//攻击力
                    player.defense += 3;//防御力
                    player.maxMP += 10;
                    player.show();
                }
                //询问玩家是否继续战斗
                System.out.println("是否继续战斗？(Y/N)");
                Scanner sc = new Scanner(System.in);
                String choice = sc.next();
                if (choice.equalsIgnoreCase("N")) {
                    System.out.println("游戏结束！");
                    break;
                }else if (choice.equalsIgnoreCase("Y")){
                    count++;//第几个敌人
                    continue;//结束此次循环，准备跟下一个敌人战斗
                }else {
                    System.out.println("没有这个选项，默认游戏继续");
                    count++;
                    continue;
                }
            }
        }
        //最终结算
        System.out.println("═══════════════════════════════");
        System.out.println("游戏结束！");
        System.out.println("总胜场数：" + wins);
        //感谢游玩文字版格斗游戏
        System.out.println("感谢游玩文字版格斗游戏");
        System.exit(0);
    }


    //打印敌我双方血条
    public void getHealthBar(String name, int HP, int maxHP) {
        int barLength = 20;
        int filled = (int) ((HP * 1.0 / maxHP) * barLength);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                sb.append("█");
            }
        }
        System.out.println(name + ": [" + sb.toString() + "] " + HP + "/" + maxHP + " HP");
    }


    //创建玩家角色
    public HeroCharacter createPlayerCharacter(String username) {
        System.out.println("创建您的角色");
        System.out.println("您的角色名为：" + username);//您的角色名为
        int points = 20;//初始有20点属性点
        System.out.println("请分配属性点：(共20点)");
        //生命值属性点分配
        System.out.println("1.生命值（每点+10HP）：");

        //攻击力属性点分配
        System.out.println("2.攻击力（每点+2 ATK）：");

        //防御力属性点分配
        System.out.println("3.防御力（每点+1 DEF）：");
        //蓝量属性点分配
        System.out.println("4.蓝量（每点+10 MP）：");
        //定义数组，保存属性名称
        String[] attributes = {"生命值", "攻击力", "防御力", "蓝量"};
        //定义数组记录属性分配的属性点
        Scanner sc = new Scanner(System.in);
        int[] values = new int[4];
        for (int i = 0; i < attributes.length; i++) {
            System.out.println("分配点数到" + attributes[i] + "(剩余点数：" + points + "):");
            int input = sc.nextInt();
            if (input < 0) {
                //无效输入，默认分配0点
                System.out.println("无效输入，默认分配0点");
                input = 0;
            }
            if (input > points) {
                //无效输入，默认分配剩余点数
                System.out.println("属性点不足！剩余属性点全部分配到" + attributes[i] + "中");
                input = points;
            }
            points -= input;
            values[i] = input;//把分配的点数保存到数组中
        }
        //创建玩家角色        player.name = username;
        //        player.HP = values[0] * 10+ 100;//初始生命100+10*属性点
        //        player.attack = values[1] * 2 + 10;//初始攻击10+2*属性点
        //        player.defense = values[2] + 0;//初始防御0+属性点
        //        player.MP = values[3] * 10 + 100;
        HeroCharacter player = new HeroCharacter(username,
                values[0] * 10 + 100,
                values[0] * 10 + 100,
                values[1] * 2 + 10,
                values[2] + 0,
                values[3] * 10 + 100,
                values[3] * 10 + 100
);
        //添加玩家技能
        player.skillList.add("普通攻击");
        player.skillList.add("强力一击");
        player.skillList.add("生命汲取");
        return player;

    }

    //玩家回合
    public void playerTurn(HeroCharacter player, EnemyCharacter enemy) {

        System.out.println("========你的回合========");
        System.out.println("1. 普通攻击");
        System.out.println("2. 强力一击 (消耗10MP)");//换成蓝量
        System.out.println("3. 生命汲取 (消耗10MP，恢复生命)");//换成蓝量
        System.out.println("选择行动 (1-3):");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();//输入其他数字默认为普通攻击
        switch (choice) {
            case 1 -> {
                //我方攻击---基础伤害公式：伤害=攻击力-敌人防御力，最小伤害为1
                int damage1 = calculateDamage(player.attack, enemy.defense);
                //💥 你对  使用了普通攻击，造成  点伤害！
                System.out.println("💥 你对 " + enemy.name + "使用了普通攻击，造成 " + damage1 + " 点伤害！");
                //敌人扣血
                enemy.takeDamage(damage1);
                break;
            }
            case 2 -> {
                //先判断当前生命够不够使用强力一击
                if (player.MP > 10) {
                    //消耗10MP
                    player.useMP(10);
                    //技能攻击伤害计算（我方攻击力*百分比，对方攻击力）
                    int damage2 = calculateDamage((int) (player.attack * 1.8), enemy.attack);//技能增幅1.8
                    //我给对方造成伤害
                    System.out.println("💥消耗10HP，你对 " + enemy.name + "使用了强力一击，造成 " + damage2 + " 点伤害！");
                    //敌人扣血
                    enemy.takeDamage(damage2);
                } else {
                    System.out.println("你的蓝量不足，无法使用强力一击！");
                }
            }

            case 3 -> {
                if (player.MP > 10) {
                    player.useMP(10);
                    //恢复0-20点生命值
                    Random r = new Random();
                    int healHP = r.nextInt(21);//随机恢复生命值
                    player.heal(healHP);
                    System.out.println("你恢复 " + healHP + " 点生命值！");
                } else {
                    System.out.println("你的蓝量不足，无法使用生命汲取！");
                }
            }
            default -> {
                System.out.println("没有这个操作，默认使用普通攻击");
                int damage3 = calculateDamage(player.attack, enemy.defense);
                //💥 你对  使用了普通攻击，造成  点伤害！
                System.out.println("💥 你对 " + enemy.name + "使用了普通攻击，造成 " + damage3 + " 点伤害！");
                //敌人扣血
                enemy.takeDamage(damage3);
                break;
            }
        }

    }

    //普通攻击伤害计算（我方攻击力，敌方防御力）
    public int calculateDamage(int attack, int enemyDefense) {
        int damage = attack - enemyDefense;
        if (damage > 1) {
            return damage;
        } else {
            return 1;//最小伤害为1
        }

    }

    //敌人回合
    public void enemyTurn(HeroCharacter player, EnemyCharacter enemy) {
        System.out.println("========" + enemy.name + "的回合========");
        //敌人对你使用什么技能，造成多少伤害，不同敌人的技能效果不同
        //敌人50%技能攻击，50%普通攻击
        //敌人默认普通攻击
        String action = "普通攻击";

        Random r = new Random();
        int skillOrAttack = r.nextInt(2);//0-1控制攻击方式

        if (skillOrAttack == 0) {
            //技能攻击
            action = enemy.skill;
        } else {
            //普通攻击
            action = "普通攻击";
        }
        switch (action) {
            case "普通攻击" -> {
                int damage1 = calculateDamage(enemy.attack, player.defense);//计算敌人普通攻击伤害
                System.out.println("💥 " + enemy.name + " 对你使用了普通攻击，造成 " + damage1 + " 点伤害！");
                player.takeDamage(damage1);
                break;
            }
            //猛击（150%伤害）
            case "猛击" -> {
                int damage2 = calculateDamage((int) (enemy.attack * 1.5), player.defense);
                System.out.println("💥 " + enemy.name + " 对你使用了猛击，造成 " + damage2 + " 点伤害！");
                player.takeDamage(damage2);
                break;
            }
            //快速攻击（2次50%伤害）
            case "快速攻击" -> {
                int damage3 = 0;
                for (int i = 0; i < 2; i++) {
                    damage3 += calculateDamage((int) (enemy.attack * 0.5), player.defense);
                }
                System.out.println("💥 " + enemy.name + " 对你使用了快速攻击，造成 " + damage3 + " 点伤害！");
                player.takeDamage(damage3);
                break;
            }
            //防御姿态（下回合伤害减半） buff（ boolean defendding）
            case "防御姿态" -> {
                enemy.defending = true;
                System.out.println(enemy.name + " 摆出了防御姿态，下回合伤害减半！");
                break;
            }
            //火球术（180%伤害）
            case "火球术" -> {
                int damage4 = calculateDamage((int) (enemy.attack * 1.8), player.defense);
                System.out.println("💥 " + enemy.name + " 对你使用了火球术，造成 " + damage4 + " 点伤害！");
                player.takeDamage(damage4);
                break;
            }
        }

    }
}

