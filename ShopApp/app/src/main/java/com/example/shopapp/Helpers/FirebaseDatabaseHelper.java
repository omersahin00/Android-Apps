package com.example.shopapp.Helpers;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseHelper<T> {
    private DatabaseReference mDatabase;
    private Class<T> type;

    public FirebaseDatabaseHelper(Class<T> type, String referance) {
        mDatabase = FirebaseDatabase.getInstance().getReference(referance);
        this.type = type;
    }

    public void removeAllData() {
        mDatabase.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Tüm veriler başarıyla silindi.");
            } else {
                Log.e(TAG, "Veriler silinirken hata oluştu.", task.getException());
            }
        });
    }

    public void addData(T data) {
        mDatabase.push().setValue(data);
    }

    public void addDataWithListener(final DataListener<Void> listener, T data) {
        mDatabase.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onDataReceived(null);
                }
                else {
                    listener.onError(task.getException());
                }
            }
        });
    }

    public void getAllData(final DataListener<T> listener) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T item = snapshot.getValue(type);
                    listener.onDataReceived(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public void getOneData(final DataListener<T> listener, String name, String parameter) {
        mDatabase.orderByChild(parameter).equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        T item = snapshot.getValue(type);
                        listener.onDataReceived(item);
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

    public void getOneDataInt(final DataListener<T> listener, int index, String parameter) {
        mDatabase.orderByChild(parameter).equalTo(index).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        T item = snapshot.getValue(type);
                        listener.onDataReceived(item);
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

    public void removeData(String name, String parameter) {
        mDatabase.orderByChild(parameter).equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Veri başarıyla silindi.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Veri silinirken bir hata oluştu.", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Silinecek veri bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Veri silinirken bir hata oluştu.", databaseError.toException());
            }
        });
    }


    public void removeDataWithListener(final DataListener<T> listener, String name, String parameter) {
        mDatabase.orderByChild(parameter).equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        T item = snapshot.getValue(type);
                                        listener.onDataReceived(item);
                                        Log.d(TAG, "Veri başarıyla silindi.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Veri silinirken bir hata oluştu.", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Silinecek veri bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Hesap silinirken bir hata oluştu.", databaseError.toException());
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
