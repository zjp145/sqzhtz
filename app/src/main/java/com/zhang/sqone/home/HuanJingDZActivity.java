package com.zhang.sqone.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Monitor;
import com.zhang.sqone.utils.HttpUtilMonitor;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;
import java.util.List;

/**
*环境监测获得地址的界面（界面使用了ExpandableListView二级下拉列表）
*@author ZJP
*created at 2016/3/6 13:30
*/
public class HuanJingDZActivity extends BaseActivity
{
    /**二级列表*/
    private ExpandableListView expandableListView;
    /**二级列表的适配器*/
    private MyExpandableListViewAdapter adapter;
    /**使用的数据集合*/
    private List<Monitor.MonIndex.companyMap> Clist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_huan_jing_dz);
        final Intent intent = getIntent();
        //请求数据
        regRequest();
        expandableListView = (ExpandableListView)findViewById(R.id.expendlist);
        //设置list的展开
        expandableListView.setGroupIndicator(null);
        // 监听组点击
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                if (Clist.get(groupPosition).getCompanyname().isEmpty())
                {
                    return true;
                }
                return false;
            }
        });

        // 监听每个分组里子控件的点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                intent.putExtra(Globals.HJ_DZ_DW, Clist.get(groupPosition).getCompanyname());
                intent.putExtra(Globals.HJ_DZ_ID,Clist.get(groupPosition).getAdresslistList().get(childPosition).getAdressid());
                intent.putExtra(Globals.HJ_DZ_NA,Clist.get(groupPosition).getAdresslistList().get(childPosition).getAdressname());
                setResult(Activity.RESULT_OK, intent);//返回页面1
                finish();
                return false;
            }
        });
//

    }
    /**加载网络数据*/
    public void regRequest() {

        Monitor.MonIndex index = Monitor.MonIndex.newBuilder().setAc("JCDZL").build();

        new HttpUtilMonitor() {
            @Override
            public <T> void analysisInputStreamData(Monitor.MonIndex index) throws IOException {
                Clist =index.getCompanylistList();

                adapter = new MyExpandableListViewAdapter(HuanJingDZActivity.this);

                expandableListView.setAdapter(adapter);
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    expandableListView.expandGroup(i);
                }
            }
        }.protocolBuffer(HuanJingDZActivity.this, Globals.HJ_URI, index, null);
    }


    // 用过ListView的人一定很熟悉，只不过这里是BaseExpandableListAdapter
    class MyExpandableListViewAdapter extends BaseExpandableListAdapter
    {

        private Context context;

        public MyExpandableListViewAdapter(Context context)
        {
            this.context = context;
        }

        /**
         *
         * 获取组的个数
         *
         * @return
         * @see android.widget.ExpandableListAdapter#getGroupCount()
         */
        @Override
        public int getGroupCount()
        {
            return Clist.size();
        }

        /**
         *
         * 获取指定组中的子元素个数
         *
         * @param groupPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
         */
        @Override
        public int getChildrenCount(int groupPosition)
        {
            return Clist.get(groupPosition).getAdresslistList().size();
        }

        /**
         *
         * 获取指定组中的数据
         *
         * @param groupPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getGroup(int)
         */
        @Override
        public Object getGroup(int groupPosition)
        {
            return Clist.get(groupPosition);
        }

        /**
         *
         * 获取指定组中的指定子元素数据。
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getChild(int, int)
         */
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            return Clist.get(groupPosition).getAdresslistList().get(childPosition);
        }

        /**
         *
         * 获取指定组的ID，这个组ID必须是唯一的
         *
         * @param groupPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getGroupId(int)
         */
        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        /**
         *
         * 获取指定组中的指定子元素ID
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see android.widget.ExpandableListAdapter#getChildId(int, int)
         */
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        /**
         *
         * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
         *
         * @return
         * @see android.widget.ExpandableListAdapter#hasStableIds()
         */
        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        /**
         *
         * 获取显示指定组的视图对象
         *
         * @param groupPosition 组位置
         * @param isExpanded 该组是展开状态还是伸缩状态
         * @param convertView 重用已有的视图对象
         * @param parent 返回的视图对象始终依附于的视图组
         * @return
         * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
         *      android.view.ViewGroup)
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            GroupHolder groupHolder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_group, null);
                groupHolder = new GroupHolder();
                groupHolder.txt = (TextView)convertView.findViewById(R.id.txt);
                groupHolder.img = (ImageView)convertView.findViewById(R.id.img);
                convertView.setTag(groupHolder);
            }
            else
            {
                groupHolder = (GroupHolder)convertView.getTag();
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
            groupHolder.img.setBackgroundResource(R.mipmap.address_w);
            groupHolder.txt.setText(Clist.get(groupPosition).getCompanyname());
            return convertView;
        }

        /**
         *
         * 获取一个视图对象，显示指定组中的指定子元素数据。
         *
         * @param groupPosition 组位置
         * @param childPosition 子元素位置
         * @param isLastChild 子元素是否处于组中的最后一个
         * @param convertView 重用已有的视图(View)对象
         * @param parent 返回的视图(View)对象始终依附于的视图组
         * @return
         * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
         *      android.view.ViewGroup)
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            ItemHolder itemHolder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_item, null);
                itemHolder = new ItemHolder();
                itemHolder.txt = (TextView)convertView.findViewById(R.id.txt);
//                itemHolder.img = (ImageView)convertView.findViewById(R.id.img);
                convertView.setTag(itemHolder);
            }
            else
            {
                itemHolder = (ItemHolder)convertView.getTag();
            }
            itemHolder.txt.setText(Clist.get(groupPosition).getAdresslist(childPosition).getAdressname());
//            itemHolder.img.setBackgroundResource(item_list2.get(groupPosition).get(childPosition));
            return convertView;
        }

        /**
         *
         * 是否选中指定位置上的子元素。
         *
         * @param groupPosition
         * @param childPosition
         * @return
         * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }

    }

    class GroupHolder
    {
        public TextView txt;

        public ImageView img;
    }

    class ItemHolder
    {
        public ImageView img;

        public TextView txt;
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
