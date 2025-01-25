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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.model.Kategori
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.asset.AssetViewModel
import com.example.finalprojectpam.ui.asset.HomeUiState
import com.example.finalprojectpam.ui.kategori.KatUiState
import com.example.finalprojectpam.ui.kategori.KategoriViewModel
import kotlinx.coroutines.launch

class InsertPendapatanFragment : Fragment() {

    private lateinit var viewModel: InsertPendapatanViewModel
    private lateinit var assetViewModel: AssetViewModel
    private lateinit var ktgViewModel: KategoriViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(InsertPendapatanViewModel::class.java)
        assetViewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(AssetViewModel::class.java)
        ktgViewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(KategoriViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    EntryPendapatanScreen(
                        navigateBack = { navigateToPendapatanFragment() },
                        viewModel = viewModel,
                        assetViewModel = assetViewModel,
                        ktgViewModel = ktgViewModel
                    )
                }
            }
        }
    }

    private fun navigateToPendapatanFragment() {
        findNavController().navigate(R.id.navigation_pendapatan)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPendapatanScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPendapatanViewModel = viewModel(factory = PenyediaViewModel.Factory),
    assetViewModel: AssetViewModel,
    ktgViewModel: KategoriViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        EntryBody(
            insertDapatState = viewModel.dapatState,
            asetList = (assetViewModel.asetUiState as? HomeUiState.Success)?.aset ?: emptyList(),
            kategoriList = (ktgViewModel.katUiState as? KatUiState.Success)?.kategori ?: emptyList(),
            onPendapatanValueChange = viewModel::updateInsertDapatState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertDapat()
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

@Composable
fun EntryBody(
    insertDapatState: InsertDapatState,
    onPendapatanValueChange: (InsertDapatEvent) -> Unit,
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
            onValueChange = onPendapatanValueChange,
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
fun FormInput(
    insertDapatEvent: InsertDapatEvent,
    asetList: List<Aset>,
    kategoriList: List<Kategori>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertDapatEvent) -> Unit,
    enabled: Boolean = true
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var expandedAset by remember { mutableStateOf(false) }
        var expandedKategori by remember { mutableStateOf(false) }

        val selectedAset = asetList.find { it.id_aset == insertDapatEvent.id_aset }?.nama_aset ?: "Pilih Aset"
        val selectedKategori = kategoriList.find { it.id_kategori == insertDapatEvent.id_kategori }?.nama_kategori ?: "Pilih Kategori"

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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
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
                            onValueChange(insertDapatEvent.copy(id_aset = aset.id_aset))
                            expandedAset = false
                        }
                    )
                }
            }
        }

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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = enabled
            )

            ExposedDropdownMenu(
                expanded = expandedKategori,
                onDismissRequest = { expandedKategori = false }
            ) {
                kategoriList.forEach { ktg ->
                    DropdownMenuItem(
                        text = { Text(ktg.nama_kategori) },
                        onClick = {
                            onValueChange(insertDapatEvent.copy(id_kategori = ktg.id_kategori))
                            expandedKategori = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = insertDapatEvent.tgl_transaksi,
            onValueChange = { onValueChange(insertDapatEvent.copy(tgl_transaksi = it)) },
            label = { Text("Tanggal Transaksi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertDapatEvent.total,
            onValueChange = { onValueChange(insertDapatEvent.copy(total = it)) },
            label = { Text("Total Pendapatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertDapatEvent.catatan,
            onValueChange = { onValueChange(insertDapatEvent.copy(catatan = it)) },
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
    }
}