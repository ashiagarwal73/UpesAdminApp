package com.agarwal.ashi.upesadminapp;
import java.util.Random;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {
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
    String societyid[]=null;
    EditText eventname,eventDesc,organiser,contact,eventtime,eventvenue;
    TextView date;
    RadioGroup workshop,seminar,competition,cultural,sports,webinar;
    Spinner schoolspinner,societyspinner;
    Button submit;
    String selectedsociety;
    String selectedsocietyid;
    String seventdesc;
    String sdate;
    String sorganiser;
    String scontact;
    String selectedSchool="Choose School";
    String schools[]={"Choose School","School of Computer Science","School of Engineering","School of Design","School of Business","School of Law","All Schools"};
    DatabaseReference mDatabase;
    Uri downloadUrl;
    StorageReference storageRef;
    Counter counter;
    String loginid;
    byte[] byteArray;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent inten=getIntent();
        loginid=inten.getStringExtra("loginId");
        FirebaseStorage storage = FirebaseStorage.getInstance();
         storageRef= storage.getReference();
        eventname=(EditText)findViewById(R.id.eventname);
        progressBar=findViewById(R.id.progrss);
        eventDesc=(EditText)findViewById(R.id.eventDescription);
        eventtime=findViewById(R.id.eventtime);
        eventvenue=findViewById(R.id.eventvenue);
        date=findViewById(R.id.date);
        organiser=(EditText)findViewById(R.id.organiser);
        contact=(EditText)findViewById(R.id.contact);
        workshop=findViewById(R.id.workshop);
         seminar=findViewById(R.id.seminar);
         competition=findViewById(R.id.competition);
         cultural=findViewById(R.id.cultural);
        sports=findViewById(R.id.sports);
         webinar=findViewById(R.id.webminar);
        final ArrayList<Society> societyArrayList= new ArrayList<Society>();
        ArrayAdapter<String> aA=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,schools);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        schoolspinner=findViewById(R.id.schoolspinner);
        societyspinner=findViewById(R.id.societyspinner);
        but=findViewById(R.id.up);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap;
                Intent intent=new Intent(MainActivity.this,Upload.class);
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
                           ArrayAdapter<String> aB=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,society);
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
                if(eventname.getText().toString().equals("")||eventDesc.getText().toString().equals("")||s.equals("")||organiser.getText().toString().equals("")||contact.getText().toString().equals("")||contact.getText().toString().equals("")||sworkshop.equals("")||sseminar.equals("")||scompetition.equals("")||scultural.equals("")||swebinar.equals("")||selectedSchool.equals("")||eventtime.getText().toString().equals("")||eventvenue.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "Enter all Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final EventsInformation eventsInformation = new EventsInformation();
                    counter=new Counter();
                    count--;
                    counter.setCounterid(count);
                    mDatabase.child("Counter").setValue(counter);
                    eventsInformation.setEventName(eventname.getText().toString());
                    eventsInformation.setEventDescription(eventDesc.getText().toString());
                   // sdate=date.getText().toString();
                    eventsInformation.setDate(s);
                    sorganiser=organiser.getText().toString();
                    eventsInformation.setOrganiser(sorganiser);
                    scontact=contact.getText().toString();
                    eventsInformation.setContact(scontact);
                    eventsInformation.setWorkshop(sworkshop);
                    eventsInformation.setSeminar(sseminar);
                    eventsInformation.setCompetition(scompetition);
                    eventsInformation.setCultural(scultural);
                    eventsInformation.setSports(ssports);
                    eventsInformation.setWebminar(swebinar);
                    eventsInformation.setSchool(selectedSchool);
                    eventsInformation.setSociety(selectedsociety);
                    eventsInformation.setVenue(eventvenue.getText().toString());
                    eventsInformation.setTime(eventtime.getText().toString());
                    //Bitmap bmp;
                    StorageReference imagesRef = storageRef.child(eventsInformation.getEventName()+".png");
                    if(byteArray==null)
                    {
                        Toast.makeText(MainActivity.this, "Upload image", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        //bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                UploadTask uploadTask = imagesRef.putBytes(byteArray);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                downloadUrl = taskSnapshot.getDownloadUrl();
                                //Toast.makeText(MainActivity.this, ""+downloadUrl, Toast.LENGTH_SHORT).show();
                                eventsInformation.setImage(downloadUrl.toString());
                                eventsInformation.setLoginid(loginid);
                                eventsInformation.setEventid(counter.getCounterid()+"");
                                mDatabase.child("EventsDetails").child(counter.getCounterid() + "").setValue(eventsInformation);
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                Toast.makeText(MainActivity.this, "Data posted succesfully", Toast.LENGTH_SHORT).show();
                                intent.putExtra("eventid",counter.getCounterid()+"");
                                startActivity(intent);
                            }
                        });
                    }
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
            dialog = new DatePickerDialog(MainActivity.this, dpickerListener, year_x, month_x, day_x);
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
}