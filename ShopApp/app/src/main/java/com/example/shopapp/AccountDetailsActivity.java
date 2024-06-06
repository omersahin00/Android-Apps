package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.databinding.ActivityAccountDetailsBinding;

import java.util.ArrayList;
import java.util.List;

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

        binding.deleteConfirmationBox.setVisibility(View.INVISIBLE);

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

        binding.accountDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.accountDeleteButton.setVisibility(View.INVISIBLE);
                binding.accountSaveButton.setVisibility(View.INVISIBLE);
                binding.deleteConfirmationBox.setVisibility(View.VISIBLE);
            }
        });

        binding.accountCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.deleteConfirmationBox.setVisibility(View.INVISIBLE);
                binding.accountDeleteButton.setVisibility(View.VISIBLE);
                binding.accountSaveButton.setVisibility(View.VISIBLE);
            }
        });

        binding.accountReDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccount();
            }
        });
    }


    private void DeleteAccount() {
        binding.deletionWarningText.setText("Hesabınız siliniyor...");

        FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
        accountFirebaseDatabaseHelper.removeDataWithListener(new FirebaseDatabaseHelper.DataListener<Account>() {
            @Override
            public void onDataReceived(Account data) {
                DeleteAccountFavorites();
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
                binding.deletionWarningText.setText("Hesap silinirken bir sorun oluştu.");
                finish();
            }
        }, account.getName(), "name");
    }

    private void DeleteAccountFavorites() {
        FirebaseDatabaseHelper<Favorites> favoritesFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Favorites.class, "favorites");

        favoritesFirebaseDatabaseHelper.removeDataWithListener(new FirebaseDatabaseHelper.DataListener<Favorites>() {
            @Override
            public void onDataReceived(Favorites data) {
                DeleteAccountCarts();
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, account.getName(), "userName");
    }

    private void DeleteAccountCarts() {
        FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");

        shoppingCartFirebaseDatabaseHelper.removeDataWithListener(new FirebaseDatabaseHelper.DataListener<ShoppingCart>() {
            @Override
            public void onDataReceived(ShoppingCart data) {
                FileHelper.deleteFile(AccountDetailsActivity.this, "isAuth");
                FileHelper.writeToFile(AccountDetailsActivity.this, "isAuth", "false");
                FileHelper.deleteFile(AccountDetailsActivity.this, "account");

                binding.deletionWarningText.setTextColor(Color.green(1));
                binding.deletionWarningText.setText("Hesabınız silindi...");

                Intent intent = new Intent(AccountDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, account.getName(), "userName");
    }
}






