package com.example.shopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private List<Brand> mBrandList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public BrandAdapter(List<Brand> brandList, OnItemClickListener listener) {
        this.mBrandList = brandList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand brand = mBrandList.get(position);
        holder.brandImage.setImageResource(brand.getImageResource());
        holder.brandImageNameText.setText(brand.getName());

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
        return mBrandList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView brandImage;
        TextView brandImageNameText;
        public ViewHolder(View itemView) {
            super(itemView);
            brandImage = itemView.findViewById(R.id.brandImage);
            brandImageNameText = itemView.findViewById(R.id.brandImageNameText);
        }
    }
}
