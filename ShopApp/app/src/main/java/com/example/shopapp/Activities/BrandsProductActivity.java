package com.example.shopapp.Activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.shopapp.Adapters.BrandAdapter;
import com.example.shopapp.Adapters.ProductAdapter;
import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.Models.Brand;
import com.example.shopapp.Models.Product;
import com.example.shopapp.R;
import com.example.shopapp.databinding.ActivityBrandsProductBinding;
import com.example.shopapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class BrandsProductActivity extends AppCompatActivity {
    private ActivityBrandsProductBinding binding;
    private Brand brand;
    private List<Product> productList;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_brands_product);

        binding = ActivityBrandsProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        productList = new ArrayList<>();

        GetBrand();
    }


    private void GetBrand() {
        String brandName = getIntent().getStringExtra("brandName");

        FirebaseDatabaseHelper<Brand> brandFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Brand.class, "brands");
        brandFirebaseDatabaseHelper.getOneData(new FirebaseDatabaseHelper.DataListener<Brand>() {
            @Override
            public void onDataReceived(Brand data) {
                if (data != null) {
                    brand = data;
                    SetLayout();
                    GetProducts();
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, brandName, "name");
    }


    private void GetProducts() {
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        productFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Product>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataReceived(Product data) {
                if (data != null) {
                    if (data.getBrandName().equals(brand.getName())) {
                        productList.add(data);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    private void SetLayout() {
        binding.brandImagePhoto.setImageResource(brand.getImageResource());
        binding.brandNameText.setText(brand.getName());
        SetProductsLayout();
    }

    private void SetProductsLayout() {
        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Burada ürünlerin listedeki pozisyonları elde edilebiliyor.
                Intent intent = new Intent(BrandsProductActivity.this, ProductDetailsActivity.class);
                intent.putExtra("productIndex", productList.get(position).getIndex());
                startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(productAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}












