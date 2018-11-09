package com.eriksarson.tagswarm;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "tag_table")
public class Tag implements Parcelable {

    public static final String TAG_EXTRA = "tag";
    public static final String PICTURE_PATH = "sdcard/tagswarm";
    public static final String PICTURE_EXT = ".jpg";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String name;
    private String description;
    private String pictureName;
    private String latitude;
    private String longitude;
    private String time;
    private String deviceID;
    private String userName;

    @NonNull
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTime() {
        return time;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(description);
        out.writeString(pictureName);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(time);
        out.writeString(deviceID);
        out.writeString(userName);
    }

    public static final Parcelable.Creator<Tag> CREATOR
            = new Parcelable.Creator<Tag>() {
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    private Tag(Parcel in){
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        pictureName = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        time = in.readString();
        deviceID = in.readString();
        userName = in.readString();
    }

    public Tag(){
        // It was necessary to add this public constructor to get around a compiler error.
    }
}
