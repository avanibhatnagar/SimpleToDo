package com.example.avani.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //field declarations - objects associated with any instance of main activity - null until initialized
    ArrayList<String> items; //array list of strings/ items
    ArrayAdapter<String> itemsAdapter;//adapter- intermediary object that wires our model(list) to the view
    ListView lvItems; //instance of list view itself

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);//1st arg - reference to activity - here this - reference to main activity, 2nd - type of item the adapter will wrap - here android.R.layout, 3rd- item list created
        lvItems = (ListView) findViewById(R.id.lvItems);//reference to list view to wire adapter to it. but android already created a list view - so no new instance need to be created -resolve already existing instance using id assigned in design view // need to cast id to list view
        lvItems.setAdapter(itemsAdapter);//wire adapter to list view

        //mockdata
//        items.add("First item");
//        items.add("Second item");

        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText eNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = eNewItem.getText().toString();
        itemsAdapter.add(itemText);
        eNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item Added to list", Toast.LENGTH_SHORT).show(); //short message
    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity","Item removed from the list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }

    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
