package company.test.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    HashMap<String, ArrayList<String>> importantdata;
    int l = 1;
    String Floder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "class";
    File directry = new File(Floder);
    File output = new File(directry, "all_class.csv");
    ConnectivityManager CM;//??????
    NetworkInfo info;//????????????
    Button resign_btn, enter;
    TextView visistor;
    List<String> data = new ArrayList<>();//??????????????????
    Map<Integer, String> test = new HashMap<>();//??????????????????
    Map<String, List> c = new HashMap<>();//??????????????????
    long first = 0;//????????????????????????
    EditText account, password;
    StringBuilder stringBuilder = new StringBuilder();
    String no;//??????
    int nowvercode;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();//???????????????
    boolean ch;//?????????????????????bool




    @Override
    protected void onCreate(Bundle savedInstanceState) //main()
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent it = new Intent(this, company.test.health_system.test.class);
        //startActivity(it);
        if (haveInternet()) {
            nowvercode = Integer.parseInt(getString(R.string.version_code));
            db.child("vercode").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int seververcode = Integer.parseInt(snapshot.getValue().toString());
                    if (nowvercode >= seververcode) {
                        db.child("vercode").setValue(nowvercode);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("??????");
                        builder.setCancelable(false);
                        builder.setMessage("?????????????????????google play??????");
                        builder.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
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
    public void onBackPressed()  //??????????????????
    {
        if (System.currentTimeMillis() - first < 2000) {
            super.onBackPressed();
        } else {
            nofition("??????????????????");
            first = System.currentTimeMillis();
        }
    }

    void check() //?????????????????????????????????
    {
        //File directory = new File();
        ch = true;
        if (!new File(Environment.getExternalStorageDirectory() + File.separator + "???????????????????????????", "important_data.csv").exists()) {
            nofition("dont_have location data");
            getsheet();
            return;
        }
        if (account.getText().toString().equals("")) {
            nofition("?????????????????????");
            return;
        }
        if (password.getText().toString().equals("")) {
            nofition("?????????????????????");
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
                        nofition("?????????????????????");
                } else {
                    nofition("????????????");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void setup() //???????????????????????????
    {
        resign_btn = findViewById(R.id.resign_btn);
        enter = findViewById(R.id.enter_main_btn);
        account = findViewById(R.id.account_main_edittext);
        password = findViewById(R.id.password_main_edittext);
        visistor = findViewById(R.id.visitor);
    }

    void nofition(String data)//??????????????????
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

    }


    void clear_d(final int no1)//?????????no1?????????????????????????????????
    {
        Log.d("jjjj", String.valueOf(no1));
        db.child("id").child(String.valueOf(no1)).setValue(null);
    }

    void insition()//???????????????????????????(??????)
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

    void testfragment(String title, String message)//??????????????????
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title).setCancelable(false).setPositiveButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    int requestpermission()//????????????
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    void getallclass()//????????????
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

    void _class(final String no)//??????????????????
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
                            nofition("????????????????????????");
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
        File directory123 = new File(Environment.getExternalStorageDirectory() + File.separator + "???????????????????????????");
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
            alertDialog.setTitle("???????????????");
            alertDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    haveInternet();
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
        Log.e("????????????=", "" + result);
        return result;
    }//??????????????????



}

