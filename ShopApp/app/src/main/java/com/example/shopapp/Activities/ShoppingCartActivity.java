package com.example.shopapp.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopapp.Models.Account;
import com.example.shopapp.Helpers.FileHelper;
import com.example.shopapp.Helpers.FirebaseDatabaseHelper;
import com.example.shopapp.Models.Product;
import com.example.shopapp.R;
import com.example.shopapp.Models.ShoppingCart;
import com.example.shopapp.Adapters.ShoppingCartAdapter;
import com.example.shopapp.databinding.ActivityShoppingCartBinding;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    private ActivityShoppingCartBinding binding;
    List<Product> shoppingCartList;
    ShoppingCartAdapter shoppingCartAdapter;
    String userName;
    Account account;
    public int totalPrice = 0;
    public int addedPrice = 0;
    private boolean isBuying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = FileHelper.readFromFile(ShoppingCartActivity.this, "account");

        FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
        accountFirebaseDatabaseHelper.getOneData(new FirebaseDatabaseHelper.DataListener<Account>() {
            @Override
            public void onDataReceived(Account data) {
                if (data != null) {
                    account = data;
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }, userName, "name");

        binding.buyConfirmationBox.setVisibility(View.INVISIBLE);

        SetCartList();

        shoppingCartAdapter = new ShoppingCartAdapter(this, shoppingCartList, userName);
        binding.listView.setAdapter(shoppingCartAdapter);

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SetPriceLayouts();
        SetBuyLayout();
    }


    public void SetCartList() {
        FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");
        List<ShoppingCart> dumpCartList = new ArrayList<>();
        shoppingCartList = new ArrayList<>();
        shoppingCartFirebaseDatabaseHelper.getAllData(new FirebaseDatabaseHelper.DataListener<ShoppingCart>() {
            @Override
            public void onDataReceived(ShoppingCart data) {
                if (data != null) {
                    if (data.getUserName().equals(userName)) {
                        dumpCartList.add(data);
                        AddShoppingList(data.getProductIndex());
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    public void AddShoppingList(int productIndex) {
        FirebaseDatabaseHelper<Product> productFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Product.class, "products");
        productFirebaseDatabaseHelper.getOneDataInt(new FirebaseDatabaseHelper.DataListener<Product>() {
            @Override
            public void onDataReceived(Product data) {
                shoppingCartList.add(data);
                shoppingCartAdapter.notifyDataSetChanged();
                SetPriceLayouts();
            }
            @Override
            public void onError(Exception e) {

            }
        }, productIndex, "index");
    }

    public void SetPriceLayouts() {
        int cartPrice = 0 + addedPrice;
        int cargoPrice = 0;

        for (Product product : shoppingCartList) {
            cartPrice += product.getPrice();
        }

        binding.cartPriceText.setText(cartPrice + " TL");

        if (cartPrice < 500) {
            cargoPrice += (shoppingCartList.size() * 50);
            binding.cargoPriceText.setText(cargoPrice + " TL");
        }
        else binding.cargoPriceText.setText("0 TL");

        totalPrice = cartPrice + cargoPrice;
        binding.totalPriceText.setText(totalPrice + " TL");

        if (totalPrice <= 0) {
            binding.buyButton.setVisibility(View.INVISIBLE);
        }
        else {
            binding.buyButton.setVisibility(View.VISIBLE);
        }
    }

    private void SetBuyLayout() {
        binding.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buyInfoText.setText("");
                binding.balanceText.setText(String.valueOf(account.getBalance()) + " TL");
                binding.buyPriceText.setText(binding.totalPriceText.getText());

                binding.buyConfirmationBox.setVisibility(View.VISIBLE);
                binding.buyButton.setVisibility(View.INVISIBLE);
            }
        });

        binding.cancelBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buyConfirmationBox.setVisibility(View.INVISIBLE);
                binding.buyButton.setVisibility(View.VISIBLE);
            }
        });

        binding.confirmBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBuying) {
                    if (account.getBalance() < totalPrice) {
                        binding.buyInfoText.setText("Yeterli bakiyeniz bulunmamakta!");
                        return;
                    }

                    binding.buyInfoText.setText("Sparişiniz oluşturuluyor...");
                    int balance = account.getBalance();
                    balance -= totalPrice;
                    account.setBalance(balance);

                    FirebaseDatabaseHelper<Account> accountFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(Account.class, "accounts");
                    FirebaseDatabaseHelper<ShoppingCart> shoppingCartFirebaseDatabaseHelper = new FirebaseDatabaseHelper<>(ShoppingCart.class, "shoppingCarts");

                    accountFirebaseDatabaseHelper.removeDataWithListener(new FirebaseDatabaseHelper.DataListener<Account>() {
                        @Override
                        public void onDataReceived(Account data) {
                            accountFirebaseDatabaseHelper.addDataWithListener(new FirebaseDatabaseHelper.DataListener<Void>() {
                                @Override
                                public void onDataReceived(Void data) {

                                    shoppingCartFirebaseDatabaseHelper.removeDataWithListener(new FirebaseDatabaseHelper.DataListener<ShoppingCart>() {
                                        @Override
                                        public void onDataReceived(ShoppingCart data) {
                                            binding.buyInfoText.setText("Sparişiniz alındı.");
                                            binding.confirmBuyingButton.setText("Ana sayfaya Dön");
                                            binding.cancelBuyingButton.setVisibility(View.INVISIBLE);
                                            binding.cartLayout.setVisibility(View.INVISIBLE);
                                            binding.BakiyenizText.setText("Güncel Bakiyeniz:");
                                            binding.balanceText.setText(String.valueOf(account.getBalance()));
                                            isBuying = true;
                                            shoppingCartAdapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onError(Exception e) {
                                            Log.e(TAG, "onError: ", e);
                                        }
                                    }, account.getName(), "userName");
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
                else {
                    finish();
                }
            }
        });
    }
}






