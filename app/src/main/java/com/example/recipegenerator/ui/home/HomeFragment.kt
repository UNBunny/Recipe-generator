package com.example.recipegenerator.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipegenerator.GPTProcessor
import com.example.recipegenerator.R
import com.example.recipegenerator.adapters.ItemAdapter
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


private const val API_KEY_OFD = "24899.NAZDuLqc322udu3my"
private const val URL = "https://proverkacheka.com/api/v1/check/get"

class HomeFragment : Fragment() {

    private lateinit var scanButton: Button
    private lateinit var generateButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var progressBar: ProgressBar
    private val namesList = ArrayList<String>()
    private var qrCodeResult: String? = null
    var textResult: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setupRecyclerView(view)
        setupButtons(view)
        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rcView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        adapter = ItemAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    private fun setupButtons(view: View) {
        generateButton = view.findViewById(R.id.generateButton)
        generateButton.visibility = View.INVISIBLE

        scanButton = view.findViewById(R.id.scanButton)
        scanButton.setOnClickListener {
            qrCodeScanner()
        }
        generateButton.setOnClickListener {
            adapter.updateData(mutableListOf())
            showProgressBar()
            val gptProcessor = GPTProcessor() // Создание экземпляра класса GPTProcessor
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            var result : String = ""
            coroutineScope.launch {
                 result =
                    gptProcessor.gptResponse(
                        "Помоги составить рецепт из данных продуктов: ${namesList!!.joinToString(", ")}." +
                                " Напиши названия блюд (до 10) без рецепта. Разделяй рецепты ;"
                    ).replace("\n", "").replace(":", "").replace(Regex("[0-9]\\.\\s"), "")
                val textResultArray = result.split(';').toMutableList()
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    replaceFragment(Home2Fragment(textResultArray))
                }
            }
        }
    }

    private fun qrCodeScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Сканируйте QR-код")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                qrCodeResult = result.contents
                retrieveDataFromNetwork()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun retrieveDataFromNetwork() {
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("token", API_KEY_OFD)
            .add("qrraw", qrCodeResult!!)
            .build()

        val request = Request.Builder()
            .url(URL)
            .post(body)
            .build()

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                val response = client.newCall(request).execute()

                val jsonObject = JSONObject(response.body?.string()!!)
                val qrCodeItems = jsonObject.optJSONObject("data")
                    ?.optJSONObject("json")
                    ?.optJSONArray("items")
                if (qrCodeItems != null && qrCodeItems.length() > 0) {
                    for (i in 0 until qrCodeItems.length()) {
                        val item = qrCodeItems.optJSONObject(i)
                        val itemName = item?.optString("name")
                        if (itemName!!.isNotEmpty()) namesList.add(itemName)
                    }
                }
                withContext(Dispatchers.Main) {
                    namesList?.let {
                        adapter.updateData(it)
                    }
                    generateButton.visibility = View.VISIBLE
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE // Показать ProgressBar
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE // Скрыть ProgressBar
    }
}