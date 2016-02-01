package com.zhang.sqone.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class User {

    public static String IconPath;
    public static String phone;
    public static boolean isLogin;
    public static String sessionId = "";
    public static String nc = "";
    public static String mSqid = "";
    public static String mSh="";

    public static String getIconPath() {

        return IconPath;
    }

    public static void setIconPath(String iconPath) {

        IconPath = iconPath;
    }

    public static String getPhone() {
        return phone;
    }

    public static String getSessionId()
    {
        return sessionId;
    }

    public static void saveLoginInfo(Context context) {
        // 获取SharedPreferences对象
        SharedPreferences sharedPre = context.getSharedPreferences("config",
                context.MODE_PRIVATE);
        // 获取Editor对象
        Editor editor = sharedPre.edit();
        // 设置参数
        editor.putString("phone", phone);
        editor.putBoolean("isLogin", isLogin);
        editor.putString("sessionId", sessionId);
        editor.putString("IconPath", IconPath);
        // 提交
        editor.commit();
    }

    public static void setLoginInfo(String phone, Boolean isLogin,
                                    String sessionId, String iconPath,String Sqid,String Sh) {
        User.phone = phone;
        User.isLogin = isLogin;
        User.sessionId = sessionId;
        User.IconPath = iconPath;
        User.mSqid = Sqid;
        User.mSh = Sh;
    }
}