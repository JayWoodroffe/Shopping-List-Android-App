package hu.bme.aut.fna1a3.shoppinglist2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fna1a3.shoppinglist.adapter.ItemAdapter
import hu.bme.aut.fna1a3.shoppinglist2.database.AppDatabase
import hu.bme.aut.fna1a3.shoppinglist2.databinding.ActivityShoppingListBinding
import hu.bme.aut.fna1a3.shoppinglist2.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var  adapter: ItemAdapter
    private lateinit var allItems:List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //adding the adapter for the category filter
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.array_categories_all,
            android.R.layout.simple_spinner_item
        )

        //sets the items in the category spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinCategory.adapter = adapter

        binding.btnNewItem.setOnClickListener{
            startActivity(Intent(this, NewItemActivity::class.java))
        }

        lifecycleScope.launch {
            queryItems()
//            //setUpItemRecyclerView()
//            adapter = ItemAdapter(allItems)
//            binding.itemRecyclerView.layoutManager = LinearLayoutManager(this@ShoppingListActivity)
//            binding.itemRecyclerView.adapter = adapter
            setUpRecyclerView(allItems)

        }


        binding.ivDelete.setOnClickListener{
            showDeleteConfirmationDialog()
        }

        //filtering by category
        binding.spinCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selItemText: String = binding.spinCategory.getItemAtPosition(position).toString()

                // Filter items based on the selected category
                val filteredItems = if (selItemText == "All") {
                    allItems // Show all items from the original list
                } else {
                    allItems.filter { item -> item.category == selItemText }
                }

                setUpRecyclerView(filteredItems)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here if needed
            }
        }
    }




    private fun setUpRecyclerView(items: List<Item>){

        //setUpItemRecyclerView()
        adapter = ItemAdapter(items) { position, status ->
            val currentItem = items[position]
            currentItem.status = status
            updateItemInDatabase(currentItem)
        }
        // Add the new item click listener
        adapter.onItemClick = { position ->
            // Handle item click
            val selectedItem = items[position]

            // Create an Intent to open ShoppingItemDetails and pass the selected item data
            val intent = Intent(this@ShoppingListActivity, ShoppingItemDetails::class.java).apply {
                putExtra("ITEM_EXTRA", selectedItem)
            }

            // Start the ShoppingItemDetails activity
            startActivity(intent)
        }
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(this@ShoppingListActivity)
        binding.itemRecyclerView.adapter = adapter

    }

    private fun updateItemInDatabase(item: Item) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@ShoppingListActivity).itemDao().updateItem(item)
            }
        }
    }


    suspend fun  queryItems(){
        withContext(Dispatchers.IO) {
            allItems = AppDatabase.getInstance(this@ShoppingListActivity).itemDao().getAllItems()
        }

    }



    //delete button function
    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Do you want to delete all items or only selected items?")
        builder.setPositiveButton("Delete All") { _, _ ->
            // Handle delete all items
            deleteAllItems()
        }
        builder.setNegativeButton("Delete Selected") { _, _ ->
            // Handle delete selected items
            lifecycleScope.launch {
                deleteSelectedItems()
            }
        }
        builder.setNeutralButton("Cancel") { _, _ ->
            // Handle cancel
        }
        builder.show()
    }

    private fun deleteAllItems(){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@ShoppingListActivity).itemDao().deleteAllItems()
            }
        }
        allItems = emptyList()
       // lifecycleScope.launch{
        setUpRecyclerView(allItems)
        //}

    }
    private suspend fun deleteSelectedItems() {
        // Filter the items with status = true
        val selectedItems = allItems.filter { it.status }

        // Delete the selected items from the database or perform any other necessary actions
        selectedItems.forEach { item ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    AppDatabase.getInstance(this@ShoppingListActivity).itemDao().deleteItem(item)
                }
            }

        }
            allItems =  allItems.filter{!it.status}
            setUpRecyclerView(allItems)

    }

}