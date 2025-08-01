package com.example.mycanteen.model;

import android.content.Context;
import android.database.Cursor;

import com.example.mycanteen.database.BindableModel;
import com.example.mycanteen.database.DBHelper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class User extends DBHelper<User> {
    public int id;
    public String username;
    public String email;
    public String role;
    Context context;

    public User(Context context)  {
        super(context, "users");
        this.context = context;


    }


    @Override
    public String[] fillable() {
        return new String[]{
                "id",
                "username",
                "email",
                "role"
        };
    }

    public static LinkedHashMap<String, String> schema() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        columns.put("username", "TEXT UNIQUE");
        columns.put("email", "TEXT UNIQUE");
        columns.put("password", "TEXT");
        columns.put("role", "TEXT DEFAULT 'user'");
        return columns;
    }

    @Override
    public LinkedHashMap<String, String> columns() {
       return schema();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Context getContext() {
        return context;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
