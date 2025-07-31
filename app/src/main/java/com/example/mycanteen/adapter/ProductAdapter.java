package com.example.mycanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycanteen.ProductDetails;
import com.example.mycanteen.R;
import com.example.mycanteen.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> itemModelArrayList;

    public ProductAdapter(Context context, ArrayList<Product> itemModelArrayList) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_container, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product itemModel = itemModelArrayList.get(position);
        holder.name.setText(itemModel.getName());
        holder.price.setText(String.valueOf(itemModel.getPrice()));
        byte[] imageBytes = itemModel.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.image.setImageBitmap(bitmap);
        }

        holder.container.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("id", itemModel.getId());
            intent.putExtra("name", itemModel.getName());
            intent.putExtra("description", itemModel.getDescription());
            intent.putExtra("price", String.valueOf(itemModel.getPrice()));
            intent.putExtra("image", imageBytes);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, price;
        LinearLayout container;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name);
            this.price = itemView.findViewById(R.id.price);
            this.container = itemView.findViewById(R.id.container);
        }
    }
}
