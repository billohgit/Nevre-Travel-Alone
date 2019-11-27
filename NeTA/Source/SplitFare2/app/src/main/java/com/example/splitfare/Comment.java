package com.example.splitfare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/// This class defines what has to be done when user enters a comment to a post
/// This activity is invoked when user clicks to open a particular post
/// User replies to a post will be stored on to the database
public class Comment extends AppCompatActivity {


    /// TextView variables for storing name,source,destination,mode of transport,mobile number of user
    TextView name,source,dest,mot,nop,mob,date,time,uid;
    /// Button variable for listening when user enters a comment
    Button comm;
    /// EditText variable for user to enter the reply
    EditText reply;
    /// ArrayList which stores all comments on a particular post
    ArrayList<String> commentlist;
    /// ArrayAdapter which is used to display comments in ListView
    ArrayAdapter<String> commadapter;
    /// Database variable for storing Firebase Database Reference
    private DatabaseReference databaseposts;
    /// ListView to display the comments of users
    ListView commlv;
    /// Fiberbase user variable to extract information about user
    FirebaseUser user;
    /// Toolbar variable to display toolbar at top
    Toolbar toolbar;

    /// OnCreate method is the first method that executes when a acitivity is opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// Setting layout for the activity
        setContentView(R.layout.activity_comment);
        /// Setting Toolbar layout and back button for it
        toolbar = (Toolbar) findViewById(R.id.toolbarcv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reply to Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /// Extracting the post object which has been passed to this activity
        Intent i = getIntent();
        final Post post = (Post)i.getSerializableExtra("myObject");
        /// Assiging id elements to all the variables mentioned above
        name = (TextView) findViewById(R.id.textView2);
        source = (TextView) findViewById(R.id.textView3);
        dest = (TextView) findViewById(R.id.textView4);
        mot = (TextView) findViewById(R.id.textView5);
        nop = (TextView) findViewById(R.id.textView10);
        mob = (TextView) findViewById(R.id.textView11);
        date = (TextView) findViewById(R.id.textView12);
        time = (TextView) findViewById(R.id.textView13);
        uid = (TextView) findViewById(R.id.textView14);
        comm = (Button) findViewById(R.id.tvbutton);
        reply = (EditText) findViewById(R.id.tvreply);
        commlv = (ListView) findViewById(R.id.tvpostslist);
        /// Getting posts instance from the firebase database
        databaseposts = FirebaseDatabase.getInstance().getReference("posts");
        /// Changing values of all editText Variables
        name.setText("Name\t\t\t\t\t  "+post.getName());
        source.setText("Source\t\t\t\t"+post.getSource());
        dest.setText("Destination\t\t\t\t "+post.getDestination());
        mot.setText("ModeofTransport\t\t\t "+post.getMoT());
        nop.setText("No of persons Needed\t" + post.getPersons());
        date.setText("Date\t\t\t\t\t  "+post.getDate());
        time.setText("Time\t\t\t\t\t  "+post.getTime());
        mob.setText("Mobile\t\t\t\t\t"+post.getMobile());
        uid.setText("User id\t\t\t\t   "+post.getUseremail());

        /// Getting the list of comments for this particular post
        commentlist = post.getList();
        commadapter = new ArrayAdapter<String>(this,R.layout.list_comment,R.id.lvcomment,commentlist);
        /// Setting adapter for listview
        commlv.setAdapter(commadapter);
        /// Dispalying updated values in the listView
        commadapter.notifyDataSetChanged();
        //Log.d("commentsarray", Arrays.toString(commentlist.toArray()));

        /// OnCLick Listener for taking action when user enters the comment
        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String ipreply = user.getEmail() +"\n"+ reply.getText().toString().trim() ;
                if (!TextUtils.isEmpty(ipreply)) {
                    ArrayList<String> temp = post.getList();

                    temp.add(ipreply);
                    post.setlist(temp);
                    databaseposts.child(post.getUid()).child(post.getPid()).setValue(post);
                    commadapter.notifyDataSetChanged();

                }else {
                    //if the value is not given displaying a toast
                    Toast.makeText(Comment.this, "Please enter reply", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /// Method defining Action to be taken when back button is pressed
    @Override
    public void onBackPressed() {

        Intent intToHome = new Intent(Comment.this,myposts.class);
        startActivity(intToHome);

    }
}
