package com.example.finalprojectpam.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.SaldoRepository
import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.model.Saldo
import kotlinx.coroutines.launch

sealed class SaldoUiState {
    data class Success(val saldo: Saldo) : SaldoUiState()
    data class Error(val message: String) : SaldoUiState()
    object Loading : SaldoUiState()
}

class HomeViewModel(private val saldoRepository: SaldoRepository) : ViewModel() {

    var saldoUiState: SaldoUiState by mutableStateOf(SaldoUiState.Loading)
        private set

    init {
        getAll()
    }

    fun getAll() {
        viewModelScope.launch {
            saldoUiState = SaldoUiState.Loading
            try {
                val saldo = saldoRepository.getSaldo()

                saldoUiState = SaldoUiState.Success(
                    Saldo(
                        total_pendapatan = saldo.total_pendapatan,
                        total_pengeluaran = saldo.total_pengeluaran,
                        saldo = saldo.saldo
                    )
                )
            } catch (e: Exception) {
                saldoUiState = SaldoUiState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}