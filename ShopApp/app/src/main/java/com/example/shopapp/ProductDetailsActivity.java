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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDetailsActivity extends AppCompatActivity {
    private ActivityProductDetailsBinding binding;
    List<Comments> commentsList;
    CommentsAdapter commentsAdapter;
    private Product product;
    private float productStar;
    String userName;
    boolean onCart = false;

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

        userName = FileHelper.readFromFile(ProductDetailsActivity.this, "account");

        SetCommentList(productIndex);
        SetProductData(productIndex);
        GetProductPoint(productIndex);
        SetButtonsLayout();
        SetButtonListeners();
    }


    public void SetButtonsLayout() {
        FirebaseDatabaseHelper<Favorites> favoritesFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Favorites.class, "favorites");
        favoritesFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Favorites>() {
            @Override
            public void onDataReceived(Favorites data) {
                if (data != null) {
                    if (data.getUserName().equals(userName) && data.getProductIndex() == product.getIndex()) {
                        binding.addFavoriteButton.setChecked(true);
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        });

        binding.addCartButton.setText("Sepete Ekle");
        FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");
        shoppingCartFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<ShoppingCart>() {
            @Override
            public void onDataReceived(ShoppingCart data) {
                if (data != null) {
                    if (data.getUserName().equals(userName) && data.getProductIndex() == product.getIndex()) {
                        onCart = true;
                        binding.addCartButton.setText("Sepete Eklendi");
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: e", e);
            }
        });
    }

    public void SetButtonListeners() {
        binding.addFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = binding.addFavoriteButton.isChecked();
                if (isChecked) {
                    FirebaseDatabaseHelper<Favorites> favoritesFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Favorites.class, "favorites");

                    if (FileHelper.readFromFile(ProductDetailsActivity.this, "isAuth").contains("true")) {
                        favoritesFirebaseDatabaseHelper.addData(new Favorites(userName, product.getIndex()));
                        binding.addFavoriteButton.setText("Favorilere Eklendi");
                        return;
                    }

                    binding.addFavoriteButton.setText("Lütfen giriş yapın");
                    binding.addFavoriteButton.setChecked(false);
                }
                else {

                    FirebaseDatabaseHelper<Favorites> favoritesFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Favorites.class, "favorites");

                    if (FileHelper.readFromFile(ProductDetailsActivity.this, "isAuth").contains("true")) {
                        favoritesFirebaseDatabaseHelper.removeData(userName + "_" + product.getIndex(), "productKey");
                        binding.addFavoriteButton.setText("Favorilere Ekle");
                        return;
                    }

                    binding.addFavoriteButton.setText("Lütfen giriş yapın");
                    binding.addFavoriteButton.setChecked(false);
                }
            }
        });

        binding.addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Daha yazılmadı.
                // Daha yazılmadı.
                // Daha yazılmadı.
                // Daha yazılmadı.
            }
        });

        binding.addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");
                if (onCart) {
                    // silinecek
                    shoppingCartFirebaseDatabaseHelper.removeData(userName + "_" + product.getIndex(), "primaryKey");
                    onCart = false;
                    binding.addCartButton.setText("Sepete Ekle");
                }
                else {
                    // eklenecek
                    shoppingCartFirebaseDatabaseHelper.addData(new ShoppingCart(userName, product.getIndex()));
                    onCart = true;
                    binding.addCartButton.setText("Sepete Eklendi");
                }
            }
        });
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