package company.test.health_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class test extends AppCompatActivity {
    Intent intent;
    RecyclerView recyclerView;
    recycke_adpter recyckeAdpter;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> temp = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        intent = getIntent();
        make_data();
    }

    private void make_data() {
        //Log.e("test",getIntent().getExtras().getStringArray("data").toString());
        ArrayList<String> t = getIntent().getExtras().getStringArrayList("view");
        //Log.e("t",t.toString());
        temp.put("judge1", t.get(8));
        temp.put("judge2", t.get(9));
        temp.put("judge3", t.get(10));
        temp.put("judge4", t.get(11));
        temp.put("judge5", t.get(12));
        temp.put("judge6", t.get(13));
        temp.put("judge7", t.get(14));
        temp.put("judge8", t.get(15));
        temp.put("score1", t.get(0));
        temp.put("score2", t.get(1));
        temp.put("score3", t.get(2));
        temp.put("score4", t.get(3));
        temp.put("score5", t.get(4));
        temp.put("score6", t.get(5));
        temp.put("score7", t.get(6));
        temp.put("score8", t.get(7));
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyckeAdpter = new recycke_adpter();
        recyclerView.setAdapter(recyckeAdpter);

    }

    class recycke_adpter extends RecyclerView.Adapter<recycke_adpter.ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.preview_score_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.judge1.setText(temp.get("judge1"));
            holder.judge2.setText(temp.get("judge2"));
            holder.judge3.setText(temp.get("judge3"));
            holder.judge4.setText(temp.get("judge4"));
            holder.judge5.setText(temp.get("judge5"));
            holder.judge6.setText(temp.get("judge6"));
            holder.judge7.setText(temp.get("judge7"));
            holder.judge8.setText(temp.get("judge8"));
            holder.score1.setText(temp.get("score1"));
            holder.score2.setText(temp.get("score2"));
            holder.score3.setText(temp.get("score3"));
            holder.score4.setText(temp.get("score4"));
            holder.score5.setText(temp.get("score5"));
            holder.score6.setText(temp.get("score6"));
            holder.score7.setText(temp.get("score7"));
            holder.score8.setText(temp.get("score8"));
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView judge1, judge2, judge3, judge4, judge5, judge6, judge7, judge8;
            TextView score1, score2, score3, score4, score5, score6, score7, score8;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                judge1 = itemView.findViewById(R.id.textview_judge1);
                judge2 = itemView.findViewById(R.id.textview_judge2);
                judge3 = itemView.findViewById(R.id.textview_judge3);
                judge4 = itemView.findViewById(R.id.textview_judge4);
                judge5 = itemView.findViewById(R.id.textview_judge5);
                judge6 = itemView.findViewById(R.id.textview_judge6);
                judge7 = itemView.findViewById(R.id.textview_judge7);
                judge8 = itemView.findViewById(R.id.textview_judge8);
                score1 = itemView.findViewById(R.id.textview_score1);
                score2 = itemView.findViewById(R.id.textview_score2);
                score3 = itemView.findViewById(R.id.textview_score3);
                score4 = itemView.findViewById(R.id.textview_score4);
                score5 = itemView.findViewById(R.id.textview_score5);
                score6 = itemView.findViewById(R.id.textview_score6);
                score7 = itemView.findViewById(R.id.textview_score7);
                score8 = itemView.findViewById(R.id.textview_score8);
            }
        }


    }


}