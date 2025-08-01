package com.example.mycanteen.service;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {
    public static int getCurrentUserId(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyCanteen", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    public static String getCurrentUserRole(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyCanteen", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userRole", "user");
    }

    public static void setCurrentUserRole(Context context, String role)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyCanteen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userRole", role).apply();
    }

    public static void setCurrentUserId(Context context, int id)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyCanteen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", id).apply();
    }

    public static void clearCurrentUserData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyCanteen", Context.MODE_PRIVATE);
        prefs.edit().remove("userId").apply();
        prefs.edit().remove("userRole").apply();
    }
}
