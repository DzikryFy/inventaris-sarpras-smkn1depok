package com.example.inventarissarprassmkn1depok.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarissarprassmkn1depok.data.local.AppDatabase
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import com.example.inventarissarprassmkn1depok.data.repository.LaporanRepository
import com.example.inventarissarprassmkn1depok.data.repository.SarprasRepository
import com.example.inventarissarprassmkn1depok.data.repository.SirkulasiRepository
import com.example.inventarissarprassmkn1depok.ui.components.BottomNavBar
import com.example.inventarissarprassmkn1depok.ui.components.BottomNavTab
import com.example.inventarissarprassmkn1depok.ui.screens.dashboard.DashboardScreen
import com.example.inventarissarprassmkn1depok.ui.screens.dashboard.DashboardViewModel
import com.example.inventarissarprassmkn1depok.ui.screens.katalog.KatalogScreen
import com.example.inventarissarprassmkn1depok.ui.screens.katalog.KatalogViewModel
import com.example.inventarissarprassmkn1depok.ui.screens.laporan.LaporanScreen
import com.example.inventarissarprassmkn1depok.ui.screens.laporan.LaporanViewModel
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
    val isStaff = remember { authRepository.isStaff() }
    val loggedInAdmin = remember { authRepository.getLoggedInAdmin() }

    val sarprasRepository = remember {
        SarprasRepository(
            barangDao = database.barangDao(),
            kategoriDao = database.kategoriDao(),
            peminjamanDao = database.peminjamanDao(),
            dashboardDao = database.dashboardDao(),
            adminDao = database.adminDao()
        )
    }

    val sirkulasiRepository = remember {
        SirkulasiRepository(
            peminjamanDao = database.peminjamanDao(),
            barangDao = database.barangDao(),
            adminDao = database.adminDao()
        )
    }

    val laporanRepository = remember {
        LaporanRepository(
            peminjamanDao = database.peminjamanDao(),
            barangDao = database.barangDao(),
            kategoriDao = database.kategoriDao(),
            dashboardDao = database.dashboardDao()
        )
    }

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(sarprasRepository, authRepository)
    )

    val katalogViewModel: KatalogViewModel = viewModel(
        factory = KatalogViewModel.Factory(sarprasRepository, authRepository)
    )

    val sirkulasiViewModel: SirkulasiViewModel = viewModel(
        factory = SirkulasiViewModel.Factory(sirkulasiRepository, authRepository)
    )

    val laporanViewModel: LaporanViewModel = viewModel(
        factory = LaporanViewModel.Factory(laporanRepository)
    )

    var currentTab by remember { mutableStateOf(BottomNavTab.DASHBOARD) }

    androidx.compose.runtime.LaunchedEffect(isStaff, currentTab) {
        if (!isStaff && currentTab == BottomNavTab.LAPORAN) {
            currentTab = BottomNavTab.DASHBOARD
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it },
                isStaff = isStaff
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                        isStaff = isStaff,
                        currentBorrowerName = loggedInAdmin?.namaLengkap ?: "",
                        currentBorrowerId = loggedInAdmin?.nipPetugas ?: "",
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
                    LaporanScreen(
                        viewModel = laporanViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
