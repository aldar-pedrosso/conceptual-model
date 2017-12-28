package com.example.pedro.westudy.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivityCoursePosts;
import com.example.pedro.westudy.R;

/**
 * Created by Aldar on 21-Nov-17.
 */

public class ActivityStudentCourseConfirmLeaving extends AppCompatActivity {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    TextView txtLabel;

    Button btnYes;
    Button btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_confirm_leaving);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Leaving course");

        txtLabel = findViewById(R.id.confirm_leaving_lblAreYouSure);

        btnYes = findViewById(R.id.confirm_leaving_btnYes);
        btnNo = findViewById(R.id.confirm_leaving_btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCoursePosts.courseLeft = true;

                Toast.makeText(getBaseContext(), "Course left", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Cancel leaving", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        txtLabel.setText("Are you sure you want to leave '" + ActivityCoursePosts.currentCourse + "'?");
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
        switch (id){

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
