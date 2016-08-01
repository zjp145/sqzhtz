package com.zhang.sqone.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.zhang.sqone.R;


public class AlertUtil {

    private AlertDialog dialog;
    private Context context;

    public AlertUtil(Context context) {
        this.context = context;
        alert();
    }

    public void alert() {
        dialog = new AlertDialog.Builder(context).create();
//        context.getAssets()
        try {
            if (context != null) {
                dialog.show();
                dialog.getWindow().setContentView(R.layout.loading);
            }
        } catch (Exception e) {
        }

    }

    public void closeDialog() {
        try {
            if (dialog != null && context != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
        }


    }

}
