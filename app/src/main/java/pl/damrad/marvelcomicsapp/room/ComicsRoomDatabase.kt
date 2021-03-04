package pl.damrad.marvelcomicsapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.damrad.marvelcomicsapp.room.model.Comics

@Database(entities = [Comics::class], version = 1, exportSchema = false)
abstract class ComicsRoomDatabase : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao

    companion object {
        @Volatile
        private var INSTANCE: ComicsRoomDatabase? = null

        fun getDatabase(context: Context): ComicsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ComicsRoomDatabase::class.java,
                    "comics_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}