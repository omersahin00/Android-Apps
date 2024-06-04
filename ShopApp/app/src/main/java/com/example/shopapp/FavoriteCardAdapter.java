package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteCardAdapter extends ArrayAdapter<Product> {
    private FavoritesActivity favoritesActivity;
    private List<Product> products;
    private String userName;


    public FavoriteCardAdapter(FavoritesActivity favoritesActivity, List<Product> products, String userName) {
        super(favoritesActivity, 0, products);
        this.favoritesActivity = favoritesActivity;
        this.products = products;
        this.userName = userName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_favorite_card, parent, false);
        }

        TextView productName = convertView.findViewById(R.id.productName);
        TextView productDescription = convertView.findViewById(R.id.productDescription);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        ImageView productImage = convertView.findViewById(R.id.productImage);
        Button removeFavoriteButton = convertView.findViewById(R.id.removeFavoriteButton);


        productName.setText(product.getName());
        productDescription.setText(product.getBrandName());
        productPrice.setText(String.valueOf(product.getPrice() + " TL"));
        productImage.setImageResource(product.getImageResource());


        return convertView;
    }
}
