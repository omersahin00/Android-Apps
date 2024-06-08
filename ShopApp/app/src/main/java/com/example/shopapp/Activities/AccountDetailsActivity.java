package com.example.shopapp.Activities;

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

import com.example.shopapp.Models.Account;
import com.example.shopapp.Models.Favorites;
import com.example.shopapp.Helpers.FileHelper;
import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.R;
import com.example.shopapp.Models.ShoppingCart;
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
                binding.accountInfoText.setTextColor(Color.RED);
                binding.accountInfoText.setText("Oturum açılmamış!");
            }
        }
        else {
            binding.accountInfoText.setTextColor(Color.RED);
            binding.accountInfoText.setText("Oturum açılmamış!");
        }
    }


    private void SetLayout() {
        if (account != null) {
            binding.accountNameText.setText(account.getName());
            binding.accountPasswordText.setText(account.getPassword());
            binding.balanceAccountText.setText(String.valueOf(account.getBalance()) + " TL");
        }
        else {
            binding.accountNameText.setText("Yükleniyor...");
            binding.accountPasswordText.setText("Yükleniyor...");
            binding.balanceAccountText.setText("Yükleniyor...");
        }
    }

    private void SetButtonsListener() {
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileHelper.readFromFile(AccountDetailsActivity.this, "isAuth").contains("true")) {

                    FileHelper.deleteFile(AccountDetailsActivity.this, "isAuth");
                    FileHelper.writeToFile(AccountDetailsActivity.this, "isAuth", "false");
                    FileHelper.deleteFile(AccountDetailsActivity.this, "account");
                    FileHelper.writeToFile(AccountDetailsActivity.this, "account", "null");

                    Intent intent = new Intent(AccountDetailsActivity.this, MainActivity.class);
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

        binding.accountDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.accountDeleteButton.setVisibility(View.INVISIBLE);
                binding.accountSaveButton.setVisibility(View.INVISIBLE);
                binding.deleteConfirmationBox.setVisibility(View.VISIBLE);
                binding.buttonLogout.setVisibility(View.INVISIBLE);
            }
        });

        binding.accountCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.deleteConfirmationBox.setVisibility(View.INVISIBLE);
                binding.accountDeleteButton.setVisibility(View.VISIBLE);
                binding.accountSaveButton.setVisibility(View.VISIBLE);
                binding.buttonLogout.setVisibility(View.VISIBLE);
            }
        });

        binding.accountReDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccount();
            }
        });

        binding.accountSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.accountInfoText.setTextColor(Color.RED);
                binding.accountInfoText.setText("Şifreniz güncelleniyor...");
                SaveAccount();
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
                DeleteAccountCarts();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }


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

            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, account.getName(), "userName");
    }

    private void SaveAccount() {
        FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
        String newPassowrd = binding.accountPasswordText.getText().toString();

        accountFirebaseDatabaseHelper.removeDataWithListener(new FirebaseDatabaseHelper.DataListener<Account>() {
            @Override
            public void onDataReceived(Account data) {
                account.setPassword(String.valueOf(newPassowrd));
                accountFirebaseDatabaseHelper.addDataWithListener(new FirebaseDatabaseHelper.DataListener<Void>() {
                    @Override
                    public void onDataReceived(Void data) {
                        binding.accountInfoText.setTextColor(Color.GREEN);
                        binding.accountInfoText.setText("Şifreniz başarıyla güncellendi.");
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }
                }, account);
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, account.getName(), "name");
    }
}






