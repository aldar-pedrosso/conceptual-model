package com.example.pedro.westudy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pedro.westudy.school.ActivitySchoolHome;
import com.example.pedro.westudy.student.ActivityStudentHome;
import com.example.pedro.westudy.teacher.ActivityTeacherHome;

import statics.DatabaseHelper;

import static statics.DatabaseHelper.currentUser;

public class ActivityMain extends AppCompatActivity {
    // ---------------------------------------------------- Options for testing!!!
    // todo: show easy login or not
    boolean bolShowEasyLogin = true;
    // ----------------------------------------------------

    // global variables
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final String TAG_prefix = "EASY-DEBUG ";

    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    // check if 'log-out' was pressed
    public static boolean bolLogOut = false;

    // normal controls on screen
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    // special controls on screen
    RelativeLayout rllEasyLogin;
    ImageButton btnReset;
    ImageButton btnStudent;
    ImageButton btnTeacher;
    ImageButton btnSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the database
        DatabaseHelper.instantiate(getBaseContext());

        // reset user
        DatabaseHelper.resetCurrentUser();

        // set normal controls
        etUsername = findViewById(R.id.content_main_etUsername);
        etPassword = findViewById(R.id.content_main_etPassword);
        btnLogin = findViewById(R.id.content_main_btnLogin);

        // login button listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        // set special controls for easy login
        rllEasyLogin = findViewById(R.id.content_main_rllEasyLogin);
        btnReset = findViewById(R.id.content_main_btnReset);
        btnStudent = findViewById(R.id.content_main_btnStudent);
        btnTeacher = findViewById(R.id.content_main_btnTeacher);
        btnSchool = findViewById(R.id.content_main_btnSchool);

        // todo: easy login data
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.resetDatabase();
                Log.d(TAG, "Database reset.");
                Toast.makeText(getBaseContext(),"Database has been reset", Toast.LENGTH_SHORT).show();
            }
        });
        // set listeners for easy login
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                etUsername.setText("StudentDefault");
                etPassword.setText("Default");
            }
        });
        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                etUsername.setText("TeacherDefault");
                etPassword.setText("Default");
            }
        });
        btnSchool.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                etUsername.setText("SchoolDefault");
                etPassword.setText("Default");
            }
        });

        // show easy login or not?
        if (bolShowEasyLogin){
            rllEasyLogin.setVisibility(View.VISIBLE);
            btnReset.setVisibility(View.VISIBLE);
        }
        else{
            rllEasyLogin.setVisibility(View.GONE);
            btnReset.setVisibility(View.GONE);
        }
    }

    // if came back to this activity, check if user logged out
    @Override
    protected void onResume() {
        super.onResume();

        // reset login
        if (bolLogOut){
            Log.d(TAG, "Logging out done, resetting data.");

            etUsername.setText("");
            etPassword.setText("");
            bolLogOut = false;
        }
    }

    // attempt to log in
    private void checkLogin() {
        if (DatabaseHelper.tryLogin(etUsername.getText().toString(), etPassword.getText().toString())){
            Intent MyHome;

            // check user & set home
            switch (currentUser.Rank){
                case Student:
                    // say hello
                    Log.d(TAG, "Recognized as student");
                    Toast.makeText(ActivityMain.this, "Welcome " + currentUser.Username, Toast.LENGTH_SHORT).show();

                    // go to home
                    MyHome = new Intent(this, ActivityStudentHome.class);
                    startActivity(MyHome);
                    break;

                case Teacher:
                    Log.d(TAG, "Recognized as teacher");
                    Toast.makeText(ActivityMain.this, "Welcome " + currentUser.Username, Toast.LENGTH_SHORT).show();

                    // go to home
                    MyHome = new Intent(this, ActivityTeacherHome.class);
                    startActivity(MyHome);
                    break;

                case School:
                    // say hello
                    Log.d(TAG, "Recognized as school");
                    Toast.makeText(this.getBaseContext(), "Welcome " + currentUser.Username, Toast.LENGTH_SHORT).show();
                    // go to home
                    MyHome = new Intent(this, ActivitySchoolHome.class);
                    startActivity(MyHome);
                    break;
            }
        }
        else {
            // wrong user
            Toast.makeText(this.getBaseContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
