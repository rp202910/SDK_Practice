package com.example.sdk_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.maxtap.WriteText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txt=findViewById(R.id.text);


        WriteText writeText=new WriteText();
        writeText.changeText(txt);




    }




}