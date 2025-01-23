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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import kotlinx.coroutines.launch

class InsertPendapatanFragment : Fragment() {

    private lateinit var viewModel: InsertPendapatanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the custom factory to create the InsertKategoriViewModel
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(InsertPendapatanViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    EntryPendapatanScreen(
                        navigateBack = { navigateToPendapatanFragment() },
                        viewModel = viewModel
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
    viewModel: InsertPendapatanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        EntryBody(
            insertDapatState = viewModel.dapatState,
            onSiswaValueChange = viewModel::updateInsertDapatState,
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
    onSiswaValueChange: (InsertDapatEvent) -> Unit,
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
    modifier: Modifier = Modifier,
    onValueChange: (InsertDapatEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertDapatEvent.id_pendapatan,
            onValueChange = {onValueChange(insertDapatEvent.copy(id_pendapatan = it))},
            label = { Text("Id Pendapatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertDapatEvent.id_aset,
            onValueChange = {onValueChange(insertDapatEvent.copy(id_aset = it))},
            label = { Text("Id Aset") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertDapatEvent.id_kategori,
            onValueChange = {onValueChange(insertDapatEvent.copy(id_kategori = it))},
            label = { Text("Id Kategori") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

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
            label = { Text("Kelas") },
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