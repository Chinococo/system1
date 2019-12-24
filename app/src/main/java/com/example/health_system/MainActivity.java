package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int l = 1;
    ConnectivityManager CM;
    NetworkInfo info;
    Button resign_btn, enter;
    TextView visistor;
    List<String> data = new ArrayList<>();
    Map<Integer, String> test = new HashMap<>();
    Map<String, List> c = new HashMap<>();
    long first = 0;
    EditText account, password;
    String no;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    boolean ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //upload();
        //insition();
        //upload_item();
        setup();
        visistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, search_socore.class);
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
                Intent intent = new Intent(MainActivity.this, resign_screen.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - first < 2000) {
            super.onBackPressed();
        } else {
            nofition("再按一次退出");
            first = System.currentTimeMillis();
        }
    }

    void check() {
        ch = true;
        if (account.getText().toString().equals("")) {
            nofition("你沒有輸入帳號");
            return;
        }
        if (password.getText().toString().equals("")) {
            nofition("你沒有輸入密碼");
            return;
        }
        db.child("account").child(account.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map temp = (Map) dataSnapshot.getValue();
                if (temp != null) {
                    String account1, password1;
                    account1 = temp.get("account").toString();
                    password1 = temp.get("password").toString();
                    if (account.getText().toString().equals(account1) && password.getText().toString().equals(password1)) {
                        Intent intent = new Intent(MainActivity.this, enter_score_screen.class);
                        no = temp.get("no").toString();

                        intent.putExtra("account", account1);
                        intent.putExtra("no", no);
                        startActivity(intent);
                    } else
                        nofition("帳號或密碼錯誤");
                } else {
                    nofition("查無此帳");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void setup() {

        resign_btn = findViewById(R.id.resign_btn);
        enter = findViewById(R.id.enter_main_btn);
        account = findViewById(R.id.account_main_edittext);
        password = findViewById(R.id.password_main_edittext);
        visistor = findViewById(R.id.visitor);
    }

    void nofition(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

    }

    Boolean check_interner() {
        CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        info = CM.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected())
                return false;
        } else {
            return false;
        }
        return false;
    }

    private void upload_item() {
        String no = "18~24";
        db.child("no").child(no).child("item").child("1").setValue("    人工垃圾");
        db.child("no").child(no).child("item").child("2").setValue("    塵土樹葉");
        db.child("no").child(no).child("item").child("3").setValue("　  草地落葉");
        db.child("no").child(no).child("item").child("4").setValue("      大樹葉");
        db.child("no").child(no).child("item").child("5").setValue("        其他");
    //                                                                          1
    }//上傳程式

    private void upload() {
        String no = "1";
        data.add("請選擇");
        data.add("空調二");
        data.add("電二乙");
        data.add("電修一");
        data.add("塗裝三");
        data.add("汽美一");
        //data.add("機二甲");
        //data.add("機三乙");
        //data.add("機三甲");
        //data.add("綜二甲");
        //data.add("綜二乙");
        //data.add("裝潢三");
        c.put("class", data);
        db.child("no").child(no).setValue(c);
    }//上傳程式

    void clear_d(final int no1) {
        Log.d("jjjj", String.valueOf(no1));
        db.child("no").child(String.valueOf(no1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Log.d("jjjj", String.valueOf(no1));
                    HashMap<Object, Object> insition = (HashMap<Object, Object>) dataSnapshot.getValue();
                    db.child("no").child(String.valueOf(no1)).setValue(null);
                    db.child("no").child(String.valueOf(no1)).child("class").setValue(insition.get("class"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void insition() {
        //clear_d(8);

        for (l = 1; l <= 24; l++) {
            Log.d("fwa", String.valueOf(l));
            clear_d(l);
        }
    }
}
