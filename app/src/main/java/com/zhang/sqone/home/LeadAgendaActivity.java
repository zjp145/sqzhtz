package com.zhang.sqone.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Schedulemk;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.HttpUtilSc;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 领导日程
 *
 * @author ZJP
 *         created at 2016/6/8 9:11
 */
public class LeadAgendaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.txl_title)
    TitleBarView txlTitle;
    @Bind(R.id.lingdaorc_list)
    ExpandableListView lingdaorcList;
    @Bind(R.id.left1_button)
    ImageView left1Button;
    @Bind(R.id.rq_text)
    TextView rqText;
    @Bind(R.id.right1_button)
    ImageView right1Button;
    private MyExpandableListViewAdapter adapter;
    private List<Schedulemk.ReqScheduleMk.ScheduleList> Clist;
    private String nowDate;
    private SimpleDateFormat sf;
    private boolean isone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_lead_agenda);
        ButterKnife.bind(this);
        //设置list的展开
        lingdaorcList.setGroupIndicator(null);
        Date date = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd");
       nowDate = sf.format(date);
        regRequest(nowDate);
        rqText.setText(nowDate);
        right1Button.setOnClickListener(this);
        left1Button.setOnClickListener(this);

    }
    //    try {
//        //获取当前日期
//        Date date = new Date();
//        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
//        String nowDate = sf.format(date);
//        System.out.println(nowDate);
//        //通过日历获取下一天日期
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(sf.parse(nowDate));
//        cal.add(Calendar.DAY_OF_YEAR, +1);
//        String nextDate_1 = sf.format(cal.getTime());
//        System.out.println(nextDate_1);
//        //通过秒获取下一天日期
//        long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒
//        date.setTime(time * 1000);//毫秒
//        String nextDate_2 = sf.format(date).toString();
//        System.out.println(nextDate_2);
//    } catch (Exception e) {
//        // TODO: handle exception
//    }


    /**
     * 加载网络数据
     */
    public void regRequest(String s) {

        Schedulemk.ReqScheduleMk reqDocument = Schedulemk.ReqScheduleMk.newBuilder().setSid(User.sid).setAc("LDRCLB").setDay(s).build();

        new HttpUtilSc() {
            @Override
            public <T> void analysisInputStreamData(Schedulemk.ReqScheduleMk index) throws IOException {
                Clist = index.getSlistList();

                Log.i("zhang", "tia" + Clist.size());
                if (isone){
                    adapter = new MyExpandableListViewAdapter(LeadAgendaActivity.this);

                    lingdaorcList.setAdapter(adapter);
                    isone=false;
                }else{
                    adapter.notifyDataSetChanged();
                }


            }
        }.protocolBuffer(LeadAgendaActivity.this, Globals.LD_RUI, reqDocument, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left1_button:
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(sf.parse(nowDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.add(Calendar.DAY_OF_YEAR, -1);
                String nextDate_1 = sf.format(cal.getTime());
                rqText.setText(nextDate_1);
                nowDate=nextDate_1;
                regRequest(nowDate);
                break;
            case R.id.right1_button:
                Calendar cal2 = Calendar.getInstance();
                try {
                    cal2.setTime(sf.parse(nowDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal2.add(Calendar.DAY_OF_YEAR, +1);
                String nextDate_2 = sf.format(cal2.getTime());
                rqText.setText(nextDate_2);
                nowDate=nextDate_2;
                regRequest(nowDate);
                break;

        }
    }

    // 用过ListView的人一定很熟悉，只不过这里是BaseExpandableListAdapter
    class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context context;

        public MyExpandableListViewAdapter(Context context) {
            this.context = context;
        }

        /**
         * 获取组的个数
         *
         * @return
         * @see ExpandableListAdapter#getGroupCount()
         */
        @Override
        public int getGroupCount() {
            return Clist.size();
        }

        /**
         * 获取指定组中的子元素个数
         *
         * @param groupPosition
         * @return
         * @see ExpandableListAdapter#getChildrenCount(int)
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return Clist.get(groupPosition).getRclistList().size();
        }

        /**
         * 获取指定组中的数据
         *
         * @param groupPosition
         * @return
         * @see ExpandableListAdapter#getGroup(int)
         */
        @Override
        public Object getGroup(int groupPosition) {
            return Clist.get(groupPosition);
        }

        /**
         * 获取指定组中的指定子元素数据。
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see ExpandableListAdapter#getChild(int, int)
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return Clist.get(groupPosition).getRclistList().get(childPosition);
        }

        /**
         * 获取指定组的ID，这个组ID必须是唯一的
         *
         * @param groupPosition
         * @return
         * @see ExpandableListAdapter#getGroupId(int)
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取指定组中的指定子元素ID
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see ExpandableListAdapter#getChildId(int, int)
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
         *
         * @return
         * @see ExpandableListAdapter#hasStableIds()
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * 获取显示指定组的视图对象
         *
         * @param groupPosition 组位置
         * @param isExpanded    该组是展开状态还是伸缩状态
         * @param convertView   重用已有的视图对象
         * @param parent        返回的视图对象始终依附于的视图组
         * @return
         * @see ExpandableListAdapter#getGroupView(int, boolean, View,
         * ViewGroup)
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lead_f, null);
                groupHolder = new GroupHolder();
                groupHolder.txt = (TextView) convertView.findViewById(R.id.lingdao_name);
                groupHolder.txt1 = (TextView) convertView.findViewById(R.id.lingdao_rc);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            //点击的使用改变的图片
//            if (!isExpanded)
//            {
//                groupHolder.img.setBackgroundResource(R.mipmap.address_w);
//            }
//            else
//            {
//                groupHolder.img.setBackgroundResource(R.mipmap.address_j);
//            }

            groupHolder.txt.setText(Clist.get(groupPosition).getName());
            if (Clist.get(groupPosition).getRclistCount()==0){groupHolder.txt1.setText("没有日程");}else{groupHolder.txt1.setText(Clist.get(groupPosition).getRclistCount()+"个日程");}

            return convertView;
        }

        /**
         * 获取一个视图对象，显示指定组中的指定子元素数据。
         *
         * @param groupPosition 组位置
         * @param childPosition 子元素位置
         * @param isLastChild   子元素是否处于组中的最后一个
         * @param convertView   重用已有的视图(View)对象
         * @param parent        返回的视图(View)对象始终依附于的视图组
         * @return
         * @see ExpandableListAdapter#getChildView(int, int, boolean, View,
         * ViewGroup)
         */
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ItemHolder itemHolder = null;
            convertView = LayoutInflater.from(context).inflate(R.layout.lead_z, null);
            itemHolder = new ItemHolder();
            itemHolder.txt = (TextView) convertView.findViewById(R.id.lingdao_shixiang);
            itemHolder.txt1 = (TextView) convertView.findViewById(R.id.lingdao_shijian);


            itemHolder.txt.setText(Clist.get(groupPosition).getRclist(childPosition).getShx());
            itemHolder.txt1.setText(Clist.get(groupPosition).getRclist(childPosition).getTim());


            return convertView;
        }

        /**
         * 是否选中指定位置上的子元素。
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see ExpandableListAdapter#isChildSelectable(int, int)
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    class GroupHolder {
        public TextView txt;

        public TextView txt1;
    }

    class ItemHolder {
        public TextView txt;
        public TextView txt1;
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
}
