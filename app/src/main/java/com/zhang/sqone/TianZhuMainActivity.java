package com.zhang.sqone;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhang.sqone.dbgw.FragmentAuth;
import com.zhang.sqone.home.FragmentAt;
import com.zhang.sqone.my.FragmentMore;
import com.zhang.sqone.txl.FragmentSpace;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.DeleteF;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 天竺社区办公系统的首页（改版前）
 *
 * @author ZJP
 *         created at 2016/2/22 15:06
 */
public class TianZhuMainActivity extends FragmentActivity implements View.OnClickListener {
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
    // 定义Fragment页面
    private FragmentAt fragmentAt;
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
    private int anInt;

    /**
     * 用户退出界面点击返回按钮的间隔时间
     */
    private long firstTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_tian_zhu_main);
        ButterKnife.bind(this);

        initView();

        initData();

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
                anInt=1;
                break;
            // 点击与我相关按钮
            case R.id.layout_auth:

                clickAuthBtn();
                anInt=2;
                break;
            // 点击我的空间按钮
            case R.id.layout_space:

                clickSpaceBtn();
                anInt=3;
                break;
            // 点击更多按钮
            case R.id.layout_more:

                clickMoreBtn();
                anInt=4;
                break;
            // 点击中间按钮
            case R.id.toggle_btn:
                clickToggleBtn();
                break;
        }
    }

    /**
     * 点击了“动态”按钮
     */
    private void clickAtBtn() {
        if (anInt!=1){
            // 实例化Fragment页面
            fragmentAt = new FragmentAt();
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
        if (anInt!=2){
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
        if (anInt!=3){
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
        if (anInt!=4){
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
        showPopupWindow(toggleImageView);
        // 改变按钮显示的图片为按下时的状态
        plusImageView.setSelected(true);
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
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, dm.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
        // popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
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
                popWindow.dismiss();
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                DeleteF d = new DeleteF() {
                    @Override
                    public void determineButton() {
                        finish();
                    }
                }.deleteDialog(TianZhuMainActivity.this, "你确定要退回到应用首页吗？", "", "");

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
}

