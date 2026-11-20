package com.example.inventarissarprassmkn1depok.ui.screens.katalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.theme.Secondary
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableText
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedText
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormBarangBottomSheet(
    barangToEdit: BarangWithKategori?,
    kategoriList: List<KategoriEntity>,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSubmit: (BarangEntity) -> Unit
) {
    val isEditMode = barangToEdit != null

    var kodeBarang by remember { mutableStateOf(barangToEdit?.kodeBarang ?: "") }
    var namaBarang by remember { mutableStateOf(barangToEdit?.namaBarang ?: "") }
    var selectedKategoriId by remember {
        mutableIntStateOf(barangToEdit?.idKategoriFk ?: kategoriList.firstOrNull()?.idKategori ?: 1)
    }
    var isKategoriExpanded by remember { mutableStateOf(false) }

    var merkSpesifikasi by remember { mutableStateOf(barangToEdit?.merkSpesifikasi ?: "") }
    var lokasiSimpan by remember { mutableStateOf(barangToEdit?.lokasiSimpan ?: "Gudang Sarpras - Rak A1") }
    var stokTotalStr by remember { mutableStateOf((barangToEdit?.stokTotal ?: 1).toString()) }
    var kondisi by remember { mutableStateOf(barangToEdit?.kondisi ?: "Baik") }
    var sumberDana by remember { mutableStateOf(barangToEdit?.sumberDana ?: "BOS Reguler") }

    val stokTotal = stokTotalStr.toIntOrNull() ?: 0
    val isFormValid = kodeBarang.isNotBlank() &&
            namaBarang.isNotBlank() &&
            merkSpesifikasi.isNotBlank() &&
            lokasiSimpan.isNotBlank() &&
            stokTotal > 0

    val sumberDanaOptions = listOf("BOS Reguler", "BOS Kinerja", "Komite Sekolah", "DAK Fisik", "Hibah Industri")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Secondary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.EditNote else Icons.Default.AddBusiness,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isEditMode) "Edit Data Sarpras" else "Tambah Aset Sarpras Baru",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = if (isEditMode) "Perbarui inventaris ${barangToEdit?.kodeBarang}" else "Registrasi aset fisik baru SMKN 1 Depok",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = OnSurfaceVariant
                    )
                }
            }

            // 1. Kode Barang & Nama Barang
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = kodeBarang,
                    onValueChange = { kodeBarang = it.uppercase() },
                    label = { Text("Kode Barang") },
                    placeholder = { Text("SPR-MM-001") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )

                OutlinedTextField(
                    value = namaBarang,
                    onValueChange = { namaBarang = it },
                    label = { Text("Nama Barang / Alat") },
                    placeholder = { Text("Proyektor Epson EB-X500") },
                    modifier = Modifier.weight(1.4f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )
            }

            // 2. Kategori Dropdown
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Kategori Sarpras:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                val selectedKategoriName = kategoriList.firstOrNull { it.idKategori == selectedKategoriId }?.namaKategori ?: "Pilih Kategori"

                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isKategoriExpanded = true }
                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedKategoriName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = OnSurfaceVariant)
                        }
                    }

                    DropdownMenu(
                        expanded = isKategoriExpanded,
                        onDismissRequest = { isKategoriExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White)
                    ) {
                        kategoriList.forEach { kat ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = kat.namaKategori, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                        Text(text = kat.keterangan ?: "", fontSize = 11.sp, color = OnSurfaceVariant)
                                    }
                                },
                                onClick = {
                                    selectedKategoriId = kat.idKategori
                                    isKategoriExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // 3. Merk / Spesifikasi Tambahan
            OutlinedTextField(
                value = merkSpesifikasi,
                onValueChange = { merkSpesifikasi = it },
                label = { Text("Merk & Spesifikasi Teknis") },
                placeholder = { Text("Misal: 3600 ANSI Lumens, Port HDMI/VGA, Resolusi XGA") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = CardBorder
                )
            )

            // 4. Lokasi Simpan Fisik & Stok Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = lokasiSimpan,
                    onValueChange = { lokasiSimpan = it },
                    label = { Text("Lokasi Simpan Fisik") },
                    placeholder = { Text("Gudang Sarpras Rak A1") },
                    modifier = Modifier.weight(1.4f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )

                OutlinedTextField(
                    value = stokTotalStr,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) stokTotalStr = input
                    },
                    label = { Text("Stok Total") },
                    placeholder = { Text("1") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(0.8f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )
            }

            // 5. Kondisi Fisik Awal
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Kondisi Fisik Barang:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isBaik = kondisi == "Baik"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isBaik) StatusAvailableBg else SurfaceContainerLow)
                            .border(
                                BorderStroke(if (isBaik) 1.5.dp else 1.dp, if (isBaik) StatusAvailableText else CardBorder),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { kondisi = "Baik" }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(StatusAvailableDot, CircleShape))
                            Text(
                                text = "Baik / Prima",
                                fontSize = 12.sp,
                                fontWeight = if (isBaik) FontWeight.Bold else FontWeight.Medium,
                                color = if (isBaik) StatusAvailableText else OnSurface
                            )
                        }
                    }

                    val isRusak = kondisi != "Baik"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isRusak) StatusDamagedBg else SurfaceContainerLow)
                            .border(
                                BorderStroke(if (isRusak) 1.5.dp else 1.dp, if (isRusak) StatusDamagedText else CardBorder),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { kondisi = "Perlu Servis" }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(StatusDamagedDot, CircleShape))
                            Text(
                                text = "Perlu Servis / Rusak",
                                fontSize = 12.sp,
                                fontWeight = if (isRusak) FontWeight.Bold else FontWeight.Medium,
                                color = if (isRusak) StatusDamagedText else OnSurface
                            )
                        }
                    }
                }
            }

            // 6. Sumber Dana Pengadaan
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Sumber Dana Pengadaan:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sumberDanaOptions.forEach { dana ->
                        val isSelected = sumberDana == dana
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Secondary.copy(alpha = 0.15f) else SurfaceContainerLow)
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Secondary else CardBorder),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { sumberDana = dana }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = dana,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Secondary else OnSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 7. Tombol Aksi Simpan
            Button(
                onClick = {
                    if (isFormValid) {
                        val entity = BarangEntity(
                            idBarang = barangToEdit?.idBarang ?: 0,
                            idKategoriFk = selectedKategoriId,
                            kodeBarang = kodeBarang.trim(),
                            namaBarang = namaBarang.trim(),
                            merkSpesifikasi = merkSpesifikasi.trim(),
                            stokTotal = stokTotal,
                            stokTersedia = barangToEdit?.stokTersedia ?: stokTotal,
                            kondisi = kondisi,
                            lokasiSimpan = lokasiSimpan.trim(),
                            sumberDana = sumberDana
                        )
                        onSubmit(entity)
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary,
                    contentColor = Color.White,
                    disabledContainerColor = CardBorder,
                    disabledContentColor = OnSurfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditMode) "PERBARUI DATA ASET" else "SIMPAN DATA ASET BARU",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
