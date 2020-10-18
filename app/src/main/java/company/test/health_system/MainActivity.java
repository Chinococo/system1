package company.test.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.DynamicsProcessing;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import company.test.health_system.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int newVerCode;
    String newVerName;
    HashMap<String, ArrayList<String>> importantdata;
    StringBuilder g_sb = new StringBuilder();
    int l = 1;
    String Floder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "class";
    File directry = new File(Floder);
    File output = new File(directry, "all_class.csv");
    ConnectivityManager CM;//日曆
    NetworkInfo info;//網路狀態
    Button resign_btn, enter;
    TextView visistor;
    List<String> data = new ArrayList<>();//上傳所需物件
    Map<Integer, String> test = new HashMap<>();//上傳所需物件
    Map<String, List> c = new HashMap<>();//上傳所需物件
    long first = 0;//退出指令所需物件
    EditText account, password;
    StringBuilder stringBuilder = new StringBuilder();
    String no;//編號
    int nowvercode;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();//網路資料庫
    boolean ch;//檢查帳號狀態的bool

    @Override
    protected void onCreate(Bundle savedInstanceState) //main()
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nowvercode = getVerCode(this);
        if(haveInternet())
        {
            db.child("vercode").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int seververcode = Integer.parseInt(snapshot.getValue().toString());
                    if(nowvercode!=seververcode)
                    {
                        db.child("vercode").setValue(Math.max(nowvercode,seververcode));
                        go2GooglePlay();
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            getsheet();
            //insition();
            requestpermission();
            getallclass();
            setup();
            event();
        }

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
        //File directory = new File();
        ch = true;
        if (!new File(Environment.getExternalStorageDirectory() + File.separator + "衛生評分系統資料夾", "important_data.csv").exists()) {
            nofition("dont_have location data");
            getsheet();
            return;
        }
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
                        Intent intent2 = new Intent(MainActivity.this, classroom.class);
                        no = temp.get("no").toString();
                        intent2.putExtra("account", account1);
                        intent2.putExtra("no", no);
                        Intent intent1 = new Intent(MainActivity.this, enter_score_screen.class);
                        no = temp.get("no").toString();
                        intent1.putExtra("account", account1);
                        intent1.putExtra("no", no);
                        //getsheet();
                        if (Integer.parseInt(no) > 29) {
                            final choose fragment = new choose();
                            fragment.show(getSupportFragmentManager(), null);
                        } else if (Integer.parseInt(no) > 24) {
                            startActivity(intent2);
                            MainActivity.this.finish();
                        } else {
                            startActivity(intent1);
                            MainActivity.this.finish();
                        }

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


    void clear_d(final int no1)//初始化no1變數編號的班機基本資料
    {
        Log.d("jjjj", String.valueOf(no1));
        db.child("id").child(String.valueOf(no1)).setValue(null);
    }

    void insition()//初始化全部基本資料(班級)
    {
        db.child("account").setValue(null);
        HashMap<String, String> worker = new HashMap<>();
        worker.put("truename", "teacher");
        worker.put("account", "teacher");
        worker.put("nickname", "teacher");
        worker.put("no", "30");
        worker.put("password", "0000");
        db.child("account").child("teacher").setValue(worker);
        for (l = 1; l <= 29; l++) {
            Log.d("fwa", String.valueOf(l));
            clear_d(l);
        }
        db.child("no").setValue(null);
        db.child("class").setValue(null);
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
                MainActivity.this.finish();
            }
        });
    }

    void testfragment(String title, String message)//繼承畫面
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("我了解了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    int requestpermission()//索取權限
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    void getallclass()//老師指令
    {
        if (requestpermission() != 0) {
            stringBuilder = new StringBuilder();
            try {
                if (!directry.exists())
                    directry.mkdirs();
                if (!output.exists())
                    output.createNewFile();
                for (int i = 1; i <= 24; i++)
                    _class(String.valueOf(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void _class(final String no)//拿資料副程式
    {
        db.child("no").child(no).child("class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ArrayList<Object> get = (ArrayList<Object>) dataSnapshot.getValue();
                    for (int i = 1; i < get.size(); i++)
                        if (i != get.size() - 1)
                            stringBuilder.append(get.get(i) + ",");
                        else
                            stringBuilder.append(get.get(i) + "\n");

                    if (no.equals("24")) {
                        OutputStream outputStream = new FileOutputStream(output);
                        outputStream.write(stringBuilder.toString().getBytes());
                        outputStream.flush();
                        outputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void getsheet() {
        get_html getHtml = new get_html("https://docs.google.com/spreadsheets/d/e/2PACX-1vTSUjx6g2eiKzdFzBE6xiCdSUMo8ugZDW1LN_eXZ_wFb_1x3cv0-P1TgewZJB4WvhX7u82DGV4-OWQ1/pub?output=csv", "just_getdata");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getHtml.start();
                try {
                    getHtml.join();
                } catch (InterruptedException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nofition(e.toString());
                        }
                    });
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nofition("成功讀取網路資料");
                        }
                    });
                }
                outfile("important_data", new StringBuilder(getHtml.returndata()));
                importdata();
            }
        }).start();
    }

    void outfile(String name, StringBuilder sb) {
        output_csv outputCsv = new output_csv(MainActivity.this);
        outputCsv.WriteFileExample(name, sb);

    }

    void importdata() {
        importantdata = new HashMap<>();
        File directory123 = new File(Environment.getExternalStorageDirectory() + File.separator + "衛生評分系統資料夾");
        File dir = Environment.getExternalStorageDirectory();
        File csv = new File(directory123, "important_data.csv");
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(csv), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split(",");
                ArrayList<String> t = new ArrayList<>();
                for (int i = 1; i < temp.length; i++)
                    t.add(temp[i]);
                importantdata.put(temp[0], t);
                //data.append(line);
            }
        } catch (Exception e) {
            ;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                ;
            }
        }
    }

    boolean haveInternet() {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            AlertDialog.Builder alertDialog =
                    new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("請打開網路");
            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getBaseContext(), "確定", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {
            if (!info.isAvailable()) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error", e.getMessage());
        }

        return verCode;
    }
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error", e.getMessage());
        }
        return verName;
    }/*
    private boolean getServerVer () {
        try {
            String verjson = NetworkTool.getContent(company.test.health_system.Config.UPDATE_SERVER + Config.UPDATE_VERJSON);
            JSONArray array = new JSONArray(verjson);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                try {
                    newVerCode = Integer.parseInt(obj.getString("verCode"));
                    newVerName = obj.getString("verName");
                } catch (Exception e) {
                    newVerCode = -1;
                    newVerName = "";
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }*/
    private void go2GooglePlay() {
        // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=company.test.health_system")));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=company.test.health_system" )));
        }
    }
}

