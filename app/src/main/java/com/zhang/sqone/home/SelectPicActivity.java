package com.zhang.sqone.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zhang.sqone.R;


/**
 * 弹出布局通过布局点击效果将 获得图片做操作(选择图片的使用)
 */
public class SelectPicActivity extends Activity implements OnClickListener {
    private LinearLayout pickPhot; // 从相册选择
    private LinearLayout photograph; // 拍照
    private LinearLayout cancel; // 取消
    //	private TextView txvTitle;   //头部
    private String type, type2;
    private Intent intent;
    private static int position = 0;
    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);
        intent = getIntent();
        type = intent.getStringExtra("type");
        type2 = intent.getStringExtra("type2");
        pickPhot = (LinearLayout) findViewById(R.id.update_icon_pick_photo);
        photograph = (LinearLayout) findViewById(R.id.update_icon_photograph);
        cancel = (LinearLayout) findViewById(R.id.update_icon_cancle);

        pickPhot.setOnClickListener(this);
        photograph.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent;
        switch (arg0.getId()) {
            // 选择照片
            case R.id.update_icon_pick_photo:
                /**从相册获取图片*/
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                //TODO
                break;
            // 拍照
            case R.id.update_icon_photograph:
                if (type.equals("icon")) {
                    Log.i("tupiance", "头像的使用4");
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 2);

                } else {
                    Log.i("tupiance", "图片上传4");
                    position++;
                    a = "file:///sdcard/temp" + position + ".jpg";
                    Uri imageUri = Uri.parse(a);
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 2);
                }

                break;
            case R.id.update_icon_cancle:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    if (type.equals("icon")) {
                        Log.i("tupiance", "头像的使用2");
//                        if (type2.equals("icon2")) {
//                            openCrop(data.getData());
//                        } else {

                            if (data != null) {
                                if (data.getExtras() != null) {
                                    intent.putExtras(data.getExtras());
                                } else if (data.getData() != null) {
                                    intent.setData(data.getData());
                                }
                                setResult(1, intent);
                                finish();
                            }
//                        }

                    } else {
                        Log.i("tupiance", "图片上传2");
                        if (data != null) {
                            if (data.getExtras() != null) {
                                Log.i("tupiance", "data.getExtras()");
                                intent.putExtras(data.getExtras());
                            } else if (data.getData() != null) {
                                Log.i("tupiance", "data.getData()");
                                intent.setData(data.getData());
                            }
                            setResult(2, intent);
                            finish();
                        }
                    }
                    break;
                case 2:
                    if (type.equals("icon")) {
                        Log.i("tupiance", "头像的使用3");
                        if (data != null) {
                            if (data.getExtras() != null) {
                                intent.putExtras(data.getExtras());
                            } else if (data.getData() != null) {
                                intent.setData(data.getData());
                            }
                            setResult(1, intent);
                            finish();
                        }
                    } else {
                        Log.i("tupiance", "图片上传3");
                        intent.putExtra("ok", a);
                        setResult(2, intent);
                        finish();
                    }
                    break;
                case 3:
                    if (data != null) {
                        if (data.getExtras() != null)
                            intent.putExtras(data.getExtras());
                        if (data.getData() != null)
                            intent.setData(data.getData());
                        setResult(1, intent);
                        finish();
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openCrop(Uri uri) {
        //TODO 裁剪方法，自己做
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);//若为false则表示不返回数据
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }

}
