package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ouput extends AppCompatActivity {
    int no;
    StringBuilder sb=new StringBuilder();
    String output;
    DatabaseReference DB = FirebaseDatabase.getInstance().getReference();//網路資料庫
    Button test1, test2, test3;
    HashMap<String, ArrayList<String>> importantdata;
    TextView preview1, preview2;
    String dateTime;//輸出時間的暫存
    Date date1 = new Date();//第一個日期的載入地
    Date date2 = new Date();//第二個日期的載入地
    ArrayList<String> all_class;
    HashMap<Integer,HashMap<String,ArrayList<String>>> alldata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouput);
        importdata();
        requestpermission();//索取權限
        setid();//設定id
        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1(v);
            }
        });//第一顆按鈕被按的事件
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker2(v);
            }
        });//第二顆按鈕被按的事件
        test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alldata=new HashMap<>();

                  for(no=1;no<=24;no++)
                  {

                      DB.child("no").child(no+"").child(output).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if(dataSnapshot.getValue()!=null)
                              {
                                  alldata.put(no,(HashMap<String, ArrayList<String>>) dataSnapshot.getValue());
                              }else
                                  alldata.put(no,new HashMap<String, ArrayList<String>>());
                              if(no==24)
                              {
                                  delay delay = new delay(500);
                                  delay.start();
                                  new Thread(new Runnable() {
                                      @Override
                                      public void run() {
                                          try {
                                              delay.join();
                                          } catch (InterruptedException e) {
                                              e.printStackTrace();
                                          }

                                      }
                                  }).start();
                              }


                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });
                  }
            }


        });

    }

    void setid() {
        test1 = findViewById(R.id.datepicker1);
        test2 = findViewById(R.id.datepicker2);
        test3 = findViewById(R.id.test3);
        preview1 = findViewById(R.id.preview1);
        preview2 = findViewById(R.id.preview2);
    }//設定id

    public void datePicker1(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateTime = year + "-" + (month + 1) + "-" + day;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date1 = simpleDateFormat.parse(dateTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("datepicker1-", date1.toString() + "-" + date1.getTime() / (60 * 60 * 24 * 1000));
                preview1.setText(date1.toString());
            }

        }, year, month, day).show();
    }//日期選擇器１

    public void datePicker2(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateTime = year + "-" + (month + 1) + "-" + day;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date2 = simpleDateFormat.parse(dateTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("datepicker2-", date2.toString() + "-" + date2.getTime() / (60 * 60 * 24 * 1000));
                preview2.setText(date2.toString());
            }

        }, year, month, day).show();
    }//日期選擇器２

    void out()
    {
        sb=new StringBuilder();
        for(long day=Math.min(date1.getTime()/(60*60*24*1000),date2.getTime()/(60*60*24*1000)); day<=Math.max(date1.getTime()/(60*60*24*1000),date2.getTime()/(60*60*24*1000));day++) {
            Date temp = new Date((day + 1) * (60 * 60 * 24 * 1000));
            Log.e("day", temp.toString());
            output = ("" + (temp.getYear() + 1900));
            output += (temp.getMonth() + 1) < 10 ? "0" + (temp.getMonth() + 1) : "" + (temp.getMonth() + 1);
            output += (temp.getDate()) < 10 ? "0" + temp.getDate() : "" + temp.getDate();
            sb.append(output+",");
        }
        for(int i=0;i<all_class.size();i++)
        {
            for(long day=Math.min(date1.getTime()/(60*60*24*1000),date2.getTime()/(60*60*24*1000)); day<=Math.max(date1.getTime()/(60*60*24*1000),date2.getTime()/(60*60*24*1000));day++) {
                Date temp = new Date((day + 1) * (60 * 60 * 24 * 1000));
                Log.e("day", temp.toString());
                output = ("" + (temp.getYear() + 1900));
                output += (temp.getMonth() + 1) < 10 ? "0" + (temp.getMonth() + 1) : "" + (temp.getMonth() + 1);
                output += (temp.getDate()) < 10 ? "0" + temp.getDate() : "" + temp.getDate();

            }
        }

    }


    int requestpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }//索取權限

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
                //data.append(line);
            }
            for(int i=1;i<=24;i++)
            {
                ArrayList<String> t = importantdata.get(i+"");
                for(int k=0;k<t.size();k++)
                    all_class.add(t.get(k)+i);
            }
        } catch (Exception e) {
            ;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                ;
            }
        }
    }
}

