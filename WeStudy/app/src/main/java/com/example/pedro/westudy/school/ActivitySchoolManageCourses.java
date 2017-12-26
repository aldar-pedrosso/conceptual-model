package com.example.pedro.westudy.school;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pedro.westudy.ActivityJoinCourse;
import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.R;
import com.example.pedro.westudy.student.ActivityStudentHome;

import java.util.ArrayList;

import objects.AdapterCourse;
import objects.AdapterCourseDelete;
import objects.RunnableSmart;
import statics.DatabaseHelper;

import static android.content.ContentValues.TAG;

public class ActivitySchoolManageCourses extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    EditText etFilter;

    static public boolean filterChanged = true;
    boolean isDead = false;
    RunnableSmart filterChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_manage_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title
        setTitle("Course list");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // experiment with this
                // https://stackoverflow.com/a/10904665

                // make custom textfield for dialog window
                final EditText etDialogText = new EditText(ActivitySchoolManageCourses.this);
                etDialogText.setHint("Course name");
                etDialogText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

                new AlertDialog.Builder(ActivitySchoolManageCourses.this)
                        .setTitle("New course!")
                        .setMessage("Please enter the name of the new course:")
                        .setIcon(R.drawable.ic_add_course)
                        .setView(etDialogText)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = etDialogText.getText().toString();

                                if (input.isEmpty())
                                    Toast.makeText(ActivitySchoolManageCourses.this, "Please enter a name before trying to add a new course.", Toast.LENGTH_LONG).show();
                                else {
                                    if (DatabaseHelper.School.addCourse(input))
                                        Toast.makeText(ActivitySchoolManageCourses.this, input + " is added to the course list!", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(ActivitySchoolManageCourses.this, input + " already exists, so it couldn't be added again", Toast.LENGTH_LONG).show();
                                }

                                // notify filter
                                filterChanged = true;
                            }
                        })
                        .create().show();
            }
        });


        // set listview to adapter
        final ListView listView = findViewById(R.id.activity_school_manage_courses_lvCourses);

        // make background checker
        filterChecker = new RunnableSmart() {
            @Override
            public void run() {
                Log.d(TAG, "thread made & started");

                // run while activity alive
                while (!isDead) {
                    Log.d(TAG, "thread refresh");

                    // check if filter changed
                    if (filterChanged) {
                        filterChanged = false;

                        // make list adapter
                        final ArrayList<String> myCourses = DatabaseHelper.School.getFilteredCourses(etFilter.getText().toString());
                        final AdapterCourseDelete adapter = new AdapterCourseDelete(getBaseContext(), myCourses);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);
                            }
                        });
                    } else {
                        Log.d(TAG, "thread wait");

                        // wait for max 1sec
                        waitSmart(1000);
                    }
                }

                Log.d(TAG, "Background checker dies");
            }
        };
        new Thread(filterChecker).start();

        // set filter
        etFilter = findViewById(R.id.activity_school_manage_courses_etFilter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterChanged = true;
                filterChecker.notifySmart();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // check if user logged out
        if (ActivityMain.bolLogOut) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDead = true;
    }
}
