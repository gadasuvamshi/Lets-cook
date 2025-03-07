package com.example.recipeapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvAdapter: PopularAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        setUpWebView()

        binding.editTextText2.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.salad.setOnClickListener {
            val myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITLE", "Salad")
            myIntent.putExtra("CATEGORY", "Salad")
            startActivity(myIntent)
        }

        binding.maindish.setOnClickListener {
            val myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITLE", "Main Dish")
            myIntent.putExtra("CATEGORY", "Dish")
            startActivity(myIntent)
        }

        binding.drinks.setOnClickListener {
            val myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITLE", "Drinks")
            myIntent.putExtra("CATEGORY", "Drinks")
            startActivity(myIntent)
        }

        binding.more.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet)

            // Find the buttons in the bottom_sheet.xml layout
            val aboutDeveloperButton = dialog.findViewById<Button>(R.id.about_developer)
            val privacyPolicyButton = dialog.findViewById<Button>(R.id.privacy_polocy)

            // Set OnClickListener for the "About Developer" button
            aboutDeveloperButton.setOnClickListener {
                val intent = Intent(this@HomeActivity, AboutDeveloperActivity::class.java)
                startActivity(intent)
                dialog.dismiss()  // Dismiss the dialog when button is clicked
            }

            // Set OnClickListener for the "Privacy Policy" button
            privacyPolicyButton.setOnClickListener {
                val intent = Intent(this@HomeActivity, PrivacyPolicyActivity::class.java)
                startActivity(intent)
                dialog.dismiss()  // Dismiss the dialog when button is clicked
            }

            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }

        binding.desert.setOnClickListener {
            val myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITLE", "Dessert")
            myIntent.putExtra("CATEGORY", "Dessert")
            startActivity(myIntent)
        }
    }

    private fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvPopuler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val db = Room.databaseBuilder(this@HomeActivity, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()

        val daoObject = db.getDao()
        val recipes = daoObject.getAll()  // Ensure this line correctly calls getAll()

        if (recipes != null) {
            for (recipe in recipes) {
                recipe?.let {
                    if (it.category.contains("Popular")) {
                        dataList.add(it)
                    }
                }
            }
        }

        rvAdapter = PopularAdapter(dataList, this)
        binding.rvPopuler.adapter = rvAdapter
    }

    private fun setUpWebView() {
        webView = binding.webView  // Ensure this matches your layout ID
        webView.webViewClient = WebViewClient() // Ensures links open in the WebView
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed

        // Load the YouTube video link in embedded format
        webView.loadUrl("https://www.youtube.com/embed/i_JQwhPKzdI")
    }

}
