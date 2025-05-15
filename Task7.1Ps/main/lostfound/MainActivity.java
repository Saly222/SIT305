package com.example.lostfound;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button btnAddNew, btnShow;
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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", itemIds.get(position));
            startActivityForResult(intent, 1); // Use this to get result
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();  // Always refresh the list
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadItems(); // Reload the list if item was deleted
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
}
