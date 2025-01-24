package com.example.finalprojectpam.ui.pendapatan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PendapatanRepository
import com.example.finalprojectpam.model.Pendapatan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class EditDapatState {
    object Loading : EditDapatState()
    object Success : EditDapatState()
    object Error : EditDapatState()
    data class DataLoaded(val pendapatan: Pendapatan) : EditDapatState()
}

class EditPendapatanViewModel(
    private val repository: PendapatanRepository,
    private val id_pendapatan: String
) : ViewModel() {

    private val _editDapatState = MutableStateFlow<EditDapatState>(EditDapatState.Loading)
    val editDapatState: StateFlow<EditDapatState> get() = _editDapatState

    private val _pendapatanData = MutableStateFlow<Pendapatan?>(null)
    val pendapatanData: StateFlow<Pendapatan?> get() = _pendapatanData

    init {
        loadPendapatanDetail(id_pendapatan = id_pendapatan)
    }

    private fun loadPendapatanDetail(id_pendapatan: String) {
        viewModelScope.launch {
            _editDapatState.value = EditDapatState.Loading
            try {
                val pendapatan = repository.detailPendapatan(id_pendapatan)
                _pendapatanData.value = pendapatan
                _editDapatState.value = EditDapatState.DataLoaded(pendapatan)
            } catch (e: Exception) {
                _editDapatState.value = EditDapatState.Error
            }
        }
    }

    fun editPendapatanDetail(updatedPendapatan: Pendapatan) {
        viewModelScope.launch {
            _editDapatState.value = EditDapatState.Loading
            try {
                repository.updatePendapatan(id_pendapatan, updatedPendapatan)
                _editDapatState.value = EditDapatState.Success
            } catch (e: Exception) {
                _editDapatState.value = EditDapatState.Error
            }
        }
    }
}
