package com.zhang.sqone.bean;
//登陆返回的数据
//{
//		Stu: 1,
//		Rst: {
//		Scd: 1,
//		Msg: "登录成功！",
//		Sid: "13811767797",
//		Ph: "http://192.168.1.161:8099/niuls/attach/201510/1.png",
//		Sqid: "1",
//		Sh: "1"
//		}
//		}

/**这个数据传输方式不使用这个方式*/
public class LoginResult {

    public Integer Stu;
    public LongResult Rst; // 企业会员

    public class LongResult {
        public Integer Scd;// 返回状态值
        public String Msg; // 返回字符串
        public String Sid; // sid手机号
        public String Ph; // 用户头像
//        public String UN; //昵称
        public String Sqid;//返回用户的登陆的状态
        public String Sh;//审核状态
    }

}

