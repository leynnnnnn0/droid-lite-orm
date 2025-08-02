package com.example.mycanteen.model;

import android.content.Context;

import com.example.mycanteen.database.DBHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Order extends DBHelper<Order> {

    public int id;
    public int user_id;
    public String order_number;
    public float total_price;
    public String status;
    public String order_date;
    public String received_date;
    public Order(Context context) {
        super(context, "orders");
    }

    @Override
    public String[] fillable() {
        return new String[]{
                "id",
                "user_id",
                "order_number",
                "total_price",
                "status",
                "order_date",
                "received_date"
        };
    }

    public static LinkedHashMap<String, String> schema() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        columns.put("user_id", "INTEGER");
        columns.put("order_number", "TEXT");
        columns.put("total_price", "FLOAT");
        columns.put("status", "TEXT");
        columns.put("order_date", "TEXT");
        columns.put("received_date", "TEXT");
        columns.put("FOREIGN_KEYS", "FOREIGN KEY(user_id) REFERENCES users(id)");
        return columns;
    }

    @Override
    public LinkedHashMap<String, String> columns() {
        return schema();
    }

    @Override
    public HashMap<String, Class<? extends DBHelper<?>>> relations() {
        HashMap<String, Class<? extends DBHelper<?>>> map = new HashMap<>();
        map.put("user", User.class);
        return map;
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

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getReceived_date() {
        return received_date;
    }

    public void setReceived_date(String received_date) {
        this.received_date = received_date;
    }
}
