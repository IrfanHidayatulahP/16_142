package com.example.finalprojectpam.ui.pendapatan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.model.Kategori
import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.asset.AssetViewModel
import com.example.finalprojectpam.ui.asset.HomeUiState
import com.example.finalprojectpam.ui.kategori.KatUiState
import com.example.finalprojectpam.ui.kategori.KategoriViewModel

class EditPendapatanFragment : Fragment() {

    private val viewModel: EditPendapatanViewModel by viewModels { PenyediaViewModel.Factory }
    private val assetViewModel: AssetViewModel by viewModels { PenyediaViewModel.Factory }
    private val ktgViewModel: KategoriViewModel by viewModels { PenyediaViewModel.Factory }

    private val args: EditPendapatanFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FormUpdate(
                        insertDapatEvent = InsertDapatEvent(),
                        asetList = (assetViewModel.asetUiState as? HomeUiState.Success)?.aset ?: emptyList(),
                        kategoriList = (ktgViewModel.katUiState as? KatUiState.Success)?.kategori ?: emptyList(),
                        onValueChange = { /* Handle event */ },
                        viewModel = viewModel,
                        enabled = true,
                        onNavigateToDetail = { navigateToDetailPendapatan(args.idPendapatan) }
                    )
                }
            }
        }
    }
    private fun navigateToDetailPendapatan(idPendapatan: String) {
        val action = EditPendapatanFragmentDirections.actionEditToDetail(idPendapatan)
        findNavController().navigate(action)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUpdate(
    insertDapatEvent: InsertDapatEvent,
    viewModel: EditPendapatanViewModel = viewModel(factory = PenyediaViewModel.Factory),
    asetList: List<Aset>,
    kategoriList: List<Kategori>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertDapatEvent) -> Unit = {},
    enabled: Boolean = true,
    onNavigateToDetail: () -> Unit
) {
    val updatedDapatState by viewModel.editDapatState.collectAsState()
    var insertDapatEvent by remember { mutableStateOf(insertDapatEvent) }

    var expandedAset by remember { mutableStateOf(false) }
    var expandedKategori by remember { mutableStateOf(false) }

    val selectedAset = asetList.find { it.id_aset == insertDapatEvent.id_aset }?.nama_aset ?: "Pilih Aset"
    val selectedKategori = kategoriList.find { it.id_kategori == insertDapatEvent.id_kategori }?.nama_kategori ?: "Pilih Kategori"

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Dropdown untuk Aset
        ExposedDropdownMenuBox(
            expanded = expandedAset,
            onExpandedChange = { expandedAset = !expandedAset }
        ) {
            OutlinedTextField(
                value = selectedAset,
                onValueChange = {},
                readOnly = true,
                label = { Text("Nama Aset") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAset) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = enabled
            )

            ExposedDropdownMenu(
                expanded = expandedAset,
                onDismissRequest = { expandedAset = false }
            ) {
                asetList.forEach { aset ->
                    DropdownMenuItem(
                        text = { Text(aset.nama_aset) },
                        onClick = {
                            insertDapatEvent = insertDapatEvent.copy(id_aset = aset.id_aset)
                            onValueChange(insertDapatEvent)
                            expandedAset = false
                        }
                    )
                }
            }
        }

        // Dropdown untuk Kategori
        ExposedDropdownMenuBox(
            expanded = expandedKategori,
            onExpandedChange = { expandedKategori = !expandedKategori }
        ) {
            OutlinedTextField(
                value = selectedKategori,
                onValueChange = {},
                readOnly = true,
                label = { Text("Nama Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = enabled
            )

            ExposedDropdownMenu(
                expanded = expandedKategori,
                onDismissRequest = { expandedKategori = false }
            ) {
                kategoriList.forEach { kategori ->
                    DropdownMenuItem(
                        text = { Text(kategori.nama_kategori) },
                        onClick = {
                            insertDapatEvent = insertDapatEvent.copy(id_kategori = kategori.id_kategori)
                            onValueChange(insertDapatEvent)
                            expandedKategori = false
                        }
                    )
                }
            }
        }

        // Input Tanggal Transaksi
        OutlinedTextField(
            value = insertDapatEvent.tgl_transaksi,
            onValueChange = { insertDapatEvent = insertDapatEvent.copy(tgl_transaksi = it) },
            label = { Text("Tanggal Transaksi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Input Total Pendapatan
        OutlinedTextField(
            value = insertDapatEvent.total,
            onValueChange = { insertDapatEvent = insertDapatEvent.copy(total = it) },
            label = { Text("Total Pendapatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Input Catatan
        OutlinedTextField(
            value = insertDapatEvent.catatan,
            onValueChange = { insertDapatEvent = insertDapatEvent.copy(catatan = it) },
            label = { Text("Catatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        Button(
            onClick = {
                viewModel.editPendapatanDetail(
                    Pendapatan(
                        id_pendapatan = insertDapatEvent.id_pendapatan,
                        id_aset = insertDapatEvent.id_aset,
                        id_kategori = insertDapatEvent.id_kategori,
                        tgl_transaksi = insertDapatEvent.tgl_transaksi,
                        total = insertDapatEvent.total,
                        catatan = insertDapatEvent.catatan
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text("Simpan")
        }

        // Navigasi setelah update berhasil
        if (updatedDapatState is EditDapatState.Success) {
            LaunchedEffect(Unit) {
                onNavigateToDetail()
            }
        }
    }
}
