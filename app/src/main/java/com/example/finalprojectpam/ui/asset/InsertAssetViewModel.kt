package com.example.finalprojectpam.ui.asset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.AsetRepository
import com.example.finalprojectpam.model.Aset
import kotlinx.coroutines.launch

class InsertAssetViewModel(private val aset : AsetRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertUiState())

    fun updateInsertAsetState(insertUiEvent: InsertUiEvent) {
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun insertAset() {
        viewModelScope.launch {
            try {
                aset.insertAset(uiState.insertUiEvent.toAset())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent()
)

data class InsertUiEvent(
    val id_aset:  String = "",
    val nama_aset: String = ""
)

fun InsertUiEvent.toAset() : Aset = Aset (
    id_aset = id_aset,
    nama_aset = nama_aset
)

fun Aset.toUiStateAset() : InsertUiState = InsertUiState (
    insertUiEvent = toInsertUiEvent()
)

fun Aset.toInsertUiEvent() : InsertUiEvent = InsertUiEvent (
    id_aset = id_aset,
    nama_aset = nama_aset
)