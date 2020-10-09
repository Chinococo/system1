package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
    long first = 0;//退出指令所需物件
    Switch aSwitch;
    boolean mode = false;
    HashMap<String, ArrayList<String>> importantdata;
    DatabaseReference DB = FirebaseDatabase.getInstance().getReference();//網路資料庫
    Button test1, test2, test3;
    TextView preview1, preview2;
    String dateTime;//輸出時間的暫存
    Date date1 = new Date();//第一個日期的載入地
    Date date2 = new Date();//第二個日期的載入地
    int max = 0, now = 0;//進度調的啟始值和目標值
    String message = "Loging.";//進度調的訊息
    HashMap<String, HashMap<String, ArrayList<String>>> out = new HashMap<>();//輸出的所有資料
    ArrayList<String> all_date;//拿所有的日期
    StringBuilder sb = new StringBuilder();//輸出資料的佔存器
    FileOutputStream os;//輸出的匯流
    ArrayList<String> all_class = new ArrayList<>();//所有班級的陣列
    int s = 0;//紀錄目前狀態，0=拿資料，1=繪出資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouput);
        requestpermission();//索取權限
        importdata();
        setid();//設定id
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode = isChecked;
            }
        });
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
                if (mode)
                    class_score();
                else
                    outside();
            }
        });

    }

    void setid() {
        test1 = findViewById(R.id.datepicker1);
        test2 = findViewById(R.id.datepicker2);
        test3 = findViewById(R.id.test3);
        preview1 = findViewById(R.id.preview1);
        preview2 = findViewById(R.id.preview2);
        aSwitch = findViewById(R.id.switch1);
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

    void test(final String no, final String date) {
        final CountDownLatch count = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DB.child("no").child(no).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> arrayList = importantdata.get(no);
                        if (all_date.get(0).equals(date))
                            for (int i = 0; i < arrayList.size(); i++) {
                                if (!out.containsKey(arrayList.get(i)))
                                    out.put(arrayList.get(i) + "-" + no, new HashMap<String, ArrayList<String>>());
                            }
                        if (dataSnapshot.child(date).getValue() != null) {
                            Log.e("n", "有資料");
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.child(date).getChildren()) {
                                //Log.e("12", "13");
                                ArrayList<String> t = (ArrayList<String>) dataSnapshot1.getValue();
                                Log.e("t", dataSnapshot1.getKey() + "-" + no);
                                out.get(dataSnapshot1.getKey() + "-" + no).put(date, t);
                            }
                        }
                        now++;
                        count.countDown();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                try {
                    count.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        thread.setPriority(5);
        thread.start();
        Log.e("123", "finish-thread" + thread.getId());

    }//拿網路資料副程式

    void test2(final String class_____, final String date) {
        final CountDownLatch count = new CountDownLatch(1);
        Log.e("h", "h");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DB.child("class").child(date).child(class_____).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e("data", date + class_____);
                        if (!out.containsKey(class_____))
                            out.put(class_____, new HashMap<>());
                        if (dataSnapshot.getValue() != null) {

                            out.get(class_____).put(date, (ArrayList<String>) dataSnapshot.getValue());
                        }

                        count.countDown();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                try {
                    count.await();
                    now++;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        thread.setPriority(5);
        thread.start();
        Log.e("123", "finish-thread" + thread.getId());

    }//拿網路資料副程式

    void porcess(final Thread doing, final String title) {
        if (max != 0) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            final Handler handler = new Handler();
            progressDialog.setMax(max);
            progressDialog.setMessage(message);
            progressDialog.setTitle(title);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(now);
            progressDialog.show();
            s = 0;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doing.start();
                    while (true) {
                        if (progressDialog.getProgress() < progressDialog.getMax()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.setProgress(now);
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("s", "" + s);
                                    if (s == 0) {
                                        now = 0;
                                        max = all_date.size() + (out.keySet().size() * all_date.size());
                                        progressDialog.setTitle("create_csv");
                                        progressDialog.setProgress(now);
                                        s = 1;
                                        outfile("outside");
                                    } else {
                                        progressDialog.dismiss();

                                    }


                                }
                            });
                            if (s == 1) {

                                //nofition("fininsh");
                                Log.e("fin-2", "fin-2");
                                break;
                            }


                        }

                        delay delay = new delay(30);
                        delay.start();
                        try {
                            delay.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
            thread.setPriority(10);
            thread.start();

            Log.e("fin-1", "fin-1");

        }

    }//進度調

    void porcess1(final Thread doing, final String title) {
        if (max != 0) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            final Handler handler = new Handler();
            progressDialog.setMax(max);
            progressDialog.setMessage(message);
            progressDialog.setTitle(title);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(now);
            progressDialog.show();
            s = 0;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doing.start();
                    while (true) {
                        if (progressDialog.getProgress() < progressDialog.getMax()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.setProgress(now);
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("s", "" + s);
                                    if (s == 0) {
                                        now = 0;
                                        max = all_date.size() + (78 * all_date.size());
                                        progressDialog.setTitle("create_csv");
                                        progressDialog.setProgress(now);
                                        s = 1;
                                        outfile1("class");
                                    } else {
                                        progressDialog.dismiss();

                                    }


                                }
                            });
                            if (s == 1) {

                                //nofition("fininsh");
                                Log.e("fin-2", "fin-2");
                                break;
                            }


                        }

                        delay delay = new delay(30);
                        delay.start();
                        try {
                            delay.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
            thread.setPriority(10);
            thread.start();

            Log.e("fin-1", "fin-1");

        }

    }//進度調

    void outfile(String name) {
        try {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "衛生評分系統資料夾");
            if (!directory.exists())
                directory.mkdirs();
            File file = new File(directory, name + ".csv");
            if (!file.exists())
                file.createNewFile();
            os = new FileOutputStream(file);
            sb = new StringBuilder();
            sb.append(",");
            //以上為目錄創建

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("hello", "" + max);
                            for (int i = 0; i < all_date.size(); i++) {
                                if (i != all_date.size() - 1) {
                                    sb.append(all_date.get(i) + ",");
                                } else {
                                    sb.append(all_date.get(i));
                                }
                                now++;
                            }
                            sb.append("\n");
                            for (int cl = 0; cl < all_class.size(); cl++) {
                                sb.append(all_class.get(cl).substring(0, 3) + ",");
                                for (int i = 0; i < all_date.size(); i++) {
                                    Log.e("123", all_date.get(i));
                                    Double sum = 0.0;
                                    //Log.e("123", out.toString());
                                    ArrayList<String> t = out.get(all_class.get(cl)).get(all_date.get(i));
                                    if (t != null) {
                                        Log.e("error", all_class.get(cl) + "-" + all_date.get(i));
                                        for (int k = 0; k < t.size() - 1; k++)
                                            if (!String.valueOf(t.get(k)).equals(""))
                                                sum += Double.parseDouble(String.valueOf(t.get(k)));
                                        Log.e("out", "" + sum);
                                    }


                                    //Log.e("1",""+t)  ;


                                    //sum+=Integer.valueOf(out.get(key).get(all_date.get(i)).get(count));
                                    if (i != all_date.size() - 1) {
                                        sb.append(sum + ",");
                                    } else {
                                        sb.append(sum);
                                    }
                                    now++;
                                }
                                sb.append("\n");

                            }
                            try {
                                os.write(sb.toString().getBytes());
                                os.flush();
                                os.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nofition("fin");
                                        all_class.clear();
                                        all_date.clear();
                                    }
                                });
                                //nofition("finish");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }
            });
            thread.setPriority(10);
            thread.start();


            Log.e("successful", "successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//輸出檔案

    void outfile1(String name) {
        try {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "衛生評分系統資料夾");
            if (!directory.exists())
                directory.mkdirs();
            File file = new File(directory, name + ".csv");
            if (!file.exists())
                file.createNewFile();
            os = new FileOutputStream(file);
            sb = new StringBuilder();
            sb.append(",");
            //以上為目錄創建

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("hello", "" + max);
                            for (int i = 0; i < all_date.size(); i++) {
                                if (i != all_date.size() - 1) {
                                    sb.append(all_date.get(i) + ",");
                                } else {
                                    sb.append(all_date.get(i));
                                }
                                now++;
                            }
                            sb.append("\n");
                            for (int cl = 0; cl < all_class.size(); cl++) {
                                sb.append(all_class.get(cl).substring(2, 5) + ",");
                                for (int i = 0; i < all_date.size(); i++) {
                                    Log.e("123", all_date.get(i));
                                    Double sum = 75.0;
                                    //Log.e("123", out.toString());

                                    ArrayList<String> t = new ArrayList<>();
                                    if (out.get(all_class.get(cl)).get(all_date.get(i)) != null)
                                        t = out.get(all_class.get(cl)).get(all_date.get(i));
                                    if (t != null) {
                                        Log.e("error", all_class.get(cl) + "-" + all_date.get(i));
                                        for (int k = 0; k < t.size() - 1; k++)
                                            if (!String.valueOf(t.get(k)).equals(""))
                                                sum += Double.parseDouble(String.valueOf(t.get(k)));
                                        Log.e("out", "" + sum);
                                    }


                                    //Log.e("1",""+t)  ;


                                    //sum+=Integer.valueOf(out.get(key).get(all_date.get(i)).get(count));
                                    if (i != all_date.size() - 1) {
                                        sb.append(sum + ",");
                                    } else {
                                        sb.append(sum);
                                    }
                                    now++;
                                }
                                sb.append("\n");

                            }
                            try {
                                os.write(sb.toString().getBytes());
                                os.flush();
                                os.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nofition("fin");
                                    }
                                });
                                //nofition("finish");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }
            });
            thread.setPriority(10);
            thread.start();


            Log.e("successful", "successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//輸出檔案

    int requestpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }//索取權限

    void getallclass() {
        all_class.clear();
        for (int i = 1; i <= 24; i++) {
            ArrayList<String> t = importantdata.get(i + "");
            for (int k = 0; k < t.size(); k++) {
                all_class.add(t.get(k) + "-" + i);
            }
        }
    }//拿所有班級３

    void importdata() {
        importantdata = new HashMap<>();
        File directory123 = new File(Environment.getExternalStorageDirectory() + File.separator + "衛生評分系統資料夾");
        File dir = Environment.getExternalStorageDirectory();
        File csv= new File(directory123,"important_data.csv");
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(csv), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split(",");
                ArrayList<String> t = new ArrayList<>();
                for (int i = 1; i < temp.length; i++)
                    t.add(temp[i]);
                importantdata.put(temp[0].replaceAll(" ", ""), t);
                //Log.e("test",temp[0].replaceAll(" ",""));
                //data.append(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                ;
            }
        }

    }

    void nofition(String data)//顯示泡泡資訊
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    void outside() {
        now = 0;
        if (!preview1.getText().toString().equals("") && !preview2.getText().toString().equals("")) {
            long first = Math.min(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
            long end = Math.max(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
            max = Integer.valueOf(24 * (1 + end - first) + "");
        } else
            max = 0;
        Thread doing = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!preview1.getText().toString().equals("") && !preview2.getText().toString().equals("")) {
                    getallclass();
                    long first = Math.min(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                    long end = Math.max(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                    all_date = new ArrayList<>();
                    for (long k = first; k <= end; k++) {
                        Date temp = new Date((k + 1) * 60 * 60 * 24 * 1000);
                        String output = ("" + (temp.getYear() + 1900));
                        output += (temp.getMonth() + 1) < 10 ? "0" + (temp.getMonth() + 1) : "" + (temp.getMonth() + 1);
                        output += (temp.getDate()) < 10 ? "0" + temp.getDate() : "" + temp.getDate();
                        all_date.add(output);
                        for (int index = 1; index <= 24; index++) {
                            test(index + "", output);
                        }
                        //Log.e("123", output);
                    }

                } else {
                    //Log.e("你沒輸入", "input is empty");
                }

            }
        });
        porcess(doing, "get data");//觸發進度調指令
    }

    void class_score() {
        now = 0;
        if (!preview1.getText().toString().equals("") && !preview2.getText().toString().equals("")) {
            long first = Math.min(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
            long end = Math.max(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
            max = Integer.valueOf(78 * (1 + end - first) + "");
        } else
            max = 0;
        Thread doing = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!preview1.getText().toString().equals("") && !preview2.getText().toString().equals("")) {
                    System.out.println(importantdata.get("all_class"));
                    for (int i = 0; i <= 8; i++)
                        all_class.add("0" + (i + 1) + importantdata.get("all_class").get(i));
                    for (int i = 9; i < importantdata.get("all_class").size(); i++)
                        all_class.add((i + 1) + importantdata.get("all_class").get(i));
                    long first = Math.min(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                    long end = Math.max(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                    all_date = new ArrayList<>();
                    for (long k = first; k <= end; k++) {
                        Date temp = new Date((k + 1) * 60 * 60 * 24 * 1000);
                        String output = ("" + (temp.getYear() + 1900));
                        output += (temp.getMonth() + 1) < 10 ? "0" + (temp.getMonth() + 1) : "" + (temp.getMonth() + 1);
                        output += (temp.getDate()) < 10 ? "0" + temp.getDate() : "" + temp.getDate();
                        all_date.add(output);
                        Log.e("size", all_class.size() + "");
                        for (int index = 0; index < all_class.size(); index++) {
                            test2(all_class.get(index), output);
                        }
                        //Log.e("123", output);
                    }

                } else {
                    //Log.e("你沒輸入", "input is empty");
                }

            }
        });
        porcess1(doing, "get data");//觸發進度調指令
    }

    @Override
    public void onBackPressed()  //退出事件
    {
        if (System.currentTimeMillis() - first < 2000) {
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        } else {
            nofition("再按一次退出");
            first = System.currentTimeMillis();
        }
    }
}
