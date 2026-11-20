package com.example.inventarissarprassmkn1depok.ui.screens.laporan

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventarissarprassmkn1depok.data.local.model.AuditSummary
import com.example.inventarissarprassmkn1depok.data.local.model.KategoriSirkulasiStat
import com.example.inventarissarprassmkn1depok.data.local.model.LokasiDistribusi
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import com.example.inventarissarprassmkn1depok.data.repository.LaporanRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

enum class LaporanTab(val title: String) {
    RINGKASAN("Rekap & Ringkasan"),
    RIWAYAT("Riwayat Sirkulasi"),
    LOG_KERUSAKAN("Log Kerusakan / Servis")
}

data class LaporanUiState(
    val selectedTab: LaporanTab = LaporanTab.RINGKASAN,
    val filterStatus: String = "Semua",
    val filterRentang: String = "Semua",
    val summary: AuditSummary = AuditSummary(),
    val distribusiLokasi: List<LokasiDistribusi> = emptyList(),
    val statistikKategori: List<KategoriSirkulasiStat> = emptyList(),
    val riwayatList: List<PeminjamanDetail> = emptyList(),
    val logKerusakan: List<PeminjamanDetail> = emptyList(),
    val exportMessage: String? = null,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class LaporanViewModel(
    private val laporanRepository: LaporanRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(LaporanTab.RINGKASAN)
    private val _filterStatus = MutableStateFlow("Semua")
    private val _filterRentang = MutableStateFlow("Semua")
    private val _exportMessage = MutableStateFlow<String?>(null)

    private val _riwayatFilterFlow = combine(_filterStatus, _filterRentang) { status, rentang ->
        Pair(status, rentang)
    }.flatMapLatest { (status, rentang) ->
        laporanRepository.getRiwayatLaporan(status, rentang)
    }

    val uiState: StateFlow<LaporanUiState> = combine(
        _selectedTab,
        _filterStatus,
        _filterRentang,
        laporanRepository.getAuditSummary(),
        laporanRepository.getDistribusiLokasi(),
        laporanRepository.getStatistikKategori(),
        _riwayatFilterFlow,
        laporanRepository.getLogKerusakan(),
        _exportMessage
    ) { params ->
        val selectedTab = params[0] as LaporanTab
        val filterStatus = params[1] as String
        val filterRentang = params[2] as String
        val summary = params[3] as AuditSummary
        @Suppress("UNCHECKED_CAST")
        val distribusi = params[4] as List<LokasiDistribusi>
        @Suppress("UNCHECKED_CAST")
        val kategoriStat = params[5] as List<KategoriSirkulasiStat>
        @Suppress("UNCHECKED_CAST")
        val riwayat = params[6] as List<PeminjamanDetail>
        @Suppress("UNCHECKED_CAST")
        val logKerusakan = params[7] as List<PeminjamanDetail>
        val exportMessage = params[8] as String?

        LaporanUiState(
            selectedTab = selectedTab,
            filterStatus = filterStatus,
            filterRentang = filterRentang,
            summary = summary,
            distribusiLokasi = distribusi,
            statistikKategori = kategoriStat,
            riwayatList = riwayat,
            logKerusakan = logKerusakan,
            exportMessage = exportMessage,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LaporanUiState()
    )

    fun selectTab(tab: LaporanTab) {
        _selectedTab.value = tab
    }

    fun setFilterStatus(status: String) {
        _filterStatus.value = status
    }

    fun setFilterRentang(rentang: String) {
        _filterRentang.value = rentang
    }

    fun shareLaporan(context: Context, asCsv: Boolean = false) {
        val state = uiState.value
        val shareContent = if (asCsv) {
            laporanRepository.generateCsvData(state.riwayatList)
        } else {
            laporanRepository.generateLaporanText(
                summary = state.summary,
                distribusi = state.distribusiLokasi,
                kategoriStat = state.statistikKategori,
                sirkulasiList = state.riwayatList
            )
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (asCsv) "text/comma-separated-values" else "text/plain"
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Rekapitulasi Inventaris Sarpras SMKN 1 Depok — " + if (asCsv) "Data CSV" else "Laporan Resmi"
            )
            putExtra(Intent.EXTRA_TEXT, shareContent)
        }

        val chooser = Intent.createChooser(intent, "Ekspor Laporan Sarpras SMKN 1 Depok").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)

        _exportMessage.value = if (asCsv) "Data CSV siap dibagikan ke spreadsheet!" else "Laporan teks resmi siap dibagikan!"
    }

    fun clearMessage() {
        _exportMessage.value = null
    }

    class Factory(
        private val laporanRepository: LaporanRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LaporanViewModel::class.java)) {
                return LaporanViewModel(laporanRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
