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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
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
    score_struct put = new score_struct();
    int max = 0, now = 0;
    String message = "Loging.";
    //String no="1",date="20191225";
    HashMap<String, HashMap<String, ArrayList<String>>> out = new HashMap<>();
    ArrayList<String> all_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouput);
        requestpermission();

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
                            long first = Math.min(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                            long end = Math.max(date1.getTime() / (60 * 60 * 24 * 1000), date2.getTime() / (60 * 60 * 24 * 1000));
                            all_date = new ArrayList<>();
                            for (long k = first; k <= end; k++) {
                                Date temp = new Date((k + 1) * 60 * 60 * 24 * 1000);
                                String output = ("" + (temp.getYear() + 1900));
                                output += (temp.getMonth() + 1) < 11 ? "0" + (temp.getMonth() + 1) : "" + (temp.getMonth() + 1);
                                output += (temp.getDate()) < 11 ? "0" + temp.getDate() : "" + temp.getDate();
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
                final Thread output_csv = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        outfile("test");
                    }
                });
                porcess(doing, "get data", output_csv);


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
                DB.child("no").child(no).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot.child("class").getValue();
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

    }

    void porcess(final Thread doing, final String title, final Thread last) {
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
                    last.start();

                }
            });
            thread.setPriority(10);
            thread.start();
        }

    }

    void outfile(String name) {
        try {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "test_csv");
            if (!directory.exists())
                directory.mkdirs();
            File file = new File(directory, "test.csv");
            if (!file.exists())
                file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            sb.append(",");
            for (int i = 0; i < all_date.size(); i++) {
                if (i != all_date.size() - 1) {
                    sb.append(all_date.get(i) + ",");
                } else {
                    sb.append(all_date.get(i));
                }
            }
            sb.append("\n");
            for (String key : out.keySet()) {
                sb.append(key + ",");
                for (int i = 0; i < all_date.size(); i++) {
                    Log.e("123", all_date.get(i));
                    Double sum = 0.0;
                    ArrayList<String> t = out.get(key).get(all_date.get(i));
                    if (t != null) {
                        Log.e("error", key + "-" + all_date.get(i));
                        for (int k = 0; k < t.size() - 1; k++)
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
                }
                sb.append("\n");

            }
            os.write(sb.toString().getBytes());
            os.flush();
            os.close();
            Log.e("successful", "successful");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    int requestpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
