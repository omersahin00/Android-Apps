package com.example.shopapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.Models.Account;
import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.R;
import com.example.shopapp.databinding.ActivityRegisterBinding;
import com.google.firebase.FirebaseApp;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCreated) {
                    String userName = binding.userNameText.getText().toString();
                    String password = binding.passwordText.getText().toString();

                    if (userName.isEmpty()) {
                        binding.errorText.setText("Kullanıcı adı boş bırakılamaz.");
                        return;
                    }
                    if (password.isEmpty()) {
                        binding.errorText.setText("Şifre alanı boş bırakılamaz.");
                        return;
                    }

                    accountFirebaseDatabaseHelper.getOneData(new FirebaseDatabaseHelper.DataListener<Account>() {
                        @Override
                        public void onDataReceived(Account data) {
                            if (data != null) {
                                binding.errorText.setText("Böyle bir kullanıcı mevcut.");
                            }
                            else {
                                Account account = new Account(userName, password, 10000);
                                accountFirebaseDatabaseHelper.addData(account);
                                binding.errorText.setText("Kayıt olma başarılı.");
                                binding.registerButton.setText("Oturum Aç");
                                isCreated = true;
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            binding.errorText.setText("Bir hata oluştu.");
                        }
                    }, userName, "name");
                }
                else {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
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