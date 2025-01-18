package com.example.finalprojectpam.ui.pengeluaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PengeluaranViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Pengeluaran Fragment"
    }
    val text: LiveData<String> = _text
}