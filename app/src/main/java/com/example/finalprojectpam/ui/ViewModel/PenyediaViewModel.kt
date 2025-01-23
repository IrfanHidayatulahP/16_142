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
import com.example.finalprojectpam.ui.kategori.KategoriViewModel
import com.example.finalprojectpam.ui.pendapatan.PendapatanViewModel
import com.example.finalprojectpam.ui.pengeluaran.PengeluaranViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            AssetViewModel(pencatatanApplication().container.asetRepository)
        }
        initializer {
            KategoriViewModel(pencatatanApplication().container.kategoriRepository)
        }
        initializer {
            PendapatanViewModel(pencatatanApplication().container.pendapatanRepository)
        }
        initializer {
            PengeluaranViewModel(pencatatanApplication().container.pengeluaranRepository)
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
    }
}

fun CreationExtras.pencatatanApplication(): PencatatanApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PencatatanApplication)
