package com.example.mycanteen.model;

import android.content.Context;

import com.example.mycanteen.database.DBHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Cart extends DBHelper<Cart> {
    public int id;
    public int user_id;
    public Cart(Context context) {
        super(context, "carts");
    }

    @Override
    public String[] fillable() {
        return new String[]{
             "id", "user_id"
        };
    }

    public static LinkedHashMap<String, String> schema() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        columns.put("user_id", "INTEGER,  FOREIGN KEY(user_id) REFERENCES users(id)");
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

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
