package com.example.mycanteen.model;

import android.content.Context;

import com.example.mycanteen.database.DBHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CartProduct extends DBHelper<CartProduct> {
    public int id;
    public int cart_id;
    public int product_id;
    public int quantity;

    public Cart cart;

    public Product product;
    public CartProduct(Context context) {
        super(context, "cart_products");
    }

    @Override
    public String[] fillable() {
        return new String[]{
                "id", "cart_id", "product_id", "quantity"
        };
    }

    public static LinkedHashMap<String, String> schema() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        columns.put("quantity", "INTEGER");
        columns.put("cart_id", "INTEGER");
        columns.put("product_id", "INTEGER");
        columns.put("FOREIGN_KEYS",
                "FOREIGN KEY(cart_id) REFERENCES carts(id) ON DELETE CASCADE, " +
                        "FOREIGN KEY(product_id) REFERENCES products(id)"
        );
        return columns;
    }

    @Override
    public LinkedHashMap<String, String> columns() {
        return schema();
    }

    @Override
    public HashMap<String, Class<? extends DBHelper<?>>> relations() {
        HashMap<String, Class<? extends DBHelper<?>>> map = new HashMap<>();
        map.put("product", Product.class);
        map.put("cart", Cart.class);
        return map;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
