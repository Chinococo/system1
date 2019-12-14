package com.example.health_system;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ask_opeator extends DialogFragment {
    EditText account, password;
    Button enter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_ask_account, null);
        alertdialog.setView(view).setTitle("繼承").setCancelable(false).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
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
                        nofition("你都輸了");
                    }
                }
            }
        });
        return alertdialog.create();
    }
    void nofition(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
    }
}
