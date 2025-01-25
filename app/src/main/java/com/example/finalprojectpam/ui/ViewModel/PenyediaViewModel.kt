package com.example.finalprojectpam.ui.ViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.finalprojectpam.DependenciesInjection.PencatatanApplication
import com.example.finalprojectpam.ui.asset.AssetViewModel
import com.example.finalprojectpam.ui.asset.InsertAssetViewModel
import com.example.finalprojectpam.ui.asset.UpdateAssetViewModel
import com.example.finalprojectpam.ui.kategori.EditKategoriViewModel
import com.example.finalprojectpam.ui.kategori.InsertKategoriViewModel
import com.example.finalprojectpam.ui.kategori.KategoriViewModel
import com.example.finalprojectpam.ui.pendapatan.DetailPendapatanViewModel
import com.example.finalprojectpam.ui.pendapatan.EditPendapatanViewModel
import com.example.finalprojectpam.ui.pendapatan.InsertPendapatanViewModel
import com.example.finalprojectpam.ui.pendapatan.PendapatanViewModel
import com.example.finalprojectpam.ui.pengeluaran.DetailPengeluaranViewModel
import com.example.finalprojectpam.ui.pengeluaran.InsertPengeluaranViewModel
import com.example.finalprojectpam.ui.pengeluaran.PengeluaranViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            AssetViewModel(pencatatanApplication().container.asetRepository)
        }
        initializer {
            InsertAssetViewModel(pencatatanApplication().container.asetRepository)
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val id_aset = savedStateHandle.get<String>("id_aset")
                ?: throw IllegalArgumentException("ID is Required")
            UpdateAssetViewModel(
                pencatatanApplication().container.asetRepository,
                id_aset
            )
        }

        initializer {
            KategoriViewModel(pencatatanApplication().container.kategoriRepository)
        }
        initializer {
            InsertKategoriViewModel(pencatatanApplication().container.kategoriRepository)
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val id_kategori = savedStateHandle.get<String>("id_kategori")
                ?: throw IllegalArgumentException("ID is Required")
            EditKategoriViewModel(
                pencatatanApplication().container.kategoriRepository,
                id_kategori
            )
        }

        initializer {
            PendapatanViewModel(pencatatanApplication().container.pendapatanRepository)
        }
        initializer {
            InsertPendapatanViewModel(pencatatanApplication().container.pendapatanRepository)
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val id_pendapatan = savedStateHandle.get<String>("id_pendapatan")
                ?: throw IllegalArgumentException("ID is Required")
            DetailPendapatanViewModel(
                pencatatanApplication().container.pendapatanRepository,
                idPendapatan = id_pendapatan
            )
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val id_pendapatan = savedStateHandle.get<String>("id_pendapatan")
                ?: throw IllegalArgumentException("ID is Required")
            EditPendapatanViewModel(
                pencatatanApplication().container.pendapatanRepository,
                id_pendapatan
            )
        }

        initializer {
            PengeluaranViewModel(pencatatanApplication().container.pengeluaranRepository)
        }
        initializer {
            InsertPengeluaranViewModel(pencatatanApplication().container.pengeluaranRepository)
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val id_pengeluaran = savedStateHandle.get<String>("id_pengeluaran")
                ?: throw IllegalArgumentException("ID is Required")
            DetailPengeluaranViewModel(pencatatanApplication().container.pengeluaranRepository,
                idPengeluaran = id_pengeluaran
            )
        }
    }
}

fun CreationExtras.pencatatanApplication(): PencatatanApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PencatatanApplication)
