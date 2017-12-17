package com.example.pedro.westudy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pedro.westudy.student.ActivityStudentHome;

import java.util.ArrayList;

import objects.AdapterCourse;
import objects.RunnableSmart;
import statics.DatabaseHelper;

public class ActivityJoinCourse extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    EditText etFilter;

    boolean filterChanged = true, isDead = false;
    RunnableSmart filterChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title
        setTitle("Join a new course");

        // set listview to adapter
        final ListView listView = findViewById(R.id.activity_join_course_lvCourses);

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
                        final ArrayList<String> myCourses = DatabaseHelper.User.getNewFilteredCourses(etFilter.getText().toString());
                        final AdapterCourse adapter = new AdapterCourse(getBaseContext(), myCourses);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);

                                // set list item click listener
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final String chosenCourse = myCourses.get(position);
                                        Log.d(TAG, "Join course '" + chosenCourse + "'?");

                                        // make a dialog window
                                        new AlertDialog.Builder(ActivityJoinCourse.this)
                                                .setTitle("Join course?")
                                                .setMessage("Do you want to join this course? \n\n" + chosenCourse)
                                                .setIcon(R.drawable.ic_add_course)
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Log.d(TAG, "User pressed 'no', and will not join the course.");
                                                    }
                                                })
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Log.d(TAG, "User pressed 'yes', and thus will join the course.");
                                                        Toast.makeText(getBaseContext(), "Course joined", Toast.LENGTH_SHORT).show();

                                                        DatabaseHelper.User.addCourse(chosenCourse);

                                                        ActivityStudentHome.updatePending = true;
                                                        finish();
                                                    }
                                                })
                                                .create().show();
                                    }
                                });
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
        etFilter = findViewById(R.id.activity_join_course_etFilter);
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

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Cancel joining a course", Toast.LENGTH_SHORT).show();
        finish();
    }
}
