package com.example.finalprojectpam.ui.kategori

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KategoriViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Kategori Fragment"
    }
    val text: LiveData<String> = _text
}