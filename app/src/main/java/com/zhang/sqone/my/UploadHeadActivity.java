package com.zhang.sqone.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.R;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 上传头像界面
 *
 * @author ZJP
 *         created at 2016/2/25 11:28
 */
public class UploadHeadActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.shangchuan_xiangce)
            /**上传头像（相册）*/
    LinearLayout shangchuanXiangce;
    @Bind(R.id.shangchuan_paizhao)
            /**上传头像（拍照）*/
    LinearLayout shangchuanPaizhao;
    private Intent intent;
    private static int position = 0;
    private String a;
    private Uri imageUri;
    private static final String IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory().toString()+"/zhtztp.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_upload_head);
        ButterKnife.bind(this);
        intent = getIntent();
        imageUri = Uri.fromFile(new File(IMAGE_FILE_LOCATION));
        onClicks();
    }

    private void onClicks() {
        shangchuanPaizhao.setOnClickListener(this);
        shangchuanXiangce.setOnClickListener(this);
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
            //拍照
            case R.id.shangchuan_paizhao:
//                拍照的时候也获得图片放在本地
                    a = "file:///sdcard/temp" + position + ".jpg";
                    Uri imageUri = Uri.parse(a);
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 2);
                break;
            //相册
            case R.id.shangchuan_xiangce:
                /**从相册获取图片*/
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                //TODO
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    Log.i("tupiance", "使用的是相册功能");
                            openCrop(data.getData());
                    break;
                case 2:

                        Log.i("tupiance", "使用的拍照功能");
                        openCrop(Uri.parse(a));


                    break;
                case 3:
                    intent.putExtra("ok", IMAGE_FILE_LOCATION);
                    setResult(2, intent);
                    finish();
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
        intent.putExtra("return-data", false);//若为false则表示不返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }

}
