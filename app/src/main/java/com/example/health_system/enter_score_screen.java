package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class enter_score_screen extends AppCompatActivity {
    score_struct score_structs1[]=new score_struct[5];
    EditText enter1, enter2, enter3, enter4, enter5;
    Button enter;
    Calendar calendar = Calendar.getInstance();
    Intent intent;
    List<String> get;
    Spinner choose;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Map<String, List> c = new HashMap<>();
    List<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_score_screen);
        intent = getIntent();
        setup();
        //upload("2");
        getdata("2");
        putdata("1");

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getdata(String no) {
        databaseReference.child("no").child(no).child("class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                get = (List<String>) dataSnapshot.getValue();
                do1();
                //choose.setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void do1() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, get);
        choose.setAdapter(adapter);
    }

    private void upload(String no) {
        data.add("no select");
        data.add("電繪一");
        data.add("裝潢一");
        data.add("裝潢三");
        data.add("裝潢二");
        data.add("汽美一");
        c.put("class", data);
        databaseReference.child("no").child(no).setValue(c);
    }

    void setup() {
        choose = findViewById(R.id.enter_score_spinner);
        enter = findViewById(R.id.enter_scorce_btn);
        enter1 = findViewById(R.id.enter_score_edit1);
        enter2 = findViewById(R.id.enter_score_edit2);
        enter3 = findViewById(R.id.enter_score_edit3);
        enter4 = findViewById(R.id.enter_score_edit4);
        enter5 = findViewById(R.id.enter_score_edit5);
    }

    void nofition(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    void putdata(String no) {

        getnowdata(no);


    }

    private void getnowdata(String no) {
        String today = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        enter5.setText(today);
        databaseReference.child("no").child(no).child(today).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                score_structs1=(score_struct[]) dataSnapshot.getValue();
                if(score_structs1==null)
                {
                    nofition("no data");
                }else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}