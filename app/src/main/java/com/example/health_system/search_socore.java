package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class search_socore extends AppCompatActivity {
    String erase_class="子三乙";
    int fun=0;
    long first = 0;//退出指令所需物件
    TextView total_score;
    HashMap<String, ArrayList<String>> importantdata = new HashMap<>();
    ArrayList<String> workplace = new ArrayList<>();
    int k;
    String item_get;
    HashMap<String, Integer> no = new HashMap<>();
    List<String> c = new ArrayList<>();
    List<String> temp = new ArrayList<>();
    List<Object> score_list = new ArrayList<>();
    List<String> item_list = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    AutoCompleteTextView ch;
    Button search;
    HashMap<String, ArrayList<Integer>> class_position = new HashMap<>();
    List<String> allclass = new ArrayList<>();
    String today;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ListView item_listview;
    Spinner choose;
    List<String> sp = new ArrayList<>();

    //String nowclass="no select";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_socore);
        setup();
        ch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sp.clear();
                score_list.clear();
                item_list.clear();
                do1();
                do5();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(ch.getText().toString().equals(erase_class))
                    fun++;
                if (!sp.get(position).equals("請選擇")) {
                    Log.e("fun",fun+"");
                    item_list.clear();
                    if(!ch.getText().toString().equals(erase_class)||(fun%10==0&&fun!=0))
                    {
                        if(ch.getText().toString().equals(erase_class))
                        {
                            nofition("好啦，讓你看一下");
                            delay delay = new delay(3000);
                            delay.start();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        delay.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    item_list.clear();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            do5();
                                            nofition("3秒行了吧");
                                        }
                                    });

                                }
                            }).start();
                        }

                        if(!choose.getSelectedItem().toString().equals("教室"))
                            databaseReference.child("no").child(no.get(sp.get(position)) + "").child(today).child(ch.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        double total = 0.0f;
                                        if (no.get(sp.get(position)) <= 12)
                                            workplace = importantdata.get("1~12");
                                        else if ((no.get(sp.get(position)) >= 18))
                                            workplace = importantdata.get("18~24");
                                        else
                                            workplace = importantdata.get("13~17");
                                        workplace.add("備註");
                                        ArrayList<String> score = (ArrayList<String>) dataSnapshot.getValue();
                                        for (int i = 0; i < 6; i++) {
                                            if (i != 5&&!score.get(i).equals(""))
                                                total += Double.parseDouble(score.get(i));
                                            item_list.add(workplace.get(i) + ":"+score.get(i));
                                        }
                                        total_score.setText("總分:" + total);
                                        do5();
                                    }else
                                    {
                                        do5();
                                        nofition("現在尚無資料");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        else
                            databaseReference.child("class").child(today).child(importantdata.get(ch.getText().toString()).get(0)+ch.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()!=null)
                                    {
                                        item_list =(List<String>) dataSnapshot.getValue();
                                        item_list.set(0,"地板:"+item_list.get(0));
                                        item_list.set(1,"窗戶:"+item_list.get(1));
                                        item_list.set(2,"垃圾:"+item_list.get(2));
                                        item_list.set(3,"桌椅:"+item_list.get(3));
                                        item_list.set(4,"黑板:"+item_list.get(4));
                                        item_list.set(5,"其他:"+item_list.get(5));
                                        item_list.set(6,"查堂成績:"+item_list.get(6));
                                        item_list.set(7,"評分編號:"+(Integer.parseInt(item_list.get(7).replaceAll("no=",""))-24));
                                        if(item_list.get(7).equals("30"))
                                            item_list.set(7,"評分編號:最高權限者");
                                        do5();
                                    }else
                                    {
                                        do5();
                                        nofition("現在尚無資料");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    }else
                        nofition("你輸入到禁忌班級，無法查詢");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.clear();
                score_list.clear();
                item_list.clear();
                do5();
                opendialogclander();


            }


        });
    }

    private void do5() {
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_custom, item_list);
        item_listview.setAdapter(adapter);
    }

    private void do4() {
        item_list.remove(0);
        item_list.add("            備註");
        for (int i = 0; i < item_list.size(); i++)
            item_list.set(i, item_list.get(i) + ":" + score_list.get(i));
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_custom, item_list);
        item_listview.setAdapter(adapter);
    }


    void do1() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_custom, sp);
        choose.setAdapter(adapter);
    }

    void do3() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_custom, allclass);
        ch.setAdapter(adapter);
    }

    void setup() {
        importdata();
        total_score = findViewById(R.id.total_score);
        item_listview = findViewById(R.id.item_listview);
        choose = findViewById(R.id.choose_no);
        today = Integer.toString(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) + 1 < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        ch = findViewById(R.id.auto_choose_class);
        search = findViewById(R.id.search_btn);
        getallclass();
        databaseReference.setPriority(10);
        ch.setThreshold(1);
    }

    void nofition(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    void getallclass() {
        allclass = importantdata.get("all_class");
        //allclass.remove(erase_class);
        do3();

    }

    void opendialogclander() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                today = Integer.toString(year);
                if (month + 1 < 10)
                    today += "0";
                today += Integer.toString(month + 1);
                if (day < 10)
                    today += "0";
                today += Integer.toString(day);
                System.out.println(today);
                if (allclass.indexOf(ch.getText().toString()) != -1) {
                    sp.add("請選擇");
                    sp.add("教室");
                    do1();
                    no.clear();
                    for (int i = 1; i <= 24; i++) {
                        for(int k=0;k<importantdata.get("position-"+i).size();k++)
                        if(importantdata.get("position-"+i).get(k).contains(ch.getText().toString()))
                        {
                            sp.add(importantdata.get("position-"+i).get(k).replaceAll(ch.getText().toString()+"=",""));
                           no.put(importantdata.get("position-"+i).get(k).replaceAll(ch.getText().toString()+"=",""),i);
                        }

                    }
                } else
                    nofition("沒有此班級");
            }

        }, year, month, day).show();
    }



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
                importantdata.put(temp[0], t);
                //data.append(line);
            }
        } catch (Exception e) {
            ;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
