package com.example.myapplication.Models;

public class DoctorsFeed {

    String id;
    String name;
    String photoId;
    String rating;

    public DoctorsFeed(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    String address;

    @Override
    public String toString() {
        return "DoctorsFeed{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photoId='" + photoId + '\'' +
                ", rating='" + rating + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
