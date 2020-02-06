package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ouput extends AppCompatActivity {
    DatabaseReference DB = FirebaseDatabase.getInstance().getReference();
    Button test1, test2, test3;
    TextView preview1, preview2;
    String dateTime;
    Date date1 = new Date();
    Date date2 = new Date();
    ArrayList<score_struct> output = new ArrayList<>();
    score_struct put = new score_struct();
    int max = 1, now = 0;
    String message = "Loging.";
    //String no="1",date="20191225";
    HashMap<String, HashMap<String, ArrayList<String>>> out = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouput);


        setid();


        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1(v);
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker2(v);
            }
        });
        test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                now = 0;
                max = 10000;
                Thread doing = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = now; i < max; i++)
                            test("1", "20191225");
                    }
                });
                porcess(doing);
                /*
                delay delay =new delay(3000);
                delay.start();
                try {
                  delay.join();
                }catch (Exception e)
                {

                }*/





                /*
                if (!preview1.getText().toString().equals("") && !preview2.getText().toString().equals("")) {
                    long first = Math.min(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                    long end = Math.max(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                    for (long k = first; k <= end; k++) {
                        Date temp = new Date(k * 60 * 60 * 24 * 1000);
                        String output = ("" + (temp.getYear() + 1900));
                        output += (temp.getMonth() + 1) < 11 ? "0" + (temp.getMonth() + 1) : "" + (temp.getMonth() + 1);
                        output += (temp.getDate()) < 11 ? "0" + temp.getDate() : "" + temp.getDate();
                        Log.e("123", output);
                    }

                } else
                    Log.e("你沒輸入", "input is empty");
                */
            }
        });

    }

    void setid() {
        test1 = findViewById(R.id.datepicker1);
        test2 = findViewById(R.id.datepicker2);
        test3 = findViewById(R.id.test3);
        preview1 = findViewById(R.id.preview1);
        preview2 = findViewById(R.id.preview2);
    }

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
    }

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
    }

    void test(final String no, final String date) {
        final CountDownLatch count = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DB.child(no).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HashMap<String, ArrayList<String>> temp = new HashMap<>();
                            temp.put(date, (ArrayList<String>) snapshot.getValue());
                            out.put(snapshot.getKey(), temp);
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

    }

    void porcess(final Thread doing) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        final Handler handler = new Handler();
        progressDialog.setMax(max);
        progressDialog.setMessage(message);
        progressDialog.setTitle("正在抓資料");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(now);
        progressDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doing.start();
                while (true) {
                    if (message.equals("Loging.")) {
                        progressDialog.setMessage("Loging..");
                        message = "Loging..";
                    } else if (message.equals("Loging..")) {
                        progressDialog.setMessage("Loging...");
                        message = "Loging...";
                    } else {
                        progressDialog.setMessage("Loging...");
                        message = "Loging.";
                    }
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
                                progressDialog.dismiss();
                            }
                        });
                        break;
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
    }

}
