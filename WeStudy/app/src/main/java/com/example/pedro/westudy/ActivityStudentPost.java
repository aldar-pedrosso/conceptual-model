package com.example.pedro.westudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Aldar on 19-Nov-17.
 */

public class ActivityStudentPost extends AppCompatActivity {
    private final String LOG_TAG = ActivityMain.LOG_TAG_prefix + this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("New post for " + ActivityStudentCourse.currentCourse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id){
            // Add post
            case R.id.menu_item_add_post:
                Log.d(LOG_TAG, "Post added");
                Toast.makeText(getBaseContext(), "Post added", Toast.LENGTH_SHORT).show();
                Intent intentCourse = new Intent(this, ActivityStudentCourse.class);
                startActivity(intentCourse);
                break;

            // Return to home
            case R.id.menu_item_home:
                Log.d(LOG_TAG, "Returned to home");
                Toast.makeText(getBaseContext(), "Returned to home", Toast.LENGTH_SHORT).show();
                Intent intentHome = new Intent(this, ActivityStudentHome.class);
                startActivity(intentHome);
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(LOG_TAG, "User logging out.");
                Toast.makeText(getBaseContext(), "Logging out", Toast.LENGTH_SHORT).show();

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(), "Cancelling a new post", Toast.LENGTH_SHORT).show();
        Intent MyHome = new Intent(this, ActivityStudentCourse.class);
        startActivity(MyHome);
    }
}
