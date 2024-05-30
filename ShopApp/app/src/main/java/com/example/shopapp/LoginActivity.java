package com.example.shopapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.databinding.ActivityLoginBinding;
import com.example.shopapp.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper(Account.class);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.userNameText.getText().toString();
                String password = binding.passwordText.getText().toString();

                firebaseDatabaseHelper.getOneAccounts(new FirebaseDatabaseHelper.DataListener<Account>() {
                    @Override
                    public void onDataReceived(Account data) {
                        if (data != null) {
                            if (data.getPassword().equals(password)) {
                                binding.errorText.setText("Giriş yapıldı.");
                            }
                            else {
                                binding.errorText.setText("Şifre hatalı.");
                            }
                        }
                        else {
                            binding.errorText.setText("Kullanıcı bulunamadı.");
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        binding.errorText.setText("Bir hata oluştu.");
                    }
                }, userName);
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}