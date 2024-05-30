package com.example.shopapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseHelper {
    private DatabaseReference mDatabase;

    public FirebaseDatabaseHelper(Object object) {
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
    }

    public void addData(Object data) {
        mDatabase.push().setValue(data);
    }

    public void getAllAccounts(final DataListener<Account> listener) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Account account = snapshot.getValue(Account.class);
                    listener.onDataReceived(account);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public void getOneAccounts(final DataListener<Account> listener, String userName) {
        mDatabase.orderByChild("userName").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        listener.onDataReceived(account);
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

    public DatabaseReference getDataReference() {
        return mDatabase;
    }

    public interface DataListener<T> {
        void onDataReceived(T data);
        void onError(Exception e);
    }
}
