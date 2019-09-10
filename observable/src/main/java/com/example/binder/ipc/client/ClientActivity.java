package com.example.binder.ipc.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.binder.ipc.Book;
import com.example.binder.ipc.server.BookManager;
import com.example.binder.ipc.server.RemoteService;
import com.example.binder.ipc.server.Stub;

import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/9/10 22:22
 * pkn    : com.example.binder.ipc.client
 * desc   :
 */

public class ClientActivity extends AppCompatActivity {
    private BookManager bookManager;
    private boolean isConnection = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (!isConnection) {
            attemptToBindService();
            return;
        }

        if (bookManager == null)
            return;

        try {
            Book book = new Book();
            book.setPrice(101);
            book.setName("编码");
            bookManager.addBook(book);

            Log.d("ClientActivity", bookManager.getBooks().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void attemptToBindService() {

        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction("com.baronzhang.ipc.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnection = true;
            bookManager = Stub.asInterface(service);
            if (bookManager != null) {
                try {
                    List<Book> books = bookManager.getBooks();
                    Log.d("ClientActivity", books.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnection = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnection) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnection) {
            unbindService(serviceConnection);
        }
    }
}
