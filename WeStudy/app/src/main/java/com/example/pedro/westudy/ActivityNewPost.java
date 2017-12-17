package com.example.pedro.westudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedro.westudy.student.ActivityStudentCoursePosts;

import enums.UserRank;
import statics.DatabaseHelper;

import static statics.DatabaseHelper.currentUser;

/**
 * Created by Aldar on 19-Nov-17.
 */

public class ActivityNewPost extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    private boolean hidden = false;
    private boolean requested = false;
    private boolean pinned = false;

    // controls
    TextView etTitle, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title
        setTitle("New post");

        // set controls
        etTitle = findViewById(R.id.activity_new_post_etTitle);
        etContent = findViewById(R.id.activity_new_post_etContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);

        // get pinned item
        MenuItem itemPinned = menu.findItem(R.id.menu_item_pinned);
        MenuItem itemHidden = menu.findItem(R.id.menu_item_hidden);

        // set visibility for pinned
        if (currentUser.Rank == UserRank.Teacher){
            itemPinned.setVisible(true);
            itemHidden.setVisible(false);
        }
        else{
            itemPinned.setVisible(false);
            itemHidden.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id){
            // Add post
            case R.id.menu_item_done:
                // update database
                DatabaseHelper.Course.addPost(etTitle.getText().toString(), etContent.getText().toString(), hidden, requested, pinned);
                Log.d(TAG, "New post added");

                Toast.makeText(getBaseContext(), "Post added", Toast.LENGTH_SHORT).show();

                ActivityStudentCoursePosts.updatePending = true;
                finish();
                break;

            // toggle hidden
            case R.id.menu_item_hidden:
                hidden = !hidden;
                Log.d(TAG, "Changed option hidden to: " + hidden);
                item.setChecked(hidden);
                break;

            // toggle hidden
            case R.id.menu_item_requested:
                requested = !requested;
                Log.d(TAG, "Changed option requested to: " + requested);
                item.setChecked(requested);
                break;

            // toggle hidden
            case R.id.menu_item_pinned:
                pinned = !pinned;
                Log.d(TAG, "Changed option pinned to: " + pinned);
                item.setChecked(pinned);
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(TAG, "User logging out.");
                Toast.makeText(getBaseContext(), "Logging out", Toast.LENGTH_SHORT).show();

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(), "Cancelling new post", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Cancelling new post.");
        finish();
    }
}
