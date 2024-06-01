package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.shopapp.databinding.ActivityMainBinding;
import com.example.shopapp.databinding.ActivityProductDetailsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDetailsActivity extends AppCompatActivity {
    private ActivityProductDetailsBinding binding;
    List<Comments> commentsList;
    CommentsAdapter commentsAdapter;
    private Product product;
    private float productStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);

        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int productIndex = intent.getIntExtra("productIndex", 0);

        SetCommentList(productIndex);
        SetProductData(productIndex);
        GetProductPoint(productIndex);
    }


    public void SetProductData(int productIndex) {
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        product = new Product();

        productFirebaseDatabaseHelper.getOneDataInt(new FirebaseDatabaseHelper.DataListener<Product>() {
            @Override
            public void onDataReceived(Product data) {
                if (data != null) {
                    product.setIndex(data.getIndex());
                    product.setName(data.getName());
                    product.setPrice(data.getPrice());
                    product.setBrandName(data.getBrandName());
                    product.setImageResource(data.getImageResource());

                    SetProductLayout();
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        }, productIndex, "index");
    }


    public void SetCommentList(int productIndex) {
        FirebaseDatabaseHelper<Comments> commentsFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Comments.class, "comments");
        commentsList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, commentsList);

        commentsFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Comments>() {
            @Override
            public void onDataReceived(Comments data) {
                if (data.getProductIndex() == productIndex) {
                    commentsList.add(data);
                    commentsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        });

        binding.commentList.setAdapter(commentsAdapter);
    }


    public void SetProductLayout() {
        binding.productImage.setImageResource(product.getImageResource());
        binding.productNameText.setText(product.getName());
        binding.productPriceText.setText(product.getPrice() + " TL");

        String star = "";
        for (int i = 0; i < productStar; i++) {
            star += "⭐";
        }
        binding.productStarText.setText("Ürün Puanı: " + star);
    }


    public void GetProductPoint(int productIndex) {
        final int[] counter = {0};
        final int[] totalStar = {0};

        FirebaseDatabaseHelper<Comments> commentsFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Comments.class, "comments");

        commentsFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Comments>() {
            @Override
            public void onDataReceived(Comments data) {
                if (data.getProductIndex() == productIndex) {
                    Log.d(TAG, "BURAYA GİRDİ");
                    counter[0]++;
                    totalStar[0] += data.getPoint();
                    productStar = (float) totalStar[0] / (float) counter[0];
                    SetProductLayout();
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        });
    }
}