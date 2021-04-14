package com.lying.ms_ipc_binder.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lying.ms_ipc_binder.R;
import com.lying.ms_ipc_binder.aidl.Book;
import com.lying.ms_ipc_binder.aidl.IBookManager;
import com.lying.ms_ipc_binder.aidl.IOnNewBookArrivedListener;
import com.lying.ms_ipc_binder.service.BookManagerService;

import java.lang.ref.WeakReference;
import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK = 111;

    private TextView textView;

    private IBookManager bokManager;
    private MyHandler myHandler;

    public void bt(View view) {
        try {
            List<Book> books = bokManager.getBookList();
            textView.setText(textView.getText() + books.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends  Handler{
        WeakReference<BookManagerActivity> weakReference ;
        public MyHandler(BookManagerActivity bookManagerActivity){
            weakReference = new WeakReference<>(bookManagerActivity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == MESSAGE_NEW_BOOK){
                Log.i(TAG, "handleMessage: 接收到新书提醒 book = " + msg.obj);
                weakReference.get().textView.setText(msg.obj.toString());
            }
        }
    };

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            myHandler.obtainMessage(MESSAGE_NEW_BOOK, newBook).sendToTarget();
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bokManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = bokManager.getBookList();
                Log.i(TAG, "onServiceConnected: query book list: " + list.toString());

                Book newBook = new Book(3, "Android 开发艺术探索");
                bokManager.addBook(newBook);
                Log.i(TAG, "onServiceConnected: 添加了一本新书");

                List<Book> newList = bokManager.getBookList();
                Log.i(TAG, "onServiceConnected: query book list, list type : " + newList.getClass().getCanonicalName());
                Log.i(TAG, "onServiceConnected: query book list: " + newList.toString());

                bokManager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bokManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = findViewById(R.id.tv_1);
        myHandler = new MyHandler(this);

        //启动服务
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "onCreate: 启动服务");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bokManager != null && bokManager.asBinder().isBinderAlive()){
            try {
                bokManager.unregisterListener(listener);
                Log.i(TAG, "onDestroy: 移除了监听");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        Log.i(TAG, "onDestroy: 销毁服务");
    }
}
