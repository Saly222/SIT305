package com.example.lostfound;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowItemsActivity extends AppCompatActivity {

    ListView listView;
    LostFoundDbHelper dbHelper;
    ArrayList<String> itemList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<Integer> itemIds = new ArrayList<>();

    private static final int REQUEST_DETAIL = 1;  // request code for ItemDetailActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        listView = findViewById(R.id.listView);
        dbHelper = new LostFoundDbHelper(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("item_id", itemIds.get(position));
            // Start for result so we can refresh list after deletion
            startActivityForResult(intent, REQUEST_DETAIL);
        });

        loadItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If we returned from ItemDetailActivity and an item was deleted, reload list
        if (requestCode == REQUEST_DETAIL && resultCode == RESULT_OK) {
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
}
