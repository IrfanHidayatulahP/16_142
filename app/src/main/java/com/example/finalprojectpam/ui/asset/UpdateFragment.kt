package com.example.finalprojectpam.ui.asset

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.Repository.AsetRepository
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel

class UpdateFragment : Fragment() {

    private lateinit var viewModel: UpdateAssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val idAset: String = arguments?.getString("id_aset")
            ?: throw IllegalStateException("id_aset diperlukan")

        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(UpdateAssetViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    UpdateScreen(
                        onNavigateToMenu = { navigateToAssetFragment() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun navigateToAssetFragment() {
        // Navigasi menggunakan NavController
        findNavController().navigate(R.id.navigation_asset)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: UpdateAssetViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val updateUiState by viewModel.updateUiState.collectAsState()
    val asetData by viewModel.asetData.collectAsState()

    var nama_aset by remember { mutableStateOf("") }

    if (updateUiState is UpdateUiState.DataLoaded) {
        val data = (updateUiState as UpdateUiState.DataLoaded).aset
        nama_aset = data.nama_aset
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Aset") },
                actions = {
                    IconButton(onClick = onNavigateToMenu) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (updateUiState is UpdateUiState.Loading) {
                CircularProgressIndicator()
            } else {
                OutlinedTextField(
                    value = nama_aset,
                    onValueChange = { nama_aset = it},
                    label = { Text("Nama Aset") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Button Simpan
                Button(
                    onClick = {
                        val updateAset = Aset (
                            id_aset = (updateUiState as UpdateUiState.DataLoaded).aset.id_aset,
                            nama_aset = nama_aset
                        )
                        viewModel.updateAsetDetail(updateAset)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Perubahan")
                }

                // Navigasi ke Menu Aset saat update berhasil
                when (updateUiState) {
                    is UpdateUiState.Success -> {
                        LaunchedEffect (Unit) {
                            onNavigateToMenu()
                        }
                        Text("Data Berhasil diperbarui!", color = MaterialTheme.colorScheme.primary)
                    }
                    is UpdateUiState.Error -> {
                        Text("Gagal Memperbarui Data.", color = MaterialTheme.colorScheme.error)
                    }
                    else -> {}
                }
            }
        }
    }
}