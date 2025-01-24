package com.example.finalprojectpam.ui.pendapatan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
class PendapatanFragment : Fragment() {

    private lateinit var viewModel: PendapatanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(PendapatanViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    HomePendapatan(
                        navigateToItemEntry = {
                            findNavController().navigate(R.id.action_pendapatan_to_insert)
                        },
                        onDetailClick = { dapat ->
                            val action = PendapatanFragmentDirections.actionPendapatanToDetail(dapat.id_pendapatan)
                            findNavController().navigate(action)
                        },
                        viewModel = viewModel,
                        navController = findNavController()
                    )
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getPendapatan() // Refresh data saat kembali ke halaman ini
    }
}

@Composable
fun HomePendapatan(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDetailClick: (Pendapatan) -> Unit = {},
    viewModel: PendapatanViewModel
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Pendapatan")
            }
        }
    ) { innerPadding ->
        DapatStatus (
            dapatUiState = viewModel.dapatUiState,
            retryAction = { viewModel.getPendapatan() },
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            onDetailClick = onDetailClick,
            onDeleteClick = { dapat ->
                viewModel.deletePendapatan(dapat.id_pendapatan)
            }
        )
    }
}

@Composable
fun DapatStatus(
    dapatUiState: DapatUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDeleteClick: (Pendapatan) -> Unit = {},
    onDetailClick: (Pendapatan) -> Unit
) {
    when (dapatUiState) {
        is DapatUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is DapatUiState.Success ->
            if (dapatUiState is DapatUiState.Success) {
                if (dapatUiState.dapat.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Tidak Ada Aset")
                    }
                } else {
                    PendapatanLayout(
                        pendapatan = dapatUiState.dapat,
                        onDetailClick = onDetailClick,
                        onDeleteClick = onDeleteClick,
                        navController = navController
                    )
                }
            }
        is DapatUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun PendapatanLayout(
    pendapatan: List<Pendapatan>,
    modifier: Modifier = Modifier,
    onDetailClick: (Pendapatan) -> Unit,
    onDeleteClick: (Pendapatan) -> Unit = {},
    navController: NavController
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pendapatan) { dapat ->
            PendapatanCard(
                pendapatan = dapat,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(dapat) },
                onDeleteClick = {
                    onDeleteClick(dapat)
                },
                navController = navController
            )
        }
    }
}

@Composable
fun PendapatanCard(
    pendapatan: Pendapatan,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDeleteClick: (Pendapatan) -> Unit = {}
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
                    text = pendapatan.id_pendapatan,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {onDeleteClick(pendapatan)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }
            Text (
                text = pendapatan.id_aset,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pendapatan.id_kategori,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pendapatan.tgl_transaksi,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pendapatan.total,
                style = MaterialTheme.typography.titleMedium,
            )
            Text (
                text = pendapatan.catatan,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}