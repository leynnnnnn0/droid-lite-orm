package com.example.mycanteen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycanteen.adapter.CartProductAdapter;
import com.example.mycanteen.model.CartProduct;
import com.example.mycanteen.model.Product;

import java.util.ArrayList;

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

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}