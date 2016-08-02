package com.zhang.sqone;

import android.content.Context;

/**
 * 全局变量类  静态常量
 */

public class Globals {

    public static int maxDisplaySize, screenWidth, screenHeight,thumbnailMaxVal, thumbnailMinVal;
    public static Context context; // 数据上下文
    public static final String SPLASH_USERTYPE = "USERTYPE";
    public static final String DY_ID = "lm";// 社区子模块类型的id
    public static final String DY_LC = "lc";//社区子模块类型的名字
    public static final String DY_XG = "xg";//社区子模块是否显示图片
    public static final String WEB_URL = "url";
    public static final String KEY_URL = "par";
    public static final String ID_URL = "id";

    public static final String TITLE_URL = "title";
    public static final String LX_LX = "mlx";
    public static final String SHARED_LOCA = "SHARED_LOCA";
    public static final String SHARED_LOCA_CODE = "SHARED_LOCA_CODE";
    public static final String USER_PHONE = "userPhoene";
    public static final String USER_JJMM = "jjmm";
    public static final String USER_PASSWORD = "userpassword";
    public static final String USER_YD="userdl";

    /**存储的是用户的头像*/
    public static final String USER_SH = "touxiang";
    /**唯一标示id*/
    public static final String WY_id = "wyid";
    public static final String C_id = "cid";
    public static final String I_isponh = "isponh";
    public static final String USER_NC = "usernc";
    public static String USER_TYPE = ""; // 账号类型 q表示求职者 z表示招聘者
    public static String LOCA = "北京市"; // 二级地区
    public static String LOCACODE = "110100"; // 二级地区代码
    public static final String PREFS = "PREFS";
    public static final String LOG_TAG = "RESPONSE";
    public static final String LOG_TAG2 = "RESPONSE2";

    /*开发环境*/
//    /**环境上传图片*/
//    public static final String BM_URL = "http://192.168.1.161:8280/MonitorPhoto";
//    /**头像上传图片*/
//    public static final String WS_URI_POTO = "http://192.168.1.161:8280/UserPhoto";
//    /**文件上传文件*/
//    public static final String WS_URI_WJ = "http://192.168.1.161:8280/sickleave";
//    /**文件上传文件(传资料)*/
//    public static final String WS_CZL_WJ = "http://192.168.1.161:8280/email";
//    /**服务器网络请求地址*/
//    public static final String WS_URI = "http://192.168.1.161:8280/Index";
//    /**网络请求公文的地址*/
//    public static  final String GW_URI ="http://192.168.1.161:8280/Document";
//    /**网络请求环境的地址*/
//    public static final String HJ_URI="http://192.168.1.161:8280/Monitor";
//    /**领导日程  和 日程安排*/
//    public static final  String LD_RUI = "http://192.168.1.161:8280/schedulemk";
//    /**网络请求OA的地址*/
//    public static final String OA_QQ="http://192.168.1.161:8280/login.cmd";
//
//    /**科室工作的地址接口*/
//    public static  final String KS_URI ="http://192.168.1.161:8280/DepartmentWork";
//
//    /**科室工作查询接口*/
//    public static  final String KSCX_URI ="http://192.168.1.161:8280/DepartmentWorkDoc";
//    /**会议查询接口*/
//    public static final String HYAP_URI = "http://192.168.1.161:8280/MeetingRoom";
//    /**班子会议接口*/
//    public static final String BZHY_URI = "http://192.168.1.161:8280/meetingDiscuss";
//    /**通知公告  和 通信录的接口*/
//    public static final String TZTX_URI="http://192.168.1.161:8280/NoticeContacts";
//    /**资料接口*/
//    public static final String ZL_URI="http://192.168.1.161:8280/outbox";
//    /**h5中使用的连接地址*/
//    public static final String MY_URI="http://192.168.1.161:8280/";
//    /**值班安排  ，我的档案*/
//    public static final String ZB_URI="http://192.168.1.161:8280/myself";
//    public static final String VERSION_XML = "http://192.168.1.161:8280/SoftUpdate/Update.xml";
//    public static final String GX = "http://192.168.1.161:8280/test11";
//    public static final String JBD_BS = "161";

/*开发环境*/
    /**环境上传图片*/
    public static final String BM_URL = "http://192.168.1.50:8280/MonitorPhoto";
    /**头像上传图片*/
    public static final String WS_URI_POTO = "http://192.168.1.50:8280/UserPhoto";
    /**文件上传文件*/
    public static final String WS_URI_WJ = "http://192.168.1.50:8280/sickleave";
    /**文件上传文件(传资料)*/
    public static final String WS_CZL_WJ = "http://192.168.1.50:8280/email";
    /**服务器网络请求地址*/
    public static final String WS_URI = "http://192.168.1.50:8280/Index";
    /**网络请求公文的地址*/
    public static  final String GW_URI ="http://192.168.1.50:8280/Document";
    /**网络请求环境的地址*/
    public static final String HJ_URI="http://192.168.1.50:8280/Monitor";
    /**领导日程  和 日程安排*/
    public static final  String LD_RUI = "http://192.168.1.50:8280/schedulemk";
    /**网络请求OA的地址*/
    public static final String OA_QQ="http://192.168.1.50:8280/login.cmd";

    /**科室工作的地址接口*/
    public static  final String KS_URI ="http://192.168.1.50:8280/DepartmentWork";

    /**科室工作查询接口*/
    public static  final String KSCX_URI ="http://192.168.1.50:8280/DepartmentWorkDoc";
    /**会议查询接口*/
    public static final String HYAP_URI = "http://192.168.1.50:8280/MeetingRoom";
    /**班子会议接口*/
    public static final String BZHY_URI = "http://192.168.1.50:8280/meetingDiscuss";
    /**通知公告  和 通信录的接口*/
    public static final String TZTX_URI="http://192.168.1.50:8280/NoticeContacts";
    /**资料接口*/
    public static final String ZL_URI="http://192.168.1.50:8280/outbox";
    /**h5中使用的连接地址*/
    public static final String MY_URI="http://192.168.1.50:8280/";
    /**值班安排  ，我的档案*/
    public static final String ZB_URI="http://192.168.1.50:8280/myself";
    public static final String VERSION_XML = "http://192.168.1.50:8280/SoftUpdate/Update.xml";
    public static final String GX = "http://192.168.1.50:8280/test11";
    public static final String JBD_BS = "50";


      /*开发环境*/
//    /**环境上传图片*/
//    public static final String BM_URL = "http://192.168.1.110:8280/MonitorPhoto";
//    /**头像上传图片*/
//    public static final String WS_URI_POTO = "http://192.168.1.110:8280/UserPhoto";
//    /**文件上传文件*/
//    public static final String WS_URI_WJ = "http://192.168.1.110:8280/sickleave";
//    /**文件上传文件(传资料)*/
//    public static final String WS_CZL_WJ = "http://192.168.1.110:8280/email";
//    /**服务器网络请求地址*/
//    public static final String WS_URI = "http://192.168.1.110:8280/Index";
//    /**网络请求公文的地址*/
//    public static  final String GW_URI ="http://192.168.1.110:8280/Document";
//    /**网络请求环境的地址*/
//    public static final String HJ_URI="http://192.168.1.110:8280/Monitor";
//    //**领导日程  和 日程安排*/
//    public static final  String LD_RUI = "http://192.168.1.110:8280/schedulemk";
//    //**我的测试使用*/
//    public static final  String MY_CS = "http://192.168.1.110:8280/zhangCs";
//    /**网络请求OA的地址*/
//    public static final String OA_QQ="http://192.168.1.110:8280/login.cmd";
//
//    /**科室工作的地址接口*/
//    public static  final String KS_URI ="http://192.168.1.110:8280/DepartmentWork";
//
//    /**科室工作查询接口*/
//    public static  final String KSCX_URI ="http://192.168.1.110:8280/DepartmentWorkDoc";
//    /**会议查询接口*/
//    public static final String HYAP_URI = "http://192.168.1.110:8280/MeetingRoom";
//    /**班子会议接口*/
//    public static final String BZHY_URI = "http://192.168.1.110:8280/meetingDiscuss";
//    /**通知公告  和 通信录的接口*/
//    public static final String TZTX_URI="http://192.168.1.110:8280/NoticeContacts";
//    /**资料接口*/
//    public static final String ZL_URI="http://192.168.1.110:8280/outbox";
//    /**h5中使用的连接地址*/
//    public static final String MY_URI="http://192.168.1.110:8280/";
//    /**值班安排  ，我的档案*/
//    public static final String ZB_URI="http://192.168.1.110:8280/myself";
//    public static final String VERSION_XML = "http://192.168.1.110:8280/SoftUpdate/Update.xml";


    /*正式的环境*/
//    /**环境上传图片*/
//    public static final String BM_URL = "http://work.zhihuitianzhu.com/MonitorPhoto";
//    /**头像上传图片*/
//    public static final String WS_URI_POTO = "http://work.zhihuitianzhu.com/UserPhoto";
//    /**服务器网络请求地址*/
//    public static final String WS_URI = "http://work.zhihuitianzhu.com/Index";
//    /**网络请求公文的地址*/
//    public static  final String GW_URI ="http://work.zhihuitianzhu.com/Document";
//    /**网络请求环境的地址*/
//    public static final String HJ_URI="http://work.zhihuitianzhu.com/Monitor";
//    /**文件上传文件(传资料)*/
//    public static final String WS_CZL_WJ = "http://work.zhihuitianzhu.com/email";
//    /**网络请求OA的地址*/
//    public static final String OA_QQ="http://work.zhihuitianzhu.com/login.cmd";
//    /**科室工作的地址接口*/
//    public static  final String KS_URI ="http://work.zhihuitianzhu.com/DepartmentWork";
//
//    /**领导日程  和 日程安排*/
//    public static final  String LD_RUI = "http://work.zhihuitianzhu.com/schedulemk";
//    /**科室工作查询接口*/
//    public static  final String KSCX_URI ="http://work.zhihuitianzhu.com/DepartmentWorkDoc";
//    /**会议查询接口*/
//    public static final String HYAP_URI = "http://work.zhihuitianzhu.com/MeetingRoom";
//    /**通知公告  和 通信录的接口*/
//    public static final String TZTX_URI="http://work.zhihuitianzhu.com/NoticeContacts";
//    /**h5中使用的连接地址*/
//    public static final String MY_URI="http://work.zhihuitianzhu.com/";
//    /**文件上传文件*/
//    public static final String WS_URI_WJ = "http://work.zhihuitianzhu.com/sickleave";
//    /**资料接口*/
//    public static final String ZL_URI="http://work.zhihuitianzhu.com/outbox";
//
//        /**班子会议接口*/
//    public static final String BZHY_URI = "http://work.zhihuitianzhu.com/meetingDiscuss";
//    /**值班安排  ， 我的档案*/
//    public static final String ZB_URI="http://work.zhihuitianzhu.com/myself";
//    public static final String VERSION_XML = "http://work.zhihuitianzhu.com/SoftUpdate/Update.xml";
//    public static final String GX = "http://work.zhihuitianzhu.com/test11";



    /*外网测试环/
   /**环境上传图片*/
//    public static final String BM_URL = "http://test.zhihuitianzhu.com/MonitorPhoto";
//    /**头像上传图片*/
//    public static final String WS_URI_POTO = "http://test.zhihuitianzhu.com/UserPhoto";
//    /**服务器网络请求地址*/
//    public static final String WS_URI = "http://test.zhihuitianzhu.com/Index";
//    /**网络请求公文的地址*/
//    public static  final String GW_URI ="http://test.zhihuitianzhu.com/Document";
//    /**网络请求环境的地址*/
//    public static final String HJ_URI="http://test.zhihuitianzhu.com/Monitor";
//    /**网络请求OA的地址*/
//    public static final String OA_QQ="http://test.zhihuitianzhu.com/login.cmd";
//    /**科室工作的地址接口*/
//    public static  final String KS_URI ="http://test.zhihuitianzhu.com/DepartmentWork";
//    /**领导日程  和 日程安排*/
//    public static final  String LD_RUI = "http://test.zhihuitianzhu.com/schedulemk";
//    /**文件上传文件(传资料)*/
//    public static final String WS_CZL_WJ = "http://test.zhihuitianzhu.com/email";
//    /**科室工作查询接口*/
//    public static  final String KSCX_URI ="http://test.zhihuitianzhu.com/DepartmentWorkDoc";
//    /**会议查询接口*/
//    public static final String HYAP_URI = "http://test.zhihuitianzhu.com/MeetingRoom";
//    /**通知公告  和 通信录的接口*/
//    public static final String TZTX_URI="http://test.zhihuitianzhu.com/NoticeContacts";
//    /**h5中使用的连接地址*/
//    public static final String MY_URI="http://test.zhihuitianzhu.com/";
//    /**文件上传文件*/
//    public static final String WS_URI_WJ = "http://test.zhihuitianzhu.com/sickleave";
//    /**资料接口*/
//    public static final String ZL_URI="http://test.zhihuitianzhu.com/outbox";
//        /**班子会议接口*/
//    public static final String BZHY_URI = "http://test.zhihuitianzhu.com/meetingDiscuss";
//
//    /**值班安排  ， 我的档案*/
//    public static final String ZB_URI="http://test.zhihuitianzhu.com/myself";
//    public static final String VERSION_XML = "http://test.zhihuitianzhu.com/SoftUpdate/Update.xml";
//    public static final String GX = "http://test.zhihuitianzhu.com/test11";

 /*公司测试环境*/
//    /**环境上传图片*/
//    public static final String BM_URL = "http://192.168.1.234:8280/MonitorPhoto";
//    /**头像上传图片*/
//    public static final String WS_URI_POTO = "http://192.168.1.234:8280/UserPhoto";
//    /**服务器网络请求地址*/
//    public static final String WS_URI = "http://192.168.1.234:8280/Index";
//    /**网络请求公文的地址*/
//    public static  final String GW_URI ="http://192.168.1.234:8280/Document";
//    /**网络请求环境的地址*/
//    public static final String HJ_URI="http://192.168.1.234:8280/Monitor";
//    /**文件上传文件(传资料)*/
//    public static final String WS_CZL_WJ = "http://192.168.1.234:8280/email";
//    /**领导日程  和 日程安排*/
//    public static final  String LD_RUI = "http://192.168.1.234:8280/schedulemk";
//    /**网络请求OA的地址*/
//    public static final String OA_QQ="http://192.168.1.234:8280/login.cmd";
//    /**科室工作的地址接口*/
//    public static  final String KS_URI ="http://192.168.1.234:8280/DepartmentWork";
//
//    /**科室工作查询接口*/
//    public static  final String KSCX_URI ="http://192.168.1.234:8280/DepartmentWorkDoc";
//    /**会议查询接口*/
//    public static final String HYAP_URI = "http://192.168.1.234:8280/MeetingRoom";
//    /**通知公告  和 通信录的接口*/
//    public static final String TZTX_URI="http://192.168.1.234:8280/NoticeContacts";
//    /**h5中使用的连接地址*/
//    public static final String MY_URI="http://192.168.1.234:8280/";
//    /**值班安排  ， 我的档案*/
//    public static final String ZB_URI="http://192.168.1.234:8280/myself";
//    /**文件上传文件*/
//    /**班子会议接口*/
//    public static final String BZHY_URI = "http://192.168.1.234:8280/meetingDiscuss";
//    public static final String WS_URI_WJ = "http://192.168.1.234:8280/sickleave";
//    /**资料接口*/
//    public static final String ZL_URI="http://192.168.1.234:8280/outbox";
//    public static final String VERSION_XML = "http://192.168.1.234:8280/SoftUpdate/Update.xml";
// public static final String GX = "http://192.168.1.234:8280/test11";
//    public static final String JBD_BS = "234";
    /**activity的tab判断界面*/
    public static final String TAB ="tab";
    /**判定是第三方验证还是修改密码的使用*/
    public  static final String YZ ="yz";
    /**文件详情的id*/
    public static final String XQID ="xqid";

    /**公文管理的list（详情）*/

    public static final String LIDS="lids";

    /**公文管理Wid（详情）*/
//    public static  final String WID="wid";
    /**文档管理中的参数key*/
    public static final String WDGL="wdgl";
    /**资料文件的key*/
    public static final String ZLWJ="zlwj";
    /**公文管理中的参数key*/
    public static final String GWGL="gwgl";
    /**请假标识*/
    public static final String QJBS="gwgl";
    /**环境地址的回传的单位id*/
    public static final String HJ_DZ_DW="dwid";

    /**环境地址的回传的地址name*/
    public static final String HJ_DZ_NA="dzna";
    /**环境地址的回传的地址id*/
    public static final String HJ_DZ_ID="dzid";
    /**环境内容的回传id*/
    public static final String HJ_NR="hjnr";

    /**环境内容分数*/
    public static final String HJ_FS="hjfs";
    /**公文管理的判断*/
    public static final String BDID = "bdid";
/**查询科室的id*/
    public static final String KSCID = "kscid";

    /**科室图片*/
    public static final String KSTP = "ketp";
    /**科室名称*/
    public static final String KSMC = "kemc";
    /**科室文本*/
    public static final String KSTX = "ketx";

    /**标识符*/
    public static final String BSF = "bsf";



    public static final String WS_POST_KEY = "id";
    public static final String SH_ID = "sqid";
    public static final String MIME_HTML = "text/html";
    public static final String ENCODING_UTF8 = "utf-8";
    public static final String EMPTY = "";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String JOB = "J";
    public static final String PROJECT_ID = "PID";
    public static final String LOCATION = "L";
    public static final String K_TYPE = "KTYPE";
    public static final String K_ID = "KID";
    public static final String MSG_QUERY_TITLE = "正在查询";
    public static final String MSG_QUERY_INFO = "请等待查询结果。";
    public static final String JSON_MODE_JOB = "JOB";
    public static final String JSON_MODE_ZZJX = "ZZJX";
    public static final String JSON_MODE_YJWC = "YJWC";
    public static final String JSON_MODE_SQXM = "SQXM";
    public static final String JSON_MODE_GZ = "GZ";


    public static final String PERSON_MODE_DETAIL = "DETAIL";
    public static final String PERSON_MODE_INFO = "INFO";
    public static final long TIME_COUNT = 1000;
    public static final String DL_PID = "5b96a815ca3a64f50f28ac2f4820c65d";
    public static final String MONEY = "chuanxisoft";
    public static Boolean FROM = false;
    public static Boolean FROMSELLOCA = false;

    /* Ver3 */
    public static final int TOAST_LONG = 1;
    public static final int TOAST_SHORT = 0;
    public static final String MD5 = "siqian";
    public static final String SER_ERROR = "服务器端发生异常，请稍后重试!";
    public static final String CACHE_FILE_NAME = "MyImg.png";
    public static final String BAIDU_MAP_KEY = "6yXylrBdaevLtg1okRCkiTkG";
    public static final String SDPATH = "qsz"; // 全身照
    public static final int QSZ = 3; // 全身照张数
    public static final String FILE_NAME_PREFIX = "-++++";
    public static final String WLCW = "网络错误";
    // intent
    public static final String AN_INTERVIEW = "inviteID";
    public static final String MAP_BAIDU_ADDRESS = "addressMap";
    public static final int COUNT = 50;
    public static final Float ALPHA = 0.7f;
}
