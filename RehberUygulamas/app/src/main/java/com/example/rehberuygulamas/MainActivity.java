package com.example.rehberuygulamas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rehberuygulamas.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        databaseHelper = new DatabaseHelper(this);
        setContentView(view);


        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person newPerson = new Person("Ömer", "211229013");
                databaseHelper.addPerson(newPerson);
                System.out.println("Yeni kullanıcı eklendi!");
                Log.d("MainActivity", "YENİ KULLANICI EKLENDİ !!!");
            }
        });


        binding.dummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Person> personList = new ArrayList<>();

                personList.add(new Person("Ömer", "211229013"));
                personList.add(new Person("Ahmet", "5331234567"));
                personList.add(new Person("Ayşe", "5559876543"));
                personList.add(new Person("Mehmet", "2129876543"));
                personList.add(new Person("Fatma", "5051234567"));

                for (Person person : personList) {
                    databaseHelper.addPerson(person);
                }
            }
        });

        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.resetTable();
                System.out.println("Tüm veriler silindi!");
                Log.d("MainActivity", "TÜM VERİLER SİLİNDİ !!!");
            }
        });



        ArrayList<Person> personList = databaseHelper.getAllPersons();

        ArrayAdapter personArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                personList.stream().map(person -> person.getName()).collect(Collectors.toList()));

        binding.listView.setAdapter(personArrayAdapter);


        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("name", personList.get(position).getName());
                intent.putExtra("phone", personList.get(position).getPhone());
                startActivity(intent);
            }
        });
    }
}
