package com.example.shopapp.Helpers;

import android.app.Activity;
import android.widget.Toast;

import com.example.shopapp.Models.Account;
import com.example.shopapp.Models.Brand;
import com.example.shopapp.Models.Comments;
import com.example.shopapp.Models.Favorites;
import com.example.shopapp.Models.Product;
import com.example.shopapp.Models.ShoppingCart;
import com.example.shopapp.R;

public class MockDataController {

    private Activity activity;

    public MockDataController(Activity activity) {
        this.activity = activity;
    }

    public void RemoveMockData() {
        FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
        FirebaseDatabaseHelper<Brand> brandFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Brand.class, "brands");
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        FirebaseDatabaseHelper<Comments> commentsFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Comments.class, "comments");
        FirebaseDatabaseHelper<Favorites> favoritesFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Favorites.class, "favorites");
        FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");

        accountFirebaseDatabaseHelper.removeAllData();
        brandFirebaseDatabaseHelper.removeAllData();
        productFirebaseDatabaseHelper.removeAllData();
        commentsFirebaseDatabaseHelper.removeAllData();
        favoritesFirebaseDatabaseHelper.removeAllData();
        shoppingCartFirebaseDatabaseHelper.removeAllData();
    }
    public void AddMockData() {
        RemoveMockData();

        FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
        FirebaseDatabaseHelper<Brand> brandFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Brand.class, "brands");
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        FirebaseDatabaseHelper<Comments> commentsFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Comments.class, "comments");

        accountFirebaseDatabaseHelper.addData(new Account("admin", "123", 0));
        accountFirebaseDatabaseHelper.addData(new Account("omer", "123", 10000));

        brandFirebaseDatabaseHelper.addData(new Brand("Adidas", R.drawable.brand_adidas));
        brandFirebaseDatabaseHelper.addData(new Brand("Zara", R.drawable.brand_zara));
        brandFirebaseDatabaseHelper.addData(new Brand("LV", R.drawable.brand_lv));
        brandFirebaseDatabaseHelper.addData(new Brand("Gucci", R.drawable.brand_gucci));
        brandFirebaseDatabaseHelper.addData(new Brand("Puma", R.drawable.brand_puma));
        brandFirebaseDatabaseHelper.addData(new Brand("Nike", R.drawable.brand_nike));
        brandFirebaseDatabaseHelper.addData(new Brand("Channel", R.drawable.brand_chanel));


        productFirebaseDatabaseHelper.addData(new Product(1, "Tişört", 400, "LV", R.drawable.item_1));
        productFirebaseDatabaseHelper.addData(new Product(2, "Ceket", 800, "Channel", R.drawable.item_2));
        productFirebaseDatabaseHelper.addData(new Product(3, "Çanta", 1000, "Zara", R.drawable.item_3));
        productFirebaseDatabaseHelper.addData(new Product(4, "Gömlek", 500, "Adidas", R.drawable.item_4));
        productFirebaseDatabaseHelper.addData(new Product(5, "Gömlek", 600, "LV", R.drawable.item_4_1));
        productFirebaseDatabaseHelper.addData(new Product(6, "Tişört", 700, "LV", R.drawable.item_4_2));
        productFirebaseDatabaseHelper.addData(new Product(7, "Gömlek", 400, "LV", R.drawable.item_4_3));
        productFirebaseDatabaseHelper.addData(new Product(8, "Tişört", 550, "LV", R.drawable.item_2));

        commentsFirebaseDatabaseHelper.addData(new Comments("omer", "TestComment1", 5, 1));
        commentsFirebaseDatabaseHelper.addData(new Comments("yusuf", "TestComment1", 4, 1));
        commentsFirebaseDatabaseHelper.addData(new Comments("şef", "TestComment1", 3, 1));
        commentsFirebaseDatabaseHelper.addData(new Comments("burak", "TestComment1", 2, 1));
        commentsFirebaseDatabaseHelper.addData(new Comments("nurdan", "TestComment1", 2, 1));
        commentsFirebaseDatabaseHelper.addData(new Comments("enes", "TestComment1", 1, 2));
        commentsFirebaseDatabaseHelper.addData(new Comments("osman", "TestComment1", 2, 2));
        commentsFirebaseDatabaseHelper.addData(new Comments("nisa", "TestComment1", 3, 2));


        Toast.makeText(activity, "Tüm Sahte Veriler Basıldı.", Toast.LENGTH_SHORT).show();
    }
}
