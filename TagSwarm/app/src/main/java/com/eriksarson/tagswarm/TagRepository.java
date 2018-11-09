package com.eriksarson.tagswarm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TagRepository {

    private TagDao tagDao;
    private LiveData<List<Tag>> allTags;

    TagRepository(Application application) {
        TagRoomDatabase db = TagRoomDatabase.getDatabase(application);
        tagDao = db.tagDao();
        allTags = tagDao.getAllTags();
    }

    LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

    public void insert (Tag tag) {
        new insertAsyncTask(tagDao).execute(tag);
    }

    private static class insertAsyncTask extends AsyncTask<Tag, Void, Void> {
        private TagDao asyncTaskDao;
        insertAsyncTask(TagDao dao) {
            asyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Tag... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update (Tag tag) { new updateAsyncTask(tagDao).execute(tag); }

    private static class updateAsyncTask extends AsyncTask<Tag, Void, Void> {
        private TagDao asyncTaskDao;
        updateAsyncTask(TagDao dao) { asyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final Tag... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    public void delete (Tag tag) { new deleteAsyncTask(tagDao).execute(tag); }

    private static class deleteAsyncTask extends AsyncTask<Tag, Void, Void> {
        private TagDao asyncTaskDao;
        deleteAsyncTask(TagDao dao) { asyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final Tag... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
