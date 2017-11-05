package com.example.pedro.westudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import statics.UserRank;
import statics.User;

public class ActivityMain extends AppCompatActivity {
    // ---------------------------------------------------- Options for testing!!!
    // todo: show easy login or not
    boolean bolShowEasyLogin = true;
    // ----------------------------------------------------

    // normal controls on screen
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    // special controls on screen
    RelativeLayout rllEasyLogin;
    ImageButton btnStudent;
    ImageButton btnTeacher;
    ImageButton btnSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // reset user
        User.reset();

        // set normal controls
        etUsername = findViewById(R.id.content_main_etUsername);
        etPassword = findViewById(R.id.content_main_etPassword);
        btnLogin = findViewById(R.id.content_main_btnLogin);

        // login button listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });

        // set special controls for easy login
        rllEasyLogin = findViewById(R.id.content_main_rllEasyLogin);
        btnStudent = findViewById(R.id.content_main_btnStudent);
        btnTeacher = findViewById(R.id.content_main_btnTeacher);
        btnSchool = findViewById(R.id.content_main_btnSchool);

        // set listeners for easy login
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("Student");
            }
        });
        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("Teacher");
            }
        });
        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("School");
            }
        });

        // show easy login or not?
        if (bolShowEasyLogin)
            rllEasyLogin.setVisibility(View.VISIBLE);
        else
            rllEasyLogin.setVisibility(View.GONE);
    }

    // attempt to log in
    private void tryLogin(){
        User.username = etUsername.getText().toString().trim().toLowerCase();
        Intent MyHome = new Intent(this, ActivityStudentHome.class);

        // check user & set home
        if (User.username.equals("student")){
            User.rank = UserRank.Student;
            MyHome = new Intent(this, ActivityStudentHome.class);
        }
        else if (User.username.equals("teacher")){
            User.rank = UserRank.Teacher;
            btnLogin.setText(User.username);
        }
        else if (User.username.equals("school")){
            User.rank = UserRank.School;
            btnLogin.setText(User.username);
        }
        else {
            //wrong user
            User.reset();
        }

        // go to next activity
        if (User.username != null)
            startActivity(MyHome);
    }
}
