package com.example.shopapp;

import android.app.Activity;
import android.widget.Toast;

public class MockDataController {

    private Activity activity;

    public MockDataController(Activity activity) {
        this.activity = activity;
    }

    public void AddMockData() {
        AccountFirebaseDatabaseHelper accountFirebaseDatabaseHelper = new AccountFirebaseDatabaseHelper(Account.class);
        BrandFirebaseDatabaseHelper brandFirebaseDatabaseHelper = new BrandFirebaseDatabaseHelper(Brand.class);
        ProductFirebaseDatabaseHelper productFirebaseDatabaseHelper = new ProductFirebaseDatabaseHelper(Product.class);

        accountFirebaseDatabaseHelper.addData(new Account("admin", "123", 0));
        accountFirebaseDatabaseHelper.addData(new Account("omer", "123", 10000));

        brandFirebaseDatabaseHelper.addData(new Brand("Adidas", R.drawable.brand_adidas));
        brandFirebaseDatabaseHelper.addData(new Brand("Zara", R.drawable.brand_zara));
        brandFirebaseDatabaseHelper.addData(new Brand("LV", R.drawable.brand_lv));
        brandFirebaseDatabaseHelper.addData(new Brand("Gucci", R.drawable.brand_gucci));
        brandFirebaseDatabaseHelper.addData(new Brand("Puma", R.drawable.brand_puma));
        brandFirebaseDatabaseHelper.addData(new Brand("Nike", R.drawable.brand_nike));
        brandFirebaseDatabaseHelper.addData(new Brand("Channel", R.drawable.brand_chanel));


        productFirebaseDatabaseHelper.addData(new Product("Tişört", 400, "LV", R.drawable.item_1));
        productFirebaseDatabaseHelper.addData(new Product("Ceket", 800, "Channel", R.drawable.item_2));
        productFirebaseDatabaseHelper.addData(new Product("Çanta", 1000, "Zara", R.drawable.item_3));
        productFirebaseDatabaseHelper.addData(new Product("Gömlek", 500, "Adidas", R.drawable.item_4));
        productFirebaseDatabaseHelper.addData(new Product("Gömlek", 600, "LV", R.drawable.item_4_1));
        productFirebaseDatabaseHelper.addData(new Product("Tişört", 700, "LV", R.drawable.item_4_2));
        productFirebaseDatabaseHelper.addData(new Product("Gömlek", 400, "LV", R.drawable.item_4_3));
        productFirebaseDatabaseHelper.addData(new Product("Tişört", 550, "LV", R.drawable.item_2));

        Toast.makeText(activity, "Tüm Sahte Veriler Basıldı.", Toast.LENGTH_SHORT).show();
    }
}
