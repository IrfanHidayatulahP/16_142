package com.example.finalprojectpam.ui.asset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AssetViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Asset Fragment"
    }
    val text: LiveData<String> = _text
}