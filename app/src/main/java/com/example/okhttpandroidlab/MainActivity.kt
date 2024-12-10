package com.example.okhttpandroidlab

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLoadImage = findViewById<android.widget.Button>(R.id.buttonLoadImage)
        val imageView = findViewById<ImageView>(R.id.imageView)

        buttonLoadImage.setOnClickListener {
            loadImage(imageView)
        }
    }

    private fun loadImage(imageView: ImageView) {
        // URL to fetch the JSON data
        val url = "https://picsum.photos/v2/list?page=1&limit=1"

        // Create OkHttpClient
        val client = OkHttpClient()

        // Create request
        val request = Request.Builder()
            .url(url)
            .build()

        // Make async request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Parse the response
                    val jsonArray = JSONArray(response.body?.string()) // Parse as JSONArray

                    if (jsonArray.length() > 0) {
                        val jsonObject = jsonArray.getJSONObject(0) // Get the first item from the array
                        val imageUrl = jsonObject.getString("download_url") // Extract the download URL

                        // Load the image using Picasso
                        runOnUiThread {
                            Picasso.get().load(imageUrl).into(imageView)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "No image found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
