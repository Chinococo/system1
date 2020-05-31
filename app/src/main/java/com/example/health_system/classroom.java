package com.example.health_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class classroom extends AppCompatActivity {
    HashMap<String, ArrayList<String>> importantdata;
    Set<String> all_place=new HashSet<>();
    HashMap<String, ArrayList<String>> place = new HashMap<>();
    ArrayList<String> position=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        new Thread(new Runnable() {
            @Override
            public void run() {
                delay delay = new delay(200);
                delay.start();
                try {
                    delay.join();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                importdata();
            }
        }).start();




    }
    void importdata() {
        importantdata = new HashMap<>();
        File dir = Environment.getExternalStorageDirectory();
        File csv= new File(dir,"important_data.csv");
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(csv), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] temp =line.split(",");
                ArrayList<String> t= new ArrayList<>();
                for(int i=1;i<temp.length;i++)
                    t.add(temp[i]);
                importantdata.put(temp[0],t);
                for(String key:importantdata.keySet())
                {
                    if(importantdata.get(key).size()==1)
                        all_place.add(importantdata.get(key).get(0));
                }
                for(String key:all_place)
                {
                    place.put(key,new ArrayList<>());
                    position.add(key);
                }

                for(String key:importantdata.keySet())
                {
                    if(importantdata.get(key).size()==1)
                        place.get(importantdata.get(key).get(0)).add(key);
                }

                for(String key:place.keySet())
                    for(int index=0;index<place.get(key).size();index++)
                        Log.e("key",key+"-"+place.get(key).get(index));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
