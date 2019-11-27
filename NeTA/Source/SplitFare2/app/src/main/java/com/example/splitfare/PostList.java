package com.example.splitfare;


import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

import java.util.List;

/// This is a helper class which helps in retrieving values from Firebase and displaying it on the activity from which it is called
/// This class is beinng used in Home Activity and Myposts activity
/// This takes the post object given to it and then sets the Texviews values of layout to source and destination values of post

public class PostList extends ArrayAdapter<Post> {

    private Activity context;
    List<Post> posts;
    String bclr,tclr;
    int count=0;

    /// Constructor to initiliase context and post object
    public PostList(Activity context, List<Post> artists) {
        super(context, R.layout.list_layout, artists);
        this.context = context;
        this.posts = artists;
    }

    /// This method sets the textviews of ListView to appropriate values
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        if(count%2==0){
            bclr = "#CDDC49";
            tclr = "#FFFFFF";
        }else {
            bclr = "#0097A7";
            tclr = "#000000";
        }


        //Name.setTextColor(Color.parseColor(tclr));
        TextView Source = (TextView) listViewItem.findViewById(R.id.lsource);
        Source.setBackgroundColor(Color.parseColor(bclr));
        //Source.
        TextView Destination = (TextView) listViewItem.findViewById(R.id.ldestination);
        Destination.setBackgroundColor(Color.parseColor(bclr));
        Button open = (Button) listViewItem.findViewById(R.id.lbutton);

        final Post post = posts.get(position);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToComment = new Intent(context,Comment.class);
                intToComment.putExtra("myObject", post);
                context.startActivity(intToComment);
            }
        });

        Source.setText("Source    "+post.getSource());
        Destination.setText("Destination    "+post.getDestination());


        count++;
        return listViewItem;
    }

}
