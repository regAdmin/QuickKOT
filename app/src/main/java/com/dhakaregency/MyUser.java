package com.dhakaregency;

/**
 * Created by Administrator on 16/03/2016.
 */
public class MyUser {
    private String userid;
    private String password;

    public MyUser(String _userid,String _password){
        this.userid=_userid;
        this.password=_password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
