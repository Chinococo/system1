package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class search_socore extends AppCompatActivity {
    int k;
    String item_get;
    List<String> c = new ArrayList<>();
    List<String> temp = new ArrayList<>();
    List<Object> score_list = new ArrayList<>();
    List<String>item_list=new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    AutoCompleteTextView ch;
    Button search;
    Map<String,Integer>class_position=new HashMap<>();
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
                if (!sp.get(position).equals("請選擇")) {
                    nofition(String.valueOf(class_position.get(sp.get(position).toString())));
                get_position(class_position.get(sp.get(position)));

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

                if (allclass.indexOf(ch.getText().toString()) != -1) {
                    int l = 1;
                    sp.clear();
                    sp.add("請選擇");
                    for (k = 1; k <= 24; k++)
                        getdata(k);
                } else
                    nofition("沒有此班級");

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
        for(int i=0;i<item_list.size();i++)
        item_list.set(i,item_list.get(i)+":"+score_list.get(i));
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_custom, item_list);
        item_listview.setAdapter(adapter);
    }
    void get_sp(final int index)
    {
        databaseReference.child("no").child("position").child(String.valueOf(index)).child(ch.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    class_position.put(dataSnapshot.getValue().toString(),index);
                    nofition(String.valueOf(index));
                    sp.add(dataSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void getdata(final int index) {

        databaseReference.child("no").child(String.valueOf(index)).child(today).child(ch.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    get_sp(index);
                if (index == 24)
                    do1();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    void get_position(final int position)
    {
        databaseReference.child("no").child(String.valueOf(position)).child(today).child(ch.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    score_list = (List<Object>) dataSnapshot.getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if(position>=1&&position<=12)
            item_get="1~12";
        else if(position>=13&&position<=17)
            item_get="13~17";
        else if(position>=18&&position<=24)
            item_get="18~24";
        nofition(item_get);
        databaseReference.child("no").child(item_get).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    item_list=(ArrayList<String>)dataSnapshot.getValue();
                    do4();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    void do1() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_custom, sp);
        choose.setAdapter(adapter);
    }
/*
    void do2() {
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_custom, score_list);
        score_listView.setAdapter(adapter);
    }
*/
    void do3() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_custom, allclass);
        ch.setAdapter(adapter);
    }

    void setup() {
        item_listview=findViewById(R.id.item_listview);
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
        for (int i = 1; i <= 24; i++)
            getclass(i);


    }

    void getclass(final int index) {
        databaseReference.child("no").child(String.valueOf(index)).child("class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    temp = (ArrayList<String>) dataSnapshot.getValue();
                    for (int k1 = 0; k1 < temp.size(); k1++)
                        if (allclass.indexOf(temp.get(k1)) == -1)
                            allclass.add(temp.get(k1));
                }
                if (index == 10)
                    do3();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
