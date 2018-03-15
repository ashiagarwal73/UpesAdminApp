package com.agarwal.ashi.upesadminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    int count=0;
    Counter counter;
    Button submit;
     DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       counter = dataSnapshot.child("Counter").getValue(Counter.class);
                        count = counter.getCounterid();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                EventsInformation eventsInformation = new EventsInformation();
                Counter counter=new Counter();
                eventsInformation.setEventName("new event");
               counter.setCounterid(++count);
                mDatabase.child("Counter").setValue(counter);
                mDatabase.child("EventsDetails").child(counter.getCounterid() + "").setValue(eventsInformation);
            }
        });

        //Toast.makeText(SplashActivity.this, ""+counter.getCounterid(), Toast.LENGTH_SHORT).show();
    }
}
