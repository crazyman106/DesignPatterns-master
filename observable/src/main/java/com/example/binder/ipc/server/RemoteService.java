package com.example.binder.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.binder.ipc.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/9/10 22:20
 * pkn    : com.example.binder.ipc.server
 * desc   :
 */

public class RemoteService extends Service {
    private List<Book> books = new ArrayList<>();

    public RemoteService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stubManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("三体");
        book.setPrice(88);
        books.add(book);
    }

    private Stub stubManager = new Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }
                if (book == null)
                    return;
                book.setPrice(book.getPrice() * 2);
                books.add(book);
                Log.e("Server", "books: " + book.toString());
            }
        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }
    };
}
