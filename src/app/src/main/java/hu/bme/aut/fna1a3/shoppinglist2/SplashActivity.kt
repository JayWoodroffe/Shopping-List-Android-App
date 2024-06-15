package hu.bme.aut.fna1a3.shoppinglist2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.fna1a3.shoppinglist2.databinding.ActivitySplashBinding
import java.util.Timer
import java.util.TimerTask

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                // Start the main activity here
                startActivity(Intent(this@SplashActivity, ShoppingListActivity::class.java))
                finish() // Close the splash activity so that it's not on the back stack
            }
        }, 3000) // 3000 milliseconds (3 seconds) delay
    }
}