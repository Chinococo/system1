package com.example.health_system;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class output_csv {
    private static Context context;

    public output_csv(Context context) {
        this.context = context;
    }

    public  void WriteFileExample(String filename,StringBuilder message) {
        FileOutputStream fop = null;
        File file;
        StringBuilder content = message;

       try{
     File sdcard = Environment.getExternalStorageDirectory();
            file = new File(sdcard, filename+".csv"); //輸出檔案位置
            Log.i("Write File:", file + "");
            fop = new FileOutputStream(file);

            if (!file.exists()) { // 如果檔案不存在，建立檔案
                file.createNewFile();
            }

            byte[] contentInBytes = content.toString().getBytes();// 取的字串內容bytes

            fop.write(contentInBytes); //輸出
            fop.flush();
            fop.close();
        } catch (Exception e) {
            Log.i("Write E:", e + "");
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (Exception e) {
                Log.i("Write IOException", e + "");
                e.printStackTrace();
            }
        }
    }
}