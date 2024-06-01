package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.databinding.ActivityMainBinding;
import com.example.shopapp.databinding.ActivityShoppingCartBinding;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    private ActivityShoppingCartBinding binding;
    List<Product> shoppingCartList;
    ShoppingCartAdapter shoppingCartAdapter;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = FileHelper.readFromFile(ShoppingCartActivity.this, "account");

        SetCartList();
        shoppingCartAdapter = new ShoppingCartAdapter(this, shoppingCartList);
        binding.listView.setAdapter(shoppingCartAdapter);
    }


    public void SetCartList() {
        FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");
        List<ShoppingCart> dumpCartList = new ArrayList<>();
        shoppingCartList = new ArrayList<>();
        shoppingCartFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<ShoppingCart>() {
            @Override
            public void onDataReceived(ShoppingCart data) {
                if (data != null) {
                    if (data.getUserName().equals(userName)) {
                        dumpCartList.add(data);
                        AddShoppingList(data.getProductIndex());
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    public void AddShoppingList(int productIndex) {
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        productFirebaseDatabaseHelper.getOneDataInt(new FirebaseDatabaseHelper.DataListener<Product>() {
            @Override
            public void onDataReceived(Product data) {
                shoppingCartList.add(data);
                shoppingCartAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Exception e) {

            }
        }, productIndex, "index");
    }
}








