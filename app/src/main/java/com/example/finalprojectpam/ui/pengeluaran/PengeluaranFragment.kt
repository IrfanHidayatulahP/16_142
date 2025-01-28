package com.example.finalprojectpam.ui.pengeluaran

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.databinding.FragmentPengeluaranBinding
import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import com.example.finalprojectpam.ui.home.HomeViewModel
import com.example.finalprojectpam.ui.home.SaldoUiState
import com.example.finalprojectpam.ui.pendapatan.DapatUiState
import com.example.finalprojectpam.ui.pendapatan.PendapatanViewModel

class PengeluaranFragment : Fragment() {

    private lateinit var viewModel: PengeluaranViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(PengeluaranViewModel::class.java)
        homeViewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(HomeViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    val saldoState = homeViewModel.saldoUiState
                    val saldo = if (saldoState is SaldoUiState.Success) (saldoState as SaldoUiState.Success).saldo.saldo else 0

                    HomePengeluaran(
                        navigateToItemEntry = {
                            if (saldo < 0) {
                                showSaldoMinusDialog()
                            } else {
                                findNavController().navigate(R.id.action_pengeluaran_to_insert)
                            }
                        },
                        navController = findNavController(),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
    private fun showSaldoMinusDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Peringatan")
            .setMessage("Keuangan anda sudah minus, apakah anda yakin menambah data pengeluaran?")
            .setPositiveButton("Ya") { _, _ ->
                findNavController().navigate(R.id.action_pengeluaran_to_insert)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}

@Composable
fun HomePengeluaran(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDetailClick: (Pengeluaran) -> Unit = {},
    viewModel: PengeluaranViewModel
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Asset")
            }
        }
    ) { innerPadding ->
        KeluarStatus (
            keluarUiState = viewModel.keluarUiState,
            retryAction = { viewModel.getPengeluaran() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { keluar ->
                viewModel.deletePendapatan(keluar.id_pengeluaran)
            },
            navController = navController
        )
    }
}

@Composable
fun KeluarStatus(
    keluarUiState: KeluarUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDeleteClick: (Pengeluaran) -> Unit = {},
    onDetailClick: (Pengeluaran) -> Unit
) {
    when (keluarUiState) {
        is KeluarUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is KeluarUiState.Success ->
            if (keluarUiState is KeluarUiState.Success) {
                if (keluarUiState.keluar.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Tidak Ada Aset")
                    }
                } else {
                    PengeluaranLayout(
                        pengeluaran = keluarUiState.keluar,
                        onDetailClick = onDetailClick,
                        onDeleteClick = onDeleteClick,
                        navController = navController
                    )
                }
            }
        is KeluarUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.cycle),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.cycle), contentDescription = ""
        )
        Text(
            text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun PengeluaranLayout(
    pengeluaran: List<Pengeluaran>,
    modifier: Modifier = Modifier,
    onDetailClick: (Pengeluaran) -> Unit,
    onDeleteClick: (Pengeluaran) -> Unit = {},
    navController: NavController
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pengeluaran) { keluar ->
            PengeluaranCard(
                pengeluaran = keluar,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(keluar) },
                onDeleteClick = {
                    onDeleteClick(keluar)
                },
                navController = navController
            )
        }
    }
}

@Composable
fun PengeluaranCard(
    pengeluaran: Pengeluaran,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDeleteClick: (Pengeluaran) -> Unit = {}
) {
    Card (
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text (
                    text = pengeluaran.id_pengeluaran,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {onDeleteClick(pengeluaran)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }
            Text (
                text = pengeluaran.id_aset,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pengeluaran.id_kategori,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pengeluaran.tgl_transaksi,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pengeluaran.total,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pengeluaran.catatan,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}