package com.example.lostfound;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button btnAddNew, btnShow, btnShowMap;
    LostFoundDbHelper dbHelper;
    ArrayList<String> itemList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<Integer> itemIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnAddNew = findViewById(R.id.addButton);
        btnShow = findViewById(R.id.showButton);
        btnShowMap = findViewById(R.id.showMapButton);

        dbHelper = new LostFoundDbHelper(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        btnAddNew.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddItemActivity.class));
        });

        btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowItemsActivity.class);
            startActivity(intent);
        });

        btnShowMap.setOnClickListener(v -> {
            ArrayList<LostFoundItem> allItems = getAllLostAndFoundItems();


            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putParcelableArrayListExtra("items", allItems);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", itemIds.get(position));
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadItems();
        }
    }

    private void loadItems() {
        itemList.clear();
        itemIds.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(LostFoundDbHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_TITLE));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_STATUS));
            itemList.add(title + " [" + status + "]");
            itemIds.add(id);
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }


    private ArrayList<LostFoundItem> getAllLostAndFoundItems() {
        ArrayList<LostFoundItem> itemList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(LostFoundDbHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_TITLE));
            String status = cursor.getString((cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_STATUS)));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_LONGITUDE));

            LostFoundItem item = new LostFoundItem(title, status, latitude, longitude);
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }
}
