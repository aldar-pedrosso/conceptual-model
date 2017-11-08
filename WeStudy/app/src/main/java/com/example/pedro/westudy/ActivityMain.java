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
import android.widget.Toast;

import statics.CurrentUser;
import statics.DatabaseHelper;
import statics.UserRank;

public class ActivityMain extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();

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

        // Instantiate the database
        DatabaseHelper.Instantiate(getBaseContext());

        // reset user
        CurrentUser.reset();

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
        btnStudent = findViewById(R.id.content_main_btnStudent);
        btnTeacher = findViewById(R.id.content_main_btnTeacher);
        btnSchool = findViewById(R.id.content_main_btnSchool);

        // todo new action
        // set listeners for easy login
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("StudentDefault");
                etPassword.setText("Default");
            }
        });
        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("TeacherDefault");
                etPassword.setText("Default");
            }
        });
        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("SchoolDefault");
                etPassword.setText("Default");
            }
        });

        // show easy login or not?
        if (bolShowEasyLogin)
            rllEasyLogin.setVisibility(View.VISIBLE);
        else
            rllEasyLogin.setVisibility(View.GONE);
    }

    // attempt to log in
    private void checkLogin() {
        // todo: new login?
        Intent MyHome = new Intent(this, ActivityStudentHome.class);

        if (DatabaseHelper.tryLogin(etUsername.getText().toString(), etPassword.getText().toString())){
            // check user & set home
            switch (CurrentUser.user.Rank){
                case Student:
                    MyHome = new Intent(this, ActivityStudentHome.class);
                    break;

                case Teacher:
                    // todo: change to other home
                    MyHome = new Intent(this, ActivityStudentHome.class);
                    break;

                case School:
                    // todo: change to other home
                    MyHome = new Intent(this, ActivityStudentHome.class);
                    break;
            }

            // say hello
            Toast.makeText(this.getBaseContext(), "Welcome " + CurrentUser.user.Username, Toast.LENGTH_SHORT).show();

            // go to home
            startActivity(MyHome);
        }
        else {
            // wrong user
            Toast.makeText(this.getBaseContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
