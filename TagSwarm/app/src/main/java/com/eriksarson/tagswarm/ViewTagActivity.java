package com.eriksarson.tagswarm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ViewTagActivity extends AppCompatActivity {

    public static final String DELETE_KEY = "com.eriksarson.tagswarm.DELETE";
    public static final String DELETE_VALUE = "delete";
    public static final String UPDATE_KEY = "com.eriksarson.tagswarm.UPDATE";
    public static final String UPDATE_VALUE = "update";

    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag);

        final TextView tagNameView = findViewById(R.id.vtag_name);
        final TextView tagDescriptionView = findViewById(R.id.vtag_description);
        final TextView latitudeView = findViewById(R.id.vlatitude);
        final TextView longitudeView = findViewById(R.id.vlongitude);
        final TextView timeView = findViewById(R.id.vtime);
        final TextView userNameView = findViewById(R.id.vusername);
        final ImageView imageView = findViewById(R.id.vimage_view);

        Bundle data = getIntent().getExtras();
        tag = (Tag) data.getParcelable(Tag.TAG_EXTRA);

        setTextView(tagNameView, getString(R.string.tag_name), tag.getName());
        setTextView(tagDescriptionView, getString(R.string.tag_description), tag.getDescription());
        setTextView(latitudeView, getString(R.string.latitude_empty), tag.getLatitude());
        setTextView(longitudeView, getString(R.string.longitude_empty), tag.getLongitude());
        setTextView(timeView, getString(R.string.time_empty), tag.getTime());
        setTextView(userNameView, getString(R.string.username_empty), tag.getUserName());

        File file = getFile(tag.getPictureName());
        if(!file.exists()){
            Toast.makeText(ViewTagActivity.this, "File does not exist.", Toast.LENGTH_SHORT).show();
        } else {
            Drawable d = Drawable.createFromPath(file.getAbsolutePath());
            imageView.setImageDrawable(d);
        }

        final Button editButton = findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent editIntent = new Intent(ViewTagActivity.this, EditTagActivity.class);
                editIntent.putExtra(Tag.TAG_EXTRA, tag);
                startActivity(editIntent);
            }
        });

        final Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent deleteIntent = new Intent(ViewTagActivity.this, MainActivity.class);
                deleteIntent.putExtra(Tag.TAG_EXTRA, tag);
                deleteIntent.putExtra(DELETE_KEY, DELETE_VALUE);
                startActivity(deleteIntent);
            }
        });

        final Button returnButton = findViewById(R.id.button_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent(ViewTagActivity.this, MainActivity.class);
                replyIntent.putExtra(Tag.TAG_EXTRA, tag);
                replyIntent.putExtra(UPDATE_KEY, UPDATE_VALUE);
                startActivity(replyIntent);
            }
        });

    }

    private File getFile(String fileName){
        File folder = new File(Tag.PICTURE_PATH);
        if(!folder.exists()){
            Toast.makeText(ViewTagActivity.this, "File folder does not exist.", Toast.LENGTH_SHORT).show();
        }
        File image_file = new File(folder, fileName);
        return image_file;
    }

    private void setTextView(TextView t, String title, String value){
        String field = title + " " + value;
        if (value != null){
            t.setText(field);
        } else t.setText(title);
    }

}
