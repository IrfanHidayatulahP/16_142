package com.example.finalprojectpam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finalprojectpam.model.Saldo
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(HomeViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val saldoState = viewModel.saldoUiState

    when (saldoState) {
        is SaldoUiState.Loading -> {
            LoadingView()
        }
        is SaldoUiState.Success -> {
            SuccessView(saldo = saldoState.saldo)
        }
        is SaldoUiState.Error -> {
            ErrorView(message = saldoState.message)
        }
    }
}


@Composable
fun SuccessView(saldo: Saldo) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SaldoCard(title = "Pendapatan", amount = saldo.total_pendapatan.toString())
        SaldoCard(title = "Pengeluaran", amount = saldo.total_pengeluaran.toString())
        SaldoCard(title = "Saldo", amount = saldo.saldo.toString())
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun SaldoCard(title: String, amount: String) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = amount, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
