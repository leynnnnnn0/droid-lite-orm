package com.example.mycanteen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mycanteen.model.Product;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class EditProduct extends AppCompatActivity {

    EditText name, description, price, stock;
    ImageView image;
    Button updateButton;
    Product productDb;

    Intent intent;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        updateButton = findViewById(R.id.updateButton);
        stock = findViewById(R.id.stock);

        intent = getIntent();
        productDb = new Product(this);
        Product product = productDb.mapCursor(productDb.findById(intent.getIntExtra("id", 0)));

        name.setText(product.getName());
        description.setText(product.getDescription());
        image.setImageBitmap(product.getBitmapImage());
        price.setText(String.valueOf(product.getPrice()));
        stock.setText(String.valueOf(product.getStock()));

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        image.setImageURI(imageUri);
                    }
                }
        );

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        });



        updateButton.setOnClickListener(v -> {
            String productName = name.getText().toString().trim();
            String productDescription = description.getText().toString().trim();
            float productPrice = Float.parseFloat(price.getText().toString());
            int productStock = Integer.parseInt(stock.getText().toString().trim());

            Boolean result = productDb.update(new HashMap<>(){{
                put("image", imageViewToByte(image));
                put("name", productName);
                put("description", productDescription);
                put("price", productPrice);
                put("stock", productStock);
            }}, product.getId());
            toast(result, "Updated Successfully.");
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

    public static byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}