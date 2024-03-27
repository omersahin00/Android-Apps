package com.example.rehberuygulamas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rehberuygulamas.databinding.ActivityDetailsBinding;


public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Intent intent = getIntent();
        String name = (String) intent.getSerializableExtra("name");
        String phone = (String) intent.getSerializableExtra("phone");
        binding.textView.setText(name + "\n" + phone);

        /*
        Intent intent = getIntent();
        String item = (String) intent.getSerializableExtra("item");
        binding.textView.setText(item);
        */

    }
}

