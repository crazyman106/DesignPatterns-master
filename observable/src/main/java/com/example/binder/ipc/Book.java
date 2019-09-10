package com.example.binder.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/9/10 22:01
 * pkn    : com.example.binder.ipc
 * desc   :
 */

public class Book implements Parcelable {

    private double price;
    private String name;

    protected Book(Parcel in) {
        price = in.readDouble();
        name = in.readString();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeString(name);
    }
}
