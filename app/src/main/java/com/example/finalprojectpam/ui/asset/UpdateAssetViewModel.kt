package com.example.finalprojectpam.ui.asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.AsetRepository
import com.example.finalprojectpam.model.Aset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UpdateUiState {
    data class DataLoaded(val aset: Aset) : UpdateUiState()
    object Success : UpdateUiState()
    object Error : UpdateUiState()
    object Loading : UpdateUiState()
}

class UpdateAssetViewModel (
    private val repository: AsetRepository,
    private val id_aset: String
) : ViewModel() {

    private val _updateUiState = MutableStateFlow<UpdateUiState>(UpdateUiState.Loading)
    val updateUiState: StateFlow<UpdateUiState> get() = _updateUiState

    private val _asetData = MutableStateFlow<Aset?>(null)
    val asetData: StateFlow<Aset?> get() = _asetData

    init {
        loadAsetDetail(id_aset = id_aset)
    }

    private fun loadAsetDetail(id_aset: String) {
        viewModelScope.launch {
            _updateUiState.value = UpdateUiState.Loading
            try {
                val aset = repository.detailAset(id_aset)
                _asetData.value = aset
                _updateUiState.value = UpdateUiState.DataLoaded(aset)
            } catch (e: Exception) {
                _updateUiState.value = UpdateUiState.Error
            }
        }
    }

    fun updateAsetDetail(updateAset: Aset) {
        viewModelScope.launch {
            _updateUiState.value = UpdateUiState.Loading
            try {
                repository.updateAset(id_aset, updateAset)
                _updateUiState.value = UpdateUiState.Success
            } catch (e: Exception) {
                _updateUiState.value = UpdateUiState.Error
            }
        }
    }
}