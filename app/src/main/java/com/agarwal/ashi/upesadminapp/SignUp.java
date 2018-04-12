package com.agarwal.ashi.upesadminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    EditText name,id,password,otp;
    RadioGroup school;
    Button button;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        name=findViewById(R.id.editText3);
        id=findViewById(R.id.editText4);
        password=findViewById(R.id.editText5);
        otp=findViewById(R.id.editText6);
        school=findViewById(R.id.schoolss);
        button=findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(school.getCheckedRadioButtonId()==0||name.getText().toString().equals("")||password.getText().toString().equals("")||id.getText().toString().equals(""))
                {
                    Toast.makeText(SignUp.this, "Enter all Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final Society society=new Society();
                    RadioButton radioButton=findViewById(school.getCheckedRadioButtonId());
                    society.setSchool(radioButton.getText().toString());
                    society.setSocietyName(name.getText().toString());
                    society.setPassword(password.getText().toString());
                    society.setId(id.getText().toString());
                    mDatabase.child("OTP").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(otp.getText().toString().equals(dataSnapshot.getValue().toString()))
                            {
                                mDatabase.child("Society").child(society.getSocietyName()).setValue(society);
                                Intent intent=new Intent(SignUp.this,Main2Activity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }
}
