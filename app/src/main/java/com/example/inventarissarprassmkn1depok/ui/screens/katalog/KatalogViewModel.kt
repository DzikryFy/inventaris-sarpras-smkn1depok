package com.example.inventarissarprassmkn1depok.ui.screens.katalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import com.example.inventarissarprassmkn1depok.data.repository.SarprasRepository
import com.example.inventarissarprassmkn1depok.data.repository.UserRole
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class KatalogUiState(
    val searchQuery: String = "",
    val selectedKategoriId: Int? = null,
    val categories: List<KategoriEntity> = emptyList(),
    val barangList: List<BarangWithKategori> = emptyList(),
    val totalCount: Int = 0,
    val isFormOpen: Boolean = false,
    val barangToEdit: BarangWithKategori? = null,
    val barangToDelete: BarangWithKategori? = null,
    val isStaff: Boolean = true,
    val currentBorrowerName: String = "",
    val currentBorrowerId: String = "",
    val currentBorrowerRole: String = "Siswa",
    val message: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class KatalogViewModel(
    private val sarprasRepository: SarprasRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedKategoriId = MutableStateFlow<Int?>(null)
    private val _isFormOpen = MutableStateFlow(false)
    private val _barangToEdit = MutableStateFlow<BarangWithKategori?>(null)
    private val _barangToDelete = MutableStateFlow<BarangWithKategori?>(null)
    private val _message = MutableStateFlow<String?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _categoriesFlow = sarprasRepository.getAllKategori()

    private val _barangFilteredFlow = combine(_searchQuery, _selectedKategoriId) { query, katId ->
        Pair(query, katId)
    }.flatMapLatest { (query, katId) ->
        sarprasRepository.searchBarang(query, katId)
    }

    val uiState: StateFlow<KatalogUiState> = combine(
        _searchQuery,
        _selectedKategoriId,
        _categoriesFlow,
        _barangFilteredFlow,
        _isFormOpen,
        _barangToEdit,
        _barangToDelete,
        _message,
        _errorMessage
    ) { params ->
        val query = params[0] as String
        val katId = params[1] as Int?
        @Suppress("UNCHECKED_CAST")
        val categories = params[2] as List<KategoriEntity>
        @Suppress("UNCHECKED_CAST")
        val barangList = params[3] as List<BarangWithKategori>
        val isFormOpen = params[4] as Boolean
        val barangToEdit = params[5] as BarangWithKategori?
        val barangToDelete = params[6] as BarangWithKategori?
        val message = params[7] as String?
        val errorMessage = params[8] as String?

        val isStaff = authRepository.isStaff()
        val admin = authRepository.getLoggedInAdmin()
        val borrowerNip = admin?.nipPetugas ?: ""
        val borrowerName = admin?.namaLengkap ?: ""
        val userRole = authRepository.getCurrentUserRole()
        val borrowerRole = when (userRole) {
            UserRole.GURU -> "Guru"
            UserRole.SISWA -> "Siswa"
            UserRole.STAFF -> "Toolman"
        }

        KatalogUiState(
            searchQuery = query,
            selectedKategoriId = katId,
            categories = categories,
            barangList = barangList,
            totalCount = barangList.size,
            isFormOpen = isFormOpen,
            barangToEdit = barangToEdit,
            barangToDelete = barangToDelete,
            isStaff = isStaff,
            currentBorrowerName = borrowerName,
            currentBorrowerId = borrowerNip,
            currentBorrowerRole = borrowerRole,
            message = message,
            errorMessage = errorMessage,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = KatalogUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectKategori(kategoriId: Int?) {
        _selectedKategoriId.value = kategoriId
    }

    fun openCreateBarang() {
        _barangToEdit.value = null
        _isFormOpen.value = true
    }

    fun openEditBarang(barang: BarangWithKategori) {
        _barangToEdit.value = barang
        _isFormOpen.value = true
    }

    fun closeForm() {
        _isFormOpen.value = false
        _barangToEdit.value = null
    }

    fun saveBarang(barang: BarangEntity) {
        viewModelScope.launch {
            val isEdit = barang.idBarang > 0
            val result = if (isEdit) {
                sarprasRepository.updateBarang(barang)
            } else {
                sarprasRepository.tambahBarang(barang)
            }

            if (result.isSuccess) {
                _message.value = if (isEdit) "Data aset '${barang.namaBarang}' berhasil diperbarui!" else "Aset baru '${barang.namaBarang}' berhasil disimpan ke inventaris!"
                closeForm()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Gagal menyimpan data barang."
            }
        }
    }

    fun requestDeleteBarang(barang: BarangWithKategori) {
        _barangToDelete.value = barang
    }

    fun cancelDeleteBarang() {
        _barangToDelete.value = null
    }

    fun confirmDeleteBarang() {
        val target = _barangToDelete.value ?: return
        viewModelScope.launch {
            val result = sarprasRepository.hapusBarang(target.idBarang)
            if (result.isSuccess) {
                _message.value = "Data aset '${target.namaBarang}' (${target.kodeBarang}) berhasil dihapus."
                _barangToDelete.value = null
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Gagal menghapus data barang."
                _barangToDelete.value = null
            }
        }
    }

    fun submitPinjamQuick(
        barang: BarangWithKategori,
        borrowerName: String,
        borrowerId: String,
        borrowerRole: String,
        qty: Int,
        batasKembali: String,
        keperluan: String
    ) {
        if (qty <= 0) {
            _errorMessage.value = "Jumlah unit yang dipinjam minimal 1 unit."
            return
        }
        if (keperluan.isBlank()) {
            _errorMessage.value = "Keperluan kegiatan sarpras wajib diisi."
            return
        }

        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val todayStr = dateFormat.format(Date())
            val adminId = sarprasRepository.getActiveAdminId()

            val entity = PeminjamanEntity(
                idBarangFk = barang.idBarang,
                idAdminFk = adminId,
                nomorIdentitas = borrowerId.ifBlank { "SISWA-GURU-01" },
                namaPeminjam = borrowerName.trim(),
                tipePeminjam = borrowerRole,
                keperluanKegiatan = keperluan.trim(),
                jumlahPinjam = qty,
                tglPinjam = todayStr,
                batasKembali = batasKembali,
                status = "Dipinjam"
            )

            val result = sarprasRepository.pinjamBarang(entity)
            if (result.isSuccess) {
                _message.value = "Peminjaman '${barang.namaBarang}' ($qty unit) berhasil diajukan dan masuk ke data petugas! Stok otomatis berkurang."
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Gagal memproses peminjaman. Pastikan barang tidak sedang diservis dan stok mencukupi."
            }
        }
    }

    fun clearMessage() {
        _message.value = null
        _errorMessage.value = null
    }

    class Factory(
        private val sarprasRepository: SarprasRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(KatalogViewModel::class.java)) {
                return KatalogViewModel(sarprasRepository, authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
