package com.example.myapplication.Models;

public class DoctorsFeed {

    String address;
    String name;
    String photoId;
    String rating;

    public DoctorsFeed( String name,String address,String photoId) {
        this.address = address;
        this.name = name;
        this.photoId=photoId;
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

    @Override
    public String toString() {
        return "DoctorsFeed{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", photoId='" + photoId + '\'' +
                ", rating='" + rating + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
