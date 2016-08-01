package com.zhang.sqone.home;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Departmentwork;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 科室之窗列表点击后进入的三个有选择功能的界面
 *
 * @author ZJP
 *         created at 16/3/24 下午4:08
 */
public class KeShiXQActivity extends FragmentActivity implements View.OnClickListener {
    /**科室简介*/
    @Bind(R.id.keshi_zc_jj)
    TextView keshiZcJj;
    /**天竺期刊*/
    @Bind(R.id.keshi_zc_qk)
    TextView keshiZcQk;
    /**科员风采*/
    @Bind(R.id.keshi_zc_fc)
    TextView keshiZcFc;
    /**选择布局*/
    @Bind(R.id.keshi_xuanze)
    LinearLayout keshiXuanze;
    @Bind(R.id.keshi_view)
    View keshiView;

    /**点击跳转的标识符*/
    private int bsf = 1;
    /**
     * 传递数据
     */
    private Bundle ba;
    /**
     * 详情控制科室的id
     */
    private String cid;
    private boolean aBoolean=false;
    private boolean bBoolean = true;
    private boolean cBoolean = true;
    //科室简介
    private KajjFargment kajjFargment;
    //天竺期刊
    private TzqkFragment tzqkFragment;
    //科员风采
    private KyfcFragment kyfcFragment;
    private List<Departmentwork.ReqDepartmentWork.PeriodicalMap> fileList;
    private String tp;
    private String mc;
    private String tx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_ke_shi_xq);
        ButterKnife.bind(this);
        ba = getIntent().getExtras();
        cid = ba.getString(Globals.KSCID);
        tp=ba.getString(Globals.KSTP);
        mc=ba.getString(Globals.KSMC);
        tx=ba.getString(Globals.KSTX);
        Log.i("zhang", "cid"+cid);
        // 实例化Fragment页面
        kajjFargment = new KajjFargment();
//                    Bundle args = new Bundle();
//                    args.putString("pid", pid);
        Bundle args = new Bundle();
        args.putString("pid", cid);
        args.putString(Globals.KSTP,tp);
        args.putString(Globals.KSMC,mc);
        args.putString(Globals.KSTX,tx);
        kajjFargment.setArguments(args);
        // 得到Fragment事务管理器
//                    h5Fargment.setArguments();
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();
        // 替换当前的页面
        fragmentTransaction.replace(R.id.keshi_frame, kajjFargment);
        // 事务管理提交
        fragmentTransaction.commit();
        keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);

        onClicks();
    }

    private void onClicks() {
        keshiZcJj.setOnClickListener(this);
        keshiZcFc.setOnClickListener(this);
        keshiZcQk.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            //简介
            case R.id.keshi_zc_jj:
                if (aBoolean){
                    keshiZcJj.setTextColor(this.getResources().getColor(R.color.font_fff));
                    keshiZcJj.setBackgroundResource(R.color.rul_bj);

                    keshiZcQk.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    keshiZcQk.setBackgroundResource(R.color.font_fff);
                    keshiZcFc.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    keshiZcFc.setBackgroundResource(R.color.font_fff);
                    aBoolean = false;
                    bBoolean = true;
                    cBoolean = true;
                    keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                    // 实例化Fragment页面
                    kajjFargment = new KajjFargment();
//                    Bundle args = new Bundle();
//                    args.putString("pid", pid);
                    Bundle args = new Bundle();
                    args.putString("pid", cid);
                    args.putString(Globals.KSTP,tp);
                    args.putString(Globals.KSMC,mc);
                    args.putString(Globals.KSTX,tx);
                    kajjFargment.setArguments(args);
                    // 得到Fragment事务管理器
//                    h5Fargment.setArguments();
                    // 得到Fragment事务管理器
                    FragmentTransaction fragmentTransaction = this
                            .getSupportFragmentManager().beginTransaction();
                    // 替换当前的页面
                    fragmentTransaction.replace(R.id.keshi_frame, kajjFargment);
                    // 事务管理提交
                    fragmentTransaction.commit();
                    keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                }
                break;
            //期刊
            case R.id.keshi_zc_qk:
                if (bBoolean){
                    keshiZcQk.setTextColor(this.getResources().getColor(R.color.font_fff));
                    keshiZcQk.setBackgroundResource(R.color.rul_bj);

                    keshiZcJj.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    keshiZcJj.setBackgroundResource(R.color.font_fff);
                    keshiZcFc.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    keshiZcFc.setBackgroundResource(R.color.font_fff);
                    aBoolean = true;
                    bBoolean = false;
                    cBoolean = true;
                    keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                    // 实例化Fragment页面
                     tzqkFragment = new TzqkFragment();
//                    Bundle args = new Bundle();
//                    args.putString("pid", pid);
                    Bundle args = new Bundle();
                    args.putString("pid", cid);
                    tzqkFragment.setArguments(args);
                    // 得到Fragment事务管理器
//                    h5Fargment.setArguments();
                    // 得到Fragment事务管理器
                    FragmentTransaction fragmentTransaction = this
                            .getSupportFragmentManager().beginTransaction();
                    // 替换当前的页面
                    fragmentTransaction.replace(R.id.keshi_frame, tzqkFragment);
                    // 事务管理提交
                    fragmentTransaction.commit();
                    keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                }

                break;
            //风采
            case R.id.keshi_zc_fc:
                if (cBoolean){
                    keshiZcFc.setTextColor(this.getResources().getColor(R.color.font_fff));
                    keshiZcFc.setBackgroundResource(R.color.rul_bj);

                    keshiZcJj.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    keshiZcJj.setBackgroundResource(R.color.font_fff);
                    keshiZcQk.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    keshiZcQk.setBackgroundResource(R.color.font_fff);
                    aBoolean = true;
                    bBoolean = true;
                    cBoolean = false;
                    keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                    // 实例化Fragment页面
                     kyfcFragment = new KyfcFragment();
//                    Bundle args = new Bundle();
//                    args.putString("pid", pid);
                    Bundle args = new Bundle();
                    args.putString("pid", cid);
                    kyfcFragment.setArguments(args);
                    // 得到Fragment事务管理器
//                    h5Fargment.setArguments();
                    // 得到Fragment事务管理器
                    FragmentTransaction fragmentTransaction = this
                            .getSupportFragmentManager().beginTransaction();
                    // 替换当前的页面
                    fragmentTransaction.replace(R.id.keshi_frame, kyfcFragment);
                    // 事务管理提交
                    fragmentTransaction.commit();
                    keshiXuanze.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                }
                break;

        }
    }

}
