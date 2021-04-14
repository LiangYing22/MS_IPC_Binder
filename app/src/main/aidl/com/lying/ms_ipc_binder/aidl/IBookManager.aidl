// IBookManager.aidl
package com.lying.ms_ipc_binder.aidl;
import com.lying.ms_ipc_binder.aidl.Book;
import com.lying.ms_ipc_binder.aidl.IOnNewBookArrivedListener;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
