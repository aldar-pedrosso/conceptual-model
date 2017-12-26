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
import android.widget.Toast;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.R;
import com.example.pedro.westudy.student.ActivityStudentPostComments;


// Used both for new student and new teacher
public class ActivitySchoolNewPerson extends AppCompatActivity {

    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    public static boolean isNewStudent = true;   // i.e. not new teacher

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_new_person);

        makeToolbar();

        Button btnSubmit = findViewById(R.id.activity_new_person_btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submitted a new person");
                Toast.makeText(getBaseContext(), "Submitted a new person", Toast.LENGTH_SHORT).show();
                Intent intentSchoolHome = new Intent(getBaseContext(), ActivitySchoolHome.class);
                startActivity(intentSchoolHome);
            }
        });
    }

    private void makeToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (isNewStudent)
            toolbar.setTitle("Add a new student");
        else
            toolbar.setTitle("Add a new teacher");

        setSupportActionBar(toolbar);
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
