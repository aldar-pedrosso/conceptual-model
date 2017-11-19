package com.example.pedro.westudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import objects.AdapterComment;
import objects.AdapterPost;
import objects.Comment;
import objects.Post;
import statics.DatabaseHelper;

public class ActivityStudentComments extends AppCompatActivity {
    private final String LOG_TAG = ActivityMain.LOG_TAG_prefix + this.getClass().getSimpleName();

    // current selected post
    public static String currentPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title of the course
        setTitle(currentPost);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.activity_student_comments_fabNewComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Action for adding new comment", Toast.LENGTH_SHORT).show();
            }
        });

        // make list adapter
        final ArrayList<Comment> myComments = DatabaseHelper.Post.getComments();
        AdapterComment adapter = new AdapterComment(this, myComments);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_comments_lvComments);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if user logged out
        if (ActivityMain.bolLogOut){
            Log.d(LOG_TAG, "Logged out, redirect to previous activity");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id) {

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(LOG_TAG, "User loggin out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
