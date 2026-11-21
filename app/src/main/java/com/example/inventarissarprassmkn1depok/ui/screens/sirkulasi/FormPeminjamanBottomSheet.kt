package com.example.inventarissarprassmkn1depok.ui.screens.sirkulasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.theme.Secondary
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableDot
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableText
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedBg
import com.example.inventarissarprassmkn1depok.ui.theme.StatusDamagedText
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow
import com.example.inventarissarprassmkn1depok.ui.utils.BarangImageHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormPeminjamanBottomSheet(
    barangList: List<BarangWithKategori>,
    preselectedBarang: BarangWithKategori?,
    sheetState: SheetState,
    isStaff: Boolean = true,
    defaultNomorIdentitas: String = "",
    defaultNamaPeminjam: String = "",
    defaultTipePeminjam: String = "Siswa",
    onDismiss: () -> Unit,
    onSubmit: (
        nomorIdentitas: String,
        namaPeminjam: String,
        tipePeminjam: String,
        idBarang: Int,
        jumlahPinjam: Int,
        batasKembali: String,
        keperluan: String
    ) -> Unit
) {
    val tipeOptions = listOf("Guru", "Siswa", "OSIS")
    var selectedTipe by remember { mutableStateOf(if (!isStaff) defaultTipePeminjam else "Siswa") }
    var nomorIdentitas by remember { mutableStateOf(if (!isStaff) defaultNomorIdentitas else "") }
    var namaPeminjam by remember { mutableStateOf(if (!isStaff) defaultNamaPeminjam else "") }

    var selectedBarang by remember {
        mutableStateOf(preselectedBarang ?: barangList.firstOrNull { it.stokTersedia > 0 })
    }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    var qtyPinjam by remember { mutableIntStateOf(1) }
    var keperluan by remember { mutableStateOf("") }

    // Preset tanggal pengembalian
    val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayCalendar = Calendar.getInstance()
    val todayDateStr = sdfDate.format(todayCalendar.time)

    val tomorrowCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    val tomorrowDateStr = sdfDate.format(tomorrowCalendar.time)

    val threeDaysCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 3) }
    val threeDaysDateStr = sdfDate.format(threeDaysCalendar.time)

    var batasKembali by remember { mutableStateOf("$todayDateStr 15:00") }

    val currentStok = selectedBarang?.stokTersedia ?: 0
    val isStokExceeded = qtyPinjam > currentStok
    val isSelectedGood = selectedBarang?.kondisi?.equals("Baik", ignoreCase = true) == true
    val isFormValid = nomorIdentitas.isNotBlank() &&
            namaPeminjam.isNotBlank() &&
            selectedBarang != null &&
            isSelectedGood &&
            currentStok > 0 &&
            !isStokExceeded &&
            keperluan.isNotBlank()

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
                            imageVector = Icons.Default.PostAdd,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Catat Peminjaman Sarpras",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = "Form pencatatan peminjaman aset resmi SMKN 1 Depok",
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

            // 1. Tipe Peminjam (Segmented Chips atau Informasi Terkunci)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isStaff) "Tipe Peminjam:" else "Tipe Akun (Terkunci Sesuai Sesi):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                if (isStaff) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tipeOptions.forEach { tipe ->
                            val isSelected = selectedTipe == tipe
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Secondary else SurfaceContainerLow)
                                    .border(
                                        BorderStroke(1.dp, if (isSelected) Secondary else CardBorder),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedTipe = tipe }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tipe,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else OnSurface
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceContainerLow, RoundedCornerShape(10.dp))
                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Kategori Peminjam: $defaultTipePeminjam",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondary
                        )
                    }
                }
            }

            // 2. Identitas & Nama Peminjam
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = nomorIdentitas,
                    onValueChange = { if (isStaff) nomorIdentitas = it },
                    readOnly = !isStaff,
                    label = { Text(if (selectedTipe == "Guru") "NIP / Kode Guru" else "NIS / ID Siswa") },
                    placeholder = { Text(if (selectedTipe == "Guru") "198507..." else "212210...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Secondary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )

                OutlinedTextField(
                    value = namaPeminjam,
                    onValueChange = { if (isStaff) namaPeminjam = it },
                    readOnly = !isStaff,
                    label = { Text("Nama Lengkap") },
                    placeholder = { Text("Nama Peminjam / Kelas") },
                    modifier = Modifier.weight(1.3f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Secondary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )
            }

            // 3. Pilihan Barang Sarpras (Dropdown Selector)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Pilih Barang Sarpras:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDropdownExpanded = true }
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
                            if (selectedBarang != null) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = BarangImageHelper.getDrawableRes(selectedBarang!!.kodeBarang, selectedBarang!!.namaBarang, selectedBarang!!.namaKategori)),
                                            contentDescription = selectedBarang!!.namaBarang,
                                            modifier = Modifier.fillMaxSize().padding(3.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = selectedBarang!!.namaBarang,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OnSurface
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = selectedBarang!!.kodeBarang,
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Monospace,
                                                color = OnSurfaceVariant
                                            )
                                            Text(text = "•", fontSize = 10.sp, color = CardBorder)
                                            val isSelectedGood = selectedBarang!!.kondisi.equals("Baik", ignoreCase = true)
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        if (currentStok > 0 && isSelectedGood) StatusAvailableBg else StatusDamagedBg,
                                                        RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = when {
                                                        !isSelectedGood -> "Perlu Servis"
                                                        currentStok > 0 -> "Tersedia: $currentStok unit"
                                                        else -> "Habis Dipinjam"
                                                    },
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (currentStok > 0 && isSelectedGood) StatusAvailableText else StatusDamagedText
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "-- Pilih Sarpras yang Tersedia --",
                                    fontSize = 13.sp,
                                    color = OnSurfaceVariant
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Pilih",
                                tint = OnSurfaceVariant
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White)
                    ) {
                        barangList.forEach { barang ->
                            val isItemGood = barang.kondisi.equals("Baik", ignoreCase = true)
                            val isSelectable = isItemGood && barang.stokTersedia > 0
                            DropdownMenuItem(
                                enabled = isSelectable,
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(SurfaceContainerLow),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(id = BarangImageHelper.getDrawableRes(barang.kodeBarang, barang.namaBarang, barang.namaKategori)),
                                                contentDescription = barang.namaBarang,
                                                modifier = Modifier.fillMaxSize().padding(2.dp),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = barang.namaBarang,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                color = if (isSelectable) OnSurface else OnSurfaceVariant.copy(alpha = 0.5f)
                                            )
                                            Text(
                                                text = "${barang.kodeBarang} • ${barang.namaKategori}",
                                                fontSize = 11.sp,
                                                color = OnSurfaceVariant,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isSelectable) StatusAvailableBg else StatusDamagedBg,
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = when {
                                                    !isItemGood -> "Perlu Servis"
                                                    barang.stokTersedia > 0 -> "Sisa: ${barang.stokTersedia}"
                                                    else -> "Habis"
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelectable) StatusAvailableText else StatusDamagedText
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    if (isSelectable) {
                                        selectedBarang = barang
                                        if (qtyPinjam > barang.stokTersedia && barang.stokTersedia > 0) {
                                            qtyPinjam = 1
                                        }
                                        isDropdownExpanded = false
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // 4. Counter Jumlah Unit Pinjam
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Jumlah Unit Dipinjam:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Tombol Minus
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerLow)
                                .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(10.dp))
                                .clickable(enabled = qtyPinjam > 1) {
                                    if (qtyPinjam > 1) qtyPinjam--
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Kurang",
                                tint = if (qtyPinjam > 1) OnSurface else OnSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Jumlah
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(40.dp)
                                .background(Color.White)
                                .border(
                                    BorderStroke(
                                        1.5.dp,
                                        if (isStokExceeded) StatusDamagedText else Secondary
                                    ),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$qtyPinjam",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isStokExceeded) StatusDamagedText else OnSurface
                            )
                        }

                        // Tombol Plus
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerLow)
                                .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(10.dp))
                                .clickable(enabled = qtyPinjam < currentStok) {
                                    qtyPinjam++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah",
                                tint = if (qtyPinjam < currentStok) OnSurface else OnSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = "Maks: $currentStok Unit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isStokExceeded) StatusDamagedText else OnSurfaceVariant
                    )
                }

                if (isStokExceeded) {
                    Text(
                        text = "⚠️ Jumlah pinjam melebihi stok yang tersedia saat ini ($currentStok unit).",
                        fontSize = 11.sp,
                        color = StatusDamagedText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 5. Keperluan Kegiatan
            OutlinedTextField(
                value = keperluan,
                onValueChange = { keperluan = it },
                label = { Text("Keperluan / Lokasi Penggunaan") },
                placeholder = { Text("Misal: Praktikum Jaringan Lab 2, Rapat Dinas, Porseni") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface,
                    cursorColor = Secondary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = CardBorder
                )
            )

            // 6. Batas Waktu Pengembalian (Preset Cepat)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Batas Waktu Pengembalian:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(
                        "Hari Ini (15:00)" to "$todayDateStr 15:00",
                        "Besok (08:00)" to "$tomorrowDateStr 08:00",
                        "3 Hari (KBM)" to "$threeDaysDateStr 15:00"
                    )

                    presets.forEach { (label, value) ->
                        val isSelected = batasKembali == value
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Secondary.copy(alpha = 0.15f) else SurfaceContainerLow)
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Secondary else CardBorder),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { batasKembali = value }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Secondary else OnSurface
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = batasKembali,
                    onValueChange = { batasKembali = it },
                    label = { Text("Format Batas Waktu (YYYY-MM-DD HH:mm)") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.EditCalendar, contentDescription = null, tint = Secondary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Secondary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = Secondary,
                        unfocusedBorderColor = CardBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 7. Tombol Aksi Simpan (Slate Blue #2563EB, 48dp)
            Button(
                onClick = {
                    if (isFormValid && selectedBarang != null) {
                        onSubmit(
                            nomorIdentitas.trim(),
                            namaPeminjam.trim(),
                            selectedTipe,
                            selectedBarang!!.idBarang,
                            qtyPinjam,
                            batasKembali.trim(),
                            keperluan.trim()
                        )
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
                Icon(imageVector = Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CATAT PEMINJAMAN SARPRAS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
