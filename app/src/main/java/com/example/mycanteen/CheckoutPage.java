package com.example.mycanteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycanteen.adapter.CartProductAdapter;
import com.example.mycanteen.model.Cart;
import com.example.mycanteen.model.CartProduct;
import com.example.mycanteen.model.Order;
import com.example.mycanteen.model.OrderProduct;
import com.example.mycanteen.model.Product;
import com.example.mycanteen.service.CurrentUser;
import com.example.mycanteen.service.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CheckoutPage extends AppCompatActivity {
    CartProduct cartProductDb;

    RecyclerView cartProductsRecyclerView;
    CartProductAdapter cartProductAdapter;
    Button placeOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_page);

        cartProductDb = new CartProduct(this);
        cartProductsRecyclerView = findViewById(R.id.cartProductsRecyclerView);

        ArrayList<CartProduct> cartProducts = cartProductDb.mapCursorList(cartProductDb.with(Product.class)
                .with(com.example.mycanteen.model.Cart.class).get());

        cartProductAdapter = new CartProductAdapter(this, cartProducts);
        cartProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartProductsRecyclerView.setHasFixedSize(true);
        cartProductsRecyclerView.setAdapter(cartProductAdapter);

        placeOrder = findViewById(R.id.placeOrder);

        placeOrder.setOnClickListener(v -> {
            Order orderDb = new Order(this);
            int userId = CurrentUser.getCurrentUserId(this);
            float totalPrice = 0;
            for (CartProduct item : cartProducts) {
                totalPrice += item.product.getPrice();
            }

            float finalTotalPrice = totalPrice;

            String pattern = "MMMM dd, yyyy hh:mm a";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            String formatted = sdf.format(new Date());
    String orderNumber = "ON-" + (Math.random() * 100);
            orderDb.create(new HashMap<>(){{
                put("user_id", userId);
                put("order_number", orderNumber);
                put("total_price", finalTotalPrice);
                put("status", "pending");
                put("order_date", formatted);
            }});

            Cart cart = new Cart(this);
            Log.d("cartId", String.valueOf(cartProducts.get(0).getCart_id()));
            cart.delete(cartProducts.get(0).getCart_id());

            OrderProduct orderProductDb = new OrderProduct(this);
            Order newOrder = orderDb.mapCursor(orderDb.where("order_number", orderNumber).first());
            for(CartProduct cartProduct : cartProducts){
                cartProductDb.delete(cartProduct.getId());
                orderProductDb.create(new HashMap<>(){{
                    put("order_id", newOrder.getId());
                    put("product_id", cartProduct.product.getId());
                    put("quantity", cartProduct.getQuantity());
                    put("price", cartProduct.product.getPrice());
                }});
            }
            cartProducts.clear();
            Toast.success(this, "Order Placed.");
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}