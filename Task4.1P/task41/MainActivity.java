package com.example.task41;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private ListView listView;
    private TaskAdapter taskAdapter;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        addButton = findViewById(R.id.addButton);

        // Initialize the ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Initialize the adapter and pass the ViewModel
        taskAdapter = new TaskAdapter(this, new ArrayList<>(), taskViewModel);
        listView.setAdapter(taskAdapter);

        // Observe the LiveData for tasks
        taskViewModel.getAllTasks().observe(this, tasks -> {
            if (tasks != null) {
                taskAdapter.setTasks(tasks);
            }
        });

        // Add button click
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });
    }
}
