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
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivityNewPost;
import com.example.pedro.westudy.R;

import java.util.ArrayList;
import java.util.Collections;

import objects.AdapterPost;
import objects.Post;
import statics.DatabaseHelper;

public class ActivityStudentCoursePosts extends AppCompatActivity {
    private final String LOG_TAG = ActivityMain.LOG_TAG_prefix + this.getClass().getSimpleName();
    public static boolean updatePending = false;
    public static boolean courseLeft = false;

    // current selected course
    public static String currentCourse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title of the course
        setTitle(currentCourse);

        // floating action button for adding new post
        FloatingActionButton fab = findViewById(R.id.activity_student_course_fabNewPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Open view for new post.");
                Intent NewPost = new Intent(getBaseContext(), ActivityNewPost.class);
                startActivity(NewPost);
            }
        });

        // make list adapter
        final ArrayList<Post> myPosts = DatabaseHelper.Course.getPosts();
        Collections.sort(myPosts, Collections.<Post>reverseOrder());

        AdapterPost adapter = new AdapterPost(this, myPosts);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_course_lvPosts);
        listView.setAdapter(adapter);

        // set list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Opening comments from post: " + myPosts.get(position).title);

                // set current post
                ActivityStudentPostComments.currentPost = myPosts.get(position);

                // open new activity
                Intent myComments = new Intent(getBaseContext(), ActivityStudentPostComments.class);
                startActivity(myComments);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if user logged out
        if (ActivityMain.bolLogOut) {
            Log.d(LOG_TAG, "Logged out, redirect to previous activity");
            finish();
        } else if (courseLeft) {
            Log.d(LOG_TAG, "Course left, redirect to previous activity (home)");

            // reset static field
            courseLeft = false;

            finish();
        } else if (updatePending) {
            // refresh activity if updates here
            finish();
            startActivity(getIntent());

            updatePending = false;
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

            // select leave course
            case R.id.menu_item_leave:
                Log.d(LOG_TAG, "User tries to leave the course.");
                Intent mySettings = new Intent(this, ActivityStudentCourseConfirmLeaving.class);
                startActivity(mySettings);
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(LOG_TAG, "User logging out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}