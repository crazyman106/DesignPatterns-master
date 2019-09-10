package com.example.binder.ipc.server;

import android.os.IInterface;
import android.os.RemoteException;

import com.example.binder.ipc.Book;

import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/9/10 22:04
 * pkn    : com.example.binder.ipc.server
 * desc   :
 */

public interface BookManager extends IInterface {

    void addBook(Book book) throws RemoteException;

    List<Book> getBooks() throws RemoteException;

}
