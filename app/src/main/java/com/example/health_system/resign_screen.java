package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.NullValue;

import java.util.HashMap;
import java.util.Map;

public class resign_screen extends AppCompatActivity {
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    EditText account,repeat,nickname,password,no,truename;
    Button enter;
    String temp="";
    Map<String,Object> worker=new HashMap<>();
    Map<String,Object> test=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign_screen);
        setup();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            input_data();
            if(check())
            {
                reference.child("account").child(nickname.getText().toString()).setValue(worker);
                nofition("successful");
                Intent intent=new Intent(resign_screen.this,MainActivity.class);
                startActivity(intent);
            }


    }
    boolean check()
    {
    if(truename.getText().toString().equals(""))
    {
      nofition("你的真實名子沒輸入喔 ");
      return false;
    }
    reference.child("account").child(nickname.getText().toString()).child("nickname").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         temp=dataSnapshot.getValue(String.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });

    if(!temp.equals(""))
    {
        nofition("此帳已被註冊");
        return false;
    }

    if(no.getText().toString().equals(""))
    {
        nofition("你的編號沒輸入喔 ");
        return false;
    }
    if(account.getText().toString().equals(""))
    {
        nofition("你的帳號沒輸入喔 ");
        return false;
    }
    if(nickname.getText().toString().equals(""))
    {
        nofition("你的沒輸入喔 ");
        return false;
    }
    if(password.getText().toString().equals(""))
    {
        nofition("你的密碼沒輸入喔 ");
        return false;
    }
    if(!password.getText().toString().equals(repeat.getText().toString()))
    {
        nofition("你的密碼不相同 ");
        return false;
    }
    return true;
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
        enter = findViewById(R.id.enter);
        truename = findViewById(R.id.truename_editText);

    }
}
