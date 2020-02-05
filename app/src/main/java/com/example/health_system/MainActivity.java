package com.example.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int l = 1;
    String Floder= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"class";
    File directry=new File(Floder);
    File output=new File(directry,"all_class.csv");
    ConnectivityManager CM;//日曆
    NetworkInfo info;//網路狀態
    Button resign_btn, enter;
    TextView visistor;
    List<String> data = new ArrayList<>();//上傳所需物件
    Map<Integer, String> test = new HashMap<>();//上傳所需物件
    Map<String, List> c = new HashMap<>();//上傳所需物件
    long first = 0;//退出指令所需物件
    EditText account, password;
    StringBuilder stringBuilder=new StringBuilder();
    String no;//編號
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();//網路資料庫
    boolean ch;//檢查帳號狀態的bool

    @Override
    protected void onCreate(Bundle savedInstanceState) //main()
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        just_test();





        requestpermission();
        getallclass();
        setup();
        event();

    }

    private void just_test() {
        Intent intent = new Intent(this,ouput.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()  //退出事件
    {
        if (System.currentTimeMillis() - first < 2000) {
            super.onBackPressed();
        } else {
            nofition("再按一次退出");
            first = System.currentTimeMillis();
        }
    }

    void check() //檢查是否有此帳，並登入
    {
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

    void setup() //基本資料設置，變數
    {

        resign_btn = findViewById(R.id.resign_btn);
        enter = findViewById(R.id.enter_main_btn);
        account = findViewById(R.id.account_main_edittext);
        password = findViewById(R.id.password_main_edittext);
        visistor = findViewById(R.id.visitor);
    }

    void nofition(String data)//顯示泡泡資料
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

    }

    void upload_class_positinn()//上船班及掃區位置
    {

        db.child("no").child("position").child("10").child("建二甲").setValue("囊螢樓東女廁");
        db.child("no").child("position").child("10").child("製圖二").setValue("囊螢樓西女廁");
        db.child("no").child("position").child("10").child("室一甲").setValue("囊螢樓西女廁");
        db.child("no").child("position").child("10").child("綜一丁").setValue("囊螢樓東女廁");
        db.child("no").child("position").child("10").child("製圖一").setValue("囊螢樓東女廁");
        db.child("no").child("position").child("10").child("子一乙").setValue("囊螢樓西女廁");

    }

    private void upload_item()//上傳掃區基本資料
    {
        String no = "18~24";
        db.child("no").child(no).child("item").child("1").setValue("    人工垃圾");
        db.child("no").child(no).child("item").child("2").setValue("    塵土樹葉");
        db.child("no").child(no).child("item").child("3").setValue("　  草地落葉");
        db.child("no").child(no).child("item").child("4").setValue("      大樹葉");
        db.child("no").child(no).child("item").child("5").setValue("        其他");
        //                                                                          1
    }

    private void upload()//上傳班級
    {
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
    }

    void clear_d(final int no1)//初始化no1變數編號的班機基本資料
    {
        Log.d("jjjj", String.valueOf(no1));
        db.child("no").child(String.valueOf(no1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
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

    void insition()//初始化全部基本資料(班級)
    {
        //clear_d(8);
        for (l = 1; l <= 24; l++) {
            Log.d("fwa", String.valueOf(l));
            clear_d(l);
        }
    }

    void event()//all listner
    {
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

    void testfragment(String title, String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("我了解了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    int requestpermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    void getallclass()
    {
      if(requestpermission()!=0)
      {
          stringBuilder=new StringBuilder();
          try {
              if(!directry.exists())
                  directry.mkdirs();
              if(!output.exists())
                  output.createNewFile();
              for(int i=1;i<=24;i++)
                  _class(String.valueOf(i));
          }catch (Exception e)
          {
              e.printStackTrace();
          }
      }
    }

    void _class(final String no)
    {
        db.child("no").child(no).child("class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    ArrayList<Object> get=(ArrayList<Object>)dataSnapshot.getValue() ;
                    for(int i=1;i<get.size();i++)
                    if(i!=get.size()-1)
                    stringBuilder.append(get.get(i) +",");
                    else
                    stringBuilder.append(get.get(i) +"\n");

                    if(no.equals("24"))
                    {
                        OutputStream outputStream=new FileOutputStream(output);
                        outputStream.write(stringBuilder.toString().getBytes());
                        outputStream.flush();
                        outputStream.close();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
