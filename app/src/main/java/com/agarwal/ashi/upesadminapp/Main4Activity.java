package com.agarwal.ashi.upesadminapp;
import java.util.Random;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Main4Activity extends AppCompatActivity {
    DatePickerDialog dialog;
    DatePicker dp;
    long a=System.currentTimeMillis()-1000;
    String s;
    Button dated;
    int year_x,month_x,day_x;
    static final int Dialog_id=0;
    Button but;
    String sschool,sworkshop,sseminar,scompetition,scultural,ssports,swebinar;
    int count=0;
    String society[]={"choose society"};
    EditText eventname,eventDesc,organiser,contact,eventid,eventtime,eventvenue;
    TextView date;
    RadioGroup workshop,seminar,competition,cultural,sports,webinar;
    Spinner schoolspinner,societyspinner;
    Button submit;
    String selectedsociety;
    String seventdesc;
    String sdate;
    String sorganiser;
    String scontact;
    String selectedSchool="Choose School";
    String schools[]={"Choose School","School of Computer Science","School of Engineering","School of Design","School of Business","School of Law"};
    DatabaseReference mDatabase;
    Uri downloadUrl;
    StorageReference storageRef;
    Counter counter;
    String loginid;
    byte[] byteArray;
    EventsInformation eventsInformation;
    EventsInformation eve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent inten=getIntent();
        eventsInformation=new EventsInformation();
        loginid=inten.getStringExtra("loginId");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef= storage.getReference();
        eventname=(EditText)findViewById(R.id.eventname);
        eventtime=findViewById(R.id.eventtime);
        eventvenue=findViewById(R.id.eventvenue);
        eventDesc=(EditText)findViewById(R.id.eventDescription);
        date=findViewById(R.id.date);
        organiser=(EditText)findViewById(R.id.organiser);
        contact=(EditText)findViewById(R.id.contact);
        workshop=findViewById(R.id.workshop);
        seminar=findViewById(R.id.seminar);
        competition=findViewById(R.id.competition);
        cultural=findViewById(R.id.cultural);
        sports=findViewById(R.id.sports);
        webinar=findViewById(R.id.webminar);
        eventid=findViewById(R.id.eventid);
        final ArrayList<Society> societyArrayList= new ArrayList<Society>();
        ArrayAdapter<String> aA=new ArrayAdapter<String>(Main4Activity.this,android.R.layout.simple_spinner_dropdown_item,schools);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        schoolspinner=findViewById(R.id.schoolspinner);
        societyspinner=findViewById(R.id.societyspinner);
        but=findViewById(R.id.up);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap;
                Intent intent=new Intent(Main4Activity.this,Upload.class);
                startActivityForResult(intent,1);
            }
        });
        schoolspinner.setAdapter(aA);
        schoolspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSchool=parent.getItemAtPosition(position).toString();
                if(selectedSchool.equals("Choose School"))
                {
                    societyspinner.setVisibility(View.INVISIBLE);
                }
                else {
                    societyspinner.setVisibility(View.VISIBLE);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot q :dataSnapshot.child("Society").getChildren())
                            {
                                if(q.getValue(Society.class).getSchool()!=null)
                                {
                                    if(q.getValue(Society.class).getSchool().equals(selectedSchool))
                                    {
                                        societyArrayList.add(q.getValue(Society.class));
                                    }

                                }
                            }
                            society=new String[societyArrayList.size()];
                            for (int i=0;i<societyArrayList.size();i++)
                            {
                                society[i]=societyArrayList.get(i).getSocietyName();
                            }
                            societyArrayList.clear();
                            ArrayAdapter<String> aB=new ArrayAdapter<String>(Main4Activity.this,android.R.layout.simple_spinner_dropdown_item,society);
                            societyspinner.setAdapter(aB);
                            societyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedsociety=parent.getItemAtPosition(position).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit=findViewById(R.id.submit);
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
        workshop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                sworkshop=radioButton.getText().toString();

            }
        });
        seminar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                sseminar=radioButton.getText().toString();

            }
        });
        competition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                scompetition=radioButton.getText().toString();

            }


        });
        sports.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                ssports=radioButton.getText().toString();

            }


        });
        cultural.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                scultural=radioButton.getText().toString();

            }


        });
        webinar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                swebinar=radioButton.getText().toString();

            }


        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                if(eve==null)
                                {
                                    Toast.makeText(Main4Activity.this, "Search some event to update", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    eve.setEventName(eventname.getText().toString());
                                    eve.setEventDescription(eventDesc.getText().toString());
                                    eve.setDate(date.getText().toString());
                                    eve.setOrganiser(organiser.getText().toString());
                                    eve.setContact(contact.getText().toString());
                                    eve.setTime(eventtime.getText().toString());
                                    eve.setVenue(eventvenue.getText().toString());
                                    mDatabase.child("EventsDetails").child(eventid.getText().toString()).setValue(eve);
                                    Intent intent=new Intent(Main4Activity.this,Main2Activity.class);
                                    Toast.makeText(Main4Activity.this, "Data posted succesfully", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
             }

        });
    }

    public void showOnDialogClick(View View){
        dated=findViewById(R.id.dated);

        dated.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        showDialog(Dialog_id);
                    }
                }
        );
    }

    protected Dialog onCreateDialog(int id){
        if(id == Dialog_id) {
            dialog = new DatePickerDialog(Main4Activity.this, dpickerListener, year_x, month_x, day_x);
        }
        dialog.getDatePicker().setMinDate(a);
        return dialog;

    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            =new DatePickerDialog.OnDateSetListener() {
        long a=System.currentTimeMillis()-1000;
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            year_x=year;
            month_x=month+1;
            day_x=dayOfMonth;
            String d=""+day_x;
            String m=""+month_x;
            if(day_x<10)
            {
                d="0"+day_x;
            }
            if(month_x<10)
            {
                m="0"+month_x;
            }
            s=d+"/"+m+"/"+year_x;
            date.setText(s);
            s="";
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                byteArray = data.getByteArrayExtra("image");
            }
        }
    }
    public void onSearchClick(View v)
    {

        if(eventid.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter valid Event id", Toast.LENGTH_SHORT).show();
        }
        else {
            Query query=mDatabase.child("EventsDetails").orderByChild("eventid").equalTo(eventid.getText().toString());
            Toast.makeText(this, eventid.getText().toString(), Toast.LENGTH_SHORT).show();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        for(DataSnapshot usersnapshot:dataSnapshot.getChildren())
                        {
                            eve=usersnapshot.getValue(EventsInformation.class);
                            if(eve.getLoginid().equals(loginid))
                            {
                                eventname.setText(eve.getEventName());
                                eventDesc.setText(eve.getEventDescription());
                                date.setText(eve.getDate());
                                organiser.setText(eve.getOrganiser());
                                contact.setText(eve.getContact());
                                eventtime.setText(eve.getTime());
                                eventvenue.setText(eve.getVenue());
                            }
                            else {
                                Toast.makeText(Main4Activity.this, "You dont have the permission to edit this event", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        Toast.makeText(Main4Activity.this, "User id doesnot exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
