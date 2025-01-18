package com.example.finalprojectpam.ui.pendapatan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PendapatanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Pendapatan Fragment"
    }
    val text: LiveData<String> = _text
}