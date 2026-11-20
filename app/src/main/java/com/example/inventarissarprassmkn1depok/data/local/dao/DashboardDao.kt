package com.example.inventarissarprassmkn1depok.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.inventarissarprassmkn1depok.data.local.model.DashboardStats
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardDao {
    @Query("""
        SELECT 
            (SELECT COALESCE(SUM(stok_total), 0) FROM barang_sarpras) AS totalAset,
            (SELECT COALESCE(SUM(stok_tersedia), 0) FROM barang_sarpras) AS siapDipinjam,
            (SELECT COALESCE(SUM(jumlah_pinjam), 0) FROM peminjaman_sarpras WHERE status = 'Dipinjam') AS sedangDipinjam,
            (SELECT COUNT(*) FROM barang_sarpras WHERE kondisi != 'Baik') AS perluServis
    """)
    fun getDashboardStats(): Flow<DashboardStats>

    @Query("""
        SELECT p.id_pinjam, p.id_barang_fk, b.nama_barang, b.kode_barang, 
               k.nama_kategori, p.id_admin_fk, a.nama_lengkap AS nama_petugas, 
               p.nomor_identitas, p.nama_peminjam, p.tipe_peminjam, 
               p.keperluan_kegiatan, p.jumlah_pinjam, p.tgl_pinjam, 
               p.batas_kembali, p.tgl_kembali_real, p.kondisi_kembali, 
               p.status, p.catatan
        FROM peminjaman_sarpras p
        INNER JOIN barang_sarpras b ON p.id_barang_fk = b.id_barang
        INNER JOIN kategori k ON b.id_kategori_fk = k.id_kategori
        LEFT JOIN admin_sarpras a ON p.id_admin_fk = a.id_admin
        WHERE p.status = 'Dipinjam' AND p.batas_kembali <= :today
        ORDER BY p.batas_kembali ASC
    """)
    fun getJatuhTempoHariIni(today: String): Flow<List<PeminjamanDetail>>
}
