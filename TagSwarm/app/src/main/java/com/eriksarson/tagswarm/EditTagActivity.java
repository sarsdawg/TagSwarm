package com.eriksarson.tagswarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class EditTagActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.eriksarson.tagswarm.REPLY";
    public static final String EDIT_KEY = "com.eriksarson.tagswarm.EDIT";
    public static final String EDIT_VALUE = "edit";
    static final int CAM_REQUEST = 1;

    private EditText tagNameEdit, tagDescriptionEdit;

    private TextView latitudeView, longitudeView, timeView;

    private String latString = new String();
    private String lonString = new String();
    private String timeString = new String();
    private String pictureString = new String();

    ImageView imageView;
    File newFile;
    Button pictureButton;

    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_tag);

        tagNameEdit = findViewById(R.id.eedit_name);
        tagDescriptionEdit = findViewById(R.id.eedit_description);

        latitudeView = findViewById(R.id.elatitude);
        longitudeView = findViewById(R.id.elongitude);
        timeView = findViewById(R.id.etime);
        imageView = findViewById(R.id.eimage_view);

        Bundle data = getIntent().getExtras();
        tag = (Tag) data.getParcelable(Tag.TAG_EXTRA);

        latString = tag.getLatitude();
        lonString = tag.getLongitude();
        timeString = tag.getTime();
        pictureString = tag.getPictureName();

        tagNameEdit.setText(tag.getName());
        tagDescriptionEdit.setText(tag.getDescription());
        setTextView(latitudeView, getString(R.string.latitude_empty), latString);
        setTextView(longitudeView, getString(R.string.longitude_empty), lonString);
        setTextView(timeView, getString(R.string.time_empty), timeString);


        final Button gpsButton = findViewById(R.id.ebutton_gps);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();
                if (l != null){
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    latString = Double.toString(lat);
                    latitudeView.setText(getString(R.string.latitude_empty) + " " + latString);
                    lonString = Double.toString(lon);
                    longitudeView.setText(getString(R.string.longitude_empty) + " " + lonString);
                    Date currentTime = Calendar.getInstance().getTime();
                    timeString = currentTime.toString(); // Use DateFormat.parse(String s) to return a Date
                    timeView.setText(getString(R.string.time_empty) + " " + timeString);
                }
            }
        });

        final File file = getFile(pictureString);
        if(!file.exists()){
            Toast.makeText(EditTagActivity.this, "File does not exist.", Toast.LENGTH_SHORT).show();
        } else {
            Drawable d = Drawable.createFromPath(file.getAbsolutePath());
            imageView.setImageDrawable(d);
        }

        pictureButton = findViewById(R.id.ebutton_picture);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                newFile = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });

        final Button saveChangeButton = findViewById(R.id.ebutton_save);
        saveChangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent updateIntent = new Intent(EditTagActivity.this, ViewTagActivity.class);
                if (TextUtils.isEmpty(tagNameEdit.getText())) {
                    Toast.makeText(EditTagActivity.this, R.string.empty_not_saved,
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    tag.setName(tagNameEdit.getText().toString());
                    tag.setDescription(tagDescriptionEdit.getText().toString());
                    tag.setLatitude(latString);
                    tag.setLongitude(lonString);
                    tag.setTime(timeString);
                    tag.setPictureName(pictureString);
                    updateIntent.putExtra(EDIT_KEY, EDIT_VALUE);
                    updateIntent.putExtra(Tag.TAG_EXTRA, tag);
                    setResult(RESULT_OK, updateIntent);
                    startActivity(updateIntent);
                }
            }
        });

        final Button cancelButton = findViewById(R.id.ebutton_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, "Test");  // For debug only.
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });


    }

    private File getFile(String fileName){
        File folder = new File(Tag.PICTURE_PATH);
        if(!folder.exists()){
            Toast.makeText(EditTagActivity.this, "File folder does not exist.", Toast.LENGTH_SHORT).show();
        }
        File image_file = new File(folder, fileName);
        return image_file;
    }

    private File getFile(){
        File folder = new File(Tag.PICTURE_PATH);
        if(!folder.exists()){
            folder.mkdir();
        }
        // This sets the name of the picture to the number of milliseconds since Jan 1, 1970 00:00:00
        pictureString = String.valueOf(Calendar.getInstance().getTime().getTime()) + Tag.PICTURE_EXT;
        File image_file = new File(folder, pictureString);
        return image_file;
    }

    private void setTextView(TextView t, String title, String value){
        String field = title + " " + value;
        if (value != null){
            t.setText(field);
        } else t.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Drawable d = Drawable.createFromPath(newFile.getAbsolutePath());
        if (d == null){
            pictureButton.setText("null");
        }
        imageView.setImageDrawable(d);
        int pad = (int) getResources().getDimension(R.dimen.small_padding);
        imageView.setPadding(0,0,0, pad);
    }
}
