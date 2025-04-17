package com.example.task41;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private TaskViewModel taskViewModel;

    // Constructor updated to receive TaskViewModel as a parameter
    public TaskAdapter(Context context, List<Task> tasks, TaskViewModel taskViewModel) {
        super(context, 0, tasks);
        this.taskViewModel = taskViewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        // Get the task at the current position
        Task task = getItem(position);

        // Find the views in the layout
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView dueDateTextView = convertView.findViewById(R.id.dueDateTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button updateButton = convertView.findViewById(R.id.updateButton);

        // Set the data to the views
        if (task != null) {
            titleTextView.setText(task.getTitle());
            dueDateTextView.setText(task.getDueDate());

            // Handle Update button click
            updateButton.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra("TASK_ID", task.getId());  // Pass task ID for updating
                intent.putExtra("TASK_TITLE", task.getTitle());
                intent.putExtra("TASK_DESCRIPTION", task.getDescription());
                intent.putExtra("TASK_DUE_DATE", task.getDueDate());
                getContext().startActivity(intent);
            });

            // Handle Delete button click
            deleteButton.setOnClickListener(v -> {
                taskViewModel.delete(task);
                Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

    // Method to update the list of tasks in the adapter
    public void setTasks(List<Task> tasks) {
        clear();  // Clear old tasks
        addAll(tasks);  // Add new tasks
        notifyDataSetChanged();  // Notify the adapter to refresh the view
    }
}
