package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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

public class MainActivity extends AppCompatActivity {
   ConnectivityManager CM;
    NetworkInfo info;
    Button resign_btn,enter,test;
    long first=0;
    EditText account,password;
    String no;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    boolean ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,search_socore.class);
                startActivity(intent);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             check();
            }


        });
        resign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,resign_screen.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
    if(System.currentTimeMillis()-first<2000) {
        super.onBackPressed();
    }else
    {
     nofition("再按一次退出");
     first=System.currentTimeMillis();
    }
    }

    void  check()
    {
      ch=true;
     if(account.getText().toString().equals(""))
     {
         nofition("你沒有輸入帳號");
         return ;
     }
     if(password.getText().toString().equals(""))
     {
         nofition("你沒有輸入密碼");
         return ;
     }
     db.child("account").child(account.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Map temp=(Map) dataSnapshot.getValue();
             if(temp!=null)
             {
                 String account1,password1;
                 account1=temp.get("account").toString();
                 password1=temp.get("password").toString();
                 if(account.getText().toString().equals(account1)&&password.getText().toString().equals(password1))
                 {
                 Intent intent=new Intent(MainActivity.this,enter_score_screen.class);
                 no=temp.get("no").toString();

                 intent.putExtra("account",account1);
                 intent.putExtra("no",no);
                 startActivity(intent);
                 }
                 else
                 nofition("帳號或密碼錯誤");
             }else {
                 nofition("查無此帳");

             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });


    }
    void setup()
    {

        resign_btn=findViewById(R.id.resign_btn);
        enter=findViewById(R.id.enter_main_btn);
        account=findViewById(R.id.account_main_edittext);
        password=findViewById(R.id.password_main_edittext);
        test=findViewById(R.id.test);
    }
    void nofition(String data)
    {
        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();

    }
    Boolean check_interner()
    {
         CM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        info = CM.getActiveNetworkInfo();
         if(info!=null)
         {
             if(info.isConnected())
              return false;
         }else
         {
             return false;
         }
     return false;
    }

}
