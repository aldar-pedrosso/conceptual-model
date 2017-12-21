package com.example.pedro.westudy.school;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivitySettings;
import com.example.pedro.westudy.R;
import com.example.pedro.westudy.student.ActivityStudentHome;

public class ActivitySchoolHome extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set home
        setTitle("School home");

        // set controls
        ImageButton imgNewStudent = findViewById(R.id.activity_school_home_imgNewStudent);
        imgNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewStudentClicked();
            }
        });
        Button btnNewStudent = findViewById(R.id.activity_school_home_btnNewStudent);
        btnNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewStudentClicked();
            }
        });


        ImageButton imgNewTeacher = findViewById(R.id.activity_school_home_imgNewTeacher);
        imgNewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewTeacherClicked();
            }
        });
        Button btnNewTeacher = findViewById(R.id.activity_school_home_btnNewTeacher);
        btnNewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewTeacherClicked();
            }
        });

        Button btnManageCourses = findViewById(R.id.activity_school_home_btnManageCourses);
        btnManageCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked 'manage courses'");
            }
        });
    }

    private void onNewStudentClicked(){
        Log.d(TAG, "Recognized as student");
        Intent MyHome = new Intent(this, ActivityStudentHome.class);
    }
    private void onNewTeacherClicked(){
        Log.d(TAG, "Clicked 'new teacher'");
    }


    @Override
    protected void onResume() {
        super.onResume();

        // check if user logged out
        if (ActivityMain.bolLogOut){
            Log.d(TAG, "Logged out, redirect to previous activity");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id){
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
