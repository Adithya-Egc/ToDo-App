package com.adithyaegc.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adithyaegc.todoapp.data.models.ToDo

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java, "todo_database"
            ).build()
    }


//    companion object {
//
//        @Volatile
//        private var INSTANCE: ToDoDatabase? = null
//
//        fun getDatabase(context: Context): ToDoDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context,
//                    ToDoDatabase::class.java,
//                    "my_db"
//                ).build()
//
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }


}