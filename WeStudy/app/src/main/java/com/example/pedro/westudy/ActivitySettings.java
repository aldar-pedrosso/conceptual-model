package com.example.pedro.westudy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import statics.DatabaseHelper;

import static statics.DatabaseHelper.CurrentUser;

public class ActivitySettings extends AppCompatActivity {
    private final String LOG_TAG = ActivityMain.LOG_TAG_prefix +  this.getClass().getSimpleName();
    private final int SELECT_PHOTO = 1;

    ImageView imgAvatar;
    EditText etPassword1, etPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // screen objects
        imgAvatar = findViewById(R.id.activity_settings_imgAvatar);
        etPassword1 = findViewById(R.id.activity_settings_etPassword1);
        etPassword2 = findViewById(R.id.activity_settings_etPassword2);

        // get current avatar, if any
        // with help from https://stackoverflow.com/a/9357943
        if (CurrentUser.Avatar != null){
            imgAvatar.setImageBitmap(BitmapFactory.decodeByteArray(CurrentUser.Avatar, 0, CurrentUser.Avatar.length));
        }

        // Avatar button action
        Button btnNewAvatar = findViewById(R.id.activity_settings_btnNewAvatar);
        btnNewAvatar.setOnClickListener(new View.OnClickListener() {
            // with help from https://stackoverflow.com/a/36128088
            @Override
            public void onClick(View v) {
                // check android version
                if (Build.VERSION.SDK_INT >= 23){
                    // check permission ok
                    if (ContextCompat.checkSelfPermission(getBaseContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Log.d(LOG_TAG, "Asking for permission for READ_EXTERNAL_STORAGE");

                        // request permission
                        ActivityCompat.requestPermissions(ActivitySettings.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                ActivityMain.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }else{
                        ActivityCompat.requestPermissions(ActivitySettings.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                ActivityMain.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
            }
        });

        // change password button
        Button btnChangePassword = findViewById(R.id.activity_settings_btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = etPassword1.getText().toString();
                String pass2 = etPassword2.getText().toString();

                Log.d(LOG_TAG, "Try to change password.");

                // check if passwords are equal
                if (pass1.equals(pass2)){
                    // password are equal
                    Log.d(LOG_TAG, "2 given passwords are equal, try to update in database.");

                    // update database
                    DatabaseHelper.UpdateUser.changePassword(pass1);

                    Toast.makeText(getBaseContext(),"Password is updated.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // passwords are not equal
                    Log.d(LOG_TAG, "2 given passwords are not equal, no changes are made.");
                    Toast.makeText(getBaseContext(),"The 2 given password are not the same.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ActivityMain.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            // set new avatar
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // gain image data
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        // set avatar on screen
                        imgAvatar.setImageBitmap(selectedImage);

                        // save avatar to database
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                        DatabaseHelper.UpdateUser.changeAvatar(outputStream.toByteArray());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            // close settings
            case R.id.menu_item_done:
                finish();
                break;

            // flag logout & close
            case R.id.menu_item_logout:
                Log.d(LOG_TAG, "User loggin out.");

                ActivityMain.bolLogOut = true;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
