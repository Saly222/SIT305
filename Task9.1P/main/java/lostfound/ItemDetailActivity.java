package com.example.lostfound;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    TextView itemDetails;
    Button deleteButton;
    int itemId;
    LostFoundDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemDetails = findViewById(R.id.itemDetails);
        deleteButton = findViewById(R.id.btnDelete);

        dbHelper = new LostFoundDbHelper(this);
        itemId = getIntent().getIntExtra("item_id", -1);

        loadItemDetails();

        deleteButton.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(LostFoundDbHelper.TABLE_NAME, LostFoundDbHelper.COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

    private void loadItemDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(LostFoundDbHelper.TABLE_NAME, null,
                LostFoundDbHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(itemId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            String info = "Name: " + cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_TITLE)) + "\n"
                    + "Phone: " + cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_CONTACT)) + "\n"
                    + "Description: " + cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_DESCRIPTION)) + "\n"
                    + "Date: " + cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_DATE)) + "\n"
                    + "Location: " + cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_LOCATION)) + "\n"
                    + "Status: " + cursor.getString(cursor.getColumnIndexOrThrow(LostFoundDbHelper.COLUMN_STATUS));
            itemDetails.setText(info);
        }

        cursor.close();
    }
}
