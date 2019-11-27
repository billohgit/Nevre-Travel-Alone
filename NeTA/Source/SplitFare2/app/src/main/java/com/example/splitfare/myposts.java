package com.example.splitfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/// Posts class is used for defining myPosts activity
/// This activity is used to show user his/her own posts
/// From this activity user can check comments on his/her posts

public class myposts extends AppCompatActivity {

    List<Post> my_posts;
    ListView listViewmyPosts;
    DatabaseReference databasemyposts;
    FirebaseUser curuser;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myposts);
        toolbar = (Toolbar) findViewById(R.id.toolbarmp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databasemyposts = FirebaseDatabase.getInstance().getReference().child("posts");
        listViewmyPosts = (ListView) findViewById(R.id.mypostslist);
        my_posts = new ArrayList<>();
        curuser = FirebaseAuth.getInstance().getCurrentUser();
        String curuid = curuser.getUid();
        databasemyposts.child(curuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    my_posts.add(post);
                }
                PostList mypostListAdapter = new PostList(myposts.this, my_posts);
                listViewmyPosts.setAdapter(mypostListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /// Method defining Action to be taken when back button is pressed
    @Override
    public void onBackPressed() {

        Intent intToHome = new Intent(myposts.this,Home.class);
        startActivity(intToHome);

    }
}
