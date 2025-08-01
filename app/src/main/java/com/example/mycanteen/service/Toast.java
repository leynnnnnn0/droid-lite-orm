package com.example.mycanteen.service;

import android.content.Context;

public class Toast {

    public static void error(Context context, String message){
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
    public static void success(Context context, String message){
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
