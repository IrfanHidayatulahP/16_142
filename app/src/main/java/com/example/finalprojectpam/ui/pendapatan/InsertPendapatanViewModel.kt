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

    fun updateInsertDapatState(insertDapatEvent: InsertDapatEvent) {
        dapatState = InsertDapatState(insertDapatEvent = insertDapatEvent)
    }

    suspend fun insertDapat() {
        viewModelScope.launch {
            try {
                dapat.insertPendapatan(dapatState.insertDapatEvent.toDapat())
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

fun InsertDapatEvent.toDapat() : Pendapatan = Pendapatan(
    id_pendapatan = id_pendapatan,
    id_aset = id_aset,
    id_kategori = id_kategori,
    tgl_transaksi = tgl_transaksi,
    total = total,
    catatan = catatan
)

fun Pendapatan.toUiStateDapat() : InsertDapatState = InsertDapatState(
    insertDapatEvent = toInsertDapatEvent()
)

fun Pendapatan.toInsertDapatEvent(): InsertDapatEvent = InsertDapatEvent(
    id_pendapatan = id_pendapatan,
    id_aset = id_aset,
    id_kategori = id_kategori,
    tgl_transaksi = tgl_transaksi,
    total = total,
    catatan = catatan
)