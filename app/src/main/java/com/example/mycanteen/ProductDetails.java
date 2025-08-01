package com.example.mycanteen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mycanteen.model.Cart;
import com.example.mycanteen.model.CartProduct;
import com.example.mycanteen.model.Product;
import com.example.mycanteen.service.CurrentUser;
import com.example.mycanteen.service.Toast;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    ImageView image;
    TextView name, price, description;
    Intent intent;
    Product productDb;
    Cart cartDb;
    CartProduct cartProductDb;
    ImageButton edit, delete;
    Button addToCartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        intent = getIntent();
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        addToCartButton = findViewById(R.id.addToCartButton);


        if(CurrentUser.getCurrentUserRole(this).equals("user")){
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }
        if(CurrentUser.getCurrentUserRole(this).equals("admin")){
            addToCartButton.setVisibility(View.GONE);
        }



        productDb = new Product(this);
        cartDb = new Cart(this);
        cartProductDb = new CartProduct(this);

        Product product = productDb.mapCursor(productDb.findById(intent.getIntExtra("id", 0)));

        byte[] imageBytes = product.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image.setImageBitmap(bitmap);
        }

        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(String.valueOf(product.getPrice()));

        edit.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, EditProduct.class);
            editIntent.putExtra("id", product.getId());
            startActivity(editIntent);
        });

        delete.setOnClickListener(v -> {
            productDb.delete(product.getId());
            Toast.success(this, "Deleted Successfully.");
        });

        addToCartButton.setOnClickListener(view -> {
            Cart cart = cartDb.mapCursor(cartDb.where("user_id",  String.valueOf(CurrentUser.getCurrentUserId(this))).first());
            if(cart != null){
                CartProduct cartProduct = cartProductDb.mapCursor(cartProductDb
                        .where("cart_id",  String.valueOf(cart.getId()))
                        .where("product_id", String.valueOf(product.getId()))
                        .first());
                if(cartProduct != null){
                    Toast.success(this, "You already have this on your cart.");
                }else {
                    cartProductDb.create(new HashMap<>() {{
                        put("cart_id", cart.getId());
                        put("product_id", product.getId());
                        put("quantity", 1);
                    }});
                    Toast.success(this, "Added to cart.");
                }
            }
            else {
                int id = CurrentUser.getCurrentUserId(this);
                cartDb.create(new HashMap<>(){{
                    put("user_id", id);
                }});
                Cart newCart = cartDb.mapCursor(cartDb.where("user_id",  String.valueOf(CurrentUser.getCurrentUserId(this))).first());
                cartProductDb.create(new HashMap<>() {{
                    put("cart_id", newCart.getId());
                    put("product_id", product.getId());
                    put("quantity", 1);
                }});
                Toast.success(this, "Added to cart.");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}