package com.example.shopapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductFirebaseDatabaseHelper {
    private DatabaseReference mDatabase;
    private Object object;

    public ProductFirebaseDatabaseHelper(Object object) {
        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        this.object = object;
    }

    public void addData(Object data) {
        mDatabase.push().setValue(data);
    }

    public void getAllProducts(final ProductFirebaseDatabaseHelper.DataListener<Product> listener) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    listener.onDataReceived(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public void getOneProducts(final ProductFirebaseDatabaseHelper.DataListener<Product> listener, String userName) {
        mDatabase.orderByChild("name").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        listener.onDataReceived(product);
                        return;
                    }
                } else {
                    listener.onDataReceived(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public void removeProduct(String productName) {
        mDatabase.orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Ürün başarıyla silindi.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Ürün silinirken bir hata oluştu.", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Silinecek ürün bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Ürün silinirken bir hata oluştu.", databaseError.toException());
            }
        });
    }

    public DatabaseReference getDataReference() {
        return mDatabase;
    }

    public interface DataListener<T> {
        void onDataReceived(T data);
        void onError(Exception e);
    }
}
