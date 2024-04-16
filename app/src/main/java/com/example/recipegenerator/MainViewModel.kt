package com.example.recipegenerator

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var currentFragmentTag: String? = null
    var selectedItemId: Int = 2 // По умолчанию выбран домашний элемент
}