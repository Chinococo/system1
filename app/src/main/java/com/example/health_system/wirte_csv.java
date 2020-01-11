package com.example.health_system;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class wirte_csv extends Thread {
    Context context;
    ArrayList<score_struct> data;
    ArrayList<String> item;
    File floder;
    String name;
    wirte_csv(Context context, ArrayList<score_struct> i,File floder,String name,ArrayList<String> item)
    {
        this.context=context;
        this.data=i;
        this.floder=floder;
        this.name=name;
        this.item=item;
    }

    @Override
    public void run() {
        super.run();
        File sdcard= floder;
        try
        {
            if(!sdcard.exists())
                sdcard.mkdirs();
            File output=new File(sdcard,name+".csv");
            FileOutputStream outputStream=new FileOutputStream(output);
            if(!output.exists())
                output.createNewFile();
            StringBuilder outputdata=new StringBuilder();



            for(int k=0;k<item.size();k++)
            {
                outputdata.append(item.get(k));
                if(k!=item.size())
                    outputdata.append(",");
                else
                    outputdata.append("\n");
            }



            //ouput data;
            for(int k=0;k<data.size();k++)
            {
                outputdata.append(data.get(k).name+",");
                for(int g=0;g<data.get(k).score.size();g++)
                {
                    outputdata.append(data.get(k).score.get(g));
                    if(g==data.get(k).score.size()-1)
                        outputdata.append("\n");
                    else
                        outputdata.append(",");
                }
            }
            outputStream.write(outputdata.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, ""+floder, Toast.LENGTH_LONG).show();
            //Log.e("123","123");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
