package com.zhang.sqone.my;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.Test1;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.bean.WenJianBean;
import com.zhang.sqone.utils.AlertUtil;
import com.zhang.sqone.utils.GsonUtils;
import com.zhang.sqone.utils.ImageUtil;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.TextUtilHttp;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.views.CircularImage;
import com.zhang.sqone.views.DeleteF;
import com.zhang.sqone.views.DeleteF2;
import com.zhang.sqone.views.GengXinD2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 我的模块
 *
 * @author ZJP
 *         created at 2016/2/1 13:44
 */
public class FragmentMore extends Fragment implements View.OnClickListener {

    @Bind(R.id.shouye_tuxiang)
    CircularImage shouyeTuxiang;
    @Bind(R.id.my_dangan)
    LinearLayout myDangan;
    @Bind(R.id.my_mima)
    LinearLayout myMima;
    @Bind(R.id.my_jiance)
    LinearLayout myJiance;
    @Bind(R.id.my_zhuxiao)
    LinearLayout myZhuxiao;
    @Bind(R.id.yonghu_id)
    TextView yonghuId;
    @Bind(R.id.my_gengxin)
    LinearLayout myGengxin;
    @Bind(R.id.my_head)
    LinearLayout myHead;
    private AlertUtil au;
    private String pathImage;
    private RequestParams params;
    private List<Test1.ReqTest11.Test11Map> t1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_space, container, false);
        ButterKnife.bind(this, view);
//        Bundle bundle = getArguments();
//        Log.i("zhang", "数据 ++");
        ImageLoader.getInstance().displayImage(User.IconPath, shouyeTuxiang);
        myDangan.setOnClickListener(this);
        myMima.setOnClickListener(this);
        myJiance.setOnClickListener(this);
        myZhuxiao.setOnClickListener(this);
        myGengxin.setOnClickListener(this);
        myHead.setOnClickListener(this);
        yonghuId.setText(User.sid);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        initLogin();
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = ImageUtil.getSmallBitmap(pathImage);
            shouyeTuxiang.setImageBitmap(addbmp);
            imageTj();
        }
    }

    public void initLogin() {
        Log.i("zhang", "我的用户名："+User.sid);
        Index.ReqIndex.Login.Builder login = Index.ReqIndex.Login.newBuilder();
        login.setUsername(User.sid);
        //添加密码
        login.setPassword(User.pwd);
        login.setCid(User.cid);
        login.setType(User.type);
        login.setPhncode(User.mis_id);
        //Index.ReqIndex.ReqRec.Builder message = Index.ReqIndex.ReqRec.newBuilder();
        //message.setPhone("15931295549");
        //message.setPwd("15910438651");
        //message.setYzm("123456");
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setLogin(login).setAc("LOGIN").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //登陆成功
                if(index.getScd().equals("1")){
                    //头像地址
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd=index.getLogin().getWccd();
                    User.tzts=index.getLogin().getTzts();
                    User.dbts=index.getLogin().getDbts();
                    User.sfzl=index.getLogin().getZlts();
                    User.xxzx = index.getLogin().getXxzxts();
                    Log.i("zhang", "完成度"+User.wcjd);
                    Log.i("zhang", "未读通知"+User.tzts);
                    Log.i("zhang", "未读工作"+User.dbts);
                    Log.i("zhang", "资料条数"+User.sfzl);
                    Log.i("zhang","消息中心"+User.xxzx);

                }

            }
        }.protocolBuffer(getActivity(), Globals.WS_URI, index, null);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_dangan:
                Intent intent = new Intent(getActivity(), UserDAActivity.class);
                startActivity(intent);
                break;
            case R.id.my_mima:
                Intent intent1 = new Intent(getActivity(), XiuGaiMiMaActivity.class);
                startActivity(intent1);
                break;
            case R.id.my_jiance:

                DeleteF2 d = new DeleteF2() {
                    @Override
                    public void determineButton() {

                    }
                }.deleteDialog(getActivity(), "当前是最新版本v1.7.6", "", "");
                break;
            case R.id.my_gengxin:
                final Test1.ReqTest11 index = Test1.ReqTest11.newBuilder().setAc("TEST11").build();
        new TextUtilHttp() {
            @Override
            public <T> void analysisInputStreamData(Test1.ReqTest11 index) throws IOException {
                t1 = index.getTest11ListList();
                Log.i("zhang", "_______"+t1.get(1).getTs());
                GengXinD2 d2 = new GengXinD2() {
                    @Override
                    public void determineButton() {

                    }
                }.deleteDialog(getActivity(),t1);
            }

            @Override
            public <T> void test(OutputStream outputStream) throws IOException {
                index.writeTo(outputStream);
            }

            @Override
            public <T> void test2(InputStream inputStream) throws IOException {
//                Test1.ReqTest11 jhon1 = Test1.ReqTest11.parseFrom(inputStream);
//               t1 = jhon1.getTest11ListList();
//                Log.i("zhang", "_______"+t1.get(1).getTs());

            }
        }.protocolBuffer(getActivity(), Globals.GX, index, null);

//                GengXinD2 d2 = new GengXinD2() {
//                    @Override
//                    public void determineButton() {
//
//                    }
//                }.deleteDialog(getActivity(),t1);
//                GengxinD d3 = new GengxinD() {
//                    @Override
//                    public void determineButton() {
//
//                    }
//                }.deleteDialog(getActivity(), "当前是最新版本v1.7.5", "", "");
                break;
            case R.id.my_zhuxiao:
                if (User.isLogin) {
                    DeleteF d1 = new DeleteF() {
                        @Override
                        public void determineButton() {
                            SharedPreferencesUtils.saveString(getActivity(),
                                    Globals.USER_PHONE, "");
                            SharedPreferencesUtils.saveString(getActivity(),
                                    Globals.USER_PASSWORD, "");
                            SharedPreferencesUtils.saveString(getActivity(),
                                    Globals.USER_NC, "");
                            SharedPreferencesUtils.saveString(getActivity(),
                                    Globals.USER_JJMM, "0");
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LoginActivity.class);
//                            intent.putExtra("loginInfo", "login");
                            startActivity(intent);
                            User.setLoginInfo("", false, "", "");
                            getActivity().finish();
                        }
                    }.deleteDialog(getActivity(), "你确定要退出当前账号？", "", "");
                }
                break;
            case R.id.my_head:
                //上传头像界面
                Intent intent3 = new Intent(getContext(), UploadHeadActivity.class);

                startActivityForResult(intent3, 2);

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {

            case 2:
                Log.i("tupiance", "图片上传");
                if (data != null) {
                    // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();
//                    Log.i("s123s", AppUtil.getRealFilePath(this, mImageCaptureUri));


                    // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
//                    if (mImageCaptureUri != null) {
//                        pathImage = getRealFilePath(getActivity(), mImageCaptureUri);
//                        Log.i("zhang", "上传图片的路径是：++++++++++"+pathImage);
////                        suri.add(getRealFilePath(this, mImageCaptureUri));
//                        Log.i("tupiance", "获取到相册的图片");
//                    } else {
                        if (data.getStringExtra("ok") != null) {
                            Log.i("tupiance", "获得拍照的图片");
                            String uri = data.getStringExtra("ok");
                            Uri imageUri = Uri.parse(uri);
                            pathImage = getRealFilePath(getActivity(), imageUri);
//                                suri.add(getRealFilePath(this, imageUri));
                        }
//                    }

                }
                break;
        }

    }


    private void imageTj() {
        au = new AlertUtil(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        params = new RequestParams();
        Log.i("sid", User.sid);
        String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                + User.sid + "\",\"T\":\"1\"}}";
        params.put("id", req);
        //设置文件


        try {
            File imgFile = new File(Environment.getExternalStorageDirectory().toString()+"/zhtztp.jpg");
//            FileOutputStream fos = new FileOutputStream(imgFile);
//            ImageUtil.getSmallBitmap(pathImage).compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
            params.put("MyImg.png", imgFile, "image/jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }


        client.post(getActivity(), Globals.WS_URI_POTO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String  s = new String(bytes);
                WenJianBean s1 = GsonUtils.json2bean(s.toString(),
                        WenJianBean.class);
                if (s1 == null || !(s1.getStu() == 1)) {
                    Toast.makeText(getActivity(), Globals.SER_ERROR,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String ss = "sssss";
                    Log.i("swww1", "成功---"+s1.getRst().getPo()+ss);
                    User.IconPath= s1.getRst().getPo();
                }


                au.closeDialog();
//                finish();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("swww2", throwable.getMessage());
                Log.i("swww3", throwable.toString());
                Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //将uri转换成String形式
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
