package com.example.finalprojectpam.ui.pengeluaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.pendapatan.DetailPendapatanViewModel
import com.example.finalprojectpam.ui.pendapatan.DetailScreen

class DetailPengeluaranFragment : Fragment() {

    private lateinit var viewModel: DetailPengeluaranViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val idPengeluaran: String? = arguments?.getString("id_pengeluaran")
        if (idPengeluaran.isNullOrEmpty()) {
            findNavController().popBackStack()
            return null
        }

        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory)
            .get(DetailPengeluaranViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    DetailPengeluaranScreen(
                        onNavigateBack = { navigateToPengeluaranFragment() },
                        onEditClick = { navigateToEditPengeluaranFragment(idPengeluaran) },
                        onDeleteSuccess = { navigateToPengeluaranFragment() },
                        idPengeluaran = idPengeluaran,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun navigateToPengeluaranFragment() {
        // Navigasi menggunakan NavController
        findNavController().navigate(R.id.navigation_pengeluaran)
    }

    private fun navigateToEditPengeluaranFragment(idPengeluaran: String) {
        val bundle = Bundle().apply {
            putString("id_pengeluaran", idPengeluaran)
        }
        findNavController().navigate(R.id.navigation_edit_pengeluaran, bundle)
    }
}

@Composable
fun DetailPengeluaranScreen(
    idPengeluaran: String,
    onNavigateBack: () -> Unit,
    viewModel: DetailPengeluaranViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onEditClick: () -> Unit,
    onDeleteSuccess: () -> Unit
) {

    val detailPengeluaranState by viewModel.detailPengeluaranState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        when (detailPengeluaranState) {
            is DetailPengeluaranState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is DetailPengeluaranState.Success -> {
                val pengeluaran = (detailPengeluaranState as DetailPengeluaranState.Success).pengeluaran
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "ID Pendapatan: ${pengeluaran.id_pengeluaran}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "ID Aset: ${pengeluaran.id_aset}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "ID Kategori: ${pengeluaran.id_kategori}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Tanggal Transaksi: ${pengeluaran.tgl_transaksi}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Total Pendapatan: ${pengeluaran.total}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Catatan: ${pengeluaran.catatan}", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Edit
                    Button(
                        onClick = {
                            onEditClick()
                        },
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

            is DetailPengeluaranState.Error -> Box(
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
                        viewModel.deletePengeluaran(idPengeluaran)
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