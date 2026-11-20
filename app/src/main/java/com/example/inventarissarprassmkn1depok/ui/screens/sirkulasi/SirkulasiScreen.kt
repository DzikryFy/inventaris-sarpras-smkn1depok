package com.example.inventarissarprassmkn1depok.ui.screens.sirkulasi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.R
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.theme.Outline
import com.example.inventarissarprassmkn1depok.ui.theme.OutlineVariant
import com.example.inventarissarprassmkn1depok.ui.theme.PrimaryContainer
import com.example.inventarissarprassmkn1depok.ui.theme.Secondary
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableText
import com.example.inventarissarprassmkn1depok.ui.theme.StatusBorrowedBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusBorrowedDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusBorrowedText
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedText
import com.example.inventarissarprassmkn1depok.ui.theme.Surface
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SirkulasiScreen(
    viewModel: SirkulasiViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val returnSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val formSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val todayDateStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar("Error: $error")
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Surface,
        topBar = {
            // Header Institusional Sirkulasi Sarpras
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color.White)
                    .border(BorderStroke(1.dp, CardBorder.copy(alpha = 0.5f)))
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_smkn1depok),
                        contentDescription = "Logo",
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = "SMKN 1 DEPOK",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Sirkulasi Sarpras",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }
                }

                // Badge Ringkasan Aktif
                Box(
                    modifier = Modifier
                        .background(StatusBorrowedBg, RoundedCornerShape(9999.dp))
                        .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(9999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${uiState.rawActiveCount} Dipinjam",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusBorrowedText
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openForm() },
                containerColor = Secondary,
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp),
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PostAdd,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Catat Peminjaman",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Search Bar & Filter Chips Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "Cari peminjam, barang, NIP/NIS...",
                            fontSize = 13.sp,
                            color = OnSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari",
                            tint = Secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Hapus",
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLow,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                // Filter Chips Tipe Peminjam (Semua, Guru, Siswa, OSIS)
                val filterList = listOf(null to "Semua", "Guru" to "Guru", "Siswa" to "Siswa", "OSIS" to "OSIS")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterList) { (tipeKey, label) ->
                        val isSelected = uiState.selectedTipeFilter == tipeKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(if (isSelected) Secondary else SurfaceContainerLow)
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Secondary else CardBorder),
                                    RoundedCornerShape(9999.dp)
                                )
                                .clickable { viewModel.setTipeFilter(tipeKey) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else OnSurface
                            )
                        }
                    }
                }
            }

            // 2. Segmented Tab Bar M3 (Sedang Dipinjam vs Riwayat Selesai)
            SecondaryTabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                containerColor = Color.White,
                contentColor = Secondary,
                divider = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(CardBorder)
                    )
                }
            ) {
                Tab(
                    selected = uiState.selectedTab == SirkulasiTab.AKTIF,
                    onClick = { viewModel.selectTab(SirkulasiTab.AKTIF) },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Sedang Dipinjam (${uiState.rawActiveCount})",
                                fontSize = 12.sp,
                                fontWeight = if (uiState.selectedTab == SirkulasiTab.AKTIF) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )

                Tab(
                    selected = uiState.selectedTab == SirkulasiTab.RIWAYAT,
                    onClick = { viewModel.selectTab(SirkulasiTab.RIWAYAT) },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Riwayat Selesai (${uiState.rawCompletedCount})",
                                fontSize = 12.sp,
                                fontWeight = if (uiState.selectedTab == SirkulasiTab.RIWAYAT) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
            }

            // 3. Konten List Peminjaman
            val displayList = if (uiState.selectedTab == SirkulasiTab.AKTIF) {
                uiState.activeLoans
            } else {
                uiState.completedLoans
            }

            if (displayList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(SurfaceContainerLow, CircleShape)
                                .border(BorderStroke(1.dp, CardBorder), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (uiState.selectedTab == SirkulasiTab.AKTIF) Icons.Default.AssignmentTurnedIn else Icons.Default.History,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = if (uiState.selectedTab == SirkulasiTab.AKTIF) "Tidak ada peminjaman aktif" else "Belum ada riwayat transaksi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = if (uiState.searchQuery.isNotEmpty()) "Tidak ada transaksi yang cocok dengan kata kunci pencarian" else "Seluruh peralatan sarpras berada di tempat penyimpanan.",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = displayList,
                        key = { it.idPinjam }
                    ) { loan ->
                        PeminjamanItemCard(
                            loan = loan,
                            isHistory = uiState.selectedTab == SirkulasiTab.RIWAYAT,
                            todayDate = todayDateStr,
                            onReturnClick = { viewModel.openReturnSheet(loan) }
                        )
                    }

                    // Spacer agar item terbawah tidak tertutup FAB
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet Pengembalian
    if (uiState.selectedLoanForReturn != null) {
        ReturnItemBottomSheet(
            loan = uiState.selectedLoanForReturn!!,
            sheetState = returnSheetState,
            onDismiss = { viewModel.closeReturnSheet() },
            onConfirm = { kondisi, catatan ->
                viewModel.confirmReturn(
                    idPinjam = uiState.selectedLoanForReturn!!.idPinjam,
                    kondisiKembali = kondisi,
                    catatan = catatan
                )
            }
        )
    }

    // Modal Bottom Sheet Peminjaman Baru
    if (uiState.isFormOpen) {
        FormPeminjamanBottomSheet(
            barangList = uiState.allBarang,
            preselectedBarang = uiState.preselectedBarang,
            sheetState = formSheetState,
            onDismiss = { viewModel.closeForm() },
            onSubmit = { nomorIdentitas, namaPeminjam, tipePeminjam, idBarang, jumlahPinjam, batasKembali, keperluan ->
                viewModel.submitPeminjaman(
                    nomorIdentitas = nomorIdentitas,
                    namaPeminjam = namaPeminjam,
                    tipePeminjam = tipePeminjam,
                    idBarang = idBarang,
                    jumlahPinjam = jumlahPinjam,
                    batasKembali = batasKembali,
                    keperluan = keperluan
                )
            }
        )
    }
}

@Composable
private fun PeminjamanItemCard(
    loan: PeminjamanDetail,
    isHistory: Boolean,
    todayDate: String,
    onReturnClick: () -> Unit
) {
    val isTerlambat = !isHistory && loan.batasKembali.take(10) < todayDate

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: No. Transaksi Monospace, Tipe Peminjam & Badge Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "#TRX-${loan.idPinjam.toString().padStart(4, '0')}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                    Text(text = "•", fontSize = 10.sp, color = CardBorder)
                    Box(
                        modifier = Modifier
                            .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = loan.tipePeminjam.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant
                        )
                    }
                }

                // Badge Status
                if (isHistory) {
                    val isRusak = loan.kondisiKembali?.contains("Rusak", ignoreCase = true) == true
                    Box(
                        modifier = Modifier
                            .background(if (isRusak) StatusDamagedBg else StatusAvailableBg, RoundedCornerShape(9999.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isRusak) StatusDamagedDot else StatusAvailableDot)
                            )
                            Text(
                                text = if (isRusak) "Kembali (Rusak)" else "Kembali (Baik)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isRusak) StatusDamagedText else StatusAvailableText
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(if (isTerlambat) StatusDamagedBg else StatusBorrowedBg, RoundedCornerShape(9999.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isTerlambat) StatusDamagedDot else StatusBorrowedDot)
                            )
                            Text(
                                text = if (isTerlambat) "Jatuh Tempo" else "Dipinjam",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTerlambat) StatusDamagedText else StatusBorrowedText
                            )
                        }
                    }
                }
            }

            // Peminjam & Barang
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = loan.namaPeminjam,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        text = "ID: ${loan.nomorIdentitas}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = OnSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .background(SurfaceContainerLow, RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${loan.jumlahPinjam} Unit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
            }

            // Detail Barang & Keperluan
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = loan.namaBarang,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                        Text(
                            text = "(${loan.kodeBarang})",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = OnSurfaceVariant
                        )
                    }

                    Text(
                        text = "Keperluan: \"${loan.keperluanKegiatan}\"",
                        fontSize = 11.sp,
                        color = OnSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Waktu Pinjam & Tenggat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = if (isTerlambat) StatusDamagedText else OnSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (isHistory) {
                            "Kembali: ${loan.tglKembaliReal ?: "-"}"
                        } else {
                            "Tenggat: ${loan.batasKembali}"
                        },
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isTerlambat) FontWeight.Bold else FontWeight.Normal,
                        color = if (isTerlambat) StatusDamagedText else OnSurfaceVariant
                    )
                }

                // Tombol Aksi Terima Kembali jika aktif
                if (!isHistory) {
                    Button(
                        onClick = onReturnClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "TERIMA KEMBALI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Catatan Pengembalian pada Riwayat jika ada
            if (isHistory && !loan.catatan.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Catatan: ${loan.catatan}",
                        fontSize = 10.sp,
                        color = OnSurfaceVariant
                    )
                }
            }
        }
    }
}
