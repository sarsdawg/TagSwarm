package com.eriksarson.tagswarm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class TagViewModel extends AndroidViewModel {

    private TagRepository repository;

    private LiveData<List<Tag>> allTags;

    public TagViewModel(Application application) {
        super(application);
        repository = new TagRepository(application);
        allTags = repository.getAllTags();
    }

    LiveData<List<Tag>> getAllTags() { return allTags; }

    public void insert(Tag tag) { repository.insert(tag); }

    public void update(Tag tag) { repository.update(tag); }

    public void delete(Tag tag) { repository.delete(tag); }
}
