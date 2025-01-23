package com.example.finalprojectpam.ui.kategori

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
import androidx.compose.material.icons.filled.Edit
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
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.model.Kategori
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel

class KategoriFragment : Fragment() {

    private lateinit var viewModel: KategoriViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(KategoriViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    HomeKategori(
                        navigateToItemEntry = {
                            findNavController().navigate(R.id.action_kategori_to_insert)
                        },
                        onEditClick = { kategori ->
                            val action = KategoriFragmentDirections.actionKategoriToUpdate(kategori.id_kategori)
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
        viewModel.getKategori() // Refresh data setiap kali fragment ditampilkan ulang
    }
}

@Composable
fun HomeKategori(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    onEditClick: (Kategori) -> Unit = {},
    viewModel: KategoriViewModel
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Kategori")
            }
        }
    ) { innerPadding ->
        KatStatus (
            katUiState = viewModel.katUiState,
            retryAction = { viewModel.getKategori() },
            modifier = Modifier.padding(innerPadding),
            onEditClick = onEditClick,
            onDeleteClick = { kat ->
                viewModel.deleteKategori(kat.id_kategori)
            },
            navController = navController
        )
    }
}

@Composable
fun KatStatus(
    katUiState: KatUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDeleteClick: (Kategori) -> Unit = {},
    onEditClick: (Kategori) -> Unit
) {
    when (katUiState) {
        is KatUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is KatUiState.Success ->
            if (katUiState is KatUiState.Success) {
                if (katUiState.kategori.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Tidak Ada Kategori")
                    }
                } else {
                    KategoriLayout(
                        kategori = katUiState.kategori,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick,
                        navController = navController
                    )
                }
            }
        is KatUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun KategoriLayout(
    kategori: List<Kategori>,
    modifier: Modifier = Modifier,
    onDeleteClick: (Kategori) -> Unit = {},
    onEditClick: (Kategori) -> Unit,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(kategori) { kat ->
            KategoriCard(
                kategori = kat,
                modifier = Modifier
                    .fillMaxWidth(),
                onDeleteClick = {
                    onDeleteClick(kat)
                },
                onEditClick = onEditClick,
                navController = navController
            )
        }
    }
}

@Composable
fun KategoriCard(
    kategori: Kategori,
    modifier: Modifier = Modifier,
    navController: NavController,
    onEditClick: (Kategori) -> Unit = {},
    onDeleteClick: (Kategori) -> Unit = {}
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
                    text = kategori.id_kategori,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onEditClick(kategori) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Kategori",
                    )
                }
                IconButton(onClick = {onDeleteClick(kategori)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }
            Text (
                text = kategori.nama_kategori,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}