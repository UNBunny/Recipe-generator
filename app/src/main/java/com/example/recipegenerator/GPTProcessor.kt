package com.example.recipegenerator

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


private const val API_KEY_GPT = "YOUR_CODE"
private const val URL_GPT = "https://api.openai.com/v1/engines/text-davinci-003/completions"

class GPTProcessor {

    fun gptResponse(question: String): String {
        var textResult: String = ""
        val requestBody = """
            {
            "prompt": "$question",
            "max_tokens": 1000,
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
        try {

            val response = client.newCall(request).execute()
            val data = response.body?.string()
            val jsonObject = JSONObject(data!!)
             textResult =
                jsonObject.getJSONArray("choices")
                    .getJSONObject(0)
                    .getString("text")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return textResult
    }
}
