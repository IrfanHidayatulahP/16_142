package com.example.finalprojectpam.ui.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.KategoriRepository
import com.example.finalprojectpam.model.Kategori
import kotlinx.coroutines.launch

class InsertKategoriViewModel(private val kat : KategoriRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertUiState())

    fun updateInsertKatState(insertUiEvent: InsertKtgEvent) {
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun insertKategori() {
        viewModelScope.launch {
            try {
                kat.insertKategori(uiState.insertUiEvent.toKategori())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiState(
    val insertUiEvent: InsertKtgEvent = InsertKtgEvent()
)

data class InsertKtgEvent(
    val id_kategori:  String = "",
    val nama_kategori: String = ""
)

fun InsertKtgEvent.toKategori() : Kategori = Kategori (
    id_kategori = id_kategori,
    nama_kategori = nama_kategori
)

fun Kategori.toUiStateKategori() : InsertUiState = InsertUiState (
    insertUiEvent = toInsertKtgEvent()
)

fun Kategori.toInsertKtgEvent() : InsertKtgEvent = InsertKtgEvent (
    id_kategori = id_kategori,
    nama_kategori = nama_kategori
)