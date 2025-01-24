package com.example.finalprojectpam.ui.pendapatan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.model.Kategori
import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.asset.AssetViewModel
import com.example.finalprojectpam.ui.asset.HomeUiState
import com.example.finalprojectpam.ui.kategori.KatUiState
import com.example.finalprojectpam.ui.kategori.KategoriViewModel
import kotlinx.coroutines.launch

class EditPendapatanFragment : Fragment(){

    private lateinit var viewModel: EditPendapatanViewModel
    private lateinit var assetViewModel: AssetViewModel
    private lateinit var ktgViewModel: KategoriViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Ambil `id_pendapatan` dari argument
        val idPendapatan = requireArguments().getString("id_pendapatan")
            ?: throw IllegalArgumentException("ID Pendapatan tidak ditemukan")

        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(EditPendapatanViewModel::class.java)
        assetViewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(AssetViewModel::class.java)
        ktgViewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(KategoriViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    UpdateScreen(
                        viewModel = viewModel,
                        assetViewModel = assetViewModel,
                        ktgViewModel = ktgViewModel,
                        navigateBack = { requireActivity().onBackPressedDispatcher.onBackPressed() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    viewModel: EditPendapatanViewModel,
    assetViewModel: AssetViewModel,
    ktgViewModel: KategoriViewModel,
    navigateBack: () -> Unit
) {
    val editDapatState = viewModel.editDapatState.collectAsState()
    val pendapatanData = viewModel.pendapatanData.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        when (val state = editDapatState.value) {
            is EditDapatState.Loading -> {
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is EditDapatState.DataLoaded -> {
                pendapatanData.value?.let { pendapatan ->
                    EntryUpdate(
                        insertDapatState = (viewModel.pendapatanData.value ?: Pendapatan()).toUiStateDapat(),
                        asetList = (assetViewModel.asetUiState as? HomeUiState.Success)?.aset
                            ?: emptyList(),
                        kategoriList = (ktgViewModel.katUiState as? KatUiState.Success)?.kategori
                            ?: emptyList(),
                        onSiswaValueChange = { updatedEvent ->
                            val updatedPendapatan = pendapatan.copy(
                                id_aset = updatedEvent.id_aset,
                                id_kategori = updatedEvent.id_kategori,
                                tgl_transaksi = updatedEvent.tgl_transaksi,
                                total = updatedEvent.total,
                                catatan = updatedEvent.catatan
                            )
                            viewModel.editPendapatanDetail(updatedPendapatan)
                        },
                        onSaveClick = {
                            coroutineScope.launch {
                                viewModel.editPendapatanDetail(pendapatan)
                                navigateBack()
                            }
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    )
                }
            }

            is EditDapatState.Error -> {
                Text(
                    text = "Terjadi kesalahan, coba lagi.",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is EditDapatState.Success -> {
                Text(
                    text = "Berhasil diperbarui!",
                    modifier = Modifier.padding(innerPadding)
                )
                navigateBack()
            }
        }
    }
}

@Composable
fun EntryUpdate(
    insertDapatState: InsertDapatState,
    onSiswaValueChange: (InsertDapatEvent) -> Unit,
    asetList: List<Aset>,
    kategoriList: List<Kategori>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.padding(12.dp)
    ) {
        FormInput(
            insertDapatEvent = insertDapatState.insertDapatEvent,
            onValueChange = onSiswaValueChange,
            asetList = asetList,
            kategoriList = kategoriList,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUpdate(
    insertDapatEvent: InsertDapatEvent,
    asetList: List<Aset>,
    kategoriList: List<Kategori>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertDapatEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedAsetName by remember { mutableStateOf("") }
        var selectedKategoriName by remember { mutableStateOf("") }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedAsetName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Nama Aset") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = enabled
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                asetList.forEach { aset ->
                    DropdownMenuItem(
                        text = { Text(aset.nama_aset) },
                        onClick = {
                            selectedAsetName = aset.nama_aset
                            expanded = false
                            onValueChange(insertDapatEvent.copy(id_aset = aset.id_aset))
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedKategoriName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Nama Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = enabled
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                kategoriList.forEach { ktg ->
                    DropdownMenuItem(
                        text = { Text(ktg.nama_kategori) },
                        onClick = {
                            selectedKategoriName = ktg.nama_kategori
                            expanded = false
                            onValueChange(insertDapatEvent.copy(id_kategori = ktg.id_kategori))
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = insertDapatEvent.tgl_transaksi,
            onValueChange = {onValueChange(insertDapatEvent.copy(tgl_transaksi = it))},
            label = { Text("Tanggal Transaksi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertDapatEvent.total,
            onValueChange = {onValueChange(insertDapatEvent.copy(total = it))},
            label = { Text("Total Pendapatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertDapatEvent.catatan,
            onValueChange = {onValueChange(insertDapatEvent.copy(catatan = it))},
            label = { Text("Catatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}