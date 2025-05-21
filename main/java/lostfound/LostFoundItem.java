package com.example.lostfound;

import android.os.Parcel;
import android.os.Parcelable;

public class LostFoundItem implements Parcelable {
    private String title;
    private double latitude;
    private double longitude;
    private String status;

    public LostFoundItem(String title, String status, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    protected LostFoundItem(Parcel in) {
        title = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        status = in.readString();
    }

    public static final Creator<LostFoundItem> CREATOR = new Creator<LostFoundItem>() {
        @Override
        public LostFoundItem createFromParcel(Parcel in) {
            return new LostFoundItem(in);
        }

        @Override
        public LostFoundItem[] newArray(int size) {
            return new LostFoundItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

