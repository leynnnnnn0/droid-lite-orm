package com.example.mycanteen.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mycanteen.database.DBHelper;

import java.sql.Blob;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Product extends DBHelper<Product> {

    public int id;
        public byte[] image;
    public String name;
    public String description;
    public float price;
    public int stock;
    public Product(Context context) {
        super(context, "products");
    }
    @Override
    public String[] fillable() {
        return new String[]{
                "id",
                "image",
                "name",
                "description",
                "price",
                "stock"
        };
    }

    public static LinkedHashMap<String, String> schema() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        columns.put("image", "BLOB");
        columns.put("name", "TEXT");
        columns.put("description", "TEXT");
        columns.put("price", "REAL");
        columns.put("stock", "INTEGER");
        columns.put("is_available", "BOOLEAN DEFAULT 1");
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getBitmapImage()
    {
        byte[] imageBytes = this.getImage();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
