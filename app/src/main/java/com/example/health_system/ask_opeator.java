package com.example.health_system;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ask_opeator extends DialogFragment {
    EditText account, password;
    Button enter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String account_prev = "", password_prev = "";
    HashMap<String, String> worker, worker2 = new HashMap<>();

    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        return super.show(transaction, tag);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String temp = getArguments().getString("account");
        worker = (HashMap<String, String>) getArguments().getSerializable("worker");
        get(temp);
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.activity_ask_account, null);
        alertdialog.setView(view).setCancelable(false);
        account = view.findViewById(R.id.ask_account);
        password = view.findViewById(R.id.ask_password);
        enter = view.findViewById(R.id.ask_enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString().equals("")) {
                    nofition("你沒輸入帳號");
                } else {
                    if (password.getText().toString().equals("")) {
                        nofition("你沒輸入密碼");
                    } else {
                        if (account_prev.equals(account.getText().toString()) && password_prev.equals(password.getText().toString())) {
                            databaseReference.child("account").child(account_prev).removeValue();
                            databaseReference.child("account").child(worker.get("account")).setValue(worker);
                            databaseReference.child("id").child(worker.get("no")).setValue(worker.get("account"));
                            Log.d("test", worker.get("account") + "123");
                            nofition("succedful 繼承\n感謝以下同學"+account_prev);
                            //Log.e("fin","fin");
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            startActivity(intent);
                            getDialog().dismiss();
                        } else {
                            nofition("帳密有誤");
                        }
                    }
                }
            }
        });
        return alertdialog.create();
    }

    void nofition(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
    }

    void get(String tag) {

        databaseReference.child("account").child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                worker2 = (HashMap<String, String>) dataSnapshot.getValue();
                if (dataSnapshot.getValue() != null) {
                    account_prev = worker2.get("account");
                    password_prev = worker2.get("password");
                } else {
                    nofition("no get");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
