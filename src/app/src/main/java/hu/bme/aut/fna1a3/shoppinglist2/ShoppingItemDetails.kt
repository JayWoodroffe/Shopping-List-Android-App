package hu.bme.aut.fna1a3.shoppinglist2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.bme.aut.fna1a3.shoppinglist2.databinding.ActivityShoppingItemDetailsBinding
import hu.bme.aut.fna1a3.shoppinglist2.model.Item
import hu.bme.aut.fna1a3.shoppinglist2.model.Money
import hu.bme.aut.fna1a3.shoppinglist2.network.MoneyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class ShoppingItemDetails : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingItemDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityShoppingItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting up retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val currencyAPI = retrofit.create(MoneyAPI::class.java)

        val item: Item? = intent.getSerializableExtra("ITEM_EXTRA") as? Item



        // Check if the item is not null
        if (item != null) {
            // getting the properties of the item
            val itemName = item.itemName
            val category = item.category
            val description = item.description
            val status = item.status
            val price = item.estimatedPrice

            //getting the exhange rates
            val moneyCall = currencyAPI.getMoney("HUF")
            moneyCall.enqueue(object : Callback<Money> {
                override fun onFailure(call: Call<Money>, t: Throwable) {
                    println(t.message)

                }
                override fun onResponse(call: Call<Money>, response: Response<Money>) {
                    var moneyResult = response.body()

                    val gbpToHufConversionRate = moneyResult?.rates?.GBP
                    var gbpAmount = 0.0
                    if (gbpToHufConversionRate != null) {
                        // Perform the conversion
                        if (price != null) {
                            gbpAmount = price * gbpToHufConversionRate
                        }
                    }
                    val usdToHufConversionRate = moneyResult?.rates?.USD
                    var usdAmount = 0.0
                    if (usdToHufConversionRate != null) {
                        // Perform the conversion
                        if (price != null) {
                            usdAmount = price * usdToHufConversionRate
                        }
                    }

                    val hkdToHufConversionRate = moneyResult?.rates?.HKD
                    var hkdAmount = 0.0
                    if (hkdToHufConversionRate != null) {
                        // Perform the conversion
                        if (price != null) {
                            hkdAmount = price * hkdToHufConversionRate
                        }
                    }
                    val zarToHufConversionRate = moneyResult?.rates?.ZAR
                    var zarAmount = 0.0
                    if (zarToHufConversionRate != null) {
                        // Perform the conversion
                        if (price != null) {
                            zarAmount = price * zarToHufConversionRate
                        }
                    }


                    binding.tvGBPData.text = "£${gbpAmount}"
                    binding.tvUSDData.text = "\$${usdAmount}"
                    binding.tvHKDData.text = "HK\$${hkdAmount}"
                    binding.tvZARData.text = "R${zarAmount}"
                }
            })

            //adding the data to the components
            binding.tvItemNameData.text = itemName
            binding.tvCategoryData.text = category
            binding.tvDescData.text = description
            binding.tvHUFData.text = ""+ price + " HUF"
            binding.chPurchased.isChecked = status
            binding.chPurchased.isClickable = false

            binding.btnBack.setOnClickListener {
                startActivity(Intent(this, ShoppingListActivity::class.java))
            }
        }
    }
}