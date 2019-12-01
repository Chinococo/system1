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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
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
    int pos=-1;
    score_struct[] score_s1=new score_struct[5];
    EditText enter1, enter2, enter3, enter4, enter5;
    Button enter;
    Calendar calendar = Calendar.getInstance();
    String today = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
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
        if(score_s1!=null)
        nofition("good");
        intent = getIntent();
        setup();
        //upload("2");
        getdata("2");
        getnowdata("1");
        event();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            for(int i=0;i<5;i++)
            databaseReference.child("no").child("2").child(today).child(score_s1[i].getName()).setValue(score_s1[i].getScore());
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
        score_s1[0].setName("1");

    }//中繼站

    private void upload(String no) {
        data.add("no select");
        data.add("電繪一");
        data.add("裝潢一");
        data.add("裝潢三");
        data.add("裝潢二");
        data.add("汽美一");
        c.put("class", data);
        databaseReference.child("no").child(no).setValue(c);
    }//上傳程式

    void setup() {
        for(int i=0;i<5;i++)
        score_s1[i]=new score_struct();
        score_s1[0].setScore(1,100);
        choose = findViewById(R.id.enter_score_spinner);
        enter = findViewById(R.id.enter_scorce_btn);
        enter1 = findViewById(R.id.enter_score_edit1);
        enter2 = findViewById(R.id.enter_score_edit2);
        enter3 = findViewById(R.id.enter_score_edit3);
        enter4 = findViewById(R.id.enter_score_edit4);
        enter5 = findViewById(R.id.enter_score_edit5);
    }//初始化變數

    void nofition(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    private void getnowdata(String no) {
        String today = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        enter5.setText(today);
        databaseReference.child("no").child(no).child(today).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()==null)
                {
                    nofition("no data");
                }else
                { //score_structs1=(score_struct[]) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
   private void event()
   {
       choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               nofition(Integer.toString(position));
               pos=position-1;
               //Log.d("ERROR",String.valueOf(pos));
               if(position-1>=0)
               {

                   int[] temp=score_s1[pos].getScore();

                   enter1.setText(Integer.toString(temp[0]));
                   enter2.setText(Integer.toString(temp[1]));
                   enter3.setText(Integer.toString(temp[2]));
                   enter4.setText(Integer.toString(temp[3]));
                   enter5.setText(Integer.toString(temp[4]));
               }



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

             if(!enter1.getText().toString().equals("")&&pos>=0)
             {
                 score_s1[pos].setScore(0,Integer.parseInt(enter1.getText().toString()));
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

               if(!enter2.getText().toString().equals("")&&pos>=0)
               {
                   score_s1[pos].setScore(1,Integer.parseInt(enter2.getText().toString()));
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

               if(!enter3.getText().toString().equals("")&&pos>=0)
               {
                   score_s1[pos].setScore(2,Integer.parseInt(enter3.getText().toString()));
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

               if(!enter4.getText().toString().equals("")&&pos>=0)
               {
                   score_s1[pos].setScore(3,Integer.parseInt(enter4.getText().toString()));
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

               if(!enter5.getText().toString().equals("")&&pos>=0)
               {
                   score_s1[pos].setScore(4,Integer.parseInt(enter5.getText().toString()));
               }

           }
           @Override
           public void afterTextChanged(Editable s) {

           }
       });
   }
}