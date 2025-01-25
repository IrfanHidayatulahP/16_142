package com.example.finalprojectpam.ui.pengeluaran

import com.example.finalprojectpam.ui.pendapatan.InsertDapatEvent
import com.example.finalprojectpam.ui.pendapatan.InsertDapatState
import com.example.finalprojectpam.ui.pendapatan.InsertPendapatanViewModel
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

class InsertPengeluaranFragment : Fragment() {

    private lateinit var viewModel: InsertPengeluaranViewModel
    private lateinit var assetViewModel: AssetViewModel
    private lateinit var ktgViewModel: KategoriViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the custom factory to create the InsertKategoriViewModel
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(InsertPengeluaranViewModel::class.java)
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
        // Navigasi menggunakan NavController
        findNavController().navigate(R.id.navigation_pendapatan)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPendapatanScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPengeluaranViewModel = viewModel(factory = PenyediaViewModel.Factory),
    assetViewModel: AssetViewModel,
    ktgViewModel: KategoriViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        EntryBody(
            insertKeluarState = viewModel.keluarState,
            asetList = (assetViewModel.asetUiState as? HomeUiState.Success)?.aset ?: emptyList(),
            kategoriList = (ktgViewModel.katUiState as? KatUiState.Success)?.kategori ?: emptyList(),
            onPengeluaranValueChange = viewModel::updateInsertKeluarState,
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
    insertKeluarState: InsertKeluarState,
    onPengeluaranValueChange: (InsertKeluarEvent) -> Unit,
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
            insertKeluarEvent = insertKeluarState.insertKeluarEvent,
            onValueChange = onPengeluaranValueChange,
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
    insertKeluarEvent: InsertKeluarEvent,
    asetList: List<Aset>,
    kategoriList: List<Kategori>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertKeluarEvent) -> Unit = {},
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
                            onValueChange(insertKeluarEvent.copy(id_aset = aset.id_aset))
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
                            onValueChange(insertKeluarEvent.copy(id_kategori = ktg.id_kategori))
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = insertKeluarEvent.tgl_transaksi,
            onValueChange = {onValueChange(insertKeluarEvent.copy(tgl_transaksi = it))},
            label = { Text("Tanggal Transaksi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertKeluarEvent.total,
            onValueChange = {onValueChange(insertKeluarEvent.copy(total = it))},
            label = { Text("Total Pendapatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertKeluarEvent.catatan,
            onValueChange = {onValueChange(insertKeluarEvent.copy(catatan = it))},
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