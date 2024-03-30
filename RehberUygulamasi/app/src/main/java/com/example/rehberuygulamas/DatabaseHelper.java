package com.example.rehberuygulamas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "telephone_book";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "Persons";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    // Kişi ekleme:
    public void addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, person.getName());
        values.put(COLUMN_PHONE, person.getPhone());
        values.put(COLUMN_EMAIL, person.getEmail());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    // ID'ye göre 1 kişi getirme:
    public Person getPerson(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_EMAIL},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return person;
    }


    // Tüm kişileri getirme:
    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> personList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(cursor.getInt(0));
                person.setName(cursor.getString(1));
                person.setPhone(cursor.getString(2));
                person.setEmail(cursor.getString(3));
                personList.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return personList;
    }


    // Kişiyi güncelleme:
    public int updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, person.getName());
        values.put(COLUMN_PHONE, person.getPhone());
        values.put(COLUMN_EMAIL, person.getEmail());
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(person.getId())});
    }


    // Kişi silme:
    public void deletePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(person.getId())});
        db.close();
    }


    public void resetTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
