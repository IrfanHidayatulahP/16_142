package com.example.finalprojectpam.ui.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PengeluaranRepository
import com.example.finalprojectpam.model.Pengeluaran
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class EditKeluarState {
    object Loading : EditKeluarState()
    object Success : EditKeluarState()
    object Error : EditKeluarState()
    data class DataLoaded(val pengeluaran: Pengeluaran) : EditKeluarState()
}

class EditPengeluaranViewModel (
    private val pengeluaran: PengeluaranRepository,
    private val idPengeluaran: String
) : ViewModel() {

    private val _editKeluarState = MutableStateFlow<EditKeluarState>(EditKeluarState.Loading)
    val editKeluarState: StateFlow<EditKeluarState> get() = _editKeluarState

    private val _pengeluaranData = MutableStateFlow<Pengeluaran?>(null)
    val pengeluaranData: StateFlow<Pengeluaran?> get() = _pengeluaranData

    init {
        loadPengeluaranDetail(idPengeluaran)
    }

    private fun loadPengeluaranDetail(idPengeluaran: String) {
        viewModelScope.launch {
            _editKeluarState.value = EditKeluarState.Loading
            try {
                val pengeluaran = pengeluaran.detailPengeluaran(idPengeluaran)
                _pengeluaranData.value = pengeluaran
                _editKeluarState.value = EditKeluarState.DataLoaded(pengeluaran)
            } catch (e: Exception) {
                _editKeluarState.value = EditKeluarState.Error
            }
        }
    }

    fun editPengeluaranDetail(updatedPengeluaran: Pengeluaran) {
        viewModelScope.launch {
            _editKeluarState.value = EditKeluarState.Loading
            try {
                pengeluaran.updatePengeluaran(idPengeluaran, updatedPengeluaran)
                _editKeluarState.value = EditKeluarState.Success
            } catch (e: Exception) {
                _editKeluarState.value = EditKeluarState.Error
            }
        }
    }
}