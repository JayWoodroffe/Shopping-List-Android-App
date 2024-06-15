package hu.bme.aut.fna1a3.shoppinglist2.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping_items")
data class Item (
    @PrimaryKey(autoGenerate = true) var id:Long?,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "status") var status: Boolean,
    @ColumnInfo(name ="estimatedPrice")var estimatedPrice: Double?,
    @ColumnInfo(name = "category") var category:String,
    @ColumnInfo(name ="description")var description: String?,
): Serializable