package com.example.inventarissarprassmkn1depok.ui.screens.dashboard

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.R
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import com.example.inventarissarprassmkn1depok.ui.components.ProfileBottomSheet
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.Error
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.utils.BarangImageHelper
import com.example.inventarissarprassmkn1depok.ui.utils.DateFormatterHelper
import com.example.inventarissarprassmkn1depok.ui.utils.UserAvatarHelper
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
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainer
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerHigh
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToKatalog: () -> Unit,
    onNavigateToSirkulasi: (() -> Unit)? = null,
    onLogout: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedLoanForReturn by remember { mutableStateOf<PeminjamanDetail?>(null) }
    var selectedCondition by remember { mutableStateOf("Baik") }
    var returnNotes by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showProfileSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val profileSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(uiState.returnSuccessMessage) {
        uiState.returnSuccessMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearReturnMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Surface,
        topBar = {
            // Institutional Top Bar (Stitch Screen 3 Header) with Status Bar Insets
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
                                text = "Dashboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        IconButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLow)
                                .border(BorderStroke(1.dp, CardBorder), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Keluar Akun",
                                tint = Error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))
                // 1. Identity & Daily Status Banner
                val userRoleSubtitle = uiState.roleTitle

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showProfileSheet = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.size(44.dp)) {
                                Image(
                                    painter = painterResource(id = uiState.avatarResId),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(StatusAvailableDot)
                                        .border(BorderStroke(2.dp, Color.White), CircleShape)
                                        .align(Alignment.BottomEnd)
                                )
                            }

                            Column(modifier = Modifier.weight(1f, fill = false)) {
                                Text(
                                    text = "Halo, ${uiState.adminName}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = userRoleSubtitle,
                                    fontSize = 12.sp,
                                    color = OnSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(9999.dp),
                            color = SurfaceContainerLow,
                            border = BorderStroke(1.dp, CardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = uiState.todayDateFormatted.ifEmpty { "Hari Ini" },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }

            // 2. Bento Grid Overview Metrics (2x2 Grid)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Row 1: Total Aset & Siap Dipinjam
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1: Total Aset
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(124.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CardBorder),
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
                                    Text(
                                        text = "Total Aset\nTercatat",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(SurfaceContainerHigh, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Inventory2,
                                            contentDescription = null,
                                            tint = PrimaryContainer,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "${uiState.stats.totalAset}",
                                        fontSize = 22.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                    Text(
                                        text = "Unit sarpras aktif",
                                        fontSize = 11.sp,
                                        color = Secondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Card 2: Siap Dipinjam
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(124.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CardBorder),
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
                                    Text(
                                        text = "Siap\nDipinjam",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(StatusAvailableBg, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = StatusAvailableText,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "${uiState.stats.siapDipinjam}",
                                        fontSize = 22.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                    Row(
                                        modifier = Modifier
                                            .background(StatusAvailableBg, RoundedCornerShape(9999.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(StatusAvailableDot)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Ready",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusAvailableText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Row 2: Sedang Dipinjam & Perlu Servis
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 3: Sedang Dipinjam
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(124.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CardBorder),
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
                                    Text(
                                        text = "Sedang\nDipinjam",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(StatusBorrowedBg, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = StatusBorrowedText,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "${uiState.stats.sedangDipinjam}",
                                        fontSize = 22.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                    Row(
                                        modifier = Modifier
                                            .background(StatusBorrowedBg, RoundedCornerShape(9999.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(StatusBorrowedDot)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Aktif Pinjam",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusBorrowedText
                                        )
                                    }
                                }
                            }
                        }

                        // Card 4: Perlu Servis / Rusak
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(124.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CardBorder),
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
                                    Text(
                                        text = "Perlu Servis\n/ Rusak",
                                        fontSize = 12.sp,
                                        color = OnSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(StatusDamagedBg, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.BuildCircle,
                                            contentDescription = null,
                                            tint = StatusDamagedText,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "${uiState.stats.perluServis}",
                                        fontSize = 22.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                    Row(
                                        modifier = Modifier
                                            .background(StatusDamagedBg, RoundedCornerShape(9999.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(StatusDamagedDot)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Tindakan",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusDamagedText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Circulation Feed Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Peminjaman Aktif & Jatuh Tempo",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Box(
                            modifier = Modifier
                                .background(PrimaryContainer, RoundedCornerShape(9999.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${uiState.activeLoans.size} Aktif",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "Katalog",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Secondary,
                        modifier = Modifier
                            .clickable { onNavigateToKatalog() }
                            .padding(4.dp)
                    )
                }
            }

            // 5. Circulation Feed Cards Stream
            if (uiState.activeLoans.isEmpty()) {
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
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada sirkulasi peminjaman aktif saat ini.",
                                fontSize = 13.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(uiState.activeLoans, key = { it.idPinjam }) { loan ->
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
                            // Top bar of card: Code, Date & Status Chip
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
                                        text = loan.kodeBarang,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Secondary
                                    )
                                    Text(text = "•", fontSize = 11.sp, color = CardBorder)
                                    Text(
                                        text = DateFormatterHelper.formatIndonesianDateTime(loan.tglPinjam),
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariant
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .background(StatusBorrowedBg, RoundedCornerShape(9999.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(StatusBorrowedDot)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Dipinjam (${loan.jumlahPinjam} unit)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StatusBorrowedText
                                    )
                                }
                            }

                            // Item Image Thumbnail + Name & Category
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceContainerLow)
                                        .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = BarangImageHelper.getDrawableRes(loan.kodeBarang, loan.namaBarang, loan.namaKategori)),
                                        contentDescription = loan.namaBarang,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = loan.namaBarang,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = loan.namaKategori,
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }

                            // Borrower Details Bento Slice
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SurfaceContainerLow, RoundedCornerShape(12.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .border(BorderStroke(1.dp, CardBorder), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = UserAvatarHelper.getBorrowerAvatarRes(loan.tipePeminjam, loan.nomorIdentitas, loan.namaPeminjam)),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f, fill = false)) {
                                        Text(
                                            text = loan.namaPeminjam,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = OnSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${loan.tipePeminjam} • ${loan.nomorIdentitas}",
                                            fontSize = 10.sp,
                                            color = OnSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, CardBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = null,
                                            tint = StatusBorrowedDot,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = DateFormatterHelper.formatIndonesianDateShort(loan.batasKembali),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusBorrowedText,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            // Activity / Purpose
                            Text(
                                text = "Keperluan: ${loan.keperluanKegiatan}",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant,
                                lineHeight = 16.sp
                            )

                            // Action Buttons / Status Notice
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (uiState.isStaff) {
                                    Button(
                                        onClick = {
                                            selectedLoanForReturn = loan
                                            selectedCondition = "Baik"
                                            returnNotes = ""
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Secondary,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AssignmentTurnedIn,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Verifikasi Pengembalian",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(SurfaceContainerLow, RoundedCornerShape(10.dp))
                                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(10.dp))
                                            .padding(vertical = 10.dp, horizontal = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Barang aktif digunakan • Kembalikan ke loket Sarpras untuk verifikasi petugas",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = OnSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Modal Bottom Sheet: Verifikasi Pengembalian Aset
    if (selectedLoanForReturn != null) {
        val loan = selectedLoanForReturn!!
        ModalBottomSheet(
            onDismissRequest = { selectedLoanForReturn = null },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "VERIFIKASI PENGEMBALIAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = loan.namaBarang,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = "${loan.kodeBarang} • Peminjam: ${loan.namaPeminjam}",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )
                    }

                    IconButton(onClick = { selectedLoanForReturn = null }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                // Kondisi Fisik Alat Selector
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Kondisi Fisik Saat Diterima:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Option 1: Baik
                        val isBaik = selectedCondition == "Baik"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isBaik) StatusAvailableBg else SurfaceContainerLow)
                                .border(
                                    BorderStroke(
                                        if (isBaik) 1.5.dp else 1.dp,
                                        if (isBaik) StatusAvailableText else CardBorder
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCondition = "Baik" }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Baik / Lengkap",
                                fontSize = 11.sp,
                                fontWeight = if (isBaik) FontWeight.Bold else FontWeight.Medium,
                                color = if (isBaik) StatusAvailableText else OnSurface
                            )
                        }

                        // Option 2: Perlu Servis
                        val isServis = selectedCondition == "Perlu Servis"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isServis) StatusBorrowedBg else SurfaceContainerLow)
                                .border(
                                    BorderStroke(
                                        if (isServis) 1.5.dp else 1.dp,
                                        if (isServis) StatusBorrowedText else CardBorder
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCondition = "Perlu Servis" }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Perlu Servis",
                                fontSize = 11.sp,
                                fontWeight = if (isServis) FontWeight.Bold else FontWeight.Medium,
                                color = if (isServis) StatusBorrowedText else OnSurface
                            )
                        }

                        // Option 3: Rusak
                        val isRusak = selectedCondition == "Rusak"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isRusak) StatusDamagedBg else SurfaceContainerLow)
                                .border(
                                    BorderStroke(
                                        if (isRusak) 1.5.dp else 1.dp,
                                        if (isRusak) StatusDamagedText else CardBorder
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCondition = "Rusak" }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Rusak / Cacat",
                                fontSize = 11.sp,
                                fontWeight = if (isRusak) FontWeight.Bold else FontWeight.Medium,
                                color = if (isRusak) StatusDamagedText else OnSurface
                            )
                        }
                    }
                }

                // Catatan Penyerahan
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Catatan Kondisi / Kelengkapan (Opsional):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant
                    )

                    OutlinedTextField(
                        value = returnNotes,
                        onValueChange = { returnNotes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                text = "Tuliskan kondisi kelengkapan kabel, baterai, aksesoris...",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant
                            )
                        },
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

                // Action Confirm Button
                Button(
                    onClick = {
                        val loanId = loan.idPinjam
                        viewModel.processReturn(
                            idPinjam = loanId,
                            kondisiKembali = selectedCondition,
                            catatan = returnNotes.trim().ifEmpty { null }
                        )
                        selectedLoanForReturn = null
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
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "KONFIRMASI TERIMA BARANG",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Keluar dari Sistem?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            },
            text = {
                Text(
                    text = "Sesi aktif petugas ${uiState.adminName} akan diakhiri dan dialihkan kembali ke portal login.",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout?.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ya, Keluar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Batal", fontSize = 13.sp, color = OnSurfaceVariant)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal Bottom Sheet: Detail Profil Pengguna
    if (showProfileSheet) {
        ProfileBottomSheet(
            adminName = uiState.adminName,
            adminNip = uiState.adminNip,
            adminContact = uiState.adminContact,
            avatarResId = uiState.avatarResId,
            roleTitle = uiState.roleTitle,
            isStaff = uiState.isStaff,
            sheetState = profileSheetState,
            onDismiss = { showProfileSheet = false },
            onLogout = {
                showProfileSheet = false
                showLogoutDialog = true
            }
        )
    }
}
