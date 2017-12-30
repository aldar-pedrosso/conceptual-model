package com.example.pedro.westudy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pedro.westudy.student.ActivityStudentHome;
import com.example.pedro.westudy.teacher.ActivityTeacherHome;

import java.util.ArrayList;
import java.util.Collections;

import enums.UserRank;
import objects.AdapterPost;
import objects.Post;
import statics.DatabaseHelper;

import static statics.DatabaseHelper.currentUser;

public class ActivityCoursePosts extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();
    public static boolean updatePending = false;
    public static boolean courseLeft = false;

    // current selected course
    public static String currentCourse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title of the course
        setTitle(currentCourse);

        // floating action button for adding new post
        FloatingActionButton fab = findViewById(R.id.activity_student_course_fabNewPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Open view for new post.");
                Intent NewPost = new Intent(getBaseContext(), ActivityNewPost.class);
                startActivity(NewPost);
            }
        });

        // make list adapter
        final ArrayList<Post> myPosts = DatabaseHelper.Course.getPosts();

        AdapterPost adapter = new AdapterPost(this, myPosts);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_course_posts_lvPosts);
        listView.setAdapter(adapter);

        // set list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Opening comments from post: " + myPosts.get(position).title);

                // set current post
                ActivityPostComments.currentPost = myPosts.get(position);

                // open new activity
                Intent myComments = new Intent(getBaseContext(), ActivityPostComments.class);
                startActivity(myComments);
            }
        });

        // extra action for teachers
        if (currentUser.Rank == UserRank.Teacher)
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Log.d(TAG, "User tries to delete post about: " + myPosts.get(position).title);

                    // make a dialog window
                    new AlertDialog.Builder(ActivityCoursePosts.this)
                            .setTitle("Delete post?")
                            .setMessage("User: " + myPosts.get(position).user.Username + "\n" +
                                    "Time posted: " + myPosts.get(position).timePosted + "\n\n" +
                                    "Title: \n" +
                                    myPosts.get(position).title)
                            .setIcon(R.drawable.ic_warning)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "User pressed 'no', the post will not be deleted.");
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "User pressed 'yes', and thus the post will be deleted.");
                                    Toast.makeText(getBaseContext(), "Post deleted", Toast.LENGTH_SHORT).show();

                                    // delete post
                                    DatabaseHelper.Course.deletePost(myPosts.get(position).title,myPosts.get(position).user.Username,myPosts.get(position).timePosted);

                                    // reload
                                    updatePending = true;
                                    ActivityTeacherHome.updatePending = true;
                                    onResume();
                                }
                            })
                            .create().show();
                    return true;
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
        } else if (courseLeft) {
            onCourseLeft();
        } else if (updatePending) {
            // refresh activity if updates here
            finish();
            startActivity(getIntent());

            updatePending = false;
        }
    }

    /**
     * Action when the user left the current course
     */
    private void onCourseLeft() {
        DatabaseHelper.Course.leave();

        Log.d(TAG, "Course left, redirect to previous activity (home)");
        ActivityStudentHome.updatePending = true;
        ActivityTeacherHome.updatePending = true;
        ActivityTeacherHome.updatePending = true;

        // reset static field
        courseLeft = false;

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id) {

            // select leave course
            case R.id.menu_item_leave:
                Log.d(TAG, "User tries to leave the course.");

                // make a dialog window
                new AlertDialog.Builder(this)
                        .setTitle("Leaving course?")
                        .setMessage("Are you sure you want to leave the current course? \n\n" + currentCourse)
                        .setIcon(R.drawable.ic_warning)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "User pressed 'no', and will not leave the course.");
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "User pressed 'yes', and thus will leave the course.");
                                Toast.makeText(getBaseContext(), "Course left", Toast.LENGTH_SHORT).show();
                                onCourseLeft();
                            }
                        })
                        .create().show();

                /*
                Intent mySettings = new Intent(this, ActivityStudentCourseConfirmLeaving.class);
                startActivity(mySettings);
                */
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
}