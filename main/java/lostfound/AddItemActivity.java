package com.example.lostfound;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    EditText titleInput, descInput, locInput, dateInput, contactInput;
    RadioGroup statusGroup;
    RadioButton radioLost, radioFound;
    Button btnSave, btnGetCurrentLocation;

    LostFoundDbHelper dbHelper;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        titleInput = findViewById(R.id.inputTitle);
        descInput = findViewById(R.id.inputDescription);
        locInput = findViewById(R.id.inputLocation);
        dateInput = findViewById(R.id.inputDate);
        contactInput = findViewById(R.id.inputContact);
        statusGroup = findViewById(R.id.radioGroup);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        btnSave = findViewById(R.id.submitButton);
        btnGetCurrentLocation = findViewById(R.id.currentLocationButton);

        dbHelper = new LostFoundDbHelper(this);


        locInput.setFocusable(false);
        locInput.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        btnGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());

        btnSave.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descInput.getText().toString().trim();
            String location = locInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String contact = contactInput.getText().toString().trim();
            String status = radioLost.isChecked() ? "Lost" : "Found";

            if (title.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Title and Location are required", Toast.LENGTH_SHORT).show();
                return;
            }


            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            double latitude = 0.0;
            double longitude = 0.0;
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    latitude = address.getLatitude();
                    longitude = address.getLongitude();
                } else {
                    Toast.makeText(this, "Unable to find location coordinates", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Geocoding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put(LostFoundDbHelper.COLUMN_TITLE, title);
            values.put(LostFoundDbHelper.COLUMN_DESCRIPTION, description);
            values.put(LostFoundDbHelper.COLUMN_LOCATION, location);
            values.put(LostFoundDbHelper.COLUMN_LATITUDE, latitude);
            values.put(LostFoundDbHelper.COLUMN_LONGITUDE, longitude);
            values.put(LostFoundDbHelper.COLUMN_DATE, date);
            values.put(LostFoundDbHelper.COLUMN_CONTACT, contact);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                locInput.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(this, "Error getting place", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

            // Dummy coordinates for Burwood, Victoria
            double dummyLatitude = -37.8506;
            double dummyLongitude = 145.1024;


            Location dummyLocation = new Location("dummyprovider");
            dummyLocation.setLatitude(dummyLatitude);
            dummyLocation.setLongitude(dummyLongitude);


            setLocationField(dummyLocation);

            Toast.makeText(this, "Using dummy location: Burwood, Victoria", Toast.LENGTH_SHORT).show();
        }



        private void setLocationField(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder fullAddress = new StringBuilder();

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    fullAddress.append(address.getAddressLine(i));
                    if (i != address.getMaxAddressLineIndex()) {
                        fullAddress.append(", ");
                    }
                }
                locInput.setText(fullAddress.toString());
            } else {
                Toast.makeText(this, "No address found for location", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Geocoder failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
