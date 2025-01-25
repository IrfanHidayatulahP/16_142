package com.example.finalprojectpam.ui.pengeluaran

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
import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.asset.AssetViewModel
import com.example.finalprojectpam.ui.asset.HomeUiState
import com.example.finalprojectpam.ui.kategori.KatUiState
import com.example.finalprojectpam.ui.kategori.KategoriViewModel
import com.example.finalprojectpam.ui.pendapatan.EditDapatState
import com.example.finalprojectpam.ui.pendapatan.EditPendapatanFragmentArgs
import com.example.finalprojectpam.ui.pendapatan.EditPendapatanFragmentDirections
import com.example.finalprojectpam.ui.pendapatan.EditPendapatanViewModel
import com.example.finalprojectpam.ui.pendapatan.InsertDapatEvent

class EditPengeluaranFragment : Fragment() {

    private val viewModel: EditPengeluaranViewModel by viewModels { PenyediaViewModel.Factory }
    private val assetViewModel: AssetViewModel by viewModels { PenyediaViewModel.Factory }
    private val ktgViewModel: KategoriViewModel by viewModels { PenyediaViewModel.Factory }

    private val args: EditPengeluaranFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FormUpdate(
                        insertKeluarEvent = InsertKeluarEvent(),
                        asetList = (assetViewModel.asetUiState as? HomeUiState.Success)?.aset ?: emptyList(),
                        kategoriList = (ktgViewModel.katUiState as? KatUiState.Success)?.kategori ?: emptyList(),
                        onValueChange = { /* Handle event */ },
                        viewModel = viewModel,
                        enabled = true,
                        onNavigateToDetail = { navigateToDetailPengeluaran(args.idPengeluaran) }
                    )
                }
            }
        }
    }
    private fun navigateToDetailPengeluaran(idPendapatan: String) {
        val action = EditPendapatanFragmentDirections.actionPengeluaranToDetail(idPendapatan)
        findNavController().navigate(action)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUpdate(
    insertKeluarEvent: InsertKeluarEvent,
    viewModel: EditPengeluaranViewModel = viewModel(factory = PenyediaViewModel.Factory),
    asetList: List<Aset>,
    kategoriList: List<Kategori>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertKeluarEvent) -> Unit = {},
    enabled: Boolean = true,
    onNavigateToDetail: () -> Unit
) {
    val updatedKeluarState by viewModel.editKeluarState.collectAsState()
    var insertKeluarEvent by remember { mutableStateOf(insertKeluarEvent) }

    var expandedAset by remember { mutableStateOf(false) }
    var expandedKategori by remember { mutableStateOf(false) }

    val selectedAset = asetList.find { it.id_aset == insertKeluarEvent.id_aset }?.nama_aset ?: "Pilih Aset"
    val selectedKategori = kategoriList.find { it.id_kategori == insertKeluarEvent.id_kategori }?.nama_kategori ?: "Pilih Kategori"

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
                            insertKeluarEvent = insertKeluarEvent.copy(id_aset = aset.id_aset)
                            onValueChange(insertKeluarEvent)
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
                            insertKeluarEvent = insertKeluarEvent.copy(id_kategori = kategori.id_kategori)
                            onValueChange(insertKeluarEvent)
                            expandedKategori = false
                        }
                    )
                }
            }
        }

        // Input Tanggal Transaksi
        OutlinedTextField(
            value = insertKeluarEvent.tgl_transaksi,
            onValueChange = { insertKeluarEvent = insertKeluarEvent.copy(tgl_transaksi = it) },
            label = { Text("Tanggal Transaksi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Input Total Pendapatan
        OutlinedTextField(
            value = insertKeluarEvent.total,
            onValueChange = { insertKeluarEvent = insertKeluarEvent.copy(total = it) },
            label = { Text("Total Pendapatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Input Catatan
        OutlinedTextField(
            value = insertKeluarEvent.catatan,
            onValueChange = { insertKeluarEvent = insertKeluarEvent.copy(catatan = it) },
            label = { Text("Catatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        Button(
            onClick = {
                viewModel.editPengeluaranDetail(
                    Pengeluaran(
                        id_pengeluaran = insertKeluarEvent.id_pengeluaran,
                        id_aset = insertKeluarEvent.id_aset,
                        id_kategori = insertKeluarEvent.id_kategori,
                        tgl_transaksi = insertKeluarEvent.tgl_transaksi,
                        total = insertKeluarEvent.total,
                        catatan = insertKeluarEvent.catatan
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text("Simpan")
        }

        // Navigasi setelah update berhasil
        if (updatedKeluarState is EditKeluarState.Success) {
            LaunchedEffect(Unit) {
                onNavigateToDetail()
            }
        }
    }
}