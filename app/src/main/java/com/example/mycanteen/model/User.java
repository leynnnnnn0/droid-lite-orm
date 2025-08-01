package com.example.mycanteen.model;

import android.content.Context;

import com.example.mycanteen.database.DBHelper;


import java.util.HashMap;
import java.util.LinkedHashMap;


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

    @Override
    public HashMap<String, Class<? extends DBHelper<?>>> relations() {
        return null;
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
