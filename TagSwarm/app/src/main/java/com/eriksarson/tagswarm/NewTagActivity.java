package com.eriksarson.tagswarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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

public class NewTagActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.eriksarson.tagswarm.REPLY";


    private EditText editName, editDescription;

    String phoneIDString = new String();
    String latString = new String();
    String lonString = new String();
    String timeString = new String();
    String pictureString = new String();

    static final int CAM_REQUEST = 1;
    Button pictureButton;
    ImageView imageView;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        //final TextView phoneID = findViewById(R.id.phone_id);  // For debug

        Context context = getApplicationContext();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Unable to read phone ID.  Permission not granted.", Toast.LENGTH_SHORT).show();
        } else try {
            phoneIDString = telephonyManager.getDeviceId();
        } catch (Exception e) {}

        //phoneID.setText(getString(R.string.phone_id_empty) + " " + phoneIDString);  // For debug

        editName = findViewById(R.id.edit_name);
        editDescription = findViewById(R.id.edit_description);

        final Button gpsButton = findViewById(R.id.button_gps);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();
                if (l != null){
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    //Toast.makeText(getApplicationContext(), "LAT: "+lat+"\n LON: "+lon, Toast.LENGTH_LONG).show();
                    final TextView latitude = findViewById(R.id.latitude);
                    latString = Double.toString(lat);
                    latitude.setText(getString(R.string.latitude_empty) + " " + latString);
                    final TextView longitude = findViewById(R.id.longitude);
                    lonString = Double.toString(lon);
                    longitude.setText(getString(R.string.longitude_empty) + " " + lonString);

                    final TextView timeView = findViewById(R.id.time);
                    Date currentTime = Calendar.getInstance().getTime();
                    timeString = currentTime.toString(); // Use DateFormat.parse(String s) to return a Date
                    timeView.setText(getString(R.string.time_empty) + " " + timeString);
                }
            }
        });

        pictureButton = findViewById(R.id.button_picture);
        imageView = findViewById(R.id.image_view);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });

        final Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = editName.getText().toString();
                    String description = editDescription.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, new String[] {name,description,latString,
                                                                    lonString,timeString,phoneIDString,
                                                                    pictureString});
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Drawable d = Drawable.createFromPath(file.getAbsolutePath());
        if (d == null){
            pictureButton.setText("null");
        }
        imageView.setImageDrawable(d);
        int pad = (int) getResources().getDimension(R.dimen.small_padding);
        imageView.setPadding(0,0,0, pad);
    }
}
