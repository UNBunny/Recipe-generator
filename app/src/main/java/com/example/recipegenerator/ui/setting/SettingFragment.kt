package com.example.recipegenerator.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipegenerator.R


class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val link1TextView: TextView = view.findViewById(R.id.link1TextView)
        val link2TextView: TextView = view.findViewById(R.id.link2TextView)
        val link3TextView: TextView = view.findViewById(R.id.link3TextView)
        link1TextView.setOnClickListener {
            openUrlInBrowser("https://www.google.com")
        }
        link2TextView.setOnClickListener {
            openUrlInBrowser("https://github.com/UNBunny")
        }
        link3TextView.setOnClickListener {
            openUrlInBrowser("https://github.com/UNBunny")
        }
        return view
    }

    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}