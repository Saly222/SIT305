package com.example.task41;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public void insert(Task task) {
        new Thread(() -> taskDao.insert(task)).start();
    }

    public void update(Task task) {
        new Thread(() -> taskDao.update(task)).start();
    }

    public void delete(Task task) {
        new Thread(() -> taskDao.delete(task)).start();
    }


}

