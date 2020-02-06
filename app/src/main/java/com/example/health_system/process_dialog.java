package com.example.health_system;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class process_dialog extends Thread {
    Context context;
    String message;
    int target;
    Handler handler;
    ProgressDialog progressDialog;
    int now = 0;

    process_dialog(String message, int target, Context context) {
        handler = new Handler();
        this.context = context;
        this.message = message;
        this.target = target;
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage(message);
        progressDialog.setMax(target);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //progressDialog.dismiss();
        progressDialog.show();
    }

    @Override
    public void run() {


        super.run();
        while (true) {
            Log.e("test_process", String.valueOf(now));
            if (now > target) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
                break;
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress(now);
                    }
                });
                now++;
                try {

                    sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
