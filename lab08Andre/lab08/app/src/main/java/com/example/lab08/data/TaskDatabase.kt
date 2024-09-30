package com.example.lab08terminado

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab08.data.Task
import com.example.lab08.data.TaskDao


@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}