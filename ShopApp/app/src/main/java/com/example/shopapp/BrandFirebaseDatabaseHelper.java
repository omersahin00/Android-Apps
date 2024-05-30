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

public class BrandFirebaseDatabaseHelper {
    private DatabaseReference mDatabase;
    private Object object;

    public BrandFirebaseDatabaseHelper(Object object) {
        mDatabase = FirebaseDatabase.getInstance().getReference("brands");
        this.object = object;
    }

    public void addData(Object data) {
        mDatabase.push().setValue(data);
    }

    public void getAllBrands(final BrandFirebaseDatabaseHelper.DataListener<Brand> listener) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Brand brand = snapshot.getValue(Brand.class);
                    listener.onDataReceived(brand);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public void getOneBrands(final BrandFirebaseDatabaseHelper.DataListener<Brand> listener, String userName) {
        mDatabase.orderByChild("name").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Brand brand = snapshot.getValue(Brand.class);
                        listener.onDataReceived(brand);
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

    public void removeBrand(String brandName) {
        mDatabase.orderByChild("name").equalTo(brandName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Marka başarıyla silindi.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Marka silinirken bir hata oluştu.", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Silinecek marka bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Marka silinirken bir hata oluştu.", databaseError.toException());
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
