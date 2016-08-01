package com.zhang.sqone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.my.LoginActivity;
import com.zhang.sqone.my.RegisterActivity;
import com.zhang.sqone.utils.AppUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.CircularImage;
import com.zhang.sqone.views.GridViewForScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导页首先进入这个页面（登录和没有登录的情况是不同的）
 * 天竺社区的主界面（首页）
 * 其中包括子程序分管使用（改版前）
 *
 * @author ZJP
 *         created at 2016/2/22 15:05
 */

public class TiZhuActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    /**
     * 展示点击的gridView
     */
    @Bind(R.id.shouye_gridview)
    GridViewForScrollView shouyeGridview;
    /**
     * gridview适配使用图片资源
     */
    public int[] images = new int[]{R.mipmap.gridscanicon, R.mipmap.gridofficeicon, R.mipmap.gridvillageicon, R.mipmap.gridaircrafticon
            , R.mipmap.gridcaricon, R.mipmap.gridmedicalicon, R.mipmap.gridlisticon, R.mipmap.gridsearchicon, R.mipmap.gridcrediticon
            , R.mipmap.gridchieficon, R.mipmap.gridbookicon, R.mipmap.gridtelephoneicon, R.mipmap.gridfundicon, R.mipmap.gridtrainicon
            , R.mipmap.gridfireicon
    };
    /**
     * gridview适配使用的文字
     */
    public String[] texts = new String[]{"扫一扫", "办公系统", "村务", "航班查询", "车辆违法查询", "定点医院药店", "基本医疗保险用药目录", "驾驶人加分查询", "企业信用查询", "家政动态", "智慧教育", "健康顾问", "公积金查询", "列车查询", "生活缴费"};
    @Bind(R.id.shouye_tuxiang)
    /**用户头像*/
            CircularImage shouyeTuxiang;
    @Bind(R.id.shouye_dl_zc)
    /**用户登陆和注册*/
            LinearLayout shouyeDlZc;
    @Bind(R.id.shouye_phone)
    /**用户的手机号码*/
            TextView shouyePhone;
    @Bind(R.id.shouye_nc)
    /**昵称*/
            TextView shouyeNc;
    @Bind(R.id.shouye_nc_iamge)
    /**昵称后面的图片*/
            ImageView shouyeNcIamge;
    @Bind(R.id.shouye_title_t)
    TextView shouyeTitleT;
    /**
     * 用户退出界面点击返回按钮的间隔时间
     */
    private long firstTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_tianzhu_activity);
        ButterKnife.bind(this);
        //获取焦点
        shouyeTitleT.requestFocus();
        shouyeGridview.setOnItemClickListener(this);
        //首页判断是不是登录状态
        if (User.isLogin) {
//            如果是用户是登陆的状态我们先获得用户的信息
//            regRequest();
            //如果没有透彻
            if (!User.IconPath.equals("") && User.IconPath != null) {
                //下载头像
                ImageLoader.getInstance().displayImage(User.IconPath, shouyeTuxiang);
            }
            //如果用户没有设置昵称
            if (User.nc.equals("")) {
                shouyeNc.setText(User.sid);

            } else {
                shouyeNc.setText(User.nc);
                shouyePhone.setText(User.phone);
            }

            if (User.phone.equals("")) {
                if (AppUtil.isMobileNO(User.sid)){
                    shouyePhone.setText(User.sid.substring(0, 3) + "****" + User.sid.substring(7, User.sid.length()));

                }else{shouyePhone.setText(User.sid);
                }
            } else {
                shouyePhone.setText(User.phone.substring(0, 3) + "****" + User.phone.substring(7, User.phone.length()));
            }


            shouyeNcIamge.setImageResource(R.mipmap.v);
            //用户已经登录现在点击是进入我的页面修改用户的信息
            shouyeDlZc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TiZhuActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

        } else {
            //创建登陆和注册的点击事件
            shouyeDlZc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击登陆注册 跳转到登陆界面
                    Intent intent = new Intent(TiZhuActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            });
        }

        //准备要添加的数据条目
        addGridView();


    }

    //页面返回的时候调用
    @Override
    protected void onResume() {
        super.onResume();
        if (User.isLogin) {
//            如果是用户是登陆的状态我们先获得用户的信息
//            regRequest();
            //如果没有透彻
            if (!User.IconPath.equals("") && User.IconPath != null) {
                //下载头像
                ImageLoader.getInstance().displayImage(User.IconPath, shouyeTuxiang);
            }
            //如果用户没有设置昵称
            if (User.nc.equals("")) {
                shouyeNc.setText(User.sid);

            } else {
                shouyeNc.setText(User.nc);
                shouyePhone.setText(User.phone);
            }

            if (User.phone.equals("")) {
                if (AppUtil.isMobileNO(User.sid)){
                    shouyePhone.setText(User.sid.substring(0, 3) + "****" + User.sid.substring(7, User.sid.length()));

                }else{shouyePhone.setText(User.sid);
                }

            } else {
                shouyePhone.setText(User.phone.substring(0, 3) + "****" + User.phone.substring(7, User.phone.length()));
            }
            shouyeNcIamge.setImageResource(R.mipmap.v);
            //用户已经登录现在点击是进入我的页面修改用户的信息
            shouyeDlZc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TiZhuActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

        }else{

            shouyeTuxiang.setImageResource(R.mipmap.portrait);
            shouyeNc.setText("登录/注册");
            shouyePhone.setText("智慧天竺方便您的生活");
            shouyeNcIamge.setImageResource(R.mipmap.whitearrow);
            //创建登陆和注册的点击事件
            shouyeDlZc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击登陆注册 跳转到登陆界面
                    Intent intent = new Intent(TiZhuActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            });

        }

    }

    /**
     * 给gridView添加数据适配器展示数据
     *
     * @author ZJP
     * created at 2016/2/23 10:03
     */
    private void addGridView() {
        //适配显示数据
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 15; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", images[i]);//添加图像资源的ID
            item.put("textItem", texts[i]);//按序号添加ItemText
            items.add(item);
        }

        //实例化一个适配器
        SimpleAdapter adapter = new SimpleAdapter(this,
                items,
                R.layout.grid_item,
                new String[]{"imageItem", "textItem"},
                new int[]{R.id.grid_iamge, R.id.grid_text});
        //添加适配器
        shouyeGridview.setAdapter(adapter);

        shouyeGridview.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("我点击的是：", "第" + position + "个" + "long id" + id);
        if (position == 1) {
            //跳转到公司的oa
            if(User.isLogin){
                Intent intent = new Intent(this, TianZhuMainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"请先登录在使用此功能",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
