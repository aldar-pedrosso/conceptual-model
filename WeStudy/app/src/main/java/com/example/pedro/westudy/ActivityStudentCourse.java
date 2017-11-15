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

import objects.AdapterCourse;
import objects.AdapterPost;
import objects.Post;
import statics.DatabaseHelper;

public class ActivityStudentCourse extends AppCompatActivity {
    private final String LOG_TAG = ActivityMain.LOG_TAG_prefix + this.getClass().getSimpleName();

    // current selected course
    public static String currentCourse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.activity_student_course_fabNewPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Action for adding new post", Toast.LENGTH_SHORT).show();
            }
        });



        // make list adapter
        final ArrayList<Post> myPosts = DatabaseHelper.Course.getPosts();
        AdapterPost adapter = new AdapterPost(this, myPosts);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_course_lvPosts);
        listView.setAdapter(adapter);

        // set list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Open posts of the course '" + myPosts.get(position) + "'");

                Toast.makeText(getBaseContext(), myPosts.get(position).title + ", is clicked.", Toast.LENGTH_SHORT).show();
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id){
            // open settings
            case R.id.menu_item_settings:
                Log.d(LOG_TAG, "Opening settings");
                Intent MyHome = new Intent(this, ActivitySettings.class);
                startActivity(MyHome);
                break;

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
