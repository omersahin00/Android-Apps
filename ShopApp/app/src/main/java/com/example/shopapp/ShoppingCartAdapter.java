package com.example.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShoppingCartAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> products;

    public ShoppingCartAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopping_cart, parent, false);
        }

        TextView productName = convertView.findViewById(R.id.productName);
        TextView productDescription = convertView.findViewById(R.id.productDescription);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        ImageView productImage = convertView.findViewById(R.id.productImage);

        productName.setText(product.getName());
        productDescription.setText(product.getBrandName());
        productPrice.setText(String.valueOf(product.getPrice() + " TL"));
        productImage.setImageResource(product.getImageResource());

        return convertView;
    }
}
