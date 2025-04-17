package com.example.task41;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddTaskActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, dueDateEditText;
    private Button saveButton;
    private TaskViewModel taskViewModel;

    private int taskId = -1; // Used for updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        dueDateEditText = findViewById(R.id.editTextDueDate);
        saveButton = findViewById(R.id.saveButton);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Check if we're updating an existing task
        if (getIntent().hasExtra("TASK_ID")) {
            taskId = (int) getIntent().getLongExtra("TASK_ID", -1L);
            titleEditText.setText(getIntent().getStringExtra("TASK_TITLE"));
            descriptionEditText.setText(getIntent().getStringExtra("TASK_DESCRIPTION"));
            dueDateEditText.setText(getIntent().getStringExtra("TASK_DUE_DATE"));

            saveButton.setText("Update Task");  // Change button text to 'Update Task' for editing
        } else {
            saveButton.setText("Add Task"); // Ensure button shows 'Add Task' for new tasks
        }
        Log.d("AddTaskActivity", "Task ID: " + taskId);
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String dueDate = dueDateEditText.getText().toString().trim();

            Log.d("AddTaskActivity", "Title: " + title + ", Description: " + description + ", Due Date: " + dueDate);
            if (!title.isEmpty() && !dueDate.isEmpty()) {
                Task task = new Task(title, description, dueDate);

                if (taskId != -1) {
                    // Updating existing task
                    task.setId(taskId);
                    taskViewModel.update(task);  // Call ViewModel's update method
                    Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
                } else {
                    // Inserting a new task
                    taskViewModel.insert(task);
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
                }

                finish(); // Close this activity and return to MainActivity
            } else {
                Toast.makeText(this, "Title, Description and Due Date are required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
