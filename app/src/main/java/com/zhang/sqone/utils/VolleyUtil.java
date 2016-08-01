package com.zhang.sqone.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhang.sqone.Globals;

import java.util.Map;

/**
 * 2015年5月5日17:03:46
 */
public abstract class VolleyUtil {
    public AlertUtil au;
    private final int SPLASH_DISPLAY_LENGHT = 1500;

    // RetryPolicy retryPolicy =
    // post请求封装
    public <T> void volleyStringRequestPost(final Context context,
                                            final Map<String, String> paramsss, final Object ob) {

        Log.i(Globals.LOG_TAG, "paramsss=" + paramsss);
         RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        if(ob == null) {
            if (context != null) {
                au = new AlertUtil(context);
            }
        }

        StringRequest sr = new StringRequest(Request.Method.POST,
                Globals.WS_URI_WJ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asd123456", response);
                if (ob != null) {
                    ((PullToRefreshListView)ob).onRefreshComplete();
                }
                if (au != null) {
                    au.closeDialog();
                }
                if (response.contains("\"Stu\":0")) {
                    Log.i(Globals.LOG_TAG, "res:" + response);
                    Toast.makeText(context, Globals.SER_ERROR,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                if (ob != null) {
                    ((PullToRefreshListView)ob).onRefreshComplete();
                }
                Log.i("Response", "error" + error.getMessage());

                if (au != null) {
                    au.closeDialog();

                }
                if (AppUtil.networkCheck() == false) {
                    Toast.makeText(Globals.context, "没有网络", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, Globals.SER_ERROR,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsss;
            }
        };


        // sr.setRetryPolicy(new DefaultRetryPolicy(15 * 1000,
        // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        Log.i("asd123456", sr.toString());
        mRequestQueue.add(sr);
        // new Handler().postDelayed(new Runnable() {
        // public void run() {
        // if (au != null) {
        // au.closeDialog();
        // }
        // }
        // }, SPLASH_DISPLAY_LENGHT);

    }

    public abstract <T> void analysisData(String response);

}
