package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class resign_screen extends AppCompatActivity {
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    EditText account,repeat,nickname,password,no,truename;
    Button enter;
    //Map<String,Object> worker=new HashMap<>();
    //FirebaseFirestore db= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign_screen);
        setup();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            input_data();
            /*
            db.collection("worker").add(worker).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                nofition("id="+documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                nofition("no internet");
                }
            });*/
            //reference.child("account").child(truename.getText().toString()).setValue(worker1);
    }
});

    }
    void input_data()
    {
        /*
        worker.put("account",account.getText().toString());
        worker.put("nickname",nickname.getText().toString());
        worker.put("no",no.getText().toString());
        worker.put("password",password.getText().toString());
        */

    }
    void nofition(String data)
    {
        Toast.makeText(this,data,Toast.LENGTH_LONG);
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
