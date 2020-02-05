package com.example.health_system;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class wirte_csv extends Thread {
    File Floder= new File(Environment.getExternalStorageDirectory()+File.separator+"123");
    String name;
    Context context;
    wirte_csv(Context context)
    {
        this.context=context;
    }
    @Override
    public void run() {
        try {
            if (!Floder.exists())
                Floder.mkdirs();
            File output = new File(Floder, "test.csv");
            if (!output.exists())
                output.createNewFile();
            FileOutputStream os=new FileOutputStream(output);

        }catch (Exception e)
        {

        }
    }



}
