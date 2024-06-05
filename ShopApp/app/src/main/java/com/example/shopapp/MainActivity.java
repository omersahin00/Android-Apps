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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

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

        SetButtons();
        SetButtonsListener();

        SetBrandList();
        SetBrandLayout();

        SetProductList();
        SetProductLayout();
    }

    public void SetButtons() {
        String isAuth = FileHelper.readFromFile(MainActivity.this, "isAuth");
        Log.d(TAG, "FİLE: " + isAuth);

        // binding.mockDataButton.setVisibility(View.INVISIBLE);

        if (isAuth.contains("true")) {
            binding.buttonLogin.setVisibility(View.INVISIBLE);
            binding.mockDataButton.setVisibility(View.INVISIBLE);
            binding.buttonRegister.setVisibility(View.INVISIBLE);
        }
        else {
            binding.buttonCart.setVisibility(View.INVISIBLE);
            binding.buttonLiked.setVisibility(View.INVISIBLE);
            binding.buttonProfile.setVisibility(View.INVISIBLE);
            binding.buttonLogout.setVisibility(View.INVISIBLE);
        }
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

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileHelper.readFromFile(MainActivity.this, "isAuth").contains("true")) {

                    FileHelper.deleteFile(MainActivity.this, "isAuth");
                    FileHelper.writeToFile(MainActivity.this, "isAuth", "false");
                    FileHelper.deleteFile(MainActivity.this, "account");
                    FileHelper.writeToFile(MainActivity.this, "account", "null");

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    public void SetBrandLayout() {
        brandAdapter = new BrandAdapter(brandList, new BrandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Burada markaların listedeki pozisyonları elde edilebiliyor.
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
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                intent.putExtra("productIndex", productList.get(position).getIndex());
                startActivity(intent);
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