package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.databinding.ActivityAccountDetailsBinding;

public class AccountDetailsActivity extends AppCompatActivity {
    private ActivityAccountDetailsBinding binding;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);

        binding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SetAccount();
        SetLayout();
        SetButtonsListener();
    }

    private void SetAccount() {
        if (FileHelper.readFromFile(AccountDetailsActivity.this, "isAuth").contains("true")) {
            String userName = FileHelper.readFromFile(AccountDetailsActivity.this, "account");
            if (!userName.isEmpty()) {
                FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
                accountFirebaseDatabaseHelper.getOneData(new FirebaseDatabaseHelper.DataListener<Account>() {
                    @Override
                    public void onDataReceived(Account data) {
                        if (data != null) {
                            account = data;
                            SetLayout();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }
                }, userName, "name");
            }
            else {
                binding.accountErrorButton.setText("Oturum açılmamış!");
            }
        }
        else {
            binding.accountErrorButton.setText("Oturum açılmamış!");
        }
    }


    private void SetLayout() {
        if (account != null) {
            binding.accountNameText.setText(account.getName());
            binding.accountPasswordText.setText(account.getPassword());
            binding.balanceAccountText.setText(String.valueOf(account.getBalance()));
        }
        else {
            binding.accountNameText.setText("Yükleniyor...");
            binding.accountPasswordText.setText("Yükleniyor...");
            binding.balanceAccountText.setText("Yükleniyor...");
        }
    }

    private void SetButtonsListener() {
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Diğer butonlar da yazılacak.
        // Diğer butonlar da yazılacak.
        // Diğer butonlar da yazılacak.
        // Diğer butonlar da yazılacak.
    }
}














