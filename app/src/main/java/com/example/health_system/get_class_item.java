package com.example.health_system;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class get_class_item extends Thread {
    String no;
    String r="";
    DatabaseReference databaseReference=  FirebaseDatabase.getInstance().getReference();
    get_class_item(String no)
    {
        this.no=no;
    }



}
