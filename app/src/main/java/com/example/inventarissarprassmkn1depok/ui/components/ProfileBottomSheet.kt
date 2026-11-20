package com.example.inventarissarprassmkn1depok.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarissarprassmkn1depok.ui.theme.CardBorder
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurface
import com.example.inventarissarprassmkn1depok.ui.theme.OnSurfaceVariant
import com.example.inventarissarprassmkn1depok.ui.theme.PrimaryContainer
import com.example.inventarissarprassmkn1depok.ui.theme.Secondary
import com.example.inventarissarprassmkn1depok.ui.theme.StatusAvailableDot
import com.example.inventarissarprassmkn1depok.ui.theme.SurfaceContainerLow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    adminName: String,
    adminNip: String,
    adminContact: String,
    avatarResId: Int,
    roleTitle: String,
    isStaff: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    val idLabel = if (adminNip == "22231045" || roleTitle.contains("Siswa", ignoreCase = true)) {
        "NIS Siswa"
    } else {
        "NIP Petugas / Guru"
    }

    val accessRoleText = if (isStaff) {
        "Pengelola Penuh Sarpras (Super Admin)"
    } else if (roleTitle.contains("Guru", ignoreCase = true)) {
        "Peminjam Tenaga Pendidik Terdaftar"
    } else {
        "Peminjam Siswa Aktif Terdaftar"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar: Title & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROFIL PENGGUNA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary,
                    letterSpacing = 0.5.sp
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = OnSurfaceVariant
                    )
                }
            }

            // Big Avatar with Green Online Status Dot
            Box(
                modifier = Modifier.size(96.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(3.dp, Color(0xFFDBE1FF)), CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = avatarResId),
                        contentDescription = adminName,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(StatusAvailableDot)
                        .border(BorderStroke(2.5.dp, Color.White), CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }

            // Name & Role Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = adminName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = if (isStaff) PrimaryContainer else Color(0xFFE8F0FE)
                ) {
                    Text(
                        text = roleTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isStaff) Color.White else Secondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            // Account Metadata Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProfileMetaRow(
                        icon = Icons.Default.Badge,
                        label = idLabel,
                        value = adminNip,
                        isMonospace = true
                    )

                    ProfileMetaRow(
                        icon = Icons.Default.School,
                        label = "Unit Kerja",
                        value = "SMKN 1 Depok • Jawa Barat"
                    )

                    ProfileMetaRow(
                        icon = Icons.Default.Phone,
                        label = "Kontak WhatsApp",
                        value = adminContact.ifEmpty { "0812-3456-7890" }
                    )

                    ProfileMetaRow(
                        icon = Icons.Default.Security,
                        label = "Hak Akses",
                        value = accessRoleText
                    )

                    ProfileMetaRow(
                        icon = Icons.Default.CheckCircle,
                        label = "Status Dapodik",
                        value = "Terverifikasi Aktif"
                    )
                }
            }

            // Action Buttons
            Button(
                onClick = {
                    onDismiss()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBA1A1A),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "KELUAR / GANTI AKUN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ProfileMetaRow(
    icon: ImageVector,
    label: String,
    value: String,
    isMonospace: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )
        }

        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
            color = OnSurface
        )
    }
}
