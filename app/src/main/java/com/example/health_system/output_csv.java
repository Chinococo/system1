package com.example.health_system;

import java.util.ArrayList;
import java.util.HashMap;

public class output_csv extends Thread {
    HashMap<String, HashMap<String, ArrayList<String>>> out;
    ArrayList<String> all_date;
    output_csv(HashMap<String, HashMap<String, ArrayList<String>>> out,ArrayList<String> all_date)
    {
        this.out=out;
        this.all_date=all_date;
    }

    @Override
    public void run() {
        super.run();

    }
}
