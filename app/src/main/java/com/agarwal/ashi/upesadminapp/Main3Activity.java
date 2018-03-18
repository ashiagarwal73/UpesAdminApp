package com.agarwal.ashi.upesadminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main3Activity extends AppCompatActivity {
    Button button,button2;
    String loginid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        button=findViewById(R.id.button3);
        button2=findViewById(R.id.button5);
        Intent inte=getIntent();
        loginid=inte.getStringExtra("loginId");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                intent.putExtra("loginId",loginid);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main3Activity.this,Main4Activity.class);
                intent.putExtra("loginId",loginid);
                startActivity(intent);
            }
        });
    }
}
