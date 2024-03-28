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
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        databaseHelper = new DatabaseHelper(this);
        setContentView(view);


        Intent intent = getIntent();
        Person person = (Person) intent.getSerializableExtra("person");

        binding.nameText.setText(person.getName());
        binding.phoneText.setText(person.getPhone());
        binding.emailText.setText(person.getEmail());


        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = binding.nameText.getText().toString();
                String newPhone = binding.phoneText.getText().toString();
                String newEmail = binding.emailText.getText().toString();

                person.setName(newName);
                person.setPhone(newPhone);
                person.setEmail(newEmail);

                databaseHelper.updatePerson(person);

                Intent mainIntent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}

