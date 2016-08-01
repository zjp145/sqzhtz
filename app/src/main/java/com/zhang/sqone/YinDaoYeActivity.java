package com.zhang.sqone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhang.sqone.my.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户首次使用程序的时候显示的欢迎页面
 */
public class YinDaoYeActivity extends BaseActivity {
    @Bind(R.id.dh_viewpager)
    ViewPager dhViewpager;/*导航的Viewpager*/
    @Bind(R.id.dh_imageview1)
    ImageView dhImageview1;/*导航按钮1*/
    @Bind(R.id.dh_imageview2)
    ImageView dhImageview2;/*导航按钮2*/
    @Bind(R.id.dh_imageview3)
    ImageView dhImageview3;/*导航按钮3*/
    @Bind(R.id.dh_linearlayout1)
    LinearLayout dhLinearlayout1;/*放置按钮的布局*/

    private List<ImageView> imageViews;/*存放导航图片的所在iamgeView的集合*/
    private ImageView[] images;/*存放指示图片的数组*/
    private int[] image = {R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3};/*存放导航的资源图片id*/
    /*导航使用背景颜色*/
    private int[] color = {R.color.banner1_bj,R.color.banner2_bj,R.color.banner3_bj};
    private ArrayList<RelativeLayout> relativeLayouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yin_dao_ye_activity);
        ButterKnife.bind(this);
//        dhViewpager.setBackgroundResource(R.color.banner1_bj);
        //创建一个集合  欢迎页中使用的ImageView
        imageViews = new ArrayList<ImageView>();
         relativeLayouts = new ArrayList<RelativeLayout>();
        //给iamgeViews集合中添加数据
        for (int i = 0; i < image.length; i++) {
//            获得布局
            RelativeLayout tmp = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.banner_iamgeview,null);
            //动态创建一个iamgeView
            ImageView imageView = (ImageView) tmp.findViewById(R.id.banner_image);
            //给iamgeView中添加iamge的资源
            imageView.setImageResource(image[i]);
            tmp.setBackgroundResource(color[i]);

            //将iamgeView添加到集合中
            imageViews.add(imageView);
            relativeLayouts.add(tmp);
        }
        //将指示图片添加到数组中的方法
        imageAddViews();
        //Viewpager 添加适配器
        dhViewpager.setAdapter(new MyAdapter());
        //Viewpager中添加预加载的属性 预加载两页
        dhViewpager.setOffscreenPageLimit(2);
        //Viewpager 添加改变的触发事件
        dhViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //当前的page的改变完成是调用
            @Override
            public void onPageSelected(int position) {

////                当翻到最后一页的时候
                if (position==2){
                   Button button = (Button) relativeLayouts.get(position).findViewById(R.id.image_button);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(YinDaoYeActivity.this,  LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                //添加的是根据滑动的角标设置相对的背景颜色
//                switch (position){
//                    case 0:
//                        dhViewpager.setBackgroundResource(R.color.banner1_bj);
//                        break;
//                    case 1:
//                        dhViewpager.setBackgroundResource(R.color.banner2_bj);
//                        break;
//                    case 2:
//                        dhViewpager.setBackgroundResource(R.color.banner3_bj);
//                        break;
//
//                }

            //重新给指示图片设置选中状态
                for (int i =0;i<imageViews.size();i++){
                    //选中状态
                    images[i].setEnabled(false);
                }
                //未选中状态
                images[position].setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void imageAddViews() {
        //创建使用指示图片的数组
        images = new ImageView[image.length];
        //通过for循环将layout中指示 添加到数组中
        for (int i = 0; i<images.length;i++){
            //获得将layout中的iamgeView添加数组中
            images[i] = (ImageView) dhLinearlayout1.getChildAt(i);
            //设置iamgeView的选中状态
            images[i].setEnabled(false);
            //给imageView添加标记
            images[i].setTag(i);
            //设指示图片的点击事件 点击的时候将Viewpager更换
            images[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dhViewpager.setCurrentItem((Integer) v.getTag());
                }
            });
        }
        //默认第一个指示点未选中
        images[0].setEnabled(true);
    }
    //创建一个Viewpager的适配器
    public class MyAdapter extends PagerAdapter{
        /*
        * 一个重写了四个方法
        *
        * */
        //adapter 的适配长度
        @Override
        public int getCount() {
            return relativeLayouts.size();
        }
        //通过下角标的获得View对象

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //将集合中的iamgeView添加到adapter中
            container.addView(relativeLayouts.get(position));
            return relativeLayouts.get(position);
        }
        //View的删除功能
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(relativeLayouts.get(position));
        }
        //判断在viewpager显示的图片和我们instantiate中返回的图片是不是相同的
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
