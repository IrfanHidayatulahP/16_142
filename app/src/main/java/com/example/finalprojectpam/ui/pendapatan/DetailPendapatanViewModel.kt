package com.example.finalprojectpam.ui.pendapatan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PendapatanRepository
import com.example.finalprojectpam.model.Pendapatan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailPendapatanState {
    data class Success(val pendapatan: Pendapatan) : DetailPendapatanState()
    object Error : DetailPendapatanState()
    object Loading : DetailPendapatanState()
}

class DetailPendapatanViewModel(
    private val dapat: PendapatanRepository,
    private val idPendapatan: String
) : ViewModel() {

    private val _detailPendapatanState =
        MutableStateFlow<DetailPendapatanState>(DetailPendapatanState.Loading)
    val detailPendapatanState: StateFlow<DetailPendapatanState> get() = _detailPendapatanState

    private fun getDetailPendapatan() {
        viewModelScope.launch {
            _detailPendapatanState.value = DetailPendapatanState.Loading
            try {
                val pendapatan = dapat.detailPendapatan(idPendapatan)
                _detailPendapatanState.value = DetailPendapatanState.Success(pendapatan)
            } catch (e: Exception) {
                _detailPendapatanState.value = DetailPendapatanState.Error
            }
        }
    }

    fun deletePendapatan(idPendapatan: String) {
        viewModelScope.launch {
            try {
                dapat.deletePendapatan(idPendapatan)
            }catch (e: Exception) {
                println("Gagal Menghapus Pendapatan: ${e.message}")
            }
        }
    }
}