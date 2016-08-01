package com.zhang.sqone.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.SDCARD123Activity;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.BadgeView;
import com.zhang.sqone.views.DeleteF;
import com.zhang.sqone.views.GridViewForScrollView;
import com.zhang.sqone.zxing.android.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 改版之后（oa首页）
 *
 * @author ZJP
 *         created at 2016/4/28 13:59
 */
public class FragmentAt1 extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    @Bind(R.id.gome_view)
    WebView gomeView;
    @Bind(R.id.oa_home_fanhui)
    ImageView oaHomeFanhui;
    @Bind(R.id.oa_home_lianghongdeng)
    LinearLayout oaHomeLianghongdeng;
    @Bind(R.id.oa_home_saoyisao)
    LinearLayout oaHomeSaoyisao;
    @Bind(R.id.shouye_gridview)
    GridViewForScrollView shouyeGridview;
    /**当前工作完成度*/
//    @Bind(R.id.jindu)
//    RoundProgressBar jindu;
    @Bind(R.id.oa_shouye)
    TextView oaShouye;
    @Bind(R.id.tzgg_tzgg)
    ImageView tzggTzgg;
    private BadgeView badge4;
    private CommonAdapter adapter2;
    private ArrayList<Map<String, Object>> items;
    private BadgeView badge3;
    private BadgeView badge5;
    private BadgeView badge6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_at1, container, false);
        //获得控件
        ButterKnife.bind(this, view);

        shouyeGridview.setOnItemClickListener(this);
        oaHomeLianghongdeng.setOnClickListener(this);
        oaHomeSaoyisao.setOnClickListener(this);
        oaHomeFanhui.setOnClickListener(this);
        String s = Globals.OA_QQ + "?usercode=" + User.sid + "&pwd=" + User.pwd;
        Log.i("zhang", "网址" + s);
        badge3 = new BadgeView(getActivity(), tzggTzgg);
        Log.i("zhang", "通知公告" + User.tzts);
        badge3.setBackgroundResource(R.mipmap.xx);
        badge3.setBadgeMargin(10);
        gomeView.loadUrl(s);
        addGridView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int i = Integer.parseInt(User.xxzx);
        if (i < 10) {
            Log.i("zhang", "当前" + i);
            badge3.setText(User.xxzx);
            badge3.show();
        } else {
            badge3.setText("+");
            badge3.show();
        }
        adapter2 = new CommonAdapter<Map<String, Object>>(getActivity(), items, R.layout.grid_item) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> o) {
                if (holder.getPosition() == 0) {
                    badge4 = new BadgeView(getActivity(), holder.getView(R.id.grid_item));
                    int i = Integer.parseInt(User.dbts);
                    Log.i("zhang", "数据条数" + User.dbts);
                    if (i < 10) {
                        badge4.setText(User.dbts);
                        badge4.setBackgroundResource(R.mipmap.xx);
                        badge4.setBadgeMargin(30);
                        badge4.show();
                    } else {
                        badge4.setText("+");
                        badge4.setBackgroundResource(R.mipmap.xx);
                        badge4.setBadgeMargin(30);
                        badge4.show();
                    }

                }
                String name = (String) items.get(holder.getPosition()).get("textItem");
                if (name.equals("收资料")) {
                    badge5 = new BadgeView(getActivity(), holder.getView(R.id.grid_item));
                    int i = Integer.parseInt(User.sfzl);
                    Log.i("zhang", "" + User.sfzl);
                    if (i < 10) {
                        badge5.setText(User.sfzl);
                        badge5.setBackgroundResource(R.mipmap.xx);
                        badge5.setBadgeMargin(30);
                        badge5.show();
                    } else {
                        badge5.setText("+");
                        badge5.setBackgroundResource(R.mipmap.xx);
                        badge5.setBadgeMargin(30);
                        badge5.show();
                    }

                }

                if (name.equals("公告栏")){
                    badge6 = new BadgeView(getActivity(), holder.getView(R.id.grid_item));
                    int i = Integer.parseInt(User.tzts);
                    Log.i("zhang", "" + User.tzts);
                    if (i < 10) {
                        badge6.setText(User.tzts);
                        badge6.setBackgroundResource(R.mipmap.xx);
                        badge6.setBadgeMargin(30);
                        badge6.show();
                    } else {
                        badge6.setText("+");
                        badge6.setBackgroundResource(R.mipmap.xx);
                        badge6.setBadgeMargin(30);
                        badge6.show();
                    }
                }

                holder.setImageResource(R.id.grid_iamge, (Integer) o.get("imageItem")).setText(R.id.grid_text, (String) o.get("textItem"));
            }
        };
        //添加适配器
        shouyeGridview.setAdapter(adapter2);

//        jindu.setProgress(Integer.parseInt(User.wcjd));
    }


    /**
     * 给gridView添加数据适配器展示数据
     *
     * @author ZJP
     * created at 2016/2/23 10:03
     */
    private void addGridView() {

        //适配显示数据
        items = new ArrayList<Map<String, Object>>();
        if (User.dbgz_1.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.daiban);//添加图像资源的ID
            item.put("textItem", "待办工作");//按序号添加ItemText
            items.add(item);
        }

//        if (User.zbgz_3.equals("1")) {
//            Map<String, Object> item = new HashMap<String, Object>();
//            item.put("imageItem", R.mipmap.zaiban);//添加图像资源的ID
//            item.put("textItem", "在办工作");//按序号添加ItemText
//            items.add(item);
//        }
        if (User.bjgz_4.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.banjie);//添加图像资源的ID
            item.put("textItem", "已办工作");//按序号添加ItemText
            items.add(item);
        }
        if (User.scgz_5.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shoucang);//添加图像资源的ID
            item.put("textItem", "收藏工作");//按序号添加ItemText
            items.add(item);
        }
        if (User.szl_2.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shouziliao);//添加图像资源的ID
            item.put("textItem", "收资料");//按序号添加ItemText
            items.add(item);
        }
        if (User.czl_8.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.chuanziliao);//添加图像资源的ID
            item.put("textItem", "传资料");//按序号添加ItemText
            items.add(item);
        }
        if (User.yfs_9.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.faziliao);//添加图像资源的ID
            item.put("textItem", "已发送");//按序号添加ItemText
            items.add(item);
        }
        if (User.bzhysb_28.equals("1")){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.huiyi_s);//添加图像资源的ID
            item.put("textItem", "班子会议题上报");//按序号添加ItemText
            items.add(item);
        }
        if (User.bzhysh_6.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shenhe);//添加图像资源的ID
            item.put("textItem", "班子会议题审核");//按序号添加ItemText
            items.add(item);
        }
        if (User.bzhycx_7.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.banzi_chaxun);//添加图像资源的ID
            item.put("textItem", "班子会议题查询");//按序号添加ItemText
            items.add(item);
        }



        if (User.wdgz_12.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shouye_gonggao);//添加图像资源的ID
            item.put("textItem", "公告栏");//按序号添加ItemText
            items.add(item);
        }

        if (User.ldrc_23.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.ldrc);//添加图像资源的ID
            item.put("textItem", "领导日程");//按序号添加ItemText
            items.add(item);
        }
        if (User.rcap_24.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.rcap);//添加图像资源的ID
            item.put("textItem", "日程安排");//按序号添加ItemText
            items.add(item);
        }
        if (User.hyscx_10.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.chaxun);//添加图像资源的ID
            item.put("textItem", "会议室查询");//按序号添加ItemText
            items.add(item);
        }
        if (User.hyclgl_36.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.tijiao);//添加图像资源的ID
            item.put("textItem", "会议材料管理");//按序号添加ItemText
            items.add(item);
        }
        if (User.wdwj_11.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.dangan);//添加图像资源的ID
            item.put("textItem", "我的文件");//按序号添加ItemText
            items.add(item);
        }

        if (User.wdgz_12.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.wodegongzi);//添加图像资源的ID
            item.put("textItem", "我的工资");//按序号添加ItemText
            items.add(item);
        }

        if (User.dwwj_13.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.dagnwei);//添加图像资源的ID
            item.put("textItem", "党委文件");//按序号添加ItemText
            items.add(item);
        }
        if (User.zfwj_14.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.zhengfu);//添加图像资源的ID
            item.put("textItem", "政府文件");//按序号添加ItemText
            items.add(item);
        }
        if (User.ttzq_15.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.tianzhu);//添加图像资源的ID
            item.put("textItem", "天竺镇情");//按序号添加ItemText
            items.add(item);
        }
        if (User.zfbg_16.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.zhengfubaogao);//添加图像资源的ID
            item.put("textItem", "政府报告");//按序号添加ItemText
            items.add(item);
        }
        if (User.djxx_17.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.dangjian);//添加图像资源的ID
            item.put("textItem", "党建学习");//按序号添加ItemText
            items.add(item);
        }
        if (User.ywxx_20.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.yewuxuexi);//添加图像资源的ID
            item.put("textItem", "业务学习");//按序号添加ItemText
            items.add(item);
        }
        if (User.gzzd_19.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.guizhang);//添加图像资源的ID
            item.put("textItem", "规章制度");//按序号添加ItemText
            items.add(item);
        }
        if (User.lzjy_18.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.lianzhengjiaoyu);//添加图像资源的ID
            item.put("textItem", "廉政教育");//按序号添加ItemText
            items.add(item);
        }


        if (User.ckzl_21.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.cankaoziliao);//添加图像资源的ID
            item.put("textItem", "参考资料");//按序号添加ItemText
            items.add(item);
        }
        if (User.hjjc_22.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jiance);//添加图像资源的ID
            item.put("textItem", "环境监测");//按序号添加ItemText
            items.add(item);
        }

        if (User.tzsx_25.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shixiang);//添加图像资源的ID
            item.put("textItem", "事项通知");//按序号添加ItemText
            items.add(item);
        }
        if (User.dwzcx_26.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.dangweichaxun);//添加图像资源的ID
            item.put("textItem", "党委章查询");//按序号添加ItemText
            items.add(item);
        }
        if (User.zfzcx_27.equals("1")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.zhengfuchaxun);//添加图像资源的ID
            item.put("textItem", "政府章查询");//按序号添加ItemText
            items.add(item);
        }


        shouyeGridview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("我点击的是：", "第" + position + "个" + "long id" + id + "==");
        //点击的name
        String name = (String) items.get(position).get("textItem");

        if (name.equals("待办工作")) {
            Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
            intent5.putExtra(Globals.GWGL, 2);
            startActivity(intent5);
        }
        if (name.equals("收资料")) {
            Intent intent21 = new Intent(getActivity(), ShouziliaoActivity.class);
            startActivity(intent21);
        }
        if (name.equals("在办工作")) {
            Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
            intent5.putExtra(Globals.GWGL, 1);
            startActivity(intent5);
        }
        if (name.equals("已办工作")) {
            Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
            intent5.putExtra(Globals.GWGL, 3);
            startActivity(intent5);
        }
        if (name.equals("收藏工作")) {
            Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
            intent5.putExtra(Globals.GWGL, 4);
            startActivity(intent5);
        }
        if (name.equals("班子会议题上报")) {
            Intent intent = new Intent(getActivity(), BanZiHuiYiActivity.class);
            intent.putExtra("shenhe", 3);
            startActivity(intent);

        }
        if (name.equals("班子会议题审核")) {
            Intent intent = new Intent(getActivity(), BanZiHuiYiActivity.class);
            intent.putExtra("shenhe", 1);
            startActivity(intent);

        }
        if (name.equals("班子会议题查询")) {
            Intent intent = new Intent(getActivity(), BanZiHuiYiActivity.class);
            intent.putExtra("shenhe", 2);
            startActivity(intent);

        }
        if (name.equals("传资料")) {
            Intent intent = new Intent(getActivity(), ChuanZiLiaoActivity.class);
            startActivity(intent);

        }
        if (name.equals("已发送")) {
            Intent intent21 = new Intent(getActivity(), XieZiLiaoActivity.class);
            startActivity(intent21);
        }

        if (name.equals("公告栏")) {
            Intent intent3 = new Intent(getActivity(), TongZhigonggao.class);
            startActivity(intent3);
        }
        if (name.equals("会议室查询")) {
            Intent intent21 = new Intent(getActivity(), HyapcxActivity.class);
            startActivity(intent21);
        }
        if (name.equals("会议材料管理")) {
            Intent intent21 = new Intent(getActivity(), HYZLActivity.class);
            startActivity(intent21);
        }
        if (name.equals("我的文件")) {
            Intent intent4 = new Intent(getContext(), SDCARD123Activity.class);
            intent4.putExtra("bsf", "sy");
            startActivity(intent4);
        }

        if (name.equals("我的工资")) {
            Intent intent21 = new Intent(getActivity(), GongZiActivity.class);
            startActivity(intent21);
        }
        if (name.equals("党委文件")) {
            Intent intent21 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
            intent21.putExtra(Globals.ZLWJ, 1);
            startActivity(intent21);
        }
        if (name.equals("政府文件")) {
            Intent intent22 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
            intent22.putExtra(Globals.ZLWJ, 2);
            startActivity(intent22);
        }
        if (name.equals("天竺镇情")) {
            Intent intent25 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
            intent25.putExtra(Globals.ZLWJ, 5);
            startActivity(intent25);
        }
        if (name.equals("政府报告")) {
            Intent intent23 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
            intent23.putExtra(Globals.ZLWJ, 3);
            startActivity(intent23);
        }
        if (name.equals("党建学习")) {
            Intent intent = new Intent(getActivity(), DangJianYuansActivity.class);
            intent.putExtra(Globals.WDGL, 4);
            startActivity(intent);
        }
        if (name.equals("廉政教育")) {
            Intent intent1 = new Intent(getActivity(), DangJianYuansActivity.class);
            intent1.putExtra(Globals.WDGL, 2);
            startActivity(intent1);
        }
        if (name.equals("规章制度")) {
            Intent intent24 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
            intent24.putExtra(Globals.ZLWJ, 4);
            startActivity(intent24);
        }
        if (name.equals("业务学习")) {
            Intent intent4 = new Intent(getActivity(), DangJianYuansActivity.class);
            intent4.putExtra(Globals.WDGL, 3);
            startActivity(intent4);
        }
        if (name.equals("参考资料")) {
            Intent intent3 = new Intent(getActivity(), DangJianYuansActivity.class);
            intent3.putExtra(Globals.WDGL, 5);
            startActivity(intent3);

        }
        if (name.equals("环境监测")) {
            Intent intent3 = new Intent(getActivity(), HuanJingActivity.class);
            startActivity(intent3);
        }
        if (name.equals("领导日程")) {
            Intent intent3 = new Intent(getActivity(), LeadAgendaActivity.class);
            startActivity(intent3);
        }

        if (name.equals("日程安排")) {
            Intent intent3 = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent3);
        }
        if (name.equals("事项通知")) {
            Intent intent21 = new Intent(getActivity(), ShiXiangActivity.class);
            startActivity(intent21);
        }
        if (name.equals("党委章查询")) {
            Intent intent5 = new Intent(getActivity(), DzZfActivity.class);
            intent5.putExtra(Globals.QJBS, "党委章查询");
            startActivity(intent5);
        }
        if (name.equals("政府章查询")) {
            Intent intent5 = new Intent(getActivity(), DzZfActivity.class);
            intent5.putExtra(Globals.QJBS, "政府章查询");
            startActivity(intent5);
        }





//        //班子会议
//        else if (User.bzhyc.equals("1")) {
//            if (position == 0) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 2);
//                startActivity(intent5);
//            }
//            if (position == 1) {
//                Intent intent21 = new Intent(getActivity(), ShouziliaoActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 2) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 1);
//                startActivity(intent5);
//            }
//            if (position == 3) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 3);
//                startActivity(intent5);
//            }
//            if (position == 4) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 4);
//                startActivity(intent5);
//            }
//            if (position == 5) {
//                Intent intent = new Intent(getActivity(), BanZiHuiYiActivity.class);
//                intent.putExtra("shenhe", 2);
//                startActivity(intent);
//            }
//            if (position == 6) {
//                Intent intent = new Intent(getActivity(), ChuanZiLiaoActivity.class);
//                startActivity(intent);
//
//            }
//            if (position == 7) {
//                Intent intent21 = new Intent(getActivity(), XieZiLiaoActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 8) {
//                Intent intent21 = new Intent(getActivity(), HyapcxActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 9) {
//                Intent intent4 = new Intent(getContext(), SDCARD123Activity.class);
//                intent4.putExtra("bsf", "sy");
//                startActivity(intent4);
//            }
//
//            if (position == 10) {
//                Intent intent21 = new Intent(getActivity(), GongZiActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 11) {
//                Intent intent21 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent21.putExtra(Globals.ZLWJ, 1);
//                startActivity(intent21);
//            }
//            if (position == 12) {
//                Intent intent22 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent22.putExtra(Globals.ZLWJ, 2);
//                startActivity(intent22);
//            }
//            if (position == 13) {
//                Intent intent25 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent25.putExtra(Globals.ZLWJ, 5);
//                startActivity(intent25);
//            }
//            if (position == 14) {
//                Intent intent23 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent23.putExtra(Globals.ZLWJ, 3);
//                startActivity(intent23);
//            }
//            if (position == 15) {
//                Intent intent = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent.putExtra(Globals.WDGL, 4);
//                startActivity(intent);
//            }
//            if (position == 16) {
//                Intent intent1 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent1.putExtra(Globals.WDGL, 2);
//                startActivity(intent1);
//            }
//            if (position == 17) {
//                Intent intent24 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent24.putExtra(Globals.ZLWJ, 4);
//                startActivity(intent24);
//            }
//            if (position == 18) {
//                Intent intent4 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent4.putExtra(Globals.WDGL, 3);
//                startActivity(intent4);
//            }
//            if (position == 19) {
//                Intent intent3 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent3.putExtra(Globals.WDGL, 5);
//                startActivity(intent3);
//
//            }
//            if (position == 20) {
//                Intent intent3 = new Intent(getActivity(), HuanJingActivity.class);
//                startActivity(intent3);
//
//            }
//            if (position == 21) {
//                Intent intent3 = new Intent(getActivity(), LeadAgendaActivity.class);
//                startActivity(intent3);
//            }
//            if (position == 22) {
//                Intent intent3 = new Intent(getActivity(), ScheduleActivity.class);
//                startActivity(intent3);
//            }
//        } else {
//            /**所有人*/
//            if (position == 0) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 2);
//                startActivity(intent5);
//            }
//            if (position == 1) {
//                Intent intent21 = new Intent(getActivity(), ShouziliaoActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 2) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 1);
//                startActivity(intent5);
//            }
//            if (position == 3) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 3);
//                startActivity(intent5);
//            }
//            if (position == 4) {
//                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
//                intent5.putExtra(Globals.GWGL, 4);
//                startActivity(intent5);
//            }
//            if (position == 5) {
//                Intent intent = new Intent(getActivity(), ChuanZiLiaoActivity.class);
//                startActivity(intent);
//            }
//            if (position == 6) {
//                Intent intent21 = new Intent(getActivity(), XieZiLiaoActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 7) {
//                Intent intent21 = new Intent(getActivity(), HyapcxActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 8) {
//                Intent intent4 = new Intent(getContext(), SDCARD123Activity.class);
//                intent4.putExtra("bsf", "sy");
//                startActivity(intent4);
//            }
//
//            if (position == 9) {
//                Intent intent21 = new Intent(getActivity(), GongZiActivity.class);
//                startActivity(intent21);
//            }
//            if (position == 10) {
//                Intent intent21 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent21.putExtra(Globals.ZLWJ, 1);
//                startActivity(intent21);
//            }
//            if (position == 11) {
//                Intent intent22 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent22.putExtra(Globals.ZLWJ, 2);
//                startActivity(intent22);
//            }
//            if (position == 12) {
//                Intent intent25 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent25.putExtra(Globals.ZLWJ, 5);
//                startActivity(intent25);
//            }
//            if (position == 13) {
//                Intent intent23 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent23.putExtra(Globals.ZLWJ, 3);
//                startActivity(intent23);
//            }
//            if (position == 14) {
//                Intent intent = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent.putExtra(Globals.WDGL, 4);
//                startActivity(intent);
//            }
//            if (position == 15) {
//                Intent intent1 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent1.putExtra(Globals.WDGL, 2);
//                startActivity(intent1);
//            }
//            if (position == 16) {
//                Intent intent24 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
//                intent24.putExtra(Globals.ZLWJ, 4);
//                startActivity(intent24);
//            }
//            if (position == 17) {
//                Intent intent4 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent4.putExtra(Globals.WDGL, 3);
//                startActivity(intent4);
//            }
//            if (position == 18) {
//                Intent intent3 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent3.putExtra(Globals.WDGL, 5);
//                startActivity(intent3);
//
//            }
//            if (position == 19) {
//                Intent intent3 = new Intent(getActivity(), HuanJingActivity.class);
//                startActivity(intent3);
//            }
//            /**通知事项*/
//            if (User.sxtz.equals("1")) {
//                if (position == 20) {
//                    Intent intent21 = new Intent(getActivity(), ShiXiangActivity.class);
//                    startActivity(intent21);
//                }
//            } else {
//
//
//                if (User.leadrc.equals("1")) {
//                    if (position == 20) {
//                        Intent intent3 = new Intent(getActivity(), LeadAgendaActivity.class);
//                        startActivity(intent3);
//                    }
//                    if (position == 21) {
//                        Intent intent3 = new Intent(getActivity(), ScheduleActivity.class);
//                        startActivity(intent3);
//                    }
//                } else {
//                    if (position == 20) {
//                        Intent intent21 = new Intent(getActivity(), ScheduleActivity.class);
//                        startActivity(intent21);
//                    }
//                }
//
//            }
//            /**党委章查询*/
//            if (User.sid.equals("taojianhua")) {
//                if (position == 21) {
//                    Intent intent5 = new Intent(getActivity(), DzZfActivity.class);
//                    intent5.putExtra(Globals.QJBS, "党委章查询");
//                    startActivity(intent5);
//
//                }
//                if (position == 22) {
//                    Intent intent21 = new Intent(getActivity(), ScheduleActivity.class);
//                    startActivity(intent21);
//                }
///**政府章查询*/
//            } else if (User.sid.equals("wangzhe")) {
//                if (position == 21) {
//                    Intent intent5 = new Intent(getActivity(), DzZfActivity.class);
//                    intent5.putExtra(Globals.QJBS, "政府章查询");
//                    startActivity(intent5);
//                }
//                if (position == 22) {
//                    Intent intent21 = new Intent(getActivity(), ScheduleActivity.class);
//                    startActivity(intent21);
//                }
//            } else {
//                if (position == 21) {
//                    Intent intent21 = new Intent(getActivity(), ScheduleActivity.class);
//                    startActivity(intent21);
//                }
//            }
//
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oa_home_lianghongdeng:
                //通知公告   改为消息中心
                Intent intent3 = new Intent(getActivity(), XiaoXiZhongXinActivity.class);
                startActivity(intent3);
                break;
            case R.id.oa_home_saoyisao:
                Intent intent = new Intent(getActivity(),
                        CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.oa_home_fanhui:
                DeleteF d = new DeleteF() {
                    @Override
                    public void determineButton() {
                        getActivity().finish();
                    }
                }.deleteDialog(getActivity(), "你确定要退出应用吗？", "", "");
                break;
        }

    }
}
