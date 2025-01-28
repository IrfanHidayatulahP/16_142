package com.example.finalprojectpam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.model.Saldo
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory)
            .get(HomeViewModel::class.java)

        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            val navController = findNavController()
            HomeScreen(navController, viewModel)
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val saldoState = viewModel.saldoUiState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (saldoState) {
            is SaldoUiState.Loading -> {
                LoadingView()
            }
            is SaldoUiState.Success -> {
                SuccessView(navController, saldo = saldoState.saldo, viewModel = viewModel)
            }
            is SaldoUiState.Error -> {
                ErrorView(message = saldoState.message)
            }
        }
    }
}

@Composable
fun SuccessView(
    navController: NavController,
    viewModel: HomeViewModel,
    saldo: Saldo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF90CAF9))
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Selamat Datang",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { viewModel.getAll() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                val saldoColor = if (saldo.saldo > 0) Color.Green else Color.Red
                SaldoCard(
                    title = "Saldo",
                    amount = saldo.saldo.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    backgroundColor = saldoColor,
                    onClick = { }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SaldoCard(
                title = "Pendapatan",
                amount = saldo.total_pendapatan.toString(),
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                backgroundColor = Color.Green,
                onClick = { navController.navigate(R.id.action_home_to_pendapatan) }
            )
            SaldoCard(
                title = "Pengeluaran",
                amount = saldo.total_pengeluaran.toString(),
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                backgroundColor = Color.Red,
                onClick = { navController.navigate(R.id.action_home_to_pengeluaran) }
            )
        }
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
fun ErrorView(
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun SaldoCard(
    title: String,
    amount: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = amount, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
