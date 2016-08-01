package com.zhang.sqone.bean;

/**
 * Created by zjp on 2016/5/14.
 */
public class WenJianBean {

    /**
     * Stu : 1
     * Rst : {"Scd":1,"Msg":"上传成功！","Po":"1"}
     */

    private int Stu;
    /**
     * Scd : 1
     * Msg : 上传成功！
     * Po : 1
     */

    private RstBean Rst;

    public int getStu() {
        return Stu;
    }

    public void setStu(int Stu) {
        this.Stu = Stu;
    }

    public RstBean getRst() {
        return Rst;
    }

    public void setRst(RstBean Rst) {
        this.Rst = Rst;
    }

    public static class RstBean {
        private int Scd;
        private String Msg;
        private String Po;

        public int getScd() {
            return Scd;
        }

        public void setScd(int Scd) {
            this.Scd = Scd;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }

        public String getPo() {
            return Po;
        }

        public void setPo(String Po) {
            this.Po = Po;
        }
    }
}
