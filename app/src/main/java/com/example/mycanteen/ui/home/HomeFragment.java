package com.example.mycanteen.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycanteen.AddProduct;
import com.example.mycanteen.Cart;
import com.example.mycanteen.Login;
import com.example.mycanteen.R;
import com.example.mycanteen.adapter.ProductAdapter;
import com.example.mycanteen.databinding.FragmentHomeBinding;
import com.example.mycanteen.model.CartProduct;
import com.example.mycanteen.model.Product;
import com.example.mycanteen.service.CurrentUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ImageButton addButton, logout, cart;
    private FragmentHomeBinding binding;

    RecyclerView allProductsRecyclerView;
    ArrayList<Product> productArrayList;
    ProductAdapter productAdapter;
    Product productDb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cart = root.findViewById(R.id.cart);
        cart.setOnClickListener(v-> {
            startActivity(new Intent(requireContext(), Cart.class));
        });


        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            CurrentUser.clearCurrentUserData(requireContext());
            startActivity(new Intent(requireContext(), Login.class));
        });


        addButton = root.findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddProduct.class));
        });


        if(CurrentUser.getCurrentUserRole(requireContext()).equals("user")){
            addButton.setVisibility(View.GONE);
        }


        allProductsRecyclerView = root.findViewById(R.id.allProductsRecyclerView);
        productArrayList = new ArrayList<>();
        productDb = new Product(requireContext());
        productArrayList = productDb.mapCursorList(productDb.all());
        productAdapter = new ProductAdapter(requireContext(), productArrayList);
        allProductsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        allProductsRecyclerView.setHasFixedSize(true);
        allProductsRecyclerView.setAdapter(productAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}