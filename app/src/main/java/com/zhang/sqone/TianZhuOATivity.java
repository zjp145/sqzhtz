package com.zhang.sqone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.dbgw.FragmentAuth;
import com.zhang.sqone.home.FragmentAt1;
import com.zhang.sqone.home.SQActivity;
import com.zhang.sqone.my.FragmentMore;
import com.zhang.sqone.txl.FragmentSpace;
import com.zhang.sqone.utils.AppUtil;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.GridViewForScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 版本更改首页（oa）
 *
 * @author ZJP
 *         created at 2016/4/27 11:49
 */
public class TianZhuOATivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.oa_shouye_text)
    TextView oaShouyeText;
    @Bind(R.id.oa_tongzhi)
    TextView oaTongzhi;
    @Bind(R.id.oa_txl_text)
    TextView oaTxlText;
    @Bind(R.id.oa_wode_text)
    TextView oaWodeText;
    @Bind(R.id.toggle_btn)
    ImageView toggleBtn;
    @Bind(R.id.plus_btn)
    ImageView plusBtn;
    @Bind(R.id.frame_content)
    FrameLayout frameContent;
    @Bind(R.id.ceshi_text1)
    TextView ceshiText1;
    // 定义Fragment页面
    private FragmentAt1 fragmentAt;
    private FragmentAuth fragmentAuth;
    private FragmentSpace fragmentSpace;
    private FragmentMore fragmentMore;
    // 定义布局对象
    private FrameLayout atFl, authFl, spaceFl, moreFl;

    // 定义图片组件对象
    private ImageView atIv, authIv, spaceIv, moreIv;

    // 定义按钮图片组件
    private ImageView toggleImageView, plusImageView;

    // 定义PopupWindow
    private PopupWindow popWindow;
    // 获取手机屏幕分辨率的类
    private DisplayMetrics dm;
    private int anInt =0;

    /**
     * 用户退出界面点击返回按钮的间隔时间
     */
    private long firstTime = 0;
    private CommonAdapter<Map<String, Object>> adapter2;
    private ArrayList<Map<String, Object>> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_tian_zhu_oativity);
        ButterKnife.bind(this);
        ceshiText1.setFocusable(true);
        ceshiText1.setFocusableInTouchMode(true);
        ceshiText1.requestFocus();
        ceshiText1.requestFocusFromTouch();
        initView();
        Log.i("数据完成度", "onCreate: "+User.wcjd);
        Log.i("手机的mac", "onCreate: "+AppUtil.getMacAddress(this));
        initData();
        addGridView();
        // 初始化默认为选中点击了“动态”按钮
        clickAtBtn();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化布局对象
        atFl = (FrameLayout) findViewById(R.id.layout_at);
        authFl = (FrameLayout) findViewById(R.id.layout_auth);
        spaceFl = (FrameLayout) findViewById(R.id.layout_space);
        moreFl = (FrameLayout) findViewById(R.id.layout_more);

        // 实例化图片组件对象
        atIv = (ImageView) findViewById(R.id.image_at);
        authIv = (ImageView) findViewById(R.id.image_space);
        spaceIv = (ImageView) findViewById(R.id.image_space);
        moreIv = (ImageView) findViewById(R.id.image_more);

        // 实例化按钮图片组件
        toggleImageView = (ImageView) findViewById(R.id.toggle_btn);
        plusImageView = (ImageView) findViewById(R.id.plus_btn);

    }

    @Override
    protected void onResume() {


//        initLogin();
        // 实例化Fragment页面


        super.onResume();
    }
    /**本地有数据使用本地用户名和密码登录*/
    public void initLogin() {
        Index.ReqIndex.Login.Builder login = Index.ReqIndex.Login.newBuilder();
        login.setUsername(User.sid);
        //添加密码
        login.setPassword(User.pwd);
        login.setPhncode(User.mis_id);
        //Index.ReqIndex.ReqRec.Builder message = Index.ReqIndex.ReqRec.newBuilder();
        //message.setPhone("15931295549");
        //message.setPwd("15910438651");
        //message.setYzm("123456");
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setLogin(login).setAc("LOGIN").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //登陆成功
                if(index.getScd().equals("1")){
                    //头像地址
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd=index.getLogin().getWccd();
                    User.tzts=index.getLogin().getTzts();
                    User.dbts=index.getLogin().getDbts();
                    Log.i("zhang", "完成度"+User.wcjd);
                    Log.i("zhang", "未读通知"+User.tzts);
                    Log.i("zhang", "未读工作"+User.dbts);
                }
            }
        }.protocolBuffer(TianZhuOATivity.this, Globals.WS_URI, index, null);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 给布局对象设置监听
        atFl.setOnClickListener(this);
        authFl.setOnClickListener(this);
        spaceFl.setOnClickListener(this);
        moreFl.setOnClickListener(this);

        // 给按钮图片设置监听
        toggleImageView.setOnClickListener(this);
    }

    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//状态栏无背景
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击动态按钮
            case R.id.layout_at:

                clickAtBtn();
                anInt = 1;
                break;
            // 点击与我相关按钮
            case R.id.layout_auth:

                clickAuthBtn();
                anInt = 2;
                break;
            // 点击我的空间按钮
            case R.id.layout_space:

                clickSpaceBtn();
                anInt = 3;
                break;
            // 点击更多按钮
            case R.id.layout_more:

                clickMoreBtn();
                anInt = 4;
                break;
            // 点击中间按钮
            case R.id.toggle_btn:
                clickToggleBtn();
                break;
//
            case R.id.jia_guan:
                popWindow.dismiss();
                break;



        }
    }

    /**
     * 点击了“动态”按钮
     */
    private void clickAtBtn() {
        if (anInt != 1) {
            // 实例化Fragment页面
            fragmentAt = new FragmentAt1();
            // 得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = this
                    .getSupportFragmentManager().beginTransaction();
            // 替换当前的页面
            fragmentTransaction.replace(R.id.frame_content, fragmentAt);
            // 事务管理提交
            fragmentTransaction.commit();
            // 改变选中状态
            atFl.setSelected(true);
            atIv.setSelected(true);

            authFl.setSelected(false);
            authIv.setSelected(false);

            spaceFl.setSelected(false);
            spaceIv.setSelected(false);

            moreFl.setSelected(false);
            moreIv.setSelected(false);
            oaShouyeText.setTextColor(this.getResources().getColor(R.color.title_bg));
            oaTongzhi.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaTxlText.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaWodeText.setTextColor(this.getResources().getColor(R.color.contents_text));
        }

    }

    /**
     * 点击了“与我相关”按钮
     */
    private void clickAuthBtn() {
        if (anInt != 2) {
            // 实例化Fragment页面
            fragmentAuth = new FragmentAuth();
            // 得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = this
                    .getSupportFragmentManager().beginTransaction();
            // 替换当前的页面
            fragmentTransaction.replace(R.id.frame_content, fragmentAuth);
            // 事务管理提交
            fragmentTransaction.commit();

            atFl.setSelected(false);
            atIv.setSelected(false);

            authFl.setSelected(true);
            authIv.setSelected(true);

            spaceFl.setSelected(false);
            spaceIv.setSelected(false);

            moreFl.setSelected(false);
            moreIv.setSelected(false);
            oaShouyeText.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaTongzhi.setTextColor(this.getResources().getColor(R.color.title_bg));
            oaTxlText.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaWodeText.setTextColor(this.getResources().getColor(R.color.contents_text));
        }

    }

    /**
     * 点击了“我的空间”按钮
     */
    private void clickSpaceBtn() {
        if (anInt != 3) {
            // 实例化Fragment页面
            fragmentSpace = new FragmentSpace();
            // 得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = this
                    .getSupportFragmentManager().beginTransaction();
            // 替换当前的页面
            fragmentTransaction.replace(R.id.frame_content, fragmentSpace);
            // 事务管理提交
            fragmentTransaction.commit();

            atFl.setSelected(false);
            atIv.setSelected(false);

            authFl.setSelected(false);
            authIv.setSelected(false);

            spaceFl.setSelected(true);
            spaceIv.setSelected(true);

            moreFl.setSelected(false);
            moreIv.setSelected(false);
            oaShouyeText.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaTongzhi.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaTxlText.setTextColor(this.getResources().getColor(R.color.title_bg));
            oaWodeText.setTextColor(this.getResources().getColor(R.color.contents_text));
        }

    }

    /**
     * 点击了“更多”按钮
     */
    private void clickMoreBtn() {
        if (anInt != 4) {
            // 实例化Fragment页面
            fragmentMore = new FragmentMore();
            // 得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = this
                    .getSupportFragmentManager().beginTransaction();
            // 替换当前的页面
            fragmentTransaction.replace(R.id.frame_content, fragmentMore);
            // 事务管理提交
            fragmentTransaction.commit();

            atFl.setSelected(false);
            atIv.setSelected(false);

            authFl.setSelected(false);
            authIv.setSelected(false);

            spaceFl.setSelected(false);
            spaceIv.setSelected(false);

            moreFl.setSelected(true);
            moreIv.setSelected(true);
            oaShouyeText.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaTongzhi.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaTxlText.setTextColor(this.getResources().getColor(R.color.contents_text));
            oaWodeText.setTextColor(this.getResources().getColor(R.color.title_bg));
        }

    }

    /**
     * 点击了中间按钮
     */
    private void clickToggleBtn() {
//        Toast.makeText(this,"目前此功能暂未开发完成",Toast.LENGTH_SHORT).show();
        showPopupWindow(toggleImageView);
        // 改变按钮显示的图片为按下时的状态
        plusImageView.setSelected(true);
    }

    private void addGridView() {

        //适配显示数据
        items = new ArrayList<Map<String, Object>>();
        if (User.sjsq_28.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jia_shijia);//添加图像资源的ID
            item.put("textItem", "事假申请");//按序号添加ItemText
            items.add(item);
        }
        if (User.bjsq_29.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jia_bingjia);//添加图像资源的ID
            item.put("textItem", "病假申请");//按序号添加ItemText
            items.add(item);
        }
        if (User.njsq_30.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jia_nianjia);//添加图像资源的ID
            item.put("textItem", "年假申请");//按序号添加ItemText
            items.add(item);
        }
        if (User.dwz_31.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.dangchaxun);//添加图像资源的ID
            item.put("textItem", "党委章");//按序号添加ItemText
            items.add(item);
        }
        if (User.zfz_32.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jia_chaxun);//添加图像资源的ID
            item.put("textItem", "政府章");//按序号添加ItemText
            items.add(item);
        }if (User.hyscx_33.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.huiyishi);//添加图像资源的ID
            item.put("textItem", "会议室预定");//按序号添加ItemText
            items.add(item);
        }if (User.dwfw_34.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jia_dangfw);//添加图像资源的ID
            item.put("textItem", "党委发文");//按序号添加ItemText
            items.add(item);
        }
        if (User.zffw_35.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jia_zhengfw);//添加图像资源的ID
            item.put("textItem", "政府发文");//按序号添加ItemText
            items.add(item);
        }





    }

    /**
     * 改变显示的按钮图片为正常状态
     */
    private void changeButtonImage() {
        plusImageView.setSelected(false);
    }

    /**
     * 显示PopupWindow弹出菜单
     */
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
            dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            GridViewForScrollView gridview = (GridViewForScrollView) view.findViewById(R.id.jia_gridview);

            gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
            adapter2 = new CommonAdapter<Map<String, Object>>(TianZhuOATivity.this, items, R.layout.grid_item1) {
                @Override
                public void convert(ViewHolder holder, Map<String, Object> o) {
                    holder.setImageResource(R.id.grid_iamge1, (Integer) o.get("imageItem")).setText(R.id.grid_text1, (String) o.get("textItem"));
                }
            };
            gridview.setAdapter(adapter2);
            gridview.setOnItemClickListener(this);
//            LinearLayout shiJia = (LinearLayout) view.findViewById(R.id.jia_shijia);
//            shiJia.setOnClickListener(this);
//            LinearLayout bingJia = (LinearLayout) view.findViewById(R.id.jia_bingjia);
//            bingJia.setOnClickListener(this);
//            LinearLayout nianJia = (LinearLayout) view.findViewById(R.id.jia_nianjia);
//            nianJia.setOnClickListener(this);
//            LinearLayout dangWei = (LinearLayout) view.findViewById(R.id.jia_bangwei);
//            dangWei.setOnClickListener(this);
//            LinearLayout zhengFu = (LinearLayout) view.findViewById(R.id.jia_zhengfu);
//            zhengFu.setOnClickListener(this);
//            LinearLayout huiYi = (LinearLayout) view.findViewById(R.id.jia_huiyi);
//            huiYi.setOnClickListener(this);
            LinearLayout guan = (LinearLayout) view.findViewById(R.id.jia_guan);
            guan.setOnClickListener(this);
//            LinearLayout dangFw = (LinearLayout) view.findViewById(R.id.jia_bangfw);
//            dangFw.setOnClickListener(this);
//            LinearLayout zhengFw = (LinearLayout) view.findViewById(R.id.jia_zhangfufw);
//            zhengFw.setOnClickListener(this);
//            if (User.zhfz.equals("0")){
//                ImageView zfimage = (ImageView)view.findViewById(R.id.jia_zhengfuzhang_iamge);
//                TextView zftext = (TextView) view.findViewById(R.id.jia_zhengfuzhang_text);
//                zfimage.setImageResource(R.mipmap.jia_zhengfw);
//                zftext.setText("政府发文");
//                zhengFw.setVisibility(View.INVISIBLE);
//            }
//            if (User.dwz.equals("0")&&User.zhfz.equals("0")){
//                ImageView zfimage = (ImageView)view.findViewById(R.id.jia_zhengfuzhang_iamge);
//                TextView zftext = (TextView) view.findViewById(R.id.jia_zhengfuzhang_text);
//                zfimage.setImageResource(R.mipmap.jia_zhengfw);
//                zftext.setText("政府发文");
//                ImageView dwimage = (ImageView)view.findViewById(R.id.jia_dangweizheng_iamge);
//                TextView dwtext = (TextView) view.findViewById(R.id.jia_dangweizheng_text);
//                dwimage.setImageResource(R.mipmap.jia_dangfw);
//                dwtext.setText("党委发文");
//                zhengFw.setVisibility(View.INVISIBLE);
//                dangFw.setVisibility(View.INVISIBLE);
//            }

            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, dm.widthPixels, dm.heightPixels);

        }
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
         popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
        popWindow.showAsDropDown(parent, 0, 0);

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 改变显示的按钮图片为正常状态
                changeButtonImage();
            }
        });

        // 监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                // 改变显示的按钮图片为正常状态
                changeButtonImage();
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                moveTaskToBack(true);

//                long secondTime = System.currentTimeMillis();
//                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
//                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    firstTime = secondTime;// 更新firstTime
//                    return true;
//                } else { // 两次按键小于2秒时，退出应用
//                    System.exit(0);
//                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("我点击的是：", "第" + i + "个" + "long id" + l + "==");
        String name = (String) items.get(i).get("textItem");
        if (name.equals("事假申请")) {
            popWindow.dismiss();
                    Intent intent1 = new Intent(this, SQActivity.class);
                    intent1.putExtra(Globals.QJBS, "事假申请");
                    startActivity(intent1);
        }
        if (name.equals("病假申请")) {
            popWindow.dismiss();
                Intent intent2 = new Intent(this, SQActivity.class);
                intent2.putExtra(Globals.QJBS, "病假申请");
                startActivity(intent2);
        }
        if (name.equals("年假申请")) {
            popWindow.dismiss();
                Intent intent3 = new Intent(this, SQActivity.class);
                intent3.putExtra(Globals.QJBS, "年假申请");
                startActivity(intent3);
        }
        if (name.equals("党委章")) {
            popWindow.dismiss();
            Intent intent4 = new Intent(this, SQActivity.class);
                    intent4.putExtra(Globals.QJBS, "党委章");
                    startActivity(intent4);
        }
        if (name.equals("政府章")) {
            popWindow.dismiss();
            Intent intent5 = new Intent(this, SQActivity.class);
                    intent5.putExtra(Globals.QJBS, "政府章");
                    startActivity(intent5);
        }
        if (name.equals("会议室预定")) {
            popWindow.dismiss();
                Intent intent6 = new Intent(this, SQActivity.class);
                intent6.putExtra(Globals.QJBS, "会议室预定");
                startActivity(intent6);

        }
        if (name.equals("党委发文")) {
            popWindow.dismiss();
                Intent intent7 = new Intent(this, SQActivity.class);
                intent7.putExtra(Globals.QJBS, "党委发文");
                startActivity(intent7);

        }
        if (name.equals("政府发文")) {
            popWindow.dismiss();
                Intent intent8 = new Intent(this, SQActivity.class);
                intent8.putExtra(Globals.QJBS, "政府发文");
                startActivity(intent8);

        }

    }
}
