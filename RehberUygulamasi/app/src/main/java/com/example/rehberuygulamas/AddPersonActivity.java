package com.example.rehberuygulamas;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rehberuygulamas.databinding.ActivityAddPersonBinding;
import com.example.rehberuygulamas.databinding.ActivityDetailsBinding;

import java.time.Instant;

public class AddPersonActivity extends AppCompatActivity {
    private ActivityAddPersonBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPersonBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        databaseHelper = new DatabaseHelper(this);
        setContentView(view);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameText.getText().toString();
                String phone = binding.phoneText.getText().toString();
                String email = binding.emailText.getText().toString();

                Person person = new Person(name, phone, email);
                databaseHelper.addPerson(person);

                Intent intent = new Intent(AddPersonActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}