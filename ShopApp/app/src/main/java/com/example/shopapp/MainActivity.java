package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shopapp.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    List<Brand> brandList;
    List<Product> productList;
    ProductAdapter productAdapter;
    BrandAdapter brandAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);

        SetButtonsListener();

        SetBrandList();
        SetBrandLayout();

        SetProductList();
        SetProductLayout();
    }


    public void SetButtonsListener() {
        binding.mockDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MockDataController mockDataController = new MockDataController(MainActivity.this);
                mockDataController.AddMockData();
                SetProductList();
                SetBrandList();
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    public void SetBrandLayout() {
        brandAdapter = new BrandAdapter(brandList, new BrandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "Brand " + brandList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.horizontalRecyclerView.setAdapter(brandAdapter);
        binding.horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    public void SetProductLayout() {
        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Burada ürünlerin listedeki pozisyonları elde edilebiliyor.
                Toast.makeText(MainActivity.this, productList.get(position).getName() + " " + productList.get(position).getIndex(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.recyclerView.setAdapter(productAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }


    public void SetProductList() {
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        productList = new ArrayList<>();

        productFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Product>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataReceived(Product data) {
                Product product = new Product(data.getIndex(), data.getName(), data.getPrice(), data.getBrandName(), data.getImageResource());
                productList.add(product);
                productAdapter.notifyDataSetChanged(); // Yeni veri geldikçe adaptör güncelleniyor.
                Log.d(TAG, "data: " + product.toString());
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        });
        SetProductLayout();
    }


    public void SetBrandList() {
        FirebaseDatabaseHelper<Brand> brandFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Brand.class, "brands");
        brandList = new ArrayList<>();

        brandFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Brand>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataReceived(Brand data) {
                brandList.add(data);
                brandAdapter.notifyDataSetChanged(); // Yeni veri geldikçe adaptör güncelleniyor.
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        });
        SetBrandLayout();
    }
}