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

public class ShoppingCartAdapter extends ArrayAdapter<Product> {
    private ShoppingCartActivity shoppingCartActivity;
    private List<Product> products;
    private List<Integer> productsCount;
    private String userName;


    public ShoppingCartAdapter(ShoppingCartActivity shoppingCartActivity, List<Product> products, String userName) {
        super(shoppingCartActivity, 0, products);
        this.shoppingCartActivity = shoppingCartActivity;
        this.products = products;
        productsCount = new ArrayList<>();
        this.userName = userName;
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
        Button decreaseQuantity = convertView.findViewById(R.id.decreaseButton);
        Button increaseQuantity = convertView.findViewById(R.id.increaseButton);
        TextView productQuantity = convertView.findViewById(R.id.productQuantity);

        productName.setText(product.getName());
        productDescription.setText(product.getBrandName());
        productPrice.setText(String.valueOf(product.getPrice() + " TL"));
        productImage.setImageResource(product.getImageResource());

        productsCount.add(1);

        productQuantity.setText(String.valueOf(productsCount.get(position)));

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(productQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    productsCount.set(position, productsCount.get(position) - 1);
                    shoppingCartActivity.addedPrice -= products.get(position).getPrice();
                    shoppingCartActivity.SetPriceLayouts();
                    productQuantity.setText(String.valueOf(quantity));
                }
                else {
                    FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");

                    String primaryKey = userName + "_" + products.get(position).getIndex();
                    shoppingCartFirebaseDatabaseHelper.removeData(primaryKey, "primaryKey");

                    productsCount.set(position, productsCount.get(position) - 1);
                    productsCount.remove(position);

                    for (Product product1 : products) {
                        if (product1.getIndex() == products.get(position).getIndex()) {
                            products.remove(product1);
                            notifyDataSetChanged();
                            shoppingCartActivity.SetPriceLayouts();
                            break;
                        }
                    }

                    quantity--;
                    productQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(productQuantity.getText().toString());
                quantity++;
                productQuantity.setText(String.valueOf(quantity));
                productsCount.set(position, productsCount.get(position) + 1);
                shoppingCartActivity.addedPrice += products.get(position).getPrice();
                shoppingCartActivity.SetPriceLayouts();
            }
        });

        return convertView;
    }
}
