package com.example.finalprojectpam.ui.kategori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.KategoriRepository
import com.example.finalprojectpam.model.Kategori
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class EditKategoriState {
    data class DataLoaded(val kategori: Kategori) : EditKategoriState()
    object Success : EditKategoriState()
    object Error : EditKategoriState()
    object Loading : EditKategoriState()
}

class EditKategoriViewModel (
    private val repository: KategoriRepository,
    private val id_kategori: String
) : ViewModel() {

    private val _editKategoriState = MutableStateFlow<EditKategoriState>(EditKategoriState.Loading)
    val editKategoriState: StateFlow<EditKategoriState> get() = _editKategoriState

    private val  _kategoriData = MutableStateFlow<Kategori?>(null)
    val kategoriData: StateFlow<Kategori?> get() = _kategoriData

    init {
        loadKategoriDetail(id_kategori = id_kategori)
    }

    fun loadKategoriDetail(id_kategori: String) {
        viewModelScope.launch {
            _editKategoriState.value = EditKategoriState.Loading
            try {
                val kategori = repository.detailKategori(id_kategori)
                _kategoriData.value = kategori
                _editKategoriState.value = EditKategoriState.DataLoaded(kategori)
            } catch (e: Exception) {
                _editKategoriState.value = EditKategoriState.Error
            }
        }
    }

    fun updateKategoriDetail(editKategori: Kategori) {
        viewModelScope.launch {
            _editKategoriState.value = EditKategoriState.Loading
            try {
                repository.updateKategori(id_kategori, editKategori)
                _editKategoriState.value = EditKategoriState.Success
            } catch (e: Exception) {
                _editKategoriState.value = EditKategoriState.Error
            }
        }
    }
}