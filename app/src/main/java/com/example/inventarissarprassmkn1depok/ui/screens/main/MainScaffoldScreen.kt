package com.example.inventarissarprassmkn1depok.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarissarprassmkn1depok.data.local.AppDatabase
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import com.example.inventarissarprassmkn1depok.data.repository.SarprasRepository
import com.example.inventarissarprassmkn1depok.data.repository.SirkulasiRepository
import com.example.inventarissarprassmkn1depok.ui.components.BottomNavBar
import com.example.inventarissarprassmkn1depok.ui.components.BottomNavTab
import com.example.inventarissarprassmkn1depok.ui.screens.dashboard.DashboardScreen
import com.example.inventarissarprassmkn1depok.ui.screens.dashboard.DashboardViewModel
import com.example.inventarissarprassmkn1depok.ui.screens.katalog.KatalogScreen
import com.example.inventarissarprassmkn1depok.ui.screens.katalog.KatalogViewModel
import com.example.inventarissarprassmkn1depok.ui.screens.sirkulasi.SirkulasiScreen
import com.example.inventarissarprassmkn1depok.ui.screens.sirkulasi.SirkulasiViewModel
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.theme.PrimaryContainer
import com.example.inventarissarprassmkn1depok.ui.theme.Secondary
import com.example.inventarissarprassmkn1depok.ui.theme.Surface
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow

@Composable
fun MainScaffoldScreen(
    database: AppDatabase,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authRepository = remember { AuthRepository(database.adminDao()) }
    val sarprasRepository = remember {
        SarprasRepository(
            barangDao = database.barangDao(),
            kategoriDao = database.kategoriDao(),
            peminjamanDao = database.peminjamanDao(),
            dashboardDao = database.dashboardDao()
        )
    }

    val sirkulasiRepository = remember {
        SirkulasiRepository(
            peminjamanDao = database.peminjamanDao(),
            barangDao = database.barangDao(),
            adminDao = database.adminDao()
        )
    }

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(sarprasRepository, authRepository)
    )

    val katalogViewModel: KatalogViewModel = viewModel(
        factory = KatalogViewModel.Factory(sarprasRepository)
    )

    val sirkulasiViewModel: SirkulasiViewModel = viewModel(
        factory = SirkulasiViewModel.Factory(sirkulasiRepository)
    )

    var currentTab by remember { mutableStateOf(BottomNavTab.DASHBOARD) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        containerColor = Surface,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (currentTab) {
                BottomNavTab.DASHBOARD -> {
                    DashboardScreen(
                        viewModel = dashboardViewModel,
                        onNavigateToKatalog = { currentTab = BottomNavTab.KATALOG },
                        onNavigateToSirkulasi = { currentTab = BottomNavTab.SIRKULASI },
                        onLogout = onLogout,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                BottomNavTab.KATALOG -> {
                    KatalogScreen(
                        viewModel = katalogViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                BottomNavTab.SIRKULASI -> {
                    SirkulasiScreen(
                        viewModel = sirkulasiViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                BottomNavTab.LAPORAN -> {
                    PlaceholderModuleScreen(
                        title = "Laporan & Riwayat Audit",
                        step = "Langkah 5",
                        description = "Rekapitulasi sirkulasi barang per bulan, data mutasi sarpras, filter tanggal fleksibel, dan fitur ekspor dokumen resmi PDF & Excel siap cetak.",
                        icon = Icons.Default.Analytics,
                        actionLabel = "Kembali ke Dashboard",
                        onAction = { currentTab = BottomNavTab.DASHBOARD }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderModuleScreen(
    title: String,
    step: String,
    description: String,
    icon: ImageVector,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(SurfaceContainerLow, CircleShape)
                        .border(BorderStroke(1.dp, CardBorder), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Secondary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .background(SurfaceContainerLow, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Fokus Berikutnya: $step",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Secondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = actionLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
