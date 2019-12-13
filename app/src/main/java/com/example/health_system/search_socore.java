package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class search_socore extends AppCompatActivity {
    int k;
    List<Integer> list=new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    AutoCompleteTextView ch;
    Button search;
    String today;
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
    ListView listView;
    Spinner choose;
    List<String> sp=new ArrayList<>();
    String test="汽美一";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_socore);
        setup();

        choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(!sp.get(position).equals("請選擇"))
            {
                nofition(sp.get(position));

                databaseReference.child("no").child(sp.get(position)).child(today).child(test).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       list=(List<Integer>)dataSnapshot.getValue();
                       do2();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int l=1;
            sp.clear();
            sp.add("請選擇");
            for( k=1;k<3;k++)
            {
                Log.d("k=", String.valueOf(k));
                getdata(k);
            }

            }
        });
    }
public void getdata(final int index)
{
    databaseReference.child("no").child(String.valueOf(index)).child(today).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue()!=null)
        sp.add(String.valueOf(index));
        if(index==2)
        do1();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}
    void do1()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sp);
        adapter.setDropDownViewResource(R.layout.spinner_custom);
        choose.setAdapter(adapter);
    }
    void do2()
    {
        ListAdapter adapter = new ArrayAdapter<Integer>(this , android.R.layout.simple_list_item_1 ,list);
        listView.setAdapter(adapter);
    }
    void setup() {
        choose=findViewById(R.id.choose_no);
        today = Integer.toString(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) + 1 < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            today += "0";
        today += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        ch = findViewById(R.id.auto_choose_class);
        listView=findViewById(R.id.listview);
        search=findViewById(R.id.search_btn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.class_spinner, android.R.layout.simple_list_item_1);
        ch.setAdapter(adapter);
        databaseReference.setPriority(10);

    }
    void nofition(String data)
    {
        Toast.makeText(this,data,Toast.LENGTH_LONG).show();
    }
}
