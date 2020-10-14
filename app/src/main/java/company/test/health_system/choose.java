package company.test.health_system;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class choose extends DialogFragment {
    Button b1,b2,b3;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.choose, null);
        alertdialog.setView(view).setCancelable(false);
        b1=view.findViewById(R.id.button123);
        b2=view.findViewById(R.id.button321);
        b3=view.findViewById(R.id.button567);
        Intent intent2 = new Intent(view.getContext(), classroom.class);
        intent2.putExtra("no", "30");
        Intent intent1 = new Intent(view.getContext(), enter_score_screen.class);
        intent1.putExtra("no", "30");
        intent1.putExtra("account", "teacher");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(intent2);
                getDialog().dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);
                getActivity().finish();
                getDialog().dismiss();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), ouput.class));
                getActivity().finish();
                getDialog().dismiss();
            }
        });
        return alertdialog.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
