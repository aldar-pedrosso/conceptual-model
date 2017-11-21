package com.example.pedro.westudy.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivitySettings;
import com.example.pedro.westudy.R;

/**
 * Created by Aldar on 21-Nov-17.
 */

public class ActivityStudentConfirmLeaving extends AppCompatActivity {

    private final String LOG_TAG = ActivityMain.LOG_TAG_prefix + this.getClass().getSimpleName();

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
                Toast.makeText(getBaseContext(), "Confirm leaving", Toast.LENGTH_SHORT).show();

                Intent Courses = new Intent(getBaseContext(), ActivityStudentCourse.class);
                startActivity(Courses);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Cancel leaving", Toast.LENGTH_SHORT).show();

                Intent Courses = new Intent(getBaseContext(), ActivityStudentCourse.class);
                startActivity(Courses);
            }
        });



        txtLabel.setText("Are you sure you want to leave '" + ActivityStudentCourse.currentCourse + "'?");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // menu actions
        switch (id){

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(LOG_TAG, "User logging out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
