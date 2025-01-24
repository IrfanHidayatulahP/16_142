package com.example.finalprojectpam.ui.pendapatan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PendapatanRepository
import com.example.finalprojectpam.model.Pendapatan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class EditDapatState {
    data class DataLoaded(val pendapatan: Pendapatan) : EditDapatState()
    object Loading : EditDapatState()
    object Error : EditDapatState()
    object Success : EditDapatState()
}

class EditPendapatanViewModel(
    private val dapat: PendapatanRepository,
    private val id_pendapatan: String
) : ViewModel() {

    private val _editDapatState = MutableStateFlow<EditDapatState>(EditDapatState.Loading)
    val editDapatState: StateFlow<EditDapatState> get() = _editDapatState

    private val _pendapatanData = MutableStateFlow<Pendapatan?>(null)
    val pendapatanData: StateFlow<Pendapatan?> get() = _pendapatanData

    private fun loadPendapatanDetail() {
        viewModelScope.launch {
            _editDapatState.value = EditDapatState.Loading
            try {
                val dapat = dapat.detailPendapatan(id_pendapatan)
                _pendapatanData.value = dapat
                _editDapatState.value = EditDapatState.DataLoaded(dapat)
            } catch (e: Exception) {
                _editDapatState.value = EditDapatState.Error
            }
        }
    }

    fun editPendapatanDetail(updatedPendapatan: Pendapatan) {
        viewModelScope.launch {
            _editDapatState.value = EditDapatState.Loading
            try {
                dapat.updatePendapatan(id_pendapatan, updatedPendapatan)
                _editDapatState.value = EditDapatState.Success
            } catch (e: Exception) {
                _editDapatState.value = EditDapatState.Error
            }
        }
    }
}

