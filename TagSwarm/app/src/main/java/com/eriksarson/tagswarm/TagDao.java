package com.eriksarson.tagswarm;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Tag tag);

    @Query("DELETE FROM tag_table")
    void deleteAll();

    @Query("SELECT * from tag_table ORDER BY name ASC")
    LiveData<List<Tag>> getAllTags();

    @Update
    void update(Tag tag);

    @Delete
    void delete(Tag tag);
}
