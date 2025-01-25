package com.example.finalprojectpam.ui.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PengeluaranRepository
import com.example.finalprojectpam.model.Pengeluaran
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailPengeluaranState {
    data class Success(val pengeluaran: Pengeluaran) : DetailPengeluaranState()
    object Error : DetailPengeluaranState()
    object Loading : DetailPengeluaranState()
}

class DetailPengeluaranViewModel(
    private val keluar: PengeluaranRepository,
    private val idPengeluaran: String
) : ViewModel() {

    private val _detailPengeluaranState = MutableStateFlow<DetailPengeluaranState>(
        DetailPengeluaranState.Loading
    )
    val detailPengeluaranState: StateFlow<DetailPengeluaranState> get() = _detailPengeluaranState

    init {
        getDetailPengeluaran()
    }

    private fun getDetailPengeluaran() {
        viewModelScope.launch {
            _detailPengeluaranState.value = DetailPengeluaranState.Loading
            try {
                val pengeluaran = keluar.detailPengeluaran(idPengeluaran)
                _detailPengeluaranState.value = DetailPengeluaranState.Success(pengeluaran)
            } catch (e: Exception) {
                _detailPengeluaranState.value = DetailPengeluaranState.Error
            }
        }
    }

    fun deletePengeluaran(idPengeluaran: String) {
        viewModelScope.launch {
            try {
                keluar.deletePengeluaran(idPengeluaran)
            } catch (e: Exception) {
                println("Gagal Menghapus Data ${e.message}")
            }
        }
    }
}