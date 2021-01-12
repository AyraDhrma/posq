package id.co.arya.posq.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.co.arya.posq.data.model.Cart

@Database(entities = [Cart::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): RoomDao
}