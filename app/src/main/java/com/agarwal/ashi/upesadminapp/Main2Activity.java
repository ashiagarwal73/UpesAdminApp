package com.agarwal.ashi.upesadminapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
    EditText editText,editText2;
    TextView signup;
    Button button;
    String id;
    String password;
    DatabaseReference mDatabase;
    SharedPreferences sharedPreferencesId;
    SharedPreferences sharedPreferencesPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        button=findViewById(R.id.button2);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        signup=findViewById(R.id.textView2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent inte=new Intent(Main2Activity.this,SignUp.class);
            startActivity(inte);
            }
        });
        sharedPreferencesId=getSharedPreferences("sharedid",MODE_PRIVATE);
        sharedPreferencesPassword=getSharedPreferences("sharedpassword",MODE_PRIVATE);
           editText.setText(sharedPreferencesId.getString("sharedid",""));
           editText2.setText(sharedPreferencesPassword.getString("sharedpassword",""));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=editText.getText().toString();
                password=editText2.getText().toString();
                if(id.equals("")||password.equals(""))
                {
                    Toast.makeText(Main2Activity.this, "Please Enter id password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Query query=mDatabase.child("Society").orderByChild("id").equalTo(id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for(DataSnapshot usersnapshot:dataSnapshot.getChildren())
                                {
                                    Society soc=usersnapshot.getValue(Society.class);
                                    if(soc.getPassword().equals(password))
                                    {

                                        SharedPreferences.Editor editor=sharedPreferencesId.edit();
                                        editor.putString("sharedid",id);
                                        SharedPreferences.Editor editor1=sharedPreferencesPassword.edit();
                                        editor1.putString("sharedpassword",password);
                                        editor.commit();
                                        editor1.commit();
                                        Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                                        intent.putExtra("loginId",id);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(Main2Activity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            else {
                                Toast.makeText(Main2Activity.this, "User id doesnot exist", Toast.LENGTH_SHORT).show();
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
