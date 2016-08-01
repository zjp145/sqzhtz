package com.zhang.sqone.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhang.sqone.R;


/*
* 自定义的actionbar继承了相对布局 创建一些特定的属性
* */
public class TitleBarView extends RelativeLayout {
	//点击的返回按钮回调接口
	public interface OnClickBackButtonListener{
		public void onClickBackButton(View v);
	}
	//点击右侧按钮回调接口
	public interface OnClickEnterButtonListener{
		public void onClickEnterButton(View v);
	}

	private static final String BACK = "BACK";
	private static final String ENTER = "ENTER";
	private static final String Android = "http://schemas.android.com/apk/res/android";
	public static Boolean Signed = false;
	public static Boolean SignedBack = true;
	private ImageButtonTouchListener EventInstance;
	private TextView txvTitle;
	private ImageButton imbBack;
	public ImageButton imbEnter;
	private OnClickBackButtonListener _Back_Handler_;
	private OnClickEnterButtonListener _Enter_Handler_;
	private static Drawable BackImage;
	private static Drawable BackPressImage;
	private static Drawable RightImage;
	private static Drawable RightImagePress;
	//自定义方法等点击返回按钮的时候实现接口中的方法
	public void setClickBackButtonListener(OnClickBackButtonListener handler){
		_Back_Handler_ = handler;
	}
	//自定义方法等点击提交按钮的时候实现接口中的方法
	public void setClickEnterButtonListener(OnClickEnterButtonListener handler){
		_Enter_Handler_ = handler;
	}
	//自定义获取标题的方法
	public CharSequence getText() {
		if(txvTitle == null) return "";
		return txvTitle.getText();
	}
	//自定义添加标题的方法
	public void setText(CharSequence text) {
		if(txvTitle == null) return;
		txvTitle.setText(text);
	}
	//三个构造方法
	public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ApplyStyle(context, attrs);
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ApplyStyle(context, attrs);
	}
	
	public TitleBarView(Context context) {
		super(context);
		ApplyStyle(context, null);
	}
	//等我们创建对象时候也会将数据添加到这个方法中
	@SuppressLint("ResourceAsColor")
	private void ApplyStyle(Context context, AttributeSet attrs) {
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_bar_view, this);
		
		txvTitle = (TextView)findViewById(R.id.tbv_title);
		imbBack = (ImageButton)findViewById(R.id.tbv_back);
		imbEnter = (ImageButton)findViewById(R.id.tbv_enter);
		BackImage = context.getResources().getDrawable(R.mipmap.back_icon);
		BackPressImage = context.getResources().getDrawable(R.mipmap.back_icon);
		EventInstance = new ImageButtonTouchListener();
		//cxtActivity = context;
		//如果是在xml中设置的text  Java设置的title的方法
		if(attrs != null) {
			int index = attrs.getAttributeResourceValue(Android, "text" ,-1);
			if(index == -1){
				txvTitle.setText(attrs.getAttributeValue(Android, "text"));
			}else{
				txvTitle.setText(context.getResources().getText(index));
			}
		}
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
		//创建按钮的使用方法
		if(array.getBoolean(R.styleable.TitleBarView_leftButtonEnable, SignedBack)){
			imbBack.setVisibility(View.VISIBLE);
			imbBack.setImageDrawable(
					context.getResources().getDrawable(
							array.getResourceId(R.styleable.TitleBarView_leftButtonBgImg, R.mipmap.back_icon
									)));
			
			imbBack.getBackground().setAlpha(0);
			imbBack.setPadding(0, 0, 0, 0);
			imbBack.setTag(BACK);
			imbBack.setOnTouchListener(EventInstance);
		}else{
			imbBack.setVisibility(View.GONE);
		}
		
		if(array.getBoolean(R.styleable.TitleBarView_rightButtonEnable, Signed)){
			RightImage = context.getResources().getDrawable(array.getResourceId(R.styleable.TitleBarView_rightButtonBgImg, R.mipmap.tongxunlu_xuanzhong));
			RightImagePress = context.getResources().getDrawable(array.getResourceId(R.styleable.TitleBarView_rightButtonBgImgPress, R.mipmap.tongxunlu_xuanzhong));
			imbEnter.setVisibility(View.VISIBLE);
			imbEnter.setImageDrawable(
					context.getResources().getDrawable(
							array.getResourceId(R.styleable.TitleBarView_rightButtonBgImg, R.mipmap.tongxunlu_xuanzhong
									)));
			imbEnter.getBackground().setAlpha(0);
			imbEnter.setPadding(0, 0, 0, 0);
			imbEnter.setTag(ENTER);
			imbEnter.setOnTouchListener(EventInstance);
		}else{
			imbEnter.setVisibility(View.GONE);
		}
	}
	
//	@Override
//	protected void onDraw(Canvas canvas) {
//		int h = getMeasuredHeight();
//		int w = getMeasuredWidth();
//		
//		canvas.drawLine( 0, h - lineWidth, w, h - lineWidth, Line);
//		super.onDraw(canvas);
//	}
	
	class ImageButtonTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				//重新设置按下时的背景图片    
				if(v.getTag().toString().equals(BACK)){
					((ImageButton)v).setImageDrawable(BackPressImage);
				}else{
//					((ImageButton)v).setImageDrawable(RightImagePress);
				}
	        }else if(event.getAction() == MotionEvent.ACTION_UP){
	        	//再修改为抬起时的正常图片
	        	if(v.getTag().toString().equals(BACK)){
					((ImageButton)v).setImageDrawable(BackImage);
					if(_Back_Handler_ == null){
						((Activity) TitleBarView.this.getContext()).finish();
					}else{
						_Back_Handler_.onClickBackButton(v);
					}
				}else{
//					((ImageButton)v).setImageDrawable(RightImage);
					if(_Enter_Handler_ == null){
//						((Activity) TitleBarView.this.getContext()).startActivity(new Intent(
//								TitleBarView.this.getContext(),
//								SplashScreenActivity.class
//								));
					}else{
						_Enter_Handler_.onClickEnterButton(v);
					}
				}
	        }  else {
	        	;
	        }
			return false;
		}
		
	}
}
