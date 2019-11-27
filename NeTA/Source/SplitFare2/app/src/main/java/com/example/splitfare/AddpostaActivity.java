package com.example.splitfare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


/// Class defining the Add Post Activity
/// This activity is invoked when user wants to add post
/// In this Activity we are taking input from user and storing the posted information onto the database
public class AddpostaActivity extends AppCompatActivity {


    /// EditText variables for storing name,source,destination,mode of transport,mobile number of user
    private EditText name,source,destination,MoT,persons,Mobile;
    /// Button variable to listen to Addpost,Set Time and Set Date events
    private Button btnaddPost,btnDate,btnTime;
    /// Strings to store date and time values
    private String Date,Time;
    /// Database Reference to add posts of users
    private DatabaseReference databaseposts;
    /// Date Picker event listener
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    /// Object of class post
    private Post post;
    /// Calendar Variable to get Date
    Calendar cal;
    /// curHour and curMin are to store values of hours and minutes
    int curHour,curMin;
    /// Toolbar variable to display toolbar at top
    Toolbar toolbar;
    /// Fiberbase user variable to extract information about user
    FirebaseUser user;

    /// OnCreate method is the first method that executes when a acitivity is opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// Setting layout for the activity
        setContentView(R.layout.activity_addposta);
        /// Setting Toolbar layout and back button for it
        toolbar = (Toolbar) findViewById(R.id.toolbarap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /// Assiging id elements to all the variables mentioned above
        name = (EditText) findViewById(R.id.name);
        source = (EditText) findViewById(R.id.source);
        destination = (EditText) findViewById(R.id.destination);
        MoT = (EditText) findViewById(R.id.transportmode);
        persons = (EditText) findViewById(R.id.SeatsAvailable);
        Mobile = (EditText) findViewById(R.id.Mobile);
        btnaddPost = (Button) findViewById(R.id.addpost);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnTime = (Button) findViewById(R.id.btnTime);
        //btnview = (Button) findViewById(R.id.viewpost);
        post = new Post();
        /// Getting posts instance from the firebase database
        databaseposts = FirebaseDatabase.getInstance().getReference("posts");
        /// OnClick listener for Setting Date
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddpostaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                //Log.d(TAG,"onDateSet: "+y+"/"+m+"/"+d);
                m=m+1;
                Date = d + "/" + m + "/" + y;
                btnDate.setText(Date);
            }
        };
        /// OnClick listener for Setting Time
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal=Calendar.getInstance();
                curHour=cal.get(Calendar.HOUR_OF_DAY);
                curMin=cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddpostaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        /*String ap;
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                            ap = "AM";
                        } else if (hourOfDay == 12) {
                            ap = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                            ap = "PM";
                        } else {
                            ap = "AM";
                        }*/
                        Time=String.format("%02d:%02d", hourOfDay, minutes);
                        btnTime.setText(Time);
                        //selectTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + ap);
                    }
                }, curHour, curMin, false);

                timePickerDialog.show();
            }
        });

        /// OnCLick lisener for storing a post to database
        btnaddPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String ipname = name.getText().toString().trim();
                String ipsource = source.getText().toString().trim();
                String ipdest = destination.getText().toString().trim();
                String ipmot = MoT.getText().toString().trim();
                String ippersons = persons.getText().toString().trim();
                String ipmob = Mobile.getText().toString().trim();

                if (!TextUtils.isEmpty(ipname) && !TextUtils.isEmpty(ipsource) && !TextUtils.isEmpty(ipdest) && !TextUtils.isEmpty(ipmot) && !TextUtils.isEmpty(ippersons) && !TextUtils.isEmpty(ipmob) && !TextUtils.isEmpty(Date) && !TextUtils.isEmpty(Time) ) {
                    post.setName(ipname);
                    post.setSource(ipsource);
                    post.setDestination(ipdest);
                    post.setMoT(ipmot);
                    post.setPersons(Integer.parseInt(ippersons));
                    post.setMobile(ipmob);
                    post.setDate(Date);
                    post.setTime(Time);
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    post.setUid(uid);
                    String pid = databaseposts.push().getKey();
                    post.setPid(pid);
                    post.setUseremail(user.getEmail());
                    databaseposts.child(uid).child(pid).setValue(post);
                    Toast.makeText(AddpostaActivity.this, "Post Added", Toast.LENGTH_LONG).show();
                    Intent intToHome = new Intent(AddpostaActivity.this,Home.class);
                    startActivity(intToHome);

                } else {
                    //if the value is not given displaying a toast
                    Toast.makeText(AddpostaActivity.this, "Please enter all details", Toast.LENGTH_LONG).show();
                }
            }


        });


        /*btnview.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intView = new Intent(AddpostaActivity.this, Home.class);
                startActivity(intView);

            }

        });*/

    }

    /// Method defining Action to be taken when back button is pressed
    @Override
    public void onBackPressed() {

        Intent intToHome = new Intent(AddpostaActivity.this,Home.class);
        startActivity(intToHome);

    }
}
