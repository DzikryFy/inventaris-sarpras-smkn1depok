package com.example.inventarissarprassmkn1depok.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inventarissarprassmkn1depok.data.local.dao.AdminDao
import com.example.inventarissarprassmkn1depok.data.local.dao.BarangDao
import com.example.inventarissarprassmkn1depok.data.local.dao.DashboardDao
import com.example.inventarissarprassmkn1depok.data.local.dao.KategoriDao
import com.example.inventarissarprassmkn1depok.data.local.dao.PeminjamanDao
import com.example.inventarissarprassmkn1depok.data.local.entity.AdminEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        AdminEntity::class,
        KategoriEntity::class,
        BarangEntity::class,
        PeminjamanEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun adminDao(): AdminDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun barangDao(): BarangDao
    abstract fun peminjamanDao(): PeminjamanDao
    abstract fun dashboardDao(): DashboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "inventaris_sarpras.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database)
                    }
                }
            }
        }

        suspend fun seedDatabase(database: AppDatabase) {
            val adminDao = database.adminDao()
            val kategoriDao = database.kategoriDao()
            val barangDao = database.barangDao()
            val peminjamanDao = database.peminjamanDao()

            // 1. Seed Admin Default
            adminDao.insertAdmin(
                AdminEntity(
                    idAdmin = 1,
                    nipPetugas = "198507152010011002",
                    username = "admin",
                    password = "admin123",
                    namaLengkap = "Petugas Sarpras SMKN 1 Depok",
                    kontak = "081234567890"
                )
            )

            // 2. Seed Kategori (Sesuai spesifikasi proyek)
            val kategoriList = listOf(
                KategoriEntity(1, "Multimedia & Proyektor", "Perangkat proyektor, layar, dan penunjang presentasi multimedia"),
                KategoriEntity(2, "Alat Olahraga", "Peralatan olahraga bola basket, futsal, voli, dan matras senam"),
                KategoriEntity(3, "Kabel & Adaptor", "Kabel HDMI, VGA, roll kabel listrik, dan konverter display"),
                KategoriEntity(4, "Kamera & Dokumentasi", "Kamera DSLR, tripod, stabilizer, dan audio recorder"),
                KategoriEntity(5, "Toolkit & Perkakas", "Obeng set, tang crimping, tester kabel LAN, dan solder listrik")
            )
            kategoriDao.insertAll(kategoriList)

            // 3. Seed Barang Sarpras Nyata
            val barangList = listOf(
                BarangEntity(
                    idBarang = 1,
                    idKategoriFk = 1,
                    kodeBarang = "SAR-MM-001",
                    namaBarang = "Proyektor Epson EB-X500 3600 Lumens",
                    merkSpesifikasi = "Epson EB-X500 XGA 3LCD HDMI/VGA Port 3600 Lumens",
                    stokTotal = 5,
                    stokTersedia = 4,
                    kondisi = "Baik",
                    lokasiSimpan = "Ruang Sarpras Gedung A",
                    sumberDana = "BOS 2024"
                ),
                BarangEntity(
                    idBarang = 2,
                    idKategoriFk = 1,
                    kodeBarang = "SAR-MM-002",
                    namaBarang = "Proyektor InFocus IN112xv",
                    merkSpesifikasi = "InFocus DLP SVGA 3800 Lumens (Indikator Lampu Berkedip Merah)",
                    stokTotal = 2,
                    stokTersedia = 0,
                    kondisi = "Perlu Servis",
                    lokasiSimpan = "Gudang Servis Sarpras",
                    sumberDana = "BOPD 2023"
                ),
                BarangEntity(
                    idBarang = 3,
                    idKategoriFk = 2,
                    kodeBarang = "SAR-OR-001",
                    namaBarang = "Bola Basket Molten GG7X Original",
                    merkSpesifikasi = "Molten Size 7 Composite Leather FIBA Approved Premium",
                    stokTotal = 10,
                    stokTersedia = 8,
                    kondisi = "Baik",
                    lokasiSimpan = "Ruang Guru Penjas / Gudang Olahraga",
                    sumberDana = "BOS 2024"
                ),
                BarangEntity(
                    idBarang = 4,
                    idKategoriFk = 3,
                    kodeBarang = "SAR-KB-001",
                    namaBarang = "Kabel HDMI to HDMI 10 Meter Vention",
                    merkSpesifikasi = "Vention Braided Cable 4K@60Hz Gold Plated High Speed",
                    stokTotal = 8,
                    stokTersedia = 6,
                    kondisi = "Baik",
                    lokasiSimpan = "Lemari Aksesoris Multimedia",
                    sumberDana = "BOPD 2024"
                ),
                BarangEntity(
                    idBarang = 5,
                    idKategoriFk = 4,
                    kodeBarang = "SAR-DK-001",
                    namaBarang = "Kamera DSLR Canon EOS 3000D Kit 18-55mm",
                    merkSpesifikasi = "Canon EOS 3000D 18MP Wi-Fi + Lensa 18-55mm III + SD Card 64GB",
                    stokTotal = 3,
                    stokTersedia = 2,
                    kondisi = "Baik",
                    lokasiSimpan = "Drybox Ruang Multimedia",
                    sumberDana = "BOS Kinerja 2023"
                )
            )
            barangDao.insertAll(barangList)

            // 4. Seed Peminjaman Awal (Selaras dengan selisih stok_total dan stok_tersedia)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()

            val today = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val twoDaysAgo = dateFormat.format(calendar.time)

            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val twoDaysLater = dateFormat.format(calendar.time)

            val peminjamanList = listOf(
                PeminjamanEntity(
                    idPinjam = 1,
                    idBarangFk = 1,
                    idAdminFk = 1,
                    nomorIdentitas = "198204122008011015",
                    namaPeminjam = "Budi Santoso, S.Pd.",
                    tipePeminjam = "Guru",
                    keperluanKegiatan = "Presentasi Pembelajaran Multimedia di Lab RPL 2",
                    jumlahPinjam = 1,
                    tglPinjam = yesterday,
                    batasKembali = today,
                    tglKembaliReal = null,
                    kondisiKembali = null,
                    status = "Dipinjam",
                    catatan = "Prioritas mata pelajaran produktif kejuruan"
                ),
                PeminjamanEntity(
                    idPinjam = 2,
                    idBarangFk = 4,
                    idAdminFk = 1,
                    nomorIdentitas = "TLM-2024-001",
                    namaPeminjam = "Ahmad Zaki",
                    tipePeminjam = "Toolman",
                    keperluanKegiatan = "Uji coba proyektor & display Aula Pertemuan Utama",
                    jumlahPinjam = 2,
                    tglPinjam = twoDaysAgo,
                    batasKembali = today,
                    tglKembaliReal = null,
                    kondisiKembali = null,
                    status = "Dipinjam",
                    catatan = "Persiapan gladi resik seminar industri"
                ),
                PeminjamanEntity(
                    idPinjam = 3,
                    idBarangFk = 3,
                    idAdminFk = 1,
                    nomorIdentitas = "0078129384",
                    namaPeminjam = "Tim Basket Sekolah",
                    tipePeminjam = "Siswa",
                    keperluanKegiatan = "Latihan persiapan O2SN Kota Depok di Lapangan Utama",
                    jumlahPinjam = 2,
                    tglPinjam = today,
                    batasKembali = tomorrow,
                    tglKembaliReal = null,
                    kondisiKembali = null,
                    status = "Dipinjam",
                    catatan = "Disetujui guru pembimbing ekstrakurikuler"
                ),
                PeminjamanEntity(
                    idPinjam = 4,
                    idBarangFk = 5,
                    idAdminFk = 1,
                    nomorIdentitas = "0069382711",
                    namaPeminjam = "OSIS SMKN 1 Depok",
                    tipePeminjam = "OSIS",
                    keperluanKegiatan = "Dokumentasi Latihan Dasar Kepemimpinan (LDK) OSIS/MPK",
                    jumlahPinjam = 1,
                    tglPinjam = today,
                    batasKembali = twoDaysLater,
                    tglKembaliReal = null,
                    kondisiKembali = null,
                    status = "Dipinjam",
                    catatan = "Termasuk tas kamera dan charger baterai"
                )
            )
            peminjamanDao.insertAll(peminjamanList)
        }
    }
}
