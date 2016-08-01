package com.zhang.sqone.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
*程序账号中的用户信息（数据属于全局使用的方式）
*@author ZJP
*created at 2016/3/14 15:02
*/
public class User {

    /**用户id*/
    public static String  mid="";
    /**用户code*/
    public static String usercode="";
    /***/
    /**头像地址*/
    public static String IconPath="";
    /**用户手机号码*/
    public static String phone="";
    /**用户是不是登录*/
    public static boolean isLogin;
    /**用户使用的昵称*/
    public static String nc = "";

    /**使用的sid(用户账号)*/
    public static String sid="";
//    是手机还是平板
    public static String type="1";
//    手机唯一标示符
    public static String mis_id="";

    /**用户设置的自动登录*/
    public static  boolean isAuto=true;
    /**消息推送*/
    public static  boolean isPosh=true;
    public static String pwd="";
    /**完成进度*/
    public static String wcjd="0";
    /**通知公告*/
    public static String tzts="0";
    /**消息中心*/
        public static String xxzx="";
    /**待办工作*/
    public static String dbts="0";
    /**收发资料*/
    public static String sfzl="0";
    /*cid推送id*/
    public static String cid ="";
    /**是不是办公室的显示事项通知*/
    public static String sxtz ="0";
    /*是不是显示班子会议查询*/
    public static  String bzhyc="0";
    /*是不是显示班子会议汇总*/
    public static String bzhyh="0";
    /**是不是显示领导日程*/
    public static String leadrc="0";
    /**是不是显示政府章*/
    public static String zhfz="0";
    /**是不是显示党委章*/
    public static String dwz="0";

    //    待办工作(显示不显示的判断)
    public static  String dbgz_1 = "0";
    //    收资料(显示不显示的判断)
    public static  String szl_2 = "0";
    //    在办工作(显示不显示的判断)
    public static  String zbgz_3 = "0";
    //    办结工作(显示不显示的判断)
    public static  String bjgz_4 = "0";
    //    收藏工作(显示不显示的判断)
    public static  String scgz_5 = "0";
    //    班子会议题审核(显示不显示的判断)
    public static  String bzhysh_6 = "0";
    //    班子会议题查询(显示不显示的判断)
    public static  String bzhycx_7 = "0";
    //    传资料(显示不显示的判断)
    public static  String czl_8 = "0";
    //    已发送(显示不显示的判断)
    public static  String yfs_9 = "0";
    //    会议室查询(显示不显示的判断)
    public static  String hyscx_10 = "0";
    //    我的文件(显示不显示的判断)
    public static  String wdwj_11 = "0";
    //    我的工资(显示不显示的判断)
    public static  String wdgz_12 = "0";
    //    党委文件(显示不显示的判断)
    public static  String dwwj_13 = "0";
    //    政府文件(显示不显示的判断)
    public static  String zfwj_14 = "0";
    //    天竺镇情(显示不显示的判断)
    public static  String ttzq_15 = "0";
    //    政府报告(显示不显示的判断)
    public static  String zfbg_16 = "0";
    //    党建学习(显示不显示的判断)
    public static  String djxx_17 = "0";
    //    廉政教育(显示不显示的判断)
    public static  String lzjy_18 = "0";
    //    规章制度(显示不显示的判断)
    public static  String gzzd_19 = "0";
    //    业务学习(显示不显示的判断)
    public static  String ywxx_20 = "0";
    //    参考资料(显示不显示的判断)
    public static  String ckzl_21 = "0";
    //    环境监测(显示不显示的判断)
    public static  String hjjc_22 = "0";
    //    领导日程(显示不显示的判断)
    public static  String ldrc_23 = "0";
    //    日程安排(显示不显示的判断)
    public static  String rcap_24 = "0";
    //    事项通知(显示不显示的判断)
    public static  String tzsx_25 = "0";
    //    党委章查询(显示不显示的判断)
    public static  String dwzcx_26 = "0";
    //    政府章查询(显示不显示的判断)
    public static  String zfzcx_27 = "0";
//    班子会议上报(显示不显示的判断)
    public static String bzhysb_28="0";
    /**是不是显示流程*/
    /**事假申请*/
    public static  String sjsq_28 = "0";
    //    病假申请(显示不显示的判断)
    public static  String bjsq_29 = "0";
    //    年假申请(显示不显示的判断)
    public static  String njsq_30 = "0";
    //    党委章(显示不显示的判断)
    public static  String dwz_31 = "0";
    //    政府章(显示不显示的判断)
    public static  String zfz_32 = "0";
    //    会议室预定(显示不显示的判断)
    public static  String hyscx_33 = "0";
    //    党委发文(显示不显示的判断)
    public static  String dwfw_34 = "0";
    //    政府发文(显示不显示的判断)
    public static  String zffw_35 = "0";
    //    会议材料管理
    public static String hyclgl_36="1";



    //保存用户信息到本地
    public static void saveLoginInfo(Context context) {
        // 获取SharedPreferences对象
        SharedPreferences sharedPre = context.getSharedPreferences("config",
                context.MODE_PRIVATE);
        // 获取Editor对象
        Editor editor = sharedPre.edit();
        // 设置参数
        editor.putString("phone", phone);
        editor.putBoolean("isLogin", isLogin);
        editor.putString("IconPath", IconPath);
        // 提交
        editor.commit();
    }
    /**添加user用户的信息*/
    public static void setLoginInfo(String phone, Boolean isLogin,
                                     String iconPath,String Nc) {
        User.phone = phone;
        User.isLogin = isLogin;
        User.IconPath = iconPath;
        User.nc = Nc;
    }
}
