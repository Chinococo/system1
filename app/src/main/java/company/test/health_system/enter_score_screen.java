package company.test.health_system;

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

import company.test.health_system.R;
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
import java.util.HashMap;

public class enter_score_screen extends AppCompatActivity {
    long first = 0;//退出指令所需物件
    Double t1;
    ArrayList<String> now_max_score;
    Calendar calendar = Calendar.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String no, today,power_today;
    HashMap<String, ArrayList<String>> importantdata;
    Intent intent;
    Spinner choose_class, choose_no;
    TextView grade1, grade2, grade3, grade4, grade5, now_score;
    EditText enter1, enter2, enter3, enter4, enter5, enter6;
    Button full_score, datepicker,next,prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_score_screen);
        setup();
        importdata();
        if(!no.equals("30"))
        {
            updatespinner();
            updateitem();
        }

        event();
    }

    void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!no.equals("30"))
                {
                        if(choose_class.getSelectedItemPosition()<choose_class.getCount()-1)
                            choose_class.setSelection(choose_class.getSelectedItemPosition()+1);

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!no.equals("30"))
                {
                    if(choose_class.getSelectedItemPosition()>0)
                        choose_class.setSelection(choose_class.getSelectedItemPosition()-1);

                }
            }
        });
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1(v);
            }
        });
        choose_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!choose_no.getSelectedItem().toString().equals("請選擇"))
                    no=choose_no.getSelectedItem().toString();
                else
                    no="30";
                updatespinner();
                updateitem();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        full_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!no.toString().equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        if (importantdata.get("max score - " + choose_class.getSelectedItem().toString() + " - " + no) != null) {
                            now_max_score = importantdata.get("max score - " + choose_class.getSelectedItem().toString() + " - " + no);
                            enter1.setText(now_max_score.get(0).toString());
                            enter2.setText(now_max_score.get(1).toString());
                            enter3.setText(now_max_score.get(2).toString());
                            enter4.setText(now_max_score.get(3).toString());
                            enter5.setText(now_max_score.get(4).toString());
                            enter6.setText("無");
                        }else
                        {
                            System.out.println("bad");
                        }
                    }
                }
            }
        });
        choose_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("max score - " + choose_class.getSelectedItem().toString() + " - " + no);
                now_max_score = importantdata.get("max score - " + choose_class.getSelectedItem().toString() + " - " + no);
                databaseReference.child("no").child(no).child(today).child(choose_class.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            ArrayList<Object> temp = (ArrayList<Object>) dataSnapshot.getValue();
                            enter1.setText(temp.get(0).toString());
                            enter2.setText(temp.get(1).toString());
                            enter3.setText(temp.get(2).toString());
                            enter4.setText(temp.get(3).toString());
                            enter5.setText(temp.get(4).toString());
                            enter6.setText(temp.get(5).toString());
                        } else {
                            enter1.setText("");
                            enter2.setText("");
                            enter3.setText("");
                            enter4.setText("");
                            enter5.setText("");
                            enter6.setText("");
                            for(String key:importantdata.keySet())
                        System.out.println(key);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        enter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!no.equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        if (!enter1.getText().toString().equals(""))
                            if (Double.parseDouble(enter1.getText().toString()) > Double.parseDouble(now_max_score.get(0)))
                                enter1.setText(now_max_score.get(0));
                        upload(choose_class.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        enter2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!no.equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        if (!enter2.getText().toString().equals(""))
                            if (Double.parseDouble(enter2.getText().toString()) > Double.parseDouble(now_max_score.get(1)))
                                enter2.setText(now_max_score.get(1));
                        upload(choose_class.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        enter3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!no.equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        if (!enter3.getText().toString().equals(""))
                            if (Double.parseDouble(enter3.getText().toString()) > Double.parseDouble(now_max_score.get(2)))
                                enter3.setText(now_max_score.get(2));
                        upload(choose_class.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        enter4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!no.equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        if (!enter4.getText().toString().equals(""))
                            if (Double.parseDouble(enter4.getText().toString()) > Double.parseDouble(now_max_score.get(3)))
                                enter4.setText(now_max_score.get(3));
                        upload(choose_class.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        enter5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!no.equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        if (!enter5.getText().toString().equals(""))
                            if (Double.parseDouble(enter5.getText().toString()) > Double.parseDouble(now_max_score.get(4)))
                                enter5.setText(now_max_score.get(4));
                        upload(choose_class.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        enter6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!no.equals("30")) {
                    if (!choose_class.getSelectedItem().toString().equals("請選擇")) {
                        upload(choose_class.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void setup() {
        setToday();
        next=findViewById(R.id.next_enter);
        prev=findViewById(R.id.prev_enter);
        now_score = findViewById(R.id.now_score_class1);
        intent = getIntent();//拿取登入時的帳號跟編號
        no = intent.getStringExtra("no");
        Log.e("no",no+"");
        grade1 = findViewById(R.id.enter1_t);
        grade2 = findViewById(R.id.enter2_t);
        grade3 = findViewById(R.id.enter3_t);
        grade4 = findViewById(R.id.enter4_t);
        grade5 = findViewById(R.id.enter5_t);
        enter1 = findViewById(R.id.enter1);
        enter2 = findViewById(R.id.enter2);
        enter3 = findViewById(R.id.enter3);
        enter4 = findViewById(R.id.enter4);
        enter5 = findViewById(R.id.enter5);
        enter6 = findViewById(R.id.enter6);
        choose_class = findViewById(R.id.position_spinner_enter_score);
        choose_no = findViewById(R.id.opeator2);
        full_score = findViewById(R.id.auto_enter_score);
        datepicker = findViewById(R.id.date_picker2);
        if(no.equals("30"))
        {
            choose_no.setVisibility(View.VISIBLE);
            datepicker.setVisibility(View.VISIBLE);
            ArrayList<String> t =new ArrayList<>();
            t.add("請選擇");
            for(int i=1;i<25;i++)
            t.add(""+i);
            choose_no.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, t));
        }
    }

    void importdata()
    {
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
                String[] temp =line.split(",");
                ArrayList<String> t= new ArrayList<>();
                for(int i=1;i<temp.length;i++)
                    t.add(temp[i]);
                importantdata.put(temp[0],t);
                //data.append(line);
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

    void updatespinner() {
        if (importantdata.get(no) != null) {
            ArrayAdapter<String> t = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, importantdata.get(no));
            t.insert("請選擇", 0);
            choose_class.setAdapter(t);
        }

    }

    void upload(String class_name) {
        update_now_score();
        ArrayList<String> t = new ArrayList<>();
        t.add(enter1.getText().toString());
        t.add(enter2.getText().toString());
        t.add(enter3.getText().toString());
        t.add(enter4.getText().toString());
        t.add(enter5.getText().toString());
        t.add(enter6.getText().toString());
        databaseReference
                .child("no")
                .child(no)
                .child(today)
                .child(class_name)
                .setValue(t);
        ArrayList<Object> power=new ArrayList<>();
        power.add(power_today);
        power.add(class_name);
        power.add(enter1.getText().toString());
        power.add(enter2.getText().toString());
        power.add(enter3.getText().toString());
        power.add(enter4.getText().toString());
        power.add(enter5.getText().toString());
        power.add(enter6.getText().toString());
        power.add(t1);
        databaseReference.child("power_bi").child("outside").child(class_name+today).setValue(power);
    }

    void setToday() {
        today = Integer.toString(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) + 1 < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        power_today = Integer.toString(calendar.get(Calendar.YEAR));
        power_today+="/";
        if (calendar.get(Calendar.MONTH) + 1 < 10)
            power_today += "0";
        power_today += Integer.toString(calendar.get(Calendar.MONTH) + 1);
        power_today+="/";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            power_today += "0";
        power_today += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }

    void updateitem() {
        Log.e("no", no);
        int temp123=Integer.parseInt(no);
        String scope;
        if(temp123<=12)
            scope="1~12";
        else if(temp123<=17)
            scope="13~17";
            else
            scope="18~24";
        if (importantdata.get(scope) != null && !no.equals("30")) {
            ArrayList<String> item = importantdata.get(scope);
            grade1.setText(item.get(0));
            grade2.setText(item.get(1));
            grade3.setText(item.get(2));
            grade4.setText(item.get(3));
            grade5.setText(item.get(4));
        }

    }

    void update_now_score() {
        t1 = 0.0;
        if (!enter1.getText().toString().equals(""))
            t1 += Double.parseDouble(enter1.getText().toString());
        if (!enter2.getText().toString().equals(""))
            t1 += Double.parseDouble(enter2.getText().toString());
        if (!enter3.getText().toString().equals(""))
            t1 += Double.parseDouble(enter3.getText().toString());
        if (!enter4.getText().toString().equals(""))
            t1 += Double.parseDouble(enter4.getText().toString());
        if (!enter5.getText().toString().equals(""))
            t1 += Double.parseDouble(enter5.getText().toString());
        now_score.setText(t1+"");
    } public void datePicker1(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                today = ("" + year);
                today += (month+1) < 10 ? "0" + (month + 1) : "" + (month + 1);
                today += (day) < 10 ? "0" + day : "" + day;

                power_today = ("" + year);
                power_today += "/";
                power_today += (month+1) < 10 ? "0" + (month + 1) : "" + (month + 1);
                power_today += "/";
                power_today += (day) < 10 ? "0" + day : "" + day;

                Log.e("123", today);
                updatespinner();
                updateitem();
            }

        }, year, month, day).show();
    }
    void nofition(String data)//顯示泡泡資訊
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
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
