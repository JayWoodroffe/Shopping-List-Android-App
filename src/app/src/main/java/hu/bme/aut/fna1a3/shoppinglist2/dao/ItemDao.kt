package hu.bme.aut.fna1a3.shoppinglist2.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import hu.bme.aut.fna1a3.shoppinglist2.model.Item
import androidx.room.Update


@Dao
interface ItemDao {
    @Query("SELECT * FROM shopping_items")
    fun getAllItems(): List<Item>

    @Insert
    fun insertItem(vararg item: Item)


    @Update
    fun updateItem(item: Item)

    @Query("DELETE FROM shopping_items")
    fun deleteAllItems()

    @Delete
    fun deleteItem(item: Item)

}

