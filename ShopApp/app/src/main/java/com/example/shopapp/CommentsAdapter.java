package com.example.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<Comments> {

    public CommentsAdapter(Context context, List<Comments> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // View'ı geri dönüştür veya yeni bir tane oluştur
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_view, parent, false);
        }

        // Geçerli öğeyi al
        Comments comment = getItem(position);

        // Görünümdeki TextView'leri bul ve ayarla
        TextView userNameTextView = convertView.findViewById(R.id.item_title);
        TextView commentTextView = convertView.findViewById(R.id.item_content);

        String star = "";
        for (int i = 0; i < comment.getPoint(); i++) {
            star += "⭐";
        }

        userNameTextView.setText(comment.getUserName() + "  " + star);
        commentTextView.setText(comment.getComment());

        return convertView;
    }
}
