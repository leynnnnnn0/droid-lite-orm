package com.example.mycanteen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mycanteen.model.Product;

public class ProductDetails extends AppCompatActivity {
    ImageView image;
    TextView name, price, description;
    Intent intent;
    Product productDb;
    ImageButton edit, delete;
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



        productDb = new Product(this);
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
            toast(productDb.delete(product.getId()), "Deleted Successfully.");
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void toast(Boolean result, String message)
    {
        if(result) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}