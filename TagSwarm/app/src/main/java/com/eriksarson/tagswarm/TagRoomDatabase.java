package com.eriksarson.tagswarm;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Tag.class}, version = 1)
public abstract class TagRoomDatabase extends RoomDatabase {

    public abstract TagDao tagDao();

    private static volatile TagRoomDatabase INSTANCE;

    static TagRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TagRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TagRoomDatabase.class, "tag_database").addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
         new RoomDatabase.Callback() {

         @Override
         public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            //new PopulateDbAsync(INSTANCE).execute();
         }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TagDao mDao;

        PopulateDbAsync(TagRoomDatabase db) {
            mDao = db.tagDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Tag tag = new Tag();
            tag.setName("My First Tag");
            mDao.insert(tag);
            return null;
        }
    }
}

