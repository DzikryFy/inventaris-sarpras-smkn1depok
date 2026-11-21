package com.example.inventarissarprassmkn1depok.ui.screens.laporan

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.R
import com.example.inventarissarprassmkn1depok.data.local.model.AuditSummary
import com.example.inventarissarprassmkn1depok.data.local.model.KategoriSirkulasiStat
import com.example.inventarissarprassmkn1depok.data.local.model.LokasiDistribusi
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.theme.Secondary
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableDot
import com.example.inventarissarprassmkn1depok.ui.utils.DateFormatterHelper
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableText
import com.example.inventarissarprassmkn1depok.ui.theme.StatusBorrowedBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusBorrowedDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusBorrowedText
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedText
import com.example.inventarissarprassmkn1depok.ui.theme.Surface
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow

@Composable
fun LaporanScreen(
    viewModel: LaporanViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.exportMessage) {
        uiState.exportMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Surface,
        topBar = {
            // Header Institusional Laporan & Audit dengan Status Bar Insets
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(BorderStroke(1.dp, CardBorder.copy(alpha = 0.5f)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
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
                            contentDescription = "Logo SMKN 1 Depok",
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
                                text = "Laporan & Riwayat Audit",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        }
                    }

                    // Badge Rasio Ketersediaan
                    Box(
                        modifier = Modifier
                            .background(StatusAvailableBg, RoundedCornerShape(9999.dp))
                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(9999.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Siap ${uiState.summary.persentaseKetersediaan}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusAvailableText
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Sticky Bottom Action Bar untuk Kirim Rekapitulasi Laporan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(BorderStroke(1.dp, CardBorder))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Button(
                    onClick = { viewModel.shareLaporan(context, asCsv = false) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Kirim Rekapitulasi Laporan", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Segmented Tab Row M3
            SecondaryTabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                containerColor = Color.White,
                contentColor = Secondary,
                divider = {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                }
            ) {
                Tab(
                    selected = uiState.selectedTab == LaporanTab.RINGKASAN,
                    onClick = { viewModel.selectTab(LaporanTab.RINGKASAN) },
                    text = {
                        Text(
                            text = "Rekap & Audit",
                            fontSize = 12.sp,
                            fontWeight = if (uiState.selectedTab == LaporanTab.RINGKASAN) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )

                Tab(
                    selected = uiState.selectedTab == LaporanTab.RIWAYAT,
                    onClick = { viewModel.selectTab(LaporanTab.RIWAYAT) },
                    text = {
                        Text(
                            text = "Riwayat (${uiState.riwayatList.size})",
                            fontSize = 12.sp,
                            fontWeight = if (uiState.selectedTab == LaporanTab.RIWAYAT) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )

                Tab(
                    selected = uiState.selectedTab == LaporanTab.LOG_KERUSAKAN,
                    onClick = { viewModel.selectTab(LaporanTab.LOG_KERUSAKAN) },
                    text = {
                        Text(
                            text = "Log Rusak (${uiState.logKerusakan.size})",
                            fontSize = 12.sp,
                            fontWeight = if (uiState.selectedTab == LaporanTab.LOG_KERUSAKAN) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            when (uiState.selectedTab) {
                LaporanTab.RINGKASAN -> {
                    TabRingkasanContent(
                        summary = uiState.summary,
                        distribusi = uiState.distribusiLokasi,
                        kategoriStat = uiState.statistikKategori
                    )
                }
                LaporanTab.RIWAYAT -> {
                    TabRiwayatContent(
                        riwayatList = uiState.riwayatList,
                        filterStatus = uiState.filterStatus,
                        filterRentang = uiState.filterRentang,
                        onFilterStatusChange = { viewModel.setFilterStatus(it) },
                        onFilterRentangChange = { viewModel.setFilterRentang(it) }
                    )
                }
                LaporanTab.LOG_KERUSAKAN -> {
                    TabLogKerusakanContent(logKerusakan = uiState.logKerusakan)
                }
            }
        }
    }
}

@Composable
private fun TabRingkasanContent(
    summary: AuditSummary,
    distribusi: List<LokasiDistribusi>,
    kategoriStat: List<KategoriSirkulasiStat>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // 1. Bento Metric Grid 2x2
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "STATUS AUDIT INVENTARIS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceVariant,
                    letterSpacing = 0.5.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AuditMetricCard(
                        title = "Total Unit Aset",
                        value = "${summary.totalAset}",
                        subtitle = "Tercatat di sistem",
                        icon = Icons.Default.Inventory2,
                        badgeColor = Secondary,
                        modifier = Modifier.weight(1f)
                    )

                    AuditMetricCard(
                        title = "Siap Digunakan",
                        value = "${summary.siapDipinjam}",
                        subtitle = "${summary.persentaseKetersediaan}% Ready",
                        icon = Icons.Default.CheckCircle,
                        badgeColor = StatusAvailableText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AuditMetricCard(
                        title = "Sedang Dipinjam",
                        value = "${summary.sedangDipinjam}",
                        subtitle = "Di luar gudang",
                        icon = Icons.Default.Schedule,
                        badgeColor = StatusBorrowedText,
                        modifier = Modifier.weight(1f)
                    )

                    AuditMetricCard(
                        title = "Perlu Servis / Rusak",
                        value = "${summary.perluServis}",
                        subtitle = "Butuh perbaikan",
                        icon = Icons.Default.Warning,
                        badgeColor = StatusDamagedText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 2. Distribusi Lokasi Simpan Fisik
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Secondary, modifier = Modifier.size(16.dp))
                            Text(text = "Distribusi Lokasi Simpan Fisik", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                        }
                        Text(text = "${distribusi.size} Ruangan", fontSize = 11.sp, color = OnSurfaceVariant)
                    }

                    distribusi.forEach { loc ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = loc.lokasiSimpan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
                                Text(text = "${loc.totalUnit} Unit (${loc.itemJenisCount} jenis)", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                            // Visual progress bar
                            val progress = if (summary.totalAset > 0) (loc.totalUnit.toFloat() / summary.totalAset).coerceIn(0.05f, 1f) else 0.1f
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(SurfaceContainerLow)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(9999.dp))
                                        .background(Secondary)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Frekuensi Sirkulasi per Kategori
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.PieChart, contentDescription = null, tint = Secondary, modifier = Modifier.size(16.dp))
                            Text(text = "Sirkulasi Peminjaman per Kategori", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                        }
                        Text(text = "Total Sirkulasi", fontSize = 11.sp, color = OnSurfaceVariant)
                    }

                    kategoriStat.forEach { stat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceContainerLow, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = stat.namaKategori, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "${stat.totalItem} Jenis Barang Terdaftar", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${stat.totalDipinjam} Kali Dipinjam",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant)
                Icon(imageVector = icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(16.dp))
            }
            Column {
                Text(text = value, fontSize = 22.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(text = subtitle, fontSize = 10.sp, color = badgeColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TabRiwayatContent(
    riwayatList: List<PeminjamanDetail>,
    filterStatus: String,
    filterRentang: String,
    onFilterStatusChange: (String) -> Unit,
    onFilterRentangChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Filter Bar (Status & Rentang Waktu)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Status Chips
            val statusOptions = listOf("Semua", "Dipinjam", "Kembali", "Rusak")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(statusOptions) { status ->
                    val isSelected = filterStatus == status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (isSelected) Secondary else SurfaceContainerLow)
                            .border(BorderStroke(1.dp, if (isSelected) Secondary else CardBorder), RoundedCornerShape(9999.dp))
                            .clickable { onFilterStatusChange(status) }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = status,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else OnSurface
                        )
                    }
                }
            }

            // Rentang Waktu Chips
            val rentangOptions = listOf("Semua Waktu" to "Semua", "7 Hari Terakhir" to "Minggu Ini", "30 Hari Terakhir" to "Bulan Ini")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(rentangOptions) { (label, key) ->
                    val isSelected = filterRentang == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Secondary.copy(alpha = 0.15f) else Color.White)
                            .border(BorderStroke(1.dp, if (isSelected) Secondary else CardBorder), RoundedCornerShape(8.dp))
                            .clickable { onFilterRentangChange(key) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Secondary else OnSurfaceVariant
                        )
                    }
                }
            }
        }

        if (riwayatList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada riwayat sirkulasi yang cocok dengan filter yang dipilih.",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(riwayatList, key = { it.idPinjam }) { item ->
                    val isRusak = item.kondisiKembali?.contains("Rusak", ignoreCase = true) == true
                    val isKembali = item.status.equals("Kembali", ignoreCase = true)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#TRX-${item.idPinjam.toString().padStart(4, '0')} • ${DateFormatterHelper.formatIndonesianDateShort(item.tglPinjam)}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Secondary
                                )

                                Box(
                                    modifier = Modifier
                                        .background(
                                            when {
                                                isRusak -> StatusDamagedBg
                                                isKembali -> StatusAvailableBg
                                                else -> StatusBorrowedBg
                                            },
                                            RoundedCornerShape(9999.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = when {
                                            isRusak -> "Kembali (Rusak)"
                                            isKembali -> "Kembali (Baik)"
                                            else -> "Dipinjam"
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isRusak -> StatusDamagedText
                                            isKembali -> StatusAvailableText
                                            else -> StatusBorrowedText
                                        }
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = item.namaPeminjam, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                    Text(text = "${item.tipePeminjam} • ID: ${item.nomorIdentitas}", fontSize = 11.sp, color = OnSurfaceVariant)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "${item.jumlahPinjam} Unit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Secondary)
                                }
                            }

                            Text(
                                text = "Barang: ${item.namaBarang} (${item.kodeBarang}) — \"${item.keperluanKegiatan}\"",
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )

                            if (!item.catatan.isNullOrBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                                        .padding(6.dp)
                                ) {
                                    Text(text = "Catatan: ${item.catatan}", fontSize = 10.sp, color = OnSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabLogKerusakanContent(logKerusakan: List<PeminjamanDetail>) {
    if (logKerusakan.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(56.dp).background(StatusAvailableBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StatusAvailableText, modifier = Modifier.size(28.dp))
                }
                Text(text = "Kondisi Sarpras Prima", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(text = "Tidak ada laporan insiden barang rusak saat pengembalian.", fontSize = 12.sp, color = OnSurfaceVariant, textAlign = TextAlign.Center)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(logKerusakan, key = { it.idPinjam }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Insiden Pengembalian #${item.idPinjam}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusDamagedText
                            )

                            Box(
                                modifier = Modifier
                                    .background(StatusDamagedBg, RoundedCornerShape(9999.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(text = "Rusak Fisik", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusDamagedText)
                            }
                        }

                        Text(
                            text = "${item.namaBarang} (${item.kodeBarang})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )

                        Text(
                            text = "Peminjam: ${item.namaPeminjam} (${item.tipePeminjam}) • Dikembalikan: ${DateFormatterHelper.formatIndonesianDateTime(item.tglKembaliReal)}",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(StatusDamagedBg.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, StatusDamagedText.copy(alpha = 0.3f)), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Catatan Pemeriksaan: ${item.catatan ?: "Barang dikembalikan dalam kondisi rusak/cacat."}",
                                fontSize = 11.sp,
                                color = StatusDamagedText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
