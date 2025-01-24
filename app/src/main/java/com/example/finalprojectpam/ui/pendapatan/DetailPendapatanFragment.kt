package com.example.finalprojectpam.ui.pendapatan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel

class DetailPendapatanFragment : Fragment() {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    idPendapatan: String,
    onNavigateBack: () -> Unit,
    viewModel: DetailPendapatanViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onEditClick: () -> Unit,
    onDeleteSuccess: () -> Unit
) {

    val detailPendapatanState by viewModel.detailPendapatanState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        when (detailPendapatanState) {
            is DetailPendapatanState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is DetailPendapatanState.Success -> {
                val pendapatan = (detailPendapatanState as DetailPendapatanState.Success).pendapatan
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "ID Pendapatan: ${pendapatan.id_pendapatan}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "ID Aset: ${pendapatan.id_aset}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "ID Kategori: ${pendapatan.id_kategori}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Tanggal Transaksi: ${pendapatan.tgl_transaksi}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Total Pendapatan: ${pendapatan.total}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Catatan: ${pendapatan.catatan}", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Edit
                    Button(
                        onClick = onEditClick,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Edit")
                    }

                    // Tombol Delete
                    Button(
                        onClick = { showDeleteDialog = true },
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Delete")
                    }
                }
            }

            is DetailPendapatanState.Error -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Gagal memuat data. Silahkan coba lagi.")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Data") },
            text = { Text("Apakah Anda Ingin Menghapus Data Ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePendapatan(idPendapatan)
                        showDeleteDialog = false
                        onDeleteSuccess()
                    }
                ) {
                    Text("Ya, Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}