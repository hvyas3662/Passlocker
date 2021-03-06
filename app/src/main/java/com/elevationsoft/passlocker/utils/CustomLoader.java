package com.elevationsoft.passlocker.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.elevationsoft.passlocker.R;


public class CustomLoader {
    private static CustomLoader instance;
    private AlertDialog alertDialog;


    public static CustomLoader getInstance() {
        if (instance == null) {
            instance = new CustomLoader();
        }
        return instance;
    }

    public static CustomLoader getNewInstance() {
        return new CustomLoader();
    }


    public void showLoader(Activity act) {
        if (act != null) {
            hideLoader(act);
            View v = act.getLayoutInflater().inflate(R.layout.layout_loader_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.Passlocker_Dialog).setCancelable(false).setView(v);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }
    }

    public void showLoader(Activity act, String message) {
        if (act != null) {
            hideLoader(act);
            View v = act.getLayoutInflater().inflate(R.layout.layout_msg_loader_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(act).setCancelable(false).setView(v);
            alertDialog = builder.create();
            alertDialog.show();
            TextView tv_msg = v.findViewById(R.id.tv_msg);
            tv_msg.setVisibility(View.VISIBLE);
            tv_msg.setText(message);

        }
    }


    public void hideLoader(Activity act) {
        try {
            if (act != null && !act.isFinishing() && !act.isDestroyed()) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            alertDialog = null;
        }
    }


}
