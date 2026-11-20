package com.example.inventarissarprassmkn1depok.data.repository

import com.example.inventarissarprassmkn1depok.data.local.dao.BarangDao
import com.example.inventarissarprassmkn1depok.data.local.dao.DashboardDao
import com.example.inventarissarprassmkn1depok.data.local.dao.KategoriDao
import com.example.inventarissarprassmkn1depok.data.local.dao.PeminjamanDao
import com.example.inventarissarprassmkn1depok.data.local.model.AuditSummary
import com.example.inventarissarprassmkn1depok.data.local.model.KategoriSirkulasiStat
import com.example.inventarissarprassmkn1depok.data.local.model.LokasiDistribusi
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LaporanRepository(
    private val peminjamanDao: PeminjamanDao,
    private val barangDao: BarangDao,
    private val kategoriDao: KategoriDao,
    private val dashboardDao: DashboardDao
) {
    /**
     * Menghasilkan ringkasan audit aset dan rasio ketersediaan
     */
    fun getAuditSummary(): Flow<AuditSummary> {
        return dashboardDao.getDashboardStats().map { stats ->
            val persentase = if (stats.totalAset > 0) {
                (stats.siapDipinjam * 100) / stats.totalAset
            } else 0

            AuditSummary(
                totalAset = stats.totalAset,
                siapDipinjam = stats.siapDipinjam,
                sedangDipinjam = stats.sedangDipinjam,
                perluServis = stats.perluServis,
                persentaseKetersediaan = persentase
            )
        }
    }

    /**
     * Mengambil distribusi unit aset sarpras per lokasi simpan fisik
     */
    fun getDistribusiLokasi(): Flow<List<LokasiDistribusi>> =
        barangDao.getDistribusiLokasi()

    /**
     * Mengambil statistik frekuensi sirkulasi peminjaman per kategori sarpras
     */
    fun getStatistikKategori(): Flow<List<KategoriSirkulasiStat>> =
        barangDao.getStatistikKategori()

    /**
     * Mengambil riwayat peminjaman dengan filter status dan rentang tanggal
     */
    fun getRiwayatLaporan(statusFilter: String, rentangWaktu: String): Flow<List<PeminjamanDetail>> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()

        val cutoffDate = when (rentangWaktu) {
            "Minggu Ini" -> {
                cal.add(Calendar.DAY_OF_YEAR, -7)
                sdf.format(cal.time)
            }
            "Bulan Ini" -> {
                cal.add(Calendar.DAY_OF_YEAR, -30)
                sdf.format(cal.time)
            }
            else -> "" // Semua
        }

        return peminjamanDao.getAllPeminjamanDetail().map { list ->
            list.filter { item ->
                // Filter Status
                val matchStatus = when (statusFilter) {
                    "Dipinjam" -> item.status.equals("Dipinjam", ignoreCase = true)
                    "Kembali" -> item.status.equals("Kembali", ignoreCase = true)
                    "Rusak" -> item.kondisiKembali?.contains("Rusak", ignoreCase = true) == true
                    else -> true // Semua
                }

                // Filter Rentang Waktu
                val matchDate = if (cutoffDate.isEmpty()) {
                    true
                } else {
                    val pinjamDate = item.tglPinjam.take(10)
                    pinjamDate >= cutoffDate
                }

                matchStatus && matchDate
            }
        }
    }

    /**
     * Mengambil daftar seluruh riwayat sirkulasi yang mengalami insiden fisik rusak atau cacat
     */
    fun getLogKerusakan(): Flow<List<PeminjamanDetail>> {
        return peminjamanDao.getAllPeminjamanDetail().map { list ->
            list.filter { item ->
                item.kondisiKembali?.contains("Rusak", ignoreCase = true) == true
            }
        }
    }

    /**
     * Membuat teks rekapitulasi formal bertanda tangan resmi SMKN 1 Depok siap bagikan (Share Sheet)
     */
    fun generateLaporanText(
        summary: AuditSummary,
        distribusi: List<LokasiDistribusi>,
        kategoriStat: List<KategoriSirkulasiStat>,
        sirkulasiList: List<PeminjamanDetail>
    ): String {
        val tanggalCetak = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", Locale.forLanguageTag("id-ID")).format(Date())

        val sb = StringBuilder()
        sb.appendLine("==================================================")
        sb.appendLine("LAPORAN REKAPITULASI INVENTARIS SARANA & PRASARANA")
        sb.appendLine("SMK NEGERI 1 DEPOK — ONEDEX HIBER")
        sb.appendLine("Waktu Cetak: $tanggalCetak WIB")
        sb.appendLine("==================================================")
        sb.appendLine()
        sb.appendLine("I. RINGKASAN AUDIT ASET:")
        sb.appendLine("• Total Unit Sarpras   : ${summary.totalAset} Unit")
        sb.appendLine("• Siap Digunakan       : ${summary.siapDipinjam} Unit (${summary.persentaseKetersediaan}%)")
        sb.appendLine("• Sedang Dipinjam      : ${summary.sedangDipinjam} Unit")
        sb.appendLine("• Perlu Servis / Rusak : ${summary.perluServis} Unit")
        sb.appendLine()

        sb.appendLine("II. DISTRIBUSI LOKASI PENYIMPANAN:")
        distribusi.forEachIndexed { i, loc ->
            sb.appendLine("${i + 1}. ${loc.lokasiSimpan}: ${loc.totalUnit} Unit (${loc.itemJenisCount} jenis barang)")
        }
        sb.appendLine()

        sb.appendLine("III. SIRKULASI PER KATEGORI:")
        kategoriStat.forEachIndexed { i, kat ->
            sb.appendLine("${i + 1}. ${kat.namaKategori}: ${kat.totalDipinjam} kali dipinjam (${kat.totalItem} jenis)")
        }
        sb.appendLine()

        sb.appendLine("IV. DAFTAR TRANSAKSI SIRKULASI TERAKHIR (${sirkulasiList.size} Record):")
        sirkulasiList.take(20).forEach { item ->
            val kondisiStr = if (item.kondisiKembali != null) " [Kondisi: ${item.kondisiKembali}]" else ""
            sb.appendLine("• [#TRX-${item.idPinjam.toString().padStart(4, '0')}] ${item.tglPinjam.take(10)} | ${item.namaPeminjam} (${item.tipePeminjam}) - ${item.namaBarang} (${item.jumlahPinjam} unit) - Status: ${item.status}$kondisiStr")
        }
        if (sirkulasiList.size > 20) {
            sb.appendLine("... dan ${sirkulasiList.size - 20} transaksi lainnya.")
        }
        sb.appendLine()
        sb.appendLine("==================================================")
        sb.appendLine("Dokumen ini dihasilkan secara otomatis oleh:")
        sb.appendLine("Sistem Informasi Inventaris Sarpras SMKN 1 Depok")
        sb.appendLine("Jl. Tapos RT.01/RW.03, Cimpaeun, Tapos, Kota Depok")
        sb.appendLine("==================================================")

        return sb.toString()
    }

    /**
     * Membuat string format CSV standar untuk ekspor spreadsheet
     */
    fun generateCsvData(sirkulasiList: List<PeminjamanDetail>): String {
        val sb = StringBuilder()
        sb.appendLine("No Transaksi,Tanggal Pinjam,Batas Kembali,Tanggal Kembali Real,Nama Peminjam,Nomor Identitas,Tipe Peminjam,Nama Barang,Kode Barang,Jumlah Pinjam,Keperluan,Status,Kondisi Kembali,Catatan")
        sirkulasiList.forEach { item ->
            val cleanCatatan = (item.catatan ?: "-").replace(",", ";").replace("\n", " ")
            val cleanKeperluan = item.keperluanKegiatan.replace(",", ";").replace("\n", " ")
            sb.appendLine(
                "\"#TRX-${item.idPinjam.toString().padStart(4, '0')}\"," +
                        "\"${item.tglPinjam}\"," +
                        "\"${item.batasKembali}\"," +
                        "\"${item.tglKembaliReal ?: "-"}\"," +
                        "\"${item.namaPeminjam}\"," +
                        "\"'${item.nomorIdentitas}\"," +
                        "\"${item.tipePeminjam}\"," +
                        "\"${item.namaBarang}\"," +
                        "\"${item.kodeBarang}\"," +
                        "${item.jumlahPinjam}," +
                        "\"$cleanKeperluan\"," +
                        "\"${item.status}\"," +
                        "\"${item.kondisiKembali ?: "-"}\"," +
                        "\"$cleanCatatan\""
            )
        }
        return sb.toString()
    }
}
