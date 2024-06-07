package com.example.shopapp.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.Models.Brand;
import com.example.shopapp.Models.Product;
import com.example.shopapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> mProductList;
    private OnItemClickListener mListener;
    private Brand brand;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ProductAdapter(List<Product> productList, OnItemClickListener listener) {
        this.mProductList = productList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()) + " TL");
        holder.productImage.setImageResource(product.getImageResource());

        FirebaseDatabaseHelper<Brand> brandFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Brand.class, "brands");
        brandFirebaseDatabaseHelper.getOneData(new FirebaseDatabaseHelper.DataListener<Brand>() {
            @Override
            public void onDataReceived(Brand data) {
                if (data != null) {
                    brand = data;
                    holder.brandImage.setImageResource(brand.getImageResource());
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, product.getBrandName(), "name");

        // Tıklama olayı ekleniyor
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        ImageView brandImage;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            brandImage = itemView.findViewById(R.id.brandImage);
        }
    }
}
