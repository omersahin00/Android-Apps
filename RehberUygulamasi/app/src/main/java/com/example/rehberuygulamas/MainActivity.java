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
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Person> personList = databaseHelper.getAllPersons();

        if (personList == null || personList.size() == 0){
            personList.add(new Person("Ömer Sahin", "211229013", "omersahin@gmail.com"));
            personList.add(new Person("Ahmet Yılmaz", "5331234567", "ahmet@example.com"));
            personList.add(new Person("Ayşe Demir", "5559876543", "ayse@example.com"));
            personList.add(new Person("Mehmet Kaya", "2129876543", "mehmet@example.com"));
            personList.add(new Person("Fatma Şahin", "5051234567", "fatma@example.com"));

            for (Person person : personList) {
                databaseHelper.addPerson(person);
            }
        }

        ArrayAdapter personArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                personList.stream().map(person -> person.getName()).collect(Collectors.toList()));

        binding.listView.setAdapter(personArrayAdapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Person person = databaseHelper.getPerson(personList.get(position).getId());
                intent.putExtra("person", person);
                startActivity(intent);
            }
        });
    }
}
