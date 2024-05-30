package com.example.shopapp;

import static android.content.ContentValues.TAG;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
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

        // Silme kodu testi:
        /* productFirebaseDatabaseHelper.addData(new Product("Test", 100, "Nike", 123));
        productFirebaseDatabaseHelper.removeProduct("Test"); */


        binding.mockDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MockDataController mockDataController = new MockDataController(MainActivity.this);
                mockDataController.AddMockData();
            }
        });

        List<Brand> brandList = new ArrayList<Brand>();
        brandList.add(new Brand("Brand 1", R.drawable.brand_adidas));
        brandList.add(new Brand("Brand 2", R.drawable.brand_zara));
        brandList.add(new Brand("Brand 3", R.drawable.brand_lv));
        brandList.add(new Brand("Brand 4", R.drawable.brand_gucci));
        brandList.add(new Brand("Brand 5", R.drawable.brand_puma));
        brandList.add(new Brand("Brand 6", R.drawable.brand_nike));
        brandList.add(new Brand("Brand 7", R.drawable.brand_chanel));

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        List<Product> productList = new ArrayList<Product>();

        // Log.d(TAG, String.valueOf(R.drawable.item_1)); // çıktı: 2131165371

        productList.add(new Product("Ürün 1", 100, "Nike", R.drawable.item_1)); // sayı üstten geliyor. elle yazdım.
        productList.add(new Product("Ürün 2", 200, "Adidas", R.drawable.item_1));
        productList.add(new Product("Ürün 3", 200, "Chanel", R.drawable.item_2));
        productList.add(new Product("Ürün 4", 200, "Gucci", R.drawable.item_3));
        productList.add(new Product("Ürün 5", 200, "LV", R.drawable.item_4));
        productList.add(new Product("Ürün 6", 200, "Puma", R.drawable.item_4_1));
        productList.add(new Product("Ürün 7", 200, "Zara", R.drawable.item_4_2));
        productList.add(new Product("Ürün 8", 200, "Zara", R.drawable.item_4_3));


        ProductAdapter adapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Burada ürünlerin listedeki pozisyonları elde edilebiliyor.
                Toast.makeText(MainActivity.this, productList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.recyclerView.setAdapter(adapter);


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
}