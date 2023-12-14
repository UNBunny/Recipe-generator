package com.example.recipegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.adapters.ItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

private const val API_KEY_GPT = "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"
private const val URL_GPT = "https://api.openai.com/v1/engines/text-davinci-003/completions"

class QrResultActivity : AppCompatActivity() {
    private lateinit var generateButton: ImageButton
    var textResult : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)
        val namesList = intent.getStringArrayListExtra("namesList")
        namesList?.let {
            val recyclerView: RecyclerView = findViewById(R.id.rcView)
            val adapter = ItemAdapter(it)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        generateButton = findViewById(R.id.generateButton)
        generateButton.setOnClickListener {
            gptResponse("Помоги составить рецепт из данных продуктов: ${namesList!!.joinToString(", ")}. Напиши названия блюд (от 3 до 8) без рецепта")
        }

    }

    private fun gptResponse(question: String) {

        //JSON
        val requestBody="""
            {
            "prompt": "$question",
            "max_tokens": 800,
            "temperature": 0
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(URL_GPT)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $API_KEY_GPT")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()


        val client = OkHttpClient()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                val jsonObject = JSONObject(data!!)
                 textResult =
                    jsonObject.getJSONArray("choices")
                        .getJSONObject(0)
                        .getString("text")
                runOnUiThread {
                    createAlertDialog(textResult)
                }
//                Log.v("myApiTag", textResult)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun createAlertDialog(msg: String){
        val builder = AlertDialog.Builder(this@QrResultActivity)
        builder.setTitle("Рецепты")
        builder.setMessage(msg)
        builder.show()
    }
}
