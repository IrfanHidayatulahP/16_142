package com.example.finalprojectpam.ui.asset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
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
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel

class AssetFragment : Fragment() {

    private lateinit var viewModel: AssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Using the custom factory to create the AssetViewModel
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(AssetViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                HomeAset(
                    navigateToItemEntry = {
                        findNavController().navigate(R.id.action_asset_to_insert)
                    },
                    onEditClick = { aset ->
                        val action = AssetFragmentDirections.actionAssetToUpdate(aset.id_aset)
                        findNavController().navigate(action)
                    },
                    viewModel = viewModel,
                    navController = findNavController() // Pass the correct NavController
                )
            }
        }
    }
}

@Composable
fun HomeAset(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (Aset) -> Unit,
    viewModel: AssetViewModel,
    navController: NavController
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
        HomeStatus(
            homeUiState = viewModel.asetUiState,
            retryAction = { viewModel.getAset() },
            modifier = Modifier.padding(innerPadding),
            onDeleteClick = { aset ->
                viewModel.deleteAset(aset.id_aset)
            },
            onEditClick = onEditClick,
            navController = navController
        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Aset) -> Unit = {},
    navController: NavController,
    onEditClick: (Aset) -> Unit
) {
    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success ->
            if (homeUiState is HomeUiState.Success) {
                if (homeUiState.aset.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Tidak Ada Aset")
                    }
                } else {
                    AsetLayout(
                        aset = homeUiState.aset,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick,
                        navController = navController
                    )
                }
            }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun AsetLayout(
    aset: List<Aset>,
    modifier: Modifier = Modifier,
    onDeleteClick: (Aset) -> Unit = {},
    onEditClick: (Aset) -> Unit,
    navController: NavController
) {
    Log.d("AsetLayout", "Total Aset: ${aset.size}")
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(aset) { ast ->
            AsetCard(
                aset = ast,
                modifier = Modifier
                    .fillMaxWidth(),
                onDeleteClick = {
                    onDeleteClick(ast)
                },
                onEditClick = onEditClick,
                navController = navController
            )
        }
    }
}

@Composable
fun AsetCard(
    aset: Aset,
    modifier: Modifier = Modifier,
    navController: NavController,
    onDeleteClick: (Aset) -> Unit = {},
    onEditClick: (Aset) -> Unit = {} // Tambahkan parameter untuk edit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = aset.id_aset,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                // Tambahkan IconButton Edit
                IconButton(onClick = { onEditClick(aset) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Asset",
                    )
                }
                IconButton(onClick = { onDeleteClick(aset) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Asset",
                    )
                }
            }
            Text(
                text = aset.nama_aset,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
