package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class resign_screen extends AppCompatActivity {
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    EditText account,repeat,nickname,password,no,truename;
    Button enter;
    boolean ch=true;
    String temp="";
    Map<String,Object> worker=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign_screen);
        setup();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            check();
    }

});


    }
    void input_data()
    {
        worker.put("truename",truename.getText().toString());
        worker.put("account",account.getText().toString());
        worker.put("nickname",nickname.getText().toString());
        worker.put("no",no.getText().toString());
        worker.put("password",password.getText().toString());
    }
    void nofition(String data)
    {
        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
    }
    void setup()
    {
        account=findViewById(R.id.account_editText);
        repeat=findViewById(R.id.repeat_editText);
        nickname=findViewById(R.id.nickname_editText);
        password=findViewById(R.id.password_editText);
        no=findViewById(R.id.no_edittext);
        enter = findViewById(R.id.enter_resign_btn);
        truename = findViewById(R.id.truename_editText);

    }
    void check()
    {
        if(truename.getText().toString().equals(""))
        {
            nofition("你的真實名子沒輸入喔 ");
            return ;
        }
        if(no.getText().toString().equals(""))
        {
            nofition("你的編號沒輸入喔 ");
            return ;
        }
        if(account.getText().toString().equals(""))
        {
            nofition("你的帳號沒輸入喔 ");
            return ;
        }
        if(nickname.getText().toString().equals(""))
        {
            nofition("你的沒輸入喔 ");
            return ;
        }
        if(password.getText().toString().equals(""))
        {
            nofition("你的密碼沒輸入喔 ");
            return ;
        }
        if(!password.getText().toString().equals(repeat.getText().toString()))
        {
            nofition("你的密碼不相同 ");
            return ;
        }
        reference.child("account").child(account.getText().toString()).child("account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                input_data();
                String temp=dataSnapshot.getValue(String.class);
                if(temp==null)
                {
                    reference.child("account").child(account.getText().toString()).setValue(worker);
                    nofition("successful");
                    resign_screen.this.finish();
                }else
                {
                    nofition(temp+"此帳已被註冊");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
