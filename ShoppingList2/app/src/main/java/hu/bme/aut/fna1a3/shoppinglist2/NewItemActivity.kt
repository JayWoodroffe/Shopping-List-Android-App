package hu.bme.aut.fna1a3.shoppinglist2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import hu.bme.aut.fna1a3.shoppinglist2.database.AppDatabase
import hu.bme.aut.fna1a3.shoppinglist2.databinding.ActivityNewItemBinding
import hu.bme.aut.fna1a3.shoppinglist2.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val spinner: Spinner = binding.spinCategory

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.array_categories,
            android.R.layout.simple_spinner_item
        )

        //sets the items in the category spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //creating a new item after submit is clicked
        binding.button.setOnClickListener {
            val selPosSpinner: Int = spinner.selectedItemPosition
            val selItemText:String
            selItemText = if (selPosSpinner != Spinner.INVALID_POSITION) {
                spinner.getItemAtPosition(selPosSpinner).toString()
            } else {
                ""
            }

            val inputText: String = binding.etPrice.text.toString()
            val price: Double? = if (inputText.isNotEmpty()) {
                inputText.toDoubleOrNull()
            } else {
                null
            }


            var newItem = Item(
                null,
                binding.etName.text.toString(),
                binding.cbPurchased.isChecked,
                price,
                selItemText,
                binding.etDescription.text.toString())

            lifecycleScope.launch {
                saveItem(newItem)
                startActivity(Intent(this@NewItemActivity, ShoppingListActivity::class.java))
            }

            startActivity(Intent(this, ShoppingListActivity::class.java))
        }

        //back button clicked
        binding.backIcon.setOnClickListener{
            showDiscardConfirmationDialog()
        }


    }
    suspend fun saveItem(item: Item) {
        withContext(Dispatchers.IO) {
            AppDatabase.getInstance(this@NewItemActivity).itemDao().insertItem(item)
        }
    }

    private fun showDiscardConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Discard Item")
            .setMessage("Are you sure you want to discard the current item and return to the shopping list?")
            .setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes, navigate to the shopping list activity
                startActivity(Intent(this, ShoppingListActivity::class.java))
            }
            .setNegativeButton("No") { dialog, which ->
                // User clicked No, close the dialog (do nothing)
                dialog.dismiss()
            }
            .show()
    }
}