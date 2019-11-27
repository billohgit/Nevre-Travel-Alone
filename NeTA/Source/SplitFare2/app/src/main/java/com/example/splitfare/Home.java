package com.example.splitfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/// This class defines the Home Activity
/// This Activity consists of all posts done by other users
/// This activity contains navigation drawer to go to addposts,logout and my posts activities
public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Button btnLogout,btnAddpost;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;
    ListView listViewPosts;
    DatabaseReference databaseposts,databaseusers;
    List<Post> posts;
    List<String> users;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseUser curuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        //btnLogout = findViewById(R.id.logout);
       // btnAddpost = findViewById(R.id.homeaddpost);
        databaseusers = FirebaseDatabase.getInstance().getReference().child("users");
        databaseposts = FirebaseDatabase.getInstance().getReference().child("posts");
        listViewPosts = (ListView) findViewById(R.id.postslist);
        posts = new ArrayList<>();
        users = new ArrayList<>();

        /*btnAddpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToHome = new Intent(Home.this,AddpostaActivity.class);
                startActivity(intToHome);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(Home.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });*/
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getValue(String.class);
                    Log.d("user",uid);
                    users.add(uid);
                }
                curuser = FirebaseAuth.getInstance().getCurrentUser();
                String curuid = curuser.getUid();
                Log.d("current uid",curuid);
                //Log.d("current email",curuser.getEmail());
                posts.clear();
                for(String id: users) {
                    if(!(id.equals(curuid)))
                    {
                        Log.d("infor", id);
                        databaseposts.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Post post = postSnapshot.getValue(Post.class);
                                    posts.add(post);
                                }
                                PostList postListAdapter = new PostList(Home.this, posts);
                                listViewPosts.setAdapter(postListAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id){
            case R.id.addpost_id:
                Intent intToHome = new Intent(Home.this,AddpostaActivity.class);
                startActivity(intToHome);
                break;

            case R.id.logout_id:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(Home.this, LoginActivity.class);
                startActivity(intToMain);
                break;

            case R.id.myposts_id:
                Intent intTomyposts = new Intent(Home.this,myposts.class);
                startActivity(intTomyposts);
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /// Method defining Action to be taken when back button is pressed
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
