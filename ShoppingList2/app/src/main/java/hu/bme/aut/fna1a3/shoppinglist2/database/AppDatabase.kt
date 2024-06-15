package hu.bme.aut.fna1a3.shoppinglist2.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import hu.bme.aut.fna1a3.shoppinglist2.dao.ItemDao
import hu.bme.aut.fna1a3.shoppinglist2.model.Item

@Database(entities = arrayOf(Item::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase::class.java, "shopping_items.db")
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}