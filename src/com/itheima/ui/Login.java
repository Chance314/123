package com.itheima.ui;

import com.itheima.domain.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Login {

    //这个方法表示的就是登陆注册的主页面
    public void start() {
        ArrayList<User> list = new ArrayList<>();

        while (true) {
            System.out.println("╔════════════════════════════════╗");
            System.out.println("    🎮 欢迎来到文字格斗游戏 🎮   ");
            System.out.println("╚════════════════════════════════╝");
            System.out.println("请选择操作：1登录 2注册 3退出 4忘记密码");
            //接下来让用户选择
            Scanner sc = new Scanner(System.in);
            String choice = sc.next();
            switch (choice) {
                case "1" -> login(list);
                case "2" -> register(list);
                case "3" -> {
                    exit();
                    System.exit(0);//退出程序（停止虚拟机运行）
                }
                case "4" -> forgetPassword(list);
                default -> System.out.println("输入有误，请重新输入");
            }
        }
    }

    //登录操作
    public void login(ArrayList<User> list) {
        // 登录功能:
        // 1.键盘录入用户名
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入用户名：");
        String username = sc.next();
        // 不存在：提示用户名不存在，请先注册
        if (!contains(list, username)) {
            System.out.println("用户名不存在,请先注册");
            return;
        }
        //存在：禁用，提示联系客服
        int index = findIndex(list, username);
        User u = list.get(index);
        if (!u.isStatus()) {
            System.out.println("用户被禁用，请联系客服");
            return;
        }


        for (int i = 0; i < 3; i++){
            System.out.println("请输入密码：");
            String password = sc.next();

            while (true) {
                String rightCode = getCode();
                System.out.println("正确的验证码是：" + rightCode);
                System.out.println("请输入验证码:" + "");
                String code = sc.next();
                if (!code.equalsIgnoreCase(rightCode)) {
                    System.out.println("验证码错误，请重新输入");
                    continue;
                }else {
                    System.out.println("验证码正确，登录成功");
                    break;
                }
            }//每次输入密码的时候都输入验证码
            if (password.equals(u.getPassword())) {
                System.out.println("登录成功");
                //创建FightingGame类的对象，并调用方法启动游戏
                FightingGame fg = new FightingGame();
                fg.gameStart(username);
                return;
            } else {
                System.out.println("密码错误，请重新输入");
                if (i == 2){
                    System.out.println("密码错误次数过多，当前账户已被锁定");
                    u.setStatus(false);
                }else {
                    System.out.println("密码错误，还有" + (2 - i) + "次机会");
                }
            }
        }

    }

    //忘记密码操作
    public void forgetPassword(ArrayList<User> list) {
        //忘记密码的规则如下：
        //选择4进入忘记密码操作
        // 输入用户名，查询用户名是否存在
        // 不存在：提示当前用户名未注册
        //存在：输入手机号，并验证
        //手机号正确，输入新密码
        //修改密码
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.next();
        if (!contains(list, username)) {
            System.out.println("当前用户名未注册");
            return;
        }
        int index = findIndex(list, username);
        User u = list.get(index);
        System.out.println("请输入手机号：");
        String phoneNumber = sc.next();
        if (!phoneNumber.equals(u.getPhoneNumber())) {
            System.out.println("手机号错误");
            return;
        }
        while (true) {
            System.out.println("请输入新密码：");
            String newPassword = sc.next();
            if (!checkPassword(newPassword)) {
                System.out.println("密码格式错误");
                continue;
            }
            u.setPassword(newPassword);
            System.out.println("修改密码成功");
            return;
        }
    }

    //在集合中找username的索引
    public int findIndex(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (user.getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    //生成验证码
    public String getCode() {
        //验证码规则：
        //长度为5
        //由4位大写或者小写字母和1位数字组成，同一个字母可重复
        //数字可以出现在任意位置
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxzy";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = r.nextInt(chars.length());
            char c = chars.charAt(index);
            sb.append(c);
        }
        sb.append(r.nextInt(10));
        String code = sb.toString();
        char[] ch = code.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            int index = r.nextInt(ch.length);
            char temp = ch[i];
            ch[i] = ch[index];
            ch[index] = temp;
        }
        return new String(ch);
    }


    //判断字符串长度是否在指定范围之内
    public boolean checkLen(String str, int min, int max) {
        return str.length() >= min && str.length() <= max;
    }

    //统计字符串中字符、数字、其他字符的个数
    public int[] getCount(String str) {
        int charCount = 0;
        int numCount = 0;
        int otherCount = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                charCount++;
            } else if (Character.isDigit(c)) {
                numCount++;
            } else {
                otherCount++;
            }
        }
        return new int[]{charCount, numCount, otherCount};
    }


    //判断用户名是否正确
    public boolean checkUsername(String username) {
        int[] count = getCount(username);
        return count[0] > 0 && count[1] >= 0 && count[2] == 0;


    }

    //判断密码是否正确
    public boolean checkPassword(String password) {
        int[] count = getCount(password);
        return count[0] > 0 && count[1] > 0 && count[2] == 0;
    }

    //判断用户名是否唯一
    public boolean contains(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void register(ArrayList<User> list) {
        System.out.println("用户选择注册");
        //1.创建用户对象

        User u = new User();
        //2.键盘录入用户名         校验用户名
        //* 用户名唯一
        //* 长度必须在3 ~ 16位
        //* 只能由字母、数字组成，不能是纯数字
        while (true) {
            //注册先要输入手机号
            System.out.println("请输入手机号：");
            Scanner sc = new Scanner(System.in);
            String phoneNumber = sc.next();
            if (!checkPhone(phoneNumber)) {
                System.out.println("手机号格式不正确");
                continue;
            }
            u.setPhoneNumber(phoneNumber);
            System.out.println("请输入用户名：");
            sc = new Scanner(System.in);
            String username = sc.next();
            //长度的校验
            if (!checkLen(username, 3, 16)) {
                System.out.println("用户名长度必须在3 ~ 16位");
                continue;
            }
            //判断由字母、数字组成，不能是纯数字
            if (!checkUsername(username)) {
                System.out.println("用户名只能由字母、数字组成，不能是纯数字");
                continue;
            }
            //用户名唯一校验   到list当中判断

            if (contains(list, username)) {
                System.out.println("用户名已存在，请重新输入");
                continue;
            }
            //代码执行到这里说明用户名符合要求
            u.setUsername(username);
            //停止循环
            break;
        }

        //3.键盘录入密码      判断密码是否符合要求
        //* 长度3 ~ 8位
        //* 只能是字母加数字的组合，不能有其他字母
        while (true) {
            //键盘录入密码
            System.out.println("请输入密码：");
            Scanner sc = new Scanner(System.in);
            String password1 = sc.next();
            //再次输入密码
            System.out.println("请再次输入密码：");
            String password2 = sc.next();
            if (!password1.equals(password2)) {//两次输入密码要一致
                System.out.println("两次输入的密码不一致，请重新输入");
                continue;
            }
            if (!checkLen(password1, 3, 8)) {//长度校验
                System.out.println("密码长度必须在3 ~ 8位");
                continue;
            }
            if (!checkPassword(password1)) {//判断密码是否符合要求
                System.out.println("密码只能是字母加数字的组合，不能有其他字母");
                continue;
            }
            u.setPassword(password1);//添加到对象中
            break;//停止循环

        }


        //4.把用户对象添加到集合中
        list.add(u);


        //5.提示用户注册成功
        System.out.println("用户注册成功！");


    }//注册操作

    //
    public void exit() {
        System.out.println("用户选择退出");
    }

    //手机号规则：手机号规则：纯数字，长度11位，只能以数字1为开头
    public boolean checkPhone(String phoneNumber) {
        if (phoneNumber.length()!=11||!phoneNumber.startsWith("1")){
            return false;
        }
        return true;
    }
}
