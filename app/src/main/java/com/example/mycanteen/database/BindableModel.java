package com.example.mycanteen.database;

import android.database.Cursor;

import java.util.List;

public interface BindableModel<T> {
    T fromCursor(Cursor cursor);
    List<T> fromCursorList(Cursor cursor);
}
