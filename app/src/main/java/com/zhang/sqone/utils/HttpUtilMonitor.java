package com.zhang.sqone.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zhang.sqone.Globals;
import com.zhang.sqone.bean.Monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/3/7.
 */
public abstract class HttpUtilMonitor {
    /**
     * 创建的使用加载的提示框
     */
    public AlertUtil au;

    /**
     * 调用方法执行操作 访问接口获得数据
     *
     * @param context 上下文对象
     * @param url     文件的下载地址
     * @param ob      控件
     */
    public <T> void protocolBuffer(final Context context,
                                   final String url, final Monitor.MonIndex index, final Object ob) {
        /**提示*/
        final Handler myHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Toast.makeText(Globals.context, "当前网络状态不好", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        //创建加载的对话框
        if (ob == null) {
            if (context != null) {
                if (AppUtil.networkCheck() == false) {
                    Toast.makeText(Globals.context, "没有网络", Toast.LENGTH_SHORT).show();
                } else {

                    au = new AlertUtil(context);
                }
            }
        }

        Observable.create(new Observable.OnSubscribe<Monitor.MonIndex>() {
            @Override
            public void call(final Subscriber<? super Monitor.MonIndex> subscriber) {
                //创建子线程加载数据

                HttpURLConnection httpURLConnection;
                try {
                    //建立链接
                    URL gatewayUrl = new URL(url);
                    httpURLConnection = (HttpURLConnection) gatewayUrl.openConnection();

                    //设置连接属性
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);

/*时间*/
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
                    //byte[] requestStringBytes = requestString.getBytes(ENCODING_REQUEST);

                    //设置请求属性
                    //httpURLConnection.setRequestProperty("Content-length", "" + requestStringBytes.length);
                    httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");


                    //建立输出流，并写入数据
                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    // Document jhon = Document.newBuilder().setAc(ac).build();
                    try {
                        index.writeTo(outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //outputStream.write(requestStringBytes);
                    outputStream.close();

                    int responseCode = httpURLConnection.getResponseCode();
                    if (HttpURLConnection.HTTP_OK == responseCode) {
                        Log.i("onsuccess", "访问成功");
                        //请求访问成功
                        InputStream inputStream = httpURLConnection.getInputStream();

                        if (au != null) {
                            au.closeDialog();
                        }

                        if (inputStream != null) {
                            Monitor.MonIndex jhon1 = Monitor.MonIndex.parseFrom(inputStream);
                            Log.i("请求响应", "stu" + jhon1.getStu() + "______" +
                                    "scd" + jhon1.getScd() + "______" +
                                    "mag" + jhon1.getMsg()
                            );
                            subscriber.onNext(jhon1);

                        }

                        //创建集合的实例类型
//                Document.FileList.Builder fileList = Document.FileList.newBuilder();

                    } else {
                        // 请求访问失败
                        if (au != null) {
                            au.closeDialog();

                        }
                        if (AppUtil.networkCheck() == false) {
                            Toast.makeText(Globals.context, "没有网络", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Globals.context, Globals.SER_ERROR,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    au.closeDialog();
                    /*错误*/
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                    e.printStackTrace();
                }

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<Monitor.MonIndex>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Monitor.MonIndex reqIndex) {
//                                httpText.setText(reqIndex.getFlistList().get(0).getFd());
                        Log.i("jihe", "数据集合的个数" + reqIndex.getCompanylistList().size());
                        if (reqIndex.getStu() == null || !(reqIndex.getStu().equals("1"))) {
                            Toast.makeText(Globals.context,
                                    Globals.SER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        if (reqIndex.getScd()==null||reqIndex.getScd().equals("")){
//
//                        }
//                        if (!reqIndex.getScd().equals("1")) {
//                            Toast.makeText(Globals.context, reqIndex.getMsg(), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        try {
                            analysisInputStreamData(reqIndex);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public abstract <T> void analysisInputStreamData(Monitor.MonIndex index) throws IOException;
    
}
