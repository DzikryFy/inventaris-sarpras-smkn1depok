package com.example.inventarissarprassmkn1depok.ui.screens.katalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
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
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedText
import com.example.inventarissarprassmkn1depok.ui.theme.Surface
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainer
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerHigh
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow
import com.example.inventarissarprassmkn1depok.ui.utils.BarangImageHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KatalogScreen(
    viewModel: KatalogViewModel,
    isStaff: Boolean = true,
    currentBorrowerName: String = "",
    currentBorrowerId: String = "",
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedItemForLoan by remember { mutableStateOf<BarangWithKategori?>(null) }
    var borrowerName by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("Praktikum KBM") }
    var loanQty by remember { mutableStateOf("1") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val formSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    androidx.compose.runtime.LaunchedEffect(uiState.message) {
        uiState.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    androidx.compose.runtime.LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar("Error: $error")
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Surface,
        floatingActionButton = {
            if (isStaff) {
                // Extended Floating Action Button (Stitch Screen 4)
                ExtendedFloatingActionButton(
                    onClick = {
                        if (uiState.barangList.isNotEmpty()) {
                            selectedItemForLoan = uiState.barangList.firstOrNull { it.stokTersedia > 0 }
                            borrowerName = ""
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Pinjamkan",
                            tint = Color.White
                        )
                    },
                    text = {
                        Text(
                            text = "Pinjamkan Barang",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    containerColor = PrimaryContainer,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))
                // 1. Top Operational Banner & Stats Overview (Stitch Screen 4)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusAvailableDot)
                        )
                        Text(
                            text = "KATALOG SARPRAS SMKN 1 DEPOK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(SurfaceContainerHigh, RoundedCornerShape(9999.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${uiState.totalCount} Aset",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        }

                        if (isStaff) {
                            Button(
                                onClick = { viewModel.openCreateBarang() },
                                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ Aset Baru", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. Search Action Hub
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            placeholder = {
                                Text(
                                    text = "Cari proyektor, HDMI, bola, kamera...",
                                    fontSize = 13.sp,
                                    color = Outline
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
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
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface,
                                cursorColor = Secondary,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            // 3. Horizontal Filter Chips (M3 style)
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chip "Semua"
                    val isAllSelected = uiState.selectedKategoriId == null
                    item {
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(9999.dp))
                                .background(if (isAllSelected) PrimaryContainer else Color.White)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isAllSelected) PrimaryContainer else CardBorder
                                    ),
                                    RoundedCornerShape(9999.dp)
                                )
                                .clickable { viewModel.selectKategori(null) }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AllInclusive,
                                    contentDescription = null,
                                    tint = if (isAllSelected) Color.White else OnSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Semua (${uiState.totalCount})",
                                    fontSize = 12.sp,
                                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isAllSelected) Color.White else OnSurfaceVariant
                                )
                            }
                        }
                    }

                    // Chips per Kategori
                    items(uiState.categories, key = { it.idKategori }) { kategori ->
                        val isSelected = uiState.selectedKategoriId == kategori.idKategori
                        val icon = when {
                            kategori.namaKategori.contains("Multimedia", true) -> Icons.Default.Videocam
                            kategori.namaKategori.contains("Kabel", true) -> Icons.Default.Cable
                            kategori.namaKategori.contains("Olahraga", true) -> Icons.Default.SportsBasketball
                            kategori.namaKategori.contains("Kamera", true) -> Icons.Default.PhotoCamera
                            else -> Icons.Default.Build
                        }

                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(9999.dp))
                                .background(if (isSelected) PrimaryContainer else Color.White)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isSelected) PrimaryContainer else CardBorder
                                    ),
                                    RoundedCornerShape(9999.dp)
                                )
                                .clickable { viewModel.selectKategori(kategori.idKategori) }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else OnSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = kategori.namaKategori,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 4. Inventory Cards List
            if (uiState.barangList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada barang yang cocok dengan filter atau kata kunci.",
                                fontSize = 13.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(uiState.barangList, key = { it.idBarang }) { item ->
                    val isAvailable = item.stokTersedia > 0
                    val categoryPrefix = when {
                        item.namaKategori.contains("Multimedia", true) -> "MM"
                        item.namaKategori.contains("Olahraga", true) -> "OR"
                        item.namaKategori.contains("Kabel", true) -> "KB"
                        item.namaKategori.contains("Kamera", true) -> "DK"
                        else -> "TK"
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Equipment Product Image Card (72x72dp)
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SurfaceContainerLow)
                                        .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = BarangImageHelper.getDrawableRes(item.kodeBarang, item.namaBarang, item.namaKategori)),
                                        contentDescription = item.namaBarang,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(6.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Monospace Code Pill
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFDBE1FF), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = item.kodeBarang,
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                color = Secondary
                                            )
                                        }

                                        // Stock Availability Pill
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    if (isAvailable) StatusAvailableBg else StatusDamagedBg,
                                                    RoundedCornerShape(9999.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isAvailable) StatusAvailableDot else StatusDamagedText)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isAvailable) "${item.stokTersedia} dari ${item.stokTotal} Unit" else "Habis Dipinjam",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isAvailable) StatusAvailableText else StatusDamagedText
                                            )
                                        }
                                    }

                                    Text(
                                        text = item.namaBarang,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Outline,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = item.lokasiSimpan,
                                            fontSize = 11.sp,
                                            color = OnSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // Specifications & Conditions Box
                            Text(
                                text = "${item.merkSpesifikasi} • Sumber Dana: ${item.sumberDana}",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SurfaceContainerLow, RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                lineHeight = 16.sp
                            )

                            // Action Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (item.kondisi == "Baik") StatusAvailableDot else StatusBorrowedDot,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Kondisi: ${item.kondisi}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OnSurface
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (isStaff) {
                                        IconButton(
                                            onClick = { viewModel.openEditBarang(item) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Data Barang",
                                                tint = Secondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = { viewModel.requestDeleteBarang(item) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Hapus Barang",
                                                tint = Color(0xFFBA1A1A),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            selectedItemForLoan = item
                                            borrowerName = if (!isStaff) currentBorrowerName else ""
                                        },
                                        enabled = isAvailable,
                                        modifier = Modifier.height(38.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Secondary,
                                            contentColor = Color.White,
                                            disabledContainerColor = SurfaceContainerHigh
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddTask,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isAvailable) "Pinjam" else "Kosong",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    // Modal Bottom Sheet: Ajukan Pinjam Quick Sheet
    if (selectedItemForLoan != null) {
        val barang = selectedItemForLoan!!
        ModalBottomSheet(
            onDismissRequest = { selectedItemForLoan = null },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceContainerLow)
                                .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = BarangImageHelper.getDrawableRes(barang.kodeBarang, barang.namaBarang, barang.namaKategori)),
                                contentDescription = barang.namaBarang,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "FORMULIR SIRKULASI PINJAM",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = barang.namaBarang,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${barang.kodeBarang} • Sisa: ${barang.stokTersedia} unit",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = { selectedItemForLoan = null }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                // Input Nama Peminjam
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = if (!isStaff) "Identitas Peminjam (Terkunci Sesuai Akun):" else "Nama Peminjam / NIS / NIP:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = if (!isStaff) currentBorrowerName else borrowerName,
                        onValueChange = { if (isStaff) borrowerName = it },
                        readOnly = !isStaff,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        placeholder = { Text("Contoh: Rizky Pratama (XII PPLG 2)", fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            cursorColor = Secondary,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow,
                            focusedBorderColor = Secondary,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                }

                // Input Keperluan Kegiatan
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Keperluan Kegiatan:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            cursorColor = Secondary,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow,
                            focusedBorderColor = Secondary,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                }

                // Input Jumlah Pinjam
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Jumlah Unit Dipinjam:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = loanQty,
                        onValueChange = { loanQty = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            cursorColor = Secondary,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow,
                            focusedBorderColor = Secondary,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                }

                Button(
                    onClick = {
                        val name = (if (!isStaff) currentBorrowerName else borrowerName).trim()
                        if (name.isNotEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Peminjaman '${barang.namaBarang}' untuk '$name' berhasil diajukan!")
                            }
                            selectedItemForLoan = null
                            borrowerName = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Secondary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "KONFIRMASI PEMINJAMAN",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Sheet Form Tambah / Edit Master Barang
    if (uiState.isFormOpen) {
        FormBarangBottomSheet(
            barangToEdit = uiState.barangToEdit,
            kategoriList = uiState.categories,
            sheetState = formSheetState,
            onDismiss = { viewModel.closeForm() },
            onSubmit = { barangEntity ->
                viewModel.saveBarang(barangEntity)
            }
        )
    }

    // Dialog Konfirmasi Hapus Data Barang
    uiState.barangToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { viewModel.cancelDeleteBarang() },
            title = {
                Text(
                    text = "Hapus Aset Inventaris?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus '${target.namaBarang}' (${target.kodeBarang}) dari basis data? Tindakan ini tidak dapat dibatalkan.",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDeleteBarang() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ya, Hapus", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.cancelDeleteBarang() }
                ) {
                    Text("Batal", fontSize = 13.sp, color = OnSurfaceVariant)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
