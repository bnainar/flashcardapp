package com.example.flashcarddb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        TextView textView = (TextView) findViewById(R.id.textView);
        LinearLayout l = (LinearLayout) findViewById(R.id.ll);

    }
}