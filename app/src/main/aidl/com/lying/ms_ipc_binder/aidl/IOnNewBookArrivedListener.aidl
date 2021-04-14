// IOnNewBookArrivedListener.aidl
package com.lying.ms_ipc_binder.aidl;

// Declare any non-default types here with import statements
import com.lying.ms_ipc_binder.aidl.Book;
interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book newBook);
}
