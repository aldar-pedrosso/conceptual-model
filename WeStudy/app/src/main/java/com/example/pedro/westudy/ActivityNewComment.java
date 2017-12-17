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
import com.example.pedro.westudy.student.ActivityStudentPostComments;

import statics.DatabaseHelper;


public class ActivityNewComment extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    // controls
    TextView etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title
        setTitle("New comment");

        // set controls
        etContent = findViewById(R.id.activity_new_comment_etContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id) {
            // Add comment
            case R.id.menu_item_done:
                // update database
                DatabaseHelper.Post.addComment(etContent.getText().toString());
                Log.d(TAG, "New Comment added");

                Toast.makeText(getBaseContext(), "Comment added", Toast.LENGTH_SHORT).show();

                ActivityStudentCoursePosts.updatePending = true;
                ActivityStudentPostComments.updatePending = true;
                finish();
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
        Toast.makeText(getBaseContext(), "Cancelling new comment", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Cancelling new comment.");
        finish();
    }
}
