package com.example.pedro.westudy.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivityNewComment;
import com.example.pedro.westudy.R;

import java.util.ArrayList;

import objects.AdapterComment;
import objects.Comment;
import objects.Post;
import statics.DatabaseHelper;

public class ActivityStudentPostComments extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();
    public static boolean updatePending = false;

    // current selected post
    public static Post currentPost = null;

    // menu controls
    Switch requestToggle = null;
    MenuItem requestLogoOn = null;
    MenuItem requestLogoOff = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title of the course
        setTitle(currentPost.title);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.activity_student_post_comments_fabNewComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Opening activity for new comment");
                Intent myNewActivity = new Intent(getBaseContext(), ActivityNewComment.class);
                startActivity(myNewActivity);
            }
        });

        // get comments
        final ArrayList<Comment> myComments = DatabaseHelper.Post.getComments();

        // check content from post creator also
        Comment originalPost = new Comment();
        originalPost.content = currentPost.content;
        originalPost.user = currentPost.user;
        originalPost.time = currentPost.timePosted;

        // add post content to comment list
        myComments.add(0, originalPost);

        // make list adapter
        AdapterComment adapter = new AdapterComment(this, myComments);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_comments_lvComments);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Resume activated");

        // check if user logged out
        if (ActivityMain.bolLogOut){
            Log.d(TAG, "Logged out, redirect to previous activity");
            finish();
        }
        else {
            // refresh activity if updates here
            if (updatePending){
                finish();
                startActivity(getIntent());

                updatePending = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_comments, menu);

        // set menu controls
        MenuItem myItem = menu.findItem(R.id.menu_item_requestToggle);
        requestToggle = myItem.getActionView().findViewById(R.id.menu_switch);
        requestToggle.setChecked(currentPost.requested);

        requestLogoOn = menu.findItem(R.id.menu_item_requestLogoOn);
        requestLogoOff = menu.findItem(R.id.menu_item_requestLogoOff);

        requestToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRequest(!currentPost.requested);
            }
        });

        // set initial toggle
        changeRequest(currentPost.requested);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id) {
            case R.id.menu_item_requestToggle:
                // switch toggle
                changeRequest(!requestToggle.isChecked());
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(TAG, "User loggin out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeRequest(Boolean newValue){
        Log.d(TAG, "Change requested to: " + newValue);

        // change menu controls
        requestLogoOn.setVisible(newValue);
        requestLogoOff.setVisible(!newValue);

        // change local data
        currentPost.requested = newValue;

        // update database
        DatabaseHelper.Post.updateRequest();

        // toggle update in activity with posts
        ActivityStudentCoursePosts.updatePending = true;
    }
}
