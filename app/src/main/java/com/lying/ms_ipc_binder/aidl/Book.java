package com.lying.ms_ipc_binder.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {

    public int bookId;
    public String bookName;

    public Book(int bookId, String bookName){
        this.bookId = bookId;
        this.bookName = bookName;
    }

    /**
     * @return 当前对象的内容描述。含有文件描述符返回 1，否则返回 0.几乎所有情况都是返回0.
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * 序列化
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    /**
     * 反序列化
     */
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

    private Book(Parcel parcel){
        bookId = parcel.readInt();
        bookName = parcel.readString();
    }

    @NonNull
    @Override
    public String toString() {
        return "bookId = " + bookId + ", bookName = " + bookName;
    }
}
