package com.example.pedro.westudy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Switch;
import android.widget.Toast;

import com.example.pedro.westudy.teacher.ActivityTeacherHome;

import java.util.ArrayList;

import enums.UserRank;
import objects.AdapterComment;
import objects.Comment;
import objects.Post;
import statics.DatabaseHelper;

import static statics.DatabaseHelper.currentUser;

public class ActivityPostComments extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();
    public static boolean updatePending = false;

    // current selected post
    public static Post currentPost = null;

    // menu controls
    Switch requestToggle;
    MenuItem requestLogoOn;
    MenuItem requestLogoOff;
    MenuItem pinnedToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title of the course
        setTitle(currentPost.title);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.activity_student_post_comments_fabNewComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Opening activity for new comment");
                Intent myNewActivity = new Intent(getBaseContext(), ActivityNewComment.class);
                startActivity(myNewActivity);
            }
        });

        // get comments
        final ArrayList<Comment> myComments = DatabaseHelper.Post.getComments();

        // check content from post creator also
        Comment originalPost = new Comment();
        originalPost.content = currentPost.content;
        originalPost.user = currentPost.user;
        originalPost.time = currentPost.timePosted;

        // add post content to comment list
        myComments.add(0, originalPost);

        // make list adapter
        AdapterComment adapter = new AdapterComment(this, myComments);

        // set listview to adapter
        ListView listView = findViewById(R.id.activity_student_comments_lvComments);
        listView.setAdapter(adapter);

        // extra action for teachers
        if (currentUser.Rank == UserRank.Teacher)
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Log.d(TAG, "User tries to delete post from user: " + myComments.get(position).user.Username + ", at time: " + myComments.get(position).time);

                    // check if a comment was selected (not the post content)
                    if (position == 0)
                        Toast.makeText(getBaseContext(), "Can't delete the original post content", Toast.LENGTH_SHORT).show();
                    else
                        // make a dialog window
                        new AlertDialog.Builder(ActivityPostComments.this)
                                .setTitle("Delete comment?")
                                .setMessage("User: " + myComments.get(position).user.Username + "\n" +
                                        "Time: " + myComments.get(position).time + "\n\n" +
                                        "Content: \n" +
                                        myComments.get(position).content)
                                .setIcon(R.drawable.ic_warning)
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "User pressed 'no', the comment will not be deleted.");
                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "User pressed 'yes', and thus the comment will be deleted.");
                                        Toast.makeText(getBaseContext(), "Comment deleted", Toast.LENGTH_SHORT).show();

                                        // delete comment
                                        DatabaseHelper.Post.deleteComment(myComments.get(position).user.Username, myComments.get(position).time);

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

        Log.d(TAG, "Resume activated");

        // check if user logged out
        if (ActivityMain.bolLogOut) {
            Log.d(TAG, "Logged out, redirect to previous activity");
            finish();
        } else {
            // refresh activity if updates here
            if (updatePending) {
                finish();
                startActivity(getIntent());

                updatePending = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_comments, menu);

        // set request toggle
        MenuItem myItem = menu.findItem(R.id.menu_item_requestToggle);
        requestToggle = myItem.getActionView().findViewById(R.id.menu_switch);
        requestToggle.setChecked(currentPost.requested);

        requestLogoOn = menu.findItem(R.id.menu_item_requestLogoOn);
        requestLogoOff = menu.findItem(R.id.menu_item_requestLogoOff);

        requestToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRequest(!currentPost.requested);
            }
        });

        // set initial toggle
        changeRequest(currentPost.requested);

        // set pinned toggle
        pinnedToggle = menu.findItem(R.id.menu_item_pinnedToggle);
        MenuItem myWhitespace = menu.findItem(R.id.menu_item_logout_whitespace);

        // only teacher may see pinned toggle
        if (currentUser.Rank == UserRank.Teacher){
            pinnedToggle.setVisible(true);
            myWhitespace.setVisible(true);
        }
        else{
            pinnedToggle.setVisible(false);
            myWhitespace.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id) {
            case R.id.menu_item_requestToggle:
                // switch toggle
                changeRequest(!requestToggle.isChecked());
                break;

            case R.id.menu_item_pinnedToggle:
                // switch toggle
                changePinned(!currentPost.pinned);
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(TAG, "User loggin out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changePinned(Boolean newValue){
        Log.d(TAG, "Change pinned to: " + newValue);

        // set correct toggles
        currentPost.pinned = newValue;
        pinnedToggle.setChecked(newValue);

        // update database
        DatabaseHelper.Post.updatePinned();

        // toggle update in activity with posts
        ActivityCoursePosts.updatePending = true;
        ActivityTeacherHome.updatePending = true;
    }

    private void changeRequest(Boolean newValue) {
        Log.d(TAG, "Change requested to: " + newValue);

        // change menu controls
        requestLogoOn.setVisible(newValue);
        requestLogoOff.setVisible(!newValue);

        // change local data
        currentPost.requested = newValue;

        // update database
        DatabaseHelper.Post.updateRequest();

        // toggle update in activity with posts
        ActivityCoursePosts.updatePending = true;
        ActivityTeacherHome.updatePending = true;
    }
}
