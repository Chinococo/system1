package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class classroom extends AppCompatActivity {
    long first = 0;//退出指令所需物件
    ArrayList<Object> t2 = new ArrayList<>();
    Intent intent;
    Double t1;
    String no = "28";
    TextView nowscore, out_csv;
    String today;
    Button date_pick;
    Calendar calendar = Calendar.getInstance();
    HashMap<String, ArrayList<String>> importantdata;
    Spinner spinner_position, spinner_class;
    EditText grade1, grade2, grade3, grade4, grade5, grade6, grade7;
    Button full, next, prev;
    ;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Set<String> check = new HashSet<>();
    ArrayList<String> pos = new ArrayList<>();
    ArrayList<String> _class = new ArrayList<>();
    HashMap<String, ArrayList<String>> all_class_data = new HashMap<>();
    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        setup();
        new Thread(new Runnable() {
            @Override
            public void run() {
                delay delay = new delay(500);
                delay.start();
                try {
                    delay.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                importdata();
            }
        }).start();
        event();

    }

    void setup() {
        setToday();
        next = findViewById(R.id.next_class);
        prev = findViewById(R.id.prev_class);
        intent = getIntent();
        no = intent.getStringExtra("no");
        check_state();
        spinner_position = findViewById(R.id.position_spinner);
        spinner_class = findViewById(R.id.class_spinner);
        date_pick = findViewById(R.id.date_picker_enter_score_class);
        out_csv = findViewById(R.id.out_csv_btn2_class);
        grade1 = findViewById(R.id.floor);
        grade2 = findViewById(R.id.window);
        grade3 = findViewById(R.id.garbage);
        grade4 = findViewById(R.id.desk_chair);
        grade5 = findViewById(R.id.blackboard);
        grade6 = findViewById(R.id.other);
        grade7 = findViewById(R.id.grade7);
        full = findViewById(R.id.auto_class);
        enter = findViewById(R.id.enter_scorce_btn_class2);
        nowscore = findViewById(R.id.now_score_class);
        if (no.equals("30")) {
            //out_csv.setVisibility(View.VISIBLE);
            date_pick.setVisibility(View.VISIBLE);
        }
    }

    void clear() {
        grade1.setText("");
        grade2.setText("");
        grade3.setText("");
        grade4.setText("");
        grade5.setText("");
        grade6.setText("");
        grade7.setText("");

    }

    void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_position.getSelectedItem() != null) {
                    if (!spinner_position.getSelectedItem().toString().equals("請選擇")) {
                        if (spinner_class.getSelectedItemPosition() < spinner_class.getCount() - 1)
                            spinner_class.setSelection(spinner_class.getSelectedItemPosition() + 1);
                    }
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_position.getSelectedItem() != null) {
                    if (!spinner_position.getSelectedItem().toString().equals("請選擇")) {
                        if (spinner_class.getSelectedItemPosition() > 0)
                            spinner_class.setSelection(spinner_class.getSelectedItemPosition() - 1);
                    }
                }
            }
        });
        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1(v);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade1.setText("2");
                grade2.setText("2");
                grade3.setText("2");
                grade4.setText("2");
                grade5.setText("2");
                grade6.setText("0");
                grade7.setText("");
            }
        });
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getnowdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    set_class_spinner();
                else
                    do2();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        grade1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇")) {
                            if (!grade1.getText().toString().equals(""))
                                if (Double.parseDouble(grade1.getText().toString()) > 2)
                                    grade1.setText("2");
                            upload();
                        }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        grade2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇")) {
                            if (!grade2.getText().toString().equals(""))
                                if (Double.parseDouble(grade2.getText().toString()) > 2)
                                    grade2.setText("2");
                            upload();
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        grade3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇")) {
                            if (!grade3.getText().toString().equals(""))
                                if (Double.parseDouble(grade3.getText().toString()) > 2)
                                    grade3.setText("2");
                            upload();
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        grade4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇")) {
                            if (!grade4.getText().toString().equals(""))
                                if (Double.parseDouble(grade4.getText().toString()) > 2)
                                    grade4.setText("2");
                            upload();
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        grade5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇")) {
                            if (!grade5.getText().toString().equals(""))
                                if (Double.parseDouble(grade5.getText().toString()) > 2)
                                    grade5.setText("2");
                            upload();
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        grade6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇")) {
                            if (!grade6.getText().toString().equals(""))
                                if (Double.parseDouble(grade6.getText().toString()) > 0)
                                    grade6.setText("0");
                            upload();
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        grade7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinner_position.getSelectedItem().toString().equals("請選擇"))
                    if (spinner_class.getSelectedItem() != null)
                        if (!spinner_class.getSelectedItem().toString().equals("請選擇"))
                            upload();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getnowdata() {
        if (!spinner_class.getSelectedItem().toString().equals("請選擇"))
            databaseReference
                    .child("class")
                    .child(today)
                    .child(spinner_class.getSelectedItem().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                t2 = (ArrayList<Object>) dataSnapshot.getValue();
                                Log.e("123", t2.toString());
                                grade1.setText(t2.get(0).toString());
                                grade2.setText(t2.get(1).toString());
                                grade3.setText(t2.get(2).toString());
                                grade4.setText(t2.get(3).toString());
                                grade5.setText(t2.get(4).toString());
                                grade6.setText(t2.get(5).toString());
                                grade7.setText(t2.get(6).toString());
                            } else

                                clear();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        else
            clear();
    }

    void set_class_spinner() {
        spinner_class.setAdapter(new ArrayAdapter<String>(
                this
                , android.R.layout.simple_spinner_item
                , all_class_data.get(spinner_position.getSelectedItem())));
        clear();
    }

    void upload() {
        t1 = 75.0;
        if (!grade1.getText().toString().equals(""))
            t1 += Double.parseDouble(grade1.getText().toString());
        if (!grade2.getText().toString().equals(""))
            t1 += Double.parseDouble(grade2.getText().toString());
        if (!grade3.getText().toString().equals(""))
            t1 += Double.parseDouble(grade3.getText().toString());
        if (!grade4.getText().toString().equals(""))
            t1 += Double.parseDouble(grade4.getText().toString());
        if (!grade5.getText().toString().equals(""))
            t1 += Double.parseDouble(grade5.getText().toString());
        if (!grade6.getText().toString().equals(""))
            t1 += Double.parseDouble(grade6.getText().toString());
        if (!grade7.getText().toString().equals(""))
            t1 += Double.parseDouble(grade7.getText().toString());
        nowscore.setText("" + t1);
        ArrayList<Object> t = new ArrayList<>();
        t.add(grade1.getText().toString());
        t.add(grade2.getText().toString());
        t.add(grade3.getText().toString());
        t.add(grade4.getText().toString());
        t.add(grade5.getText().toString());
        t.add(grade6.getText().toString());
        t.add(grade7.getText().toString());
        t.add("no=" + no);
        if (spinner_class.getSelectedItem() != null)
            if (!spinner_class.getSelectedItem().toString().equals("請選擇"))
                databaseReference
                        .child("class")
                        .child(today)
                        .child(spinner_class.getSelectedItem().toString())
                        .setValue(t);
    }

    void importdata() {
        importantdata = new HashMap<>();
        File directory123 = new File(Environment.getExternalStorageDirectory() + File.separator + "衛生評分系統資料夾");
        File dir = Environment.getExternalStorageDirectory();
        File csv = new File(directory123, "important_data.csv");
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(csv), "utf-8"));
            String line;
            //check=new HashSet<>();
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split(",");
                if (temp.length == 2 && temp[0].length() == 5) {
                    if (all_class_data.get(temp[1]) != null)
                        all_class_data.get(temp[1]).add(temp[0]);
                    else {
                        all_class_data.put(temp[1], new ArrayList<>());
                        all_class_data.get(temp[1]).add(temp[0]);
                    }
                }
            }
            pos = new ArrayList<>();
            for (String t : all_class_data.keySet())
                pos.add(t);
            Collections.sort(pos);
            //String temp = pos.get(5);
            //pos.set(5,pos.get(6));
            //pos.set(6,temp);
            pos.add(0, "請選擇");
            for (String key : all_class_data.keySet()) {
                all_class_data.get(key).add(0, "請選擇");
            }
            ArrayAdapter<String> t = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pos);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinner_position.setAdapter(t);
                }
            });

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

    boolean check_newwork() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service
        NetworkInfo networInfo = conManager.getActiveNetworkInfo();       //在取得相關資訊
        if (networInfo == null || !networInfo.isAvailable()) { //判斷是否有網路
            Looper.prepare();
            new AlertDialog.Builder(this)
                    .setMessage("沒有網路無法做動_若不開則會不停顯示")
                    .show();
            Looper.loop();
            return false;
        } else {
            return true;
        }
    }

    void check_state() {
        delay delay = new delay(60 * 1000);
        delay.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                check_newwork();
                try {
                    delay.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                check_state();
            }
        }).start();
    }

    void setToday() {
        today = Integer.toString(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) + 1 < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void datePicker1(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                today = ("" + year);
                today += month < 10 ? "0" + (month + 1) : "" + (month + 1);
                today += (day) < 10 ? "0" + day : "" + day;
                Log.e("123", today);
                set_class_spinner();
            }

        }, year, month, day).show();
    }//日期選擇器１

    void do2() {
        spinner_class.setAdapter(new ArrayAdapter<String>(
                this
                , android.R.layout.simple_spinner_item
                , new ArrayList<>()));
        clear();
    }

    void nofition(String data)//顯示泡泡資訊
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()  //退出事件
    {
        if (System.currentTimeMillis() - first < 2000) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            nofition("再按一次退出");
            first = System.currentTimeMillis();
        }
    }
}
