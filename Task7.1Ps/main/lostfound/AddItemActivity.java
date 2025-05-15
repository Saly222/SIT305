package com.example.lostfound;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class AddItemActivity extends AppCompatActivity {

    EditText titleInput, descInput, locInput, dateInput, contactInput;
    RadioGroup statusGroup;
    RadioButton radioLost, radioFound;

    Button btnSave;
    LostFoundDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        titleInput = findViewById(R.id.inputTitle);
        descInput = findViewById(R.id.inputDescription);
        locInput = findViewById(R.id.inputLocation);
        dateInput = findViewById(R.id.inputDate);
        contactInput = findViewById(R.id.inputContact);
        statusGroup = findViewById(R.id.radioGroup);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        btnSave = findViewById(R.id.submitButton);

        dbHelper = new LostFoundDbHelper(this);

        btnSave.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(LostFoundDbHelper.COLUMN_TITLE, titleInput.getText().toString());
            values.put(LostFoundDbHelper.COLUMN_DESCRIPTION, descInput.getText().toString());
            values.put(LostFoundDbHelper.COLUMN_LOCATION, locInput.getText().toString());
            values.put(LostFoundDbHelper.COLUMN_DATE, dateInput.getText().toString());
            values.put(LostFoundDbHelper.COLUMN_CONTACT, contactInput.getText().toString());
            String status = radioLost.isChecked() ? "Lost" : "Found";
            values.put(LostFoundDbHelper.COLUMN_STATUS, status);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long newRowId = db.insert(LostFoundDbHelper.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Item Saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error Saving!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
