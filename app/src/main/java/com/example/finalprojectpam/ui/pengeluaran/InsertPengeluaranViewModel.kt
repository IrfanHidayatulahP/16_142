package com.example.finalprojectpam.ui.pengeluaran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PengeluaranRepository
import com.example.finalprojectpam.model.Pengeluaran
import kotlinx.coroutines.launch

class InsertPengeluaranViewModel(private val keluar: PengeluaranRepository) : ViewModel() {

    var keluarState by mutableStateOf(InsertKeluarState())
        private set

    fun updateInsertKeluarState(insertKeluarEvent: InsertKeluarEvent) {
        keluarState = keluarState.copy(insertKeluarEvent = insertKeluarEvent)
    }

    fun insertKeluar() {
        viewModelScope.launch {
            try {
                val pengeluaran = keluarState.insertKeluarEvent.toKeluar()
                keluar.insertPengeluaran(pengeluaran)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertKeluarState(
    val insertKeluarEvent: InsertKeluarEvent = InsertKeluarEvent()
)

data class InsertKeluarEvent(
    val id_pengeluaran: String = "",
    val id_aset: String = "",
    val id_kategori: String = "",
    val tgl_transaksi: String = "",
    val total: String = "",
    val catatan: String = "",
)

fun InsertKeluarEvent.toKeluar(): Pengeluaran = Pengeluaran(
    id_pengeluaran = id_pengeluaran,
    id_aset = id_aset,
    id_kategori = id_kategori,
    tgl_transaksi = tgl_transaksi,
    total = total,
    catatan = catatan
)

fun Pengeluaran.toUiStateKeluar() : InsertKeluarState = InsertKeluarState(
    insertKeluarEvent = toInsertKeluarEvent()
)

fun Pengeluaran.toInsertKeluarEvent(): InsertKeluarEvent = InsertKeluarEvent(
    id_pengeluaran = id_pengeluaran,
    id_aset = id_aset,
    id_kategori = id_kategori,
    tgl_transaksi = tgl_transaksi,
    total = total,
    catatan = catatan
)