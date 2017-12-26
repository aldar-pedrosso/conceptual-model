package com.example.pedro.westudy.school;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.R;

import statics.DatabaseHelper;


// Used both for new student and new teacher
public class ActivitySchoolNewPerson extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    public static boolean isNewStudent = true;   // i.e. not new teacher

    EditText etUsername, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_new_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title
        if (isNewStudent)
            setTitle("Add a new student");
        else
            setTitle("Add a new teacher");

        // remember controls
        etUsername = findViewById(R.id.activity_school_new_person_etUsername);
        etEmail = findViewById(R.id.activity_school_new_person_etEmail);

        // set submit button
        Button btnSubmit = findViewById(R.id.activity_school_new_person_btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please enter the username & email before submitting", Toast.LENGTH_SHORT).show();
                } else {
                    if (DatabaseHelper.School.addPerson(etUsername.getText().toString(), isNewStudent)) {
                        // set title
                        String usedTitle;
                        if (isNewStudent)
                            usedTitle = "Student";
                        else
                            usedTitle = "Teacher";

                        // give feedback
                        Log.d(TAG, "New " + usedTitle + " added with username: " + etUsername.getText().toString());
                        Toast.makeText(getBaseContext(), usedTitle + " " + etUsername.getText().toString() + " is added to the system. \n" +
                                "A new random password is generated and sent to " + etEmail.getText().toString(), Toast.LENGTH_LONG).show();

                        finish();
                    } else {
                        Log.d(TAG, "Tried to add user, but username already in use");
                        Toast.makeText(getBaseContext(), "Username is already used, please choose another one", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
        switch (id) {
            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(TAG, "User logging out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
