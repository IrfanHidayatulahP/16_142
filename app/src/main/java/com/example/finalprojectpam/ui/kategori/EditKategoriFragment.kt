package com.example.finalprojectpam.ui.kategori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.model.Kategori
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.asset.UpdateAssetViewModel
import com.example.finalprojectpam.ui.asset.UpdateScreen

class EditKategoriFragment : Fragment() {

    private lateinit var viewModel: EditKategoriViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val idKategori: String = arguments?.getString("id_kategori")
            ?: throw IllegalStateException("id_kategori diperlukan")

        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(EditKategoriViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    EditKategoriScreen(
                        onNavigateToMenu = { navigateToAssetFragment() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun navigateToAssetFragment() {
        // Navigasi menggunakan NavController
        findNavController().navigate(R.id.navigation_kategori)
    }
}

@Composable
fun EditKategoriScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: EditKategoriViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val updateUiState by viewModel.editKategoriState.collectAsState()
    val kategoriData by viewModel.kategoriData.collectAsState()

    var nama_kategori by remember { mutableStateOf("") }

    LaunchedEffect(updateUiState) {
        if (updateUiState is EditKategoriState.DataLoaded) {
            nama_kategori = (updateUiState as EditKategoriState.DataLoaded).kategori.id_kategori
        }
    }

    Scaffold { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (updateUiState is EditKategoriState.Loading) {
                CircularProgressIndicator()
            } else {
                OutlinedTextField(
                    value = nama_kategori,
                    onValueChange = { nama_kategori = it},
                    label = { Text("Nama Kategori") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Button Simpan
                Button(
                    onClick = {
                        val editKategori = Kategori (
                            id_kategori = (updateUiState as? EditKategoriState.DataLoaded)?.kategori?.id_kategori ?: "",
                            nama_kategori = nama_kategori
                        )
                        viewModel.updateKategoriDetail(editKategori)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Perubahan")
                }

                // Navigasi ke Menu Kategori saat update berhasil
                when (updateUiState) {
                    is EditKategoriState.Success -> {
                        LaunchedEffect(Unit) {
                            onNavigateToMenu() }
                        Text("Data Berhasil diperbarui!", color = MaterialTheme.colorScheme.primary)
                    }
                    is EditKategoriState.Error -> {
                        Text("Gagal Memperbarui Data.", color = MaterialTheme.colorScheme.error)
                    }
                    else -> {}
                }
            }
        }
    }
}