package com.example.finalprojectpam.ui.pendapatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PendapatanRepository
import com.example.finalprojectpam.model.Pendapatan
import kotlinx.coroutines.launch

class InsertPendapatanViewModel(private val dapat : PendapatanRepository) :ViewModel() {

    var dapatState by mutableStateOf(InsertDapatState())
        private set

    fun updateInsertDapatState(insertDapatEvent: InsertDapatEvent) {
        dapatState = dapatState.copy(insertDapatEvent = insertDapatEvent)
    }

    fun insertDapat() {
        viewModelScope.launch {
            try {
                val pendapatan = dapatState.insertDapatEvent.toDapat()
                dapat.insertPendapatan(pendapatan)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


data class InsertDapatState(
    val insertDapatEvent: InsertDapatEvent = InsertDapatEvent()
)

data class InsertDapatEvent(
    val id_pendapatan: String = "",
    val id_aset: String = "",
    val id_kategori: String = "",
    val tgl_transaksi: String = "",
    val total: String = "",
    val catatan: String = "",
)

fun InsertDapatEvent.toDapat(): Pendapatan = Pendapatan(
    id_pendapatan = id_pendapatan,
    id_aset = id_aset,
    id_kategori = id_kategori,
    tgl_transaksi = tgl_transaksi,
    total = total,
    catatan = catatan
)