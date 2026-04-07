package com.itheima.domain;

import java.util.Random;
import java.util.Scanner;

public class User {
    private String id;
    private String username;
    private String password;
    private boolean status;//false禁用，true可用
    //新增手机号
    private String phoneNumber;

    public User() {
        //创建用户时创建id
        id = creatId();
        //默认状态可用
        status = true;
    }
    public User(String id, String username, String password, boolean status, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    //生成随机id,id格式为heima＋五位随机数字
    public  String creatId(){
        StringBuilder sb = new StringBuilder();
        sb.append("heima");
        Random r = new Random();
        sb.append(r.nextInt(10000, 100000));
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
