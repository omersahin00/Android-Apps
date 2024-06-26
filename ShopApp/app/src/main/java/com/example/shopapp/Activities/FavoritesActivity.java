package com.example.shopapp.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.Adapters.FavoriteCardAdapter;
import com.example.shopapp.Helpers.FileHelper;
import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.Models.Favorites;
import com.example.shopapp.Models.Product;
import com.example.shopapp.R;
import com.example.shopapp.databinding.ActivityFavoritesBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private String userName;
    private List<Product> favoritesList;
    private FavoriteCardAdapter favoriteCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = FileHelper.readFromFile(FavoritesActivity.this, "account");

        SetFavoritesList();

        favoriteCardAdapter = new FavoriteCardAdapter(this, favoritesList, userName);
        binding.listView.setAdapter(favoriteCardAdapter);;
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void SetFavoritesList() {
        List<Favorites> dumpList = new ArrayList<>();
        favoritesList = new ArrayList<>();

        FirebaseDatabaseHelper<Favorites> favoritesFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Favorites.class, "favorites");
        favoritesFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<Favorites>() {
            @Override
            public void onDataReceived(Favorites data) {
                if (data != null) {
                    if (data.getUserName().equals(userName)) {
                        dumpList.add(data);
                        AddFavoritesList(data.getProductIndex());
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    private void AddFavoritesList(int productIndex) {
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        productFirebaseDatabaseHelper.getOneDataInt(new FirebaseDatabaseHelper.DataListener<Product>() {
            @Override
            public void onDataReceived(Product data) {
                favoritesList.add(data);
                favoriteCardAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, productIndex, "index");
    }
}













