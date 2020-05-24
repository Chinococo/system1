package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class enter_score_screen extends AppCompatActivity {
    Button full;
    HashMap<String, ArrayList<String>> importantdata;
    List<String> item = new ArrayList<>();
    TextView grade1, grade2, grade3, grade4, grade5, out_csv, now_score;
    int pos = 0;
    List<String> op = new ArrayList<>();//老師可選任意號碼spinner的資料放置區
    Spinner opeator;
    List<String> test = new ArrayList<>();
    score_struct[] score_s1 = new score_struct[20];
    EditText enter1, enter2, enter3, enter4, enter5, enter6;
    Button enter, date_pick;
    Calendar calendar = Calendar.getInstance();
    String today, account_name, no;//今天日期//登入時的帳號名//登入者的編號
    Intent intent;//拿過去資料的元件
    ArrayList<String> get = new ArrayList<>();
    Spinner choose;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Map<String, List> c = new HashMap<>();
    List<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_score_screen);
        intent = getIntent();//拿取登入時的帳號跟編號
        setup();//設定id和基本設定
        readDataOnInternet();
        //clear_Alldata();//清除所有資料
        event();//所有事件
    }

    private void do2() {
        get.clear();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, get);
        choose.setAdapter(adapter);
    }//沒啥意義

    private void getdata(String no) {
        Log.e("no", "" + no);
        if (!no.equals("30")) {
            get = importantdata.get(no);
            do1();
        }
    }//拿班級資料

    void clear_Alldata() {
        enter1.setText("");
        enter2.setText("");
        enter3.setText("");
        enter4.setText("");
        enter5.setText("");
        enter6.setText("");
    }//清除目前在輸入格的數字

    void do1() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, get);//選擇班級的spinner的資料
        choose.setAdapter(adapter);//設定
        Log.d("re", String.valueOf(get.size()));
        for (int i = 0; i < get.size(); i++) {
            Log.d("re", get.get(i));
            score_s1[i].setName(get.get(i));
            if (score_s1[i].getScore().size() <= 5)
                for (int x = 0; x <= 5; x++) {
                    score_s1[i].score.add(0.0);
                }
            Log.d("test1", String.valueOf(score_s1[0].getScore().size()));

        }


    }//設定選擇班級的spinner

    private void upload(String no) {
        data.add("請選擇");
        data.add("建二甲");
        data.add("製圖二");
        data.add("室一甲");
        data.add("綜一丁");
        data.add("製圖一");
        data.add("子一乙");
        c.put("class", data);
        databaseReference.child("no").child(no).setValue(c);
    }//上傳程式

    void setup() {
        importdata();
        full = findViewById(R.id.auto);
        now_score = findViewById(R.id.now_score);
        grade1 = findViewById(R.id.grade1);
        grade2 = findViewById(R.id.grade2);
        grade3 = findViewById(R.id.grade3);
        grade4 = findViewById(R.id.grade4);
        grade5 = findViewById(R.id.grade5);
        out_csv = findViewById(R.id.out_csv_btn);//老師特別權限
        opeator = findViewById(R.id.opeator);//老師特別權限
        choose = findViewById(R.id.enter_score_spinner);
        enter = findViewById(R.id.enter_scorce_btn);
        enter1 = findViewById(R.id.enter_score_edit1);
        enter2 = findViewById(R.id.enter_score_edit2);
        enter3 = findViewById(R.id.enter_score_edit3);
        enter4 = findViewById(R.id.enter_score_edit4);
        enter5 = findViewById(R.id.enter_score_edit5);
        enter6 = findViewById(R.id.enter_score_edit6);
        date_pick = findViewById(R.id.date_picker_enter_score);
        no = intent.getStringExtra("no");//拿登入時的編號
        account_name = intent.getStringExtra("account");//拿登入時的帳號名
        getitem(no); //拿評分項目
        op.add("請選擇");//加入請選擇
        for (int j = 1; j <= 24; j++) {
            op.add(String.valueOf(j));//老師特別權限
            if (j == 24) {
                if (account_name.equals("teacher")) {
                    out_csv.setVisibility(View.VISIBLE);
                    opeator.setVisibility(View.VISIBLE);
                    date_pick.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> op_adpart = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, op);
                    opeator.setAdapter(op_adpart);
                }//老師特別權限加載區
            }
        }//老師特別權限
        setToday();
        nofition("登入成功");//登入成功顯示提示
        //nofition(no);//測試
        for (int i = 0; i < 20; i++)//宣告記憶體
            score_s1[i] = new score_struct();
    }//初始化變數

    private void getitem(String no) {
        String select;
        if (Integer.parseInt(no) >= 1 && Integer.parseInt(no) <= 12)
            select = "1~12";
        else if (Integer.parseInt(no) >= 13 && Integer.parseInt(no) <= 17)
            select = "13~17";
        else if (Integer.parseInt(no) >= 18 && Integer.parseInt(no) <= 24)
            select = "18~24";
        else
            select = "opeator";
        if (!select.equals("opeator")) {
            grade1.setText(importantdata.get(select).get(0));
            grade2.setText(importantdata.get(select).get(1));
            grade3.setText(importantdata.get(select).get(2));
            grade4.setText(importantdata.get(select).get(3));
            grade5.setText(importantdata.get(select).get(4));
        }


    }//老師特別指令

    void nofition(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }//泡泡訊息

    private void getnowdata(String no) {
        databaseReference.child("no").child(no).child(today).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    for (int k = 0; k < get.size(); k++)
                        for (int x = 0; x <= 5; x++)
                            score_s1[k].score.set(x, 0.0);
                } //如果沒有之前的資料的情況
                else {
                    for (int i = 0; i < get.size(); i++) {
                        Log.d("fewf", String.valueOf(i));
                        score_s1[i].SETSCORE((ArrayList<Object>) dataSnapshot.child(get.get(i)).getValue());
                        List<Object> temp = score_s1[pos].getScore();
                        enter1.setText(String.valueOf(temp.get(0)));
                        enter2.setText(String.valueOf(temp.get(1)));
                        enter3.setText(String.valueOf(temp.get(2)));
                        enter4.setText(String.valueOf(temp.get(3)));
                        enter5.setText(String.valueOf(temp.get(4)));
                        enter6.setText(String.valueOf(temp.get(5)));
                        setNow_score();
                    }

                }//還原網路上的設定


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//拿是否有資料

    private void event() {
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoinput();
            }
        });
        out_csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(enter_score_screen.this, ouput.class);
                startActivity(intent);
            }
        });//老師全線輸出檔案按鈕的事件
        choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //nofition(Integer.toString(position));
                pos = position;
                //Log.d("ERROR",String.valueOf(pos));
                if (position >= 0) {
                    List<Object> temp = score_s1[pos].getScore();
                    enter1.setText(String.valueOf(temp.get(0)));
                    enter2.setText(String.valueOf(temp.get(1)));
                    enter3.setText(String.valueOf(temp.get(2)));
                    enter4.setText(String.valueOf(temp.get(3)));
                    enter5.setText(String.valueOf(temp.get(4)));
                    enter6.setText(String.valueOf(temp.get(5)));
                    setNow_score();
                } else
                    clear_Alldata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//當選擇器更新，更新對的檔案
        enter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!enter1.getText().toString().equals("")) {
                    score_s1[pos].setScore(0, enter1.getText().toString());
                    if (enter1.getText().toString().equals("0.0") || enter1.getText().toString().equals("0"))
                        enter1.setText("");
                    setNow_score();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });//每個按鈕上面的字更新時，更新本地資料
        enter2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!enter2.getText().toString().equals("")) {
                    score_s1[pos].setScore(1, enter2.getText().toString());
                    if (enter2.getText().toString().equals("0.0") || enter2.getText().toString().equals("0"))
                        enter2.setText("");
                    setNow_score();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });//每個按鈕上面的字更新時，更新本地資料
        enter3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!enter3.getText().toString().equals("")) {
                    score_s1[pos].setScore(2, enter3.getText().toString());
                    if (enter3.getText().toString().equals("0.0") || enter3.getText().toString().equals("0"))
                        enter3.setText("");
                    setNow_score();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });//每個按鈕上面的字更新時，更新本地資料
        enter4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!enter4.getText().toString().equals("")) {
                    score_s1[pos].setScore(3, enter4.getText().toString());
                    if (enter4.getText().toString().equals("0.0") || enter4.getText().toString().equals("0"))
                        enter4.setText("");
                    setNow_score();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });//每個按鈕上面的字更新時，更新本地資料
        enter5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!enter5.getText().toString().equals("")) {
                    score_s1[pos].setScore(4, enter5.getText().toString());
                    if (enter5.getText().toString().equals("0.0") || enter5.getText().toString().equals("0"))
                        enter5.setText("");
                    setNow_score();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });//每個按鈕上面的字更新時，更新本地資料
        enter6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!enter6.getText().toString().equals("")) {
                    score_s1[pos].setScore(5, enter6.getText().toString());
                    if (enter6.getText().toString().equals("0.0") || enter6.getText().toString().equals("0"))
                        enter6.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });//每個按鈕上面的字更新時，更新本地資料
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //today = "20200208";
                Log.d("test1", String.valueOf(score_s1[0].getScore().size()));
                for (int i = 0; i < get.size(); i++)
                    databaseReference.child("no").child(no).child(today).child(score_s1[i].getName()).setValue(score_s1[i].getScore());
                nofition("上傳完成");
            }
        });//上傳指令
        opeator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < 20; i++)//宣告記憶體
                    score_s1[i] = new score_struct();
                clear_Alldata();
                for (int i = 0; i < 20; i++)//宣告記憶體
                    score_s1[i] = new score_struct();
                no = op.get(position);
                if (!no.equals("請選擇")) {
                    clear_Alldata();//清除顯示框
                    readDataOnInternet();
                    getitem(no);
                    getnowdata(no);
                    pos = 0;

                } else {
                    do2();//我不知道為什麼我加這東西
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1(v);
            }
        });
    }//所有事件

    void setToday() {
        today = Integer.toString(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) + 1 < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }//設定當天日期

    void readDataOnInternet() {
        getdata(no);
        getnowdata(no);
    }//拿網路資料

    void setNow_score() {
        //Log.e("enter1",enter1.getText().toString());
        double now = 0;
        if (!enter1.getText().toString().equals(""))
            now += Double.parseDouble(enter1.getText().toString());
        if (!enter2.getText().toString().equals(""))
            now += Double.parseDouble(enter2.getText().toString());
        if (!enter3.getText().toString().equals(""))
            now += Double.parseDouble(enter3.getText().toString());
        if (!enter4.getText().toString().equals(""))
            now += Double.parseDouble(enter4.getText().toString());
        if (!enter5.getText().toString().equals(""))
            now += Double.parseDouble(enter5.getText().toString());
        now_score.setText("目前總分:" + String.valueOf(now));
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
                getdata(no);
                getnowdata(no);
            }

        }, year, month, day).show();
    }//日期選擇器１

    void importdata() {
        importantdata = new HashMap<>();
        File dir = Environment.getExternalStorageDirectory();
        File csv = new File(dir, "important_data.csv");
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
                importantdata.put(temp[0].replaceAll("\\s+", ""), t);
                //data.append(line);
            }
        } catch (Exception e) {
            ;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {

            }
        }
        for (String t : importantdata.keySet())
            Log.e(t, t);

    }

    void autoinput() {

        Log.e("no", no);
        if (Integer.parseInt(no) != 30) {
            Log.e("size", importantdata.get(no).size() + "");
            for (int i = 0; i < importantdata.get(no).size(); i++) {
                ArrayList<Object> t = new ArrayList<>();
                Log.e(i + "", i + "");
                Log.e("123", importantdata.get("maxscore-" + importantdata.get(no).get(i) + "-" + no + "") + "1");
                for (int k = 0; k < importantdata.get("maxscore-" + importantdata.get(no).get(i) + "-" + no + "").size(); k++)
                    t.add(importantdata.get("maxscore-" + importantdata.get(no).get(i) + "-" + no + "").get(k));
                t.add("無");
                score_s1[i].SETSCORE(t);
            }
            List<Object> temp = score_s1[pos].getScore();
            enter1.setText(String.valueOf(temp.get(0)));
            enter2.setText(String.valueOf(temp.get(1)));
            enter3.setText(String.valueOf(temp.get(2)));
            enter4.setText(String.valueOf(temp.get(3)));
            enter5.setText(String.valueOf(temp.get(4)));
            enter6.setText(String.valueOf(temp.get(5)));
            setNow_score();


        }

    }
}