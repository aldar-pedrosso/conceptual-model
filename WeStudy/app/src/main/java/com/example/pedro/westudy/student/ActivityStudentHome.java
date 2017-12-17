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

import com.example.pedro.westudy.ActivityJoinCourse;
import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivitySettings;
import com.example.pedro.westudy.R;

import java.util.ArrayList;

import objects.AdapterCourse;
import statics.DatabaseHelper;

public class ActivityStudentHome extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();
    public static boolean updatePending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.activity_student_home_fabNewCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Open activity for adding new course");

                Intent newIntent = new Intent(getBaseContext(), ActivityJoinCourse.class);
                startActivity(newIntent);
            }
        });

        // make list adapter
        final ArrayList<String> myCourses = DatabaseHelper.User.getCourses();
        AdapterCourse adapter = new AdapterCourse(this, myCourses);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_home_lvCourses);
        listView.setAdapter(adapter);

        // set list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Open posts of the course '" + myCourses.get(position) + "'");

                ActivityStudentCoursePosts.currentCourse = myCourses.get(position);
                Intent ChosenCourse = new Intent(getBaseContext(), ActivityStudentCoursePosts.class);
                startActivity(ChosenCourse);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if user logged out
        if (ActivityMain.bolLogOut){
            Log.d(TAG, "Logged out, redirect to previous activity");
            finish();
        }
        else if (updatePending) {
            // refresh activity if updates here
            finish();
            startActivity(getIntent());

            updatePending = false;
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
                Log.d(TAG, "Opening settings");
                Intent mySettings = new Intent(this, ActivitySettings.class);
                startActivity(mySettings);
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(TAG, "User logging out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // don't allow to go back to login activity
        // super.onBackPressed();
    }
}