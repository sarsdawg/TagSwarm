package com.eriksarson.tagswarm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.List;


public class MainActivity extends AppCompatActivity
                          implements UsernameDialogFragment.UsernameDialogListener {

    private String userName = User.getInstance().getUserName();

    private Tag tag;

    public void showUsernameDialog() {
        DialogFragment dialog = new UsernameDialogFragment();
        dialog.show(getSupportFragmentManager(), "UsernameDialogFragment");
    }

    // The following may be redundant after applying singleton pattern
    @Override
    public void applyUserName(String s){
        if (s == null || s.length() == 0){
            Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show();
            showUsernameDialog();
        }
        userName = s;
        Toast.makeText(getApplicationContext(), "Username is set to " + userName, Toast.LENGTH_LONG).show();
    }

    public static final int NEW_TAG_ACTIVITY_REQUEST_CODE = 1;
    private TagViewModel tagViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TagListAdapter adapter = new TagListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        tagViewModel = ViewModelProviders.of(this).get(TagViewModel.class);

        // This is sloppy below, but was the only way I could get it to work for now.
        Bundle data;
        try {
            data = getIntent().getExtras();
            if (data.get(ViewTagActivity.DELETE_KEY).toString().equals(ViewTagActivity.DELETE_VALUE)) {
                tag = (Tag) data.getParcelable(Tag.TAG_EXTRA);
                Toast.makeText(getApplicationContext(), tag.getName() + " deleted", Toast.LENGTH_LONG).show(); // For debug.
                tagViewModel.delete(tag);
            }
        } catch(Exception e){}

        try {
            data = getIntent().getExtras();
            if (data.get(ViewTagActivity.UPDATE_KEY).toString().equals(ViewTagActivity.UPDATE_VALUE)) {
                tag = (Tag) data.getParcelable(Tag.TAG_EXTRA);
                Toast.makeText(getApplicationContext(), tag.getName() + " updated", Toast.LENGTH_LONG).show(); // For debug.
                tagViewModel.update(tag);
            }
        } catch(Exception e){}

        // Add an observer on the LiveData returned
        tagViewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
           @Override
           public void onChanged(@Nullable final List<Tag> tags) {
               // Update the cached copy of the words in the adapter.
               adapter.setTags(tags);
           }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTagActivity.class);
                startActivityForResult(intent, NEW_TAG_ACTIVITY_REQUEST_CODE);
            }
        });

        if (userName == null || userName.length() == 0) showUsernameDialog();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TAG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Tag newTag = new Tag();
            String[] replies = data.getStringArrayExtra(NewTagActivity.EXTRA_REPLY);
            newTag.setName(replies[0]);
            newTag.setDescription(replies[1]);
            newTag.setLatitude(replies[2]);
            newTag.setLongitude(replies[3]);
            newTag.setTime(replies[4]);
            newTag.setDeviceID(replies[5]);
            newTag.setPictureName(replies[6]);
            newTag.setUserName(userName);
            tagViewModel.insert(newTag);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
