package com.example.administrator.designpatterns;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 1. Created by 850302 on 2016/4/29.
 */
public class Students implements Parcelable {

    private int id;
    private String name;
    private String className;
    private int age;


    protected Students(Parcel in) {
        id = in.readInt();
        name = in.readString();
        className = in.readString();
        age = in.readInt();
    }

    public Students(int id, String name, String className, int age) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.age = age;
    }

    public Students() {
    }

    public static final Creator<Students> CREATOR = new Creator<Students>() {
        @Override
        public Students createFromParcel(Parcel in) {
            return new Students(in);
        }

        @Override
        public Students[] newArray(int size) {
            return new Students[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(className);
        dest.writeInt(age);
    }

    public void readFromParcel(Parcel source) {
        id = source.readInt();
        name = source.readString();
        className = source.readString();
        age = source.readInt();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Students{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", age=" + age +
                '}';
    }
}