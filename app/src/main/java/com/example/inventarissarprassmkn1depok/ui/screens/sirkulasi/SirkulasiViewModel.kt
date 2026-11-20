package com.example.inventarissarprassmkn1depok.ui.screens.sirkulasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import com.example.inventarissarprassmkn1depok.data.repository.SirkulasiRepository
import com.example.inventarissarprassmkn1depok.data.repository.UserRole
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

enum class SirkulasiTab(val title: String) {
    AKTIF("Sedang Dipinjam"),
    RIWAYAT("Riwayat Selesai")
}

data class SirkulasiUiState(
    val selectedTab: SirkulasiTab = SirkulasiTab.AKTIF,
    val activeLoans: List<PeminjamanDetail> = emptyList(),
    val completedLoans: List<PeminjamanDetail> = emptyList(),
    val rawActiveCount: Int = 0,
    val rawCompletedCount: Int = 0,
    val allBarang: List<BarangWithKategori> = emptyList(),
    val searchQuery: String = "",
    val selectedTipeFilter: String? = null,
    val isFormOpen: Boolean = false,
    val selectedLoanForReturn: PeminjamanDetail? = null,
    val preselectedBarang: BarangWithKategori? = null,
    val isStaff: Boolean = true,
    val currentBorrowerName: String = "",
    val currentBorrowerId: String = "",
    val currentBorrowerTipe: String = "Siswa",
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

class SirkulasiViewModel(
    private val sirkulasiRepository: SirkulasiRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(SirkulasiTab.AKTIF)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedTipeFilter = MutableStateFlow<String?>(null)
    private val _isFormOpen = MutableStateFlow(false)
    private val _selectedLoanForReturn = MutableStateFlow<PeminjamanDetail?>(null)
    private val _preselectedBarang = MutableStateFlow<BarangWithKategori?>(null)
    private val _message = MutableStateFlow<String?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    val uiState: StateFlow<SirkulasiUiState> = combine(
        _selectedTab,
        _searchQuery,
        _selectedTipeFilter,
        _isFormOpen,
        _selectedLoanForReturn,
        _preselectedBarang,
        _message,
        _errorMessage,
        sirkulasiRepository.getPeminjamanAktif(),
        sirkulasiRepository.getRiwayatPeminjaman(),
        sirkulasiRepository.getAllBarang()
    ) { params ->
        val selectedTab = params[0] as SirkulasiTab
        val searchQuery = params[1] as String
        val selectedTipeFilter = params[2] as String?
        val isFormOpen = params[3] as Boolean
        val selectedLoanForReturn = params[4] as PeminjamanDetail?
        val preselectedBarang = params[5] as BarangWithKategori?
        val message = params[6] as String?
        val errorMessage = params[7] as String?
        @Suppress("UNCHECKED_CAST")
        val rawActive = params[8] as List<PeminjamanDetail>
        @Suppress("UNCHECKED_CAST")
        val rawCompleted = params[9] as List<PeminjamanDetail>
        @Suppress("UNCHECKED_CAST")
        val allBarang = params[10] as List<BarangWithKategori>

        val isStaff = authRepository.isStaff()
        val admin = authRepository.getLoggedInAdmin()
        val borrowerNip = admin?.nipPetugas ?: ""
        val borrowerName = admin?.namaLengkap ?: ""
        val borrowerUsername = admin?.username ?: ""
        val userRole = authRepository.getCurrentUserRole()
        val borrowerType = when (userRole) {
            UserRole.GURU -> "Guru"
            UserRole.SISWA -> "Siswa"
            else -> "Siswa"
        }

        val query = searchQuery.trim().lowercase()

        val filteredActive = rawActive.filter { loan ->
            val matchRole = isStaff ||
                    loan.nomorIdentitas == borrowerNip ||
                    (borrowerName.isNotBlank() && loan.namaPeminjam.contains(borrowerName, ignoreCase = true)) ||
                    (borrowerUsername.isNotBlank() && loan.namaPeminjam.contains(borrowerUsername, ignoreCase = true))

            val matchQuery = query.isEmpty() ||
                    loan.namaPeminjam.lowercase().contains(query) ||
                    loan.nomorIdentitas.lowercase().contains(query) ||
                    loan.namaBarang.lowercase().contains(query) ||
                    loan.kodeBarang.lowercase().contains(query) ||
                    loan.keperluanKegiatan.lowercase().contains(query)

            val matchTipe = selectedTipeFilter == null ||
                    loan.tipePeminjam.equals(selectedTipeFilter, ignoreCase = true)

            matchRole && matchQuery && matchTipe
        }

        val filteredCompleted = rawCompleted.filter { loan ->
            val matchRole = isStaff ||
                    loan.nomorIdentitas == borrowerNip ||
                    (borrowerName.isNotBlank() && loan.namaPeminjam.contains(borrowerName, ignoreCase = true)) ||
                    (borrowerUsername.isNotBlank() && loan.namaPeminjam.contains(borrowerUsername, ignoreCase = true))

            val matchQuery = query.isEmpty() ||
                    loan.namaPeminjam.lowercase().contains(query) ||
                    loan.nomorIdentitas.lowercase().contains(query) ||
                    loan.namaBarang.lowercase().contains(query) ||
                    loan.kodeBarang.lowercase().contains(query)

            val matchTipe = selectedTipeFilter == null ||
                    loan.tipePeminjam.equals(selectedTipeFilter, ignoreCase = true)

            matchRole && matchQuery && matchTipe
        }

        SirkulasiUiState(
            selectedTab = selectedTab,
            activeLoans = filteredActive,
            completedLoans = filteredCompleted,
            rawActiveCount = filteredActive.size,
            rawCompletedCount = filteredCompleted.size,
            allBarang = allBarang,
            searchQuery = searchQuery,
            selectedTipeFilter = selectedTipeFilter,
            isFormOpen = isFormOpen,
            selectedLoanForReturn = selectedLoanForReturn,
            preselectedBarang = preselectedBarang,
            isStaff = isStaff,
            currentBorrowerName = borrowerName,
            currentBorrowerId = borrowerNip,
            currentBorrowerTipe = borrowerType,
            isLoading = false,
            message = message,
            errorMessage = errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SirkulasiUiState()
    )

    fun selectTab(tab: SirkulasiTab) {
        _selectedTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTipeFilter(tipe: String?) {
        _selectedTipeFilter.value = if (_selectedTipeFilter.value == tipe) null else tipe
    }

    fun openForm(barang: BarangWithKategori? = null) {
        _preselectedBarang.value = barang
        _isFormOpen.value = true
    }

    fun closeForm() {
        _isFormOpen.value = false
        _preselectedBarang.value = null
    }

    fun openReturnSheet(loan: PeminjamanDetail) {
        _selectedLoanForReturn.value = loan
    }

    fun closeReturnSheet() {
        _selectedLoanForReturn.value = null
    }

    fun submitPeminjaman(
        nomorIdentitas: String,
        namaPeminjam: String,
        tipePeminjam: String,
        idBarang: Int,
        jumlahPinjam: Int,
        batasKembali: String,
        keperluan: String
    ) {
        if (nomorIdentitas.isBlank() || namaPeminjam.isBlank()) {
            _errorMessage.value = "Nomor identitas dan nama peminjam wajib diisi."
            return
        }
        if (idBarang <= 0) {
            _errorMessage.value = "Silakan pilih barang yang akan dipinjam."
            return
        }
        if (jumlahPinjam <= 0) {
            _errorMessage.value = "Jumlah pinjam minimal 1 unit."
            return
        }
        if (keperluan.isBlank()) {
            _errorMessage.value = "Keperluan peminjaman sarpras wajib diisi."
            return
        }

        viewModelScope.launch {
            val tglPinjamFormatted = isoDateFormat.format(Date())
            val adminId = sirkulasiRepository.getActiveAdminId()

            val entity = PeminjamanEntity(
                idBarangFk = idBarang,
                idAdminFk = adminId,
                nomorIdentitas = nomorIdentitas.trim(),
                namaPeminjam = namaPeminjam.trim(),
                tipePeminjam = tipePeminjam,
                keperluanKegiatan = keperluan.trim(),
                jumlahPinjam = jumlahPinjam,
                tglPinjam = tglPinjamFormatted,
                batasKembali = batasKembali,
                status = "Dipinjam"
            )

            val result = sirkulasiRepository.pinjamBarang(entity)
            if (result.isSuccess) {
                _message.value = "Peminjaman berhasil dicatat! Stok inventaris otomatis diperbarui."
                closeForm()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Gagal mencatat peminjaman."
            }
        }
    }

    fun confirmReturn(
        idPinjam: Int,
        kondisiKembali: String,
        catatan: String?
    ) {
        viewModelScope.launch {
            val nowStr = isoDateFormat.format(Date())
            val result = sirkulasiRepository.kembalikanBarang(
                idPinjam = idPinjam,
                tglKembaliReal = nowStr,
                kondisiKembali = kondisiKembali,
                catatan = catatan?.trim()?.ifEmpty { null }
            )

            if (result.isSuccess) {
                val kondisiMsg = if (kondisiKembali.contains("Rusak", ignoreCase = true)) {
                    "Barang diterima dengan status RUSAK. Aset ditandai 'Perlu Servis'."
                } else {
                    "Barang berhasil diterima dalam kondisi BAIK dan dikembalikan ke stok."
                }
                _message.value = kondisiMsg
                closeReturnSheet()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Gagal memproses pengembalian."
            }
        }
    }

    fun clearMessages() {
        _message.value = null
        _errorMessage.value = null
    }

    class Factory(
        private val sirkulasiRepository: SirkulasiRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SirkulasiViewModel::class.java)) {
                return SirkulasiViewModel(sirkulasiRepository, authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
