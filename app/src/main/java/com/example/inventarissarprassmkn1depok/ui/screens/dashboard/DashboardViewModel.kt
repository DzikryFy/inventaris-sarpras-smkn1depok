package com.example.inventarissarprassmkn1depok.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventarissarprassmkn1depok.R
import com.example.inventarissarprassmkn1depok.data.local.model.DashboardStats
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import com.example.inventarissarprassmkn1depok.data.repository.SarprasRepository
import com.example.inventarissarprassmkn1depok.ui.utils.UserAvatarHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DashboardUiState(
    val stats: DashboardStats = DashboardStats(),
    val activeLoans: List<PeminjamanDetail> = emptyList(),
    val todayDateFormatted: String = "",
    val todayIso: String = "",
    val adminName: String = "Petugas Sarpras SMKN 1 Depok",
    val adminNip: String = "198507152010011002",
    val adminContact: String = "081234567890",
    val avatarResId: Int = R.drawable.ic_avatar_admin,
    val roleTitle: String = "Koordinator Sarpras SMKN 1 Depok",
    val isStaff: Boolean = true,
    val isLoading: Boolean = false,
    val returnSuccessMessage: String? = null
)

class DashboardViewModel(
    private val sarprasRepository: SarprasRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _messageFlow = MutableStateFlow<String?>(null)

    private val todayIso: String
    private val todayFormatted: String

    init {
        val localeId = Locale.forLanguageTag("id-ID")
        val now = Date()
        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayFormat = SimpleDateFormat("EEEE, d MMM", localeId)

        todayIso = isoFormat.format(now)
        todayFormatted = displayFormat.format(now)
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        sarprasRepository.getDashboardStats(),
        sarprasRepository.getPeminjamanAktif(),
        _messageFlow
    ) { stats, loans, message ->
        val admin = authRepository.getLoggedInAdmin()
        val isStaff = authRepository.isStaff()
        val avatarRes = UserAvatarHelper.getAvatarRes(admin)
        val roleTitle = when {
            admin?.nipPetugas?.startsWith("1985") == true || admin?.username?.equals("admin", ignoreCase = true) == true -> "Koordinator Sarpras SMKN 1 Depok"
            admin?.username?.equals("toolman", ignoreCase = true) == true || admin?.nipPetugas?.startsWith("1992") == true -> "Teknisi Lab & Toolman Komputer"
            admin?.username?.equals("guru", ignoreCase = true) == true || admin?.nipPetugas?.startsWith("1979") == true -> "Guru / Tenaga Pendidik (Dapodik)"
            admin?.nipPetugas == "22231045" || admin?.username?.equals("siswa", ignoreCase = true) == true -> "Siswa Peminjam Aktif (Dapodik)"
            else -> "Staf Sarpras SMKN 1 Depok"
        }

        val userLoans = if (isStaff) {
            loans
        } else {
            val nip = admin?.nipPetugas ?: ""
            val name = admin?.namaLengkap ?: ""
            val uname = admin?.username ?: ""
            loans.filter {
                it.nomorIdentitas == nip ||
                (name.isNotBlank() && it.namaPeminjam.contains(name, ignoreCase = true)) ||
                (uname.isNotBlank() && it.namaPeminjam.contains(uname, ignoreCase = true))
            }
        }
        DashboardUiState(
            stats = stats,
            activeLoans = userLoans,
            todayDateFormatted = todayFormatted,
            todayIso = todayIso,
            adminName = admin?.namaLengkap ?: "Petugas Sarpras SMKN 1 Depok",
            adminNip = admin?.nipPetugas ?: "198507152010011002",
            adminContact = admin?.kontak ?: "",
            avatarResId = avatarRes,
            roleTitle = roleTitle,
            isStaff = isStaff,
            isLoading = false,
            returnSuccessMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(
            todayDateFormatted = todayFormatted,
            todayIso = todayIso
        )
    )

    fun processReturn(idPinjam: Int, kondisiKembali: String, catatan: String?) {
        viewModelScope.launch {
            val success = sarprasRepository.kembalikanBarang(
                idPinjam = idPinjam,
                tglKembaliReal = todayIso,
                kondisiKembali = kondisiKembali,
                catatan = catatan
            )
            if (success) {
                _messageFlow.value = "Pengembalian aset berhasil diverifikasi ke inventaris!"
            }
        }
    }

    fun clearReturnMessage() {
        _messageFlow.value = null
    }

    class Factory(
        private val sarprasRepository: SarprasRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                return DashboardViewModel(sarprasRepository, authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
