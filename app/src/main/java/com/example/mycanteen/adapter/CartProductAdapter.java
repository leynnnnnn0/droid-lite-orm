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
import com.example.mycanteen.model.CartProduct;
import com.example.mycanteen.model.Product;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartViewHolder> {

    Context context;
    ArrayList<CartProduct> itemModelArrayList;

    public CartProductAdapter(Context context, ArrayList<CartProduct> itemModelArrayList) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_container, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartProduct itemModel = itemModelArrayList.get(position);
        holder.name.setText(itemModel.product.getName());
        holder.price.setText(String.valueOf(itemModel.product.getPrice()));
        byte[] imageBytes = itemModel.product.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.image.setImageBitmap(bitmap);
        }

//        holder.container.setOnClickListener(view -> {
//            Intent intent = new Intent(context, ProductDetails.class);
//            intent.putExtra("id", itemModel.getId());
//            intent.putExtra("name", itemModel.getName());
//            intent.putExtra("description", itemModel.getDescription());
//            intent.putExtra("price", String.valueOf(itemModel.getPrice()));
//            intent.putExtra("image", imageBytes);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, price;
        LinearLayout container;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name);
            this.price = itemView.findViewById(R.id.price);
            this.container = itemView.findViewById(R.id.container);
        }
    }
}
