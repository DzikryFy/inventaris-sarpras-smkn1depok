package com.example.inventarissarprassmkn1depok.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface PeminjamanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeminjaman(peminjaman: PeminjamanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(peminjamanList: List<PeminjamanEntity>)

    @Update
    suspend fun updatePeminjaman(peminjaman: PeminjamanEntity)

    @Delete
    suspend fun deletePeminjaman(peminjaman: PeminjamanEntity)

    @Query("SELECT * FROM peminjaman_sarpras WHERE id_pinjam = :id LIMIT 1")
    suspend fun getPeminjamanById(id: Int): PeminjamanEntity?

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
        ORDER BY p.id_pinjam DESC
    """)
    fun getAllPeminjamanDetail(): Flow<List<PeminjamanDetail>>

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
        WHERE p.status = :status
        ORDER BY p.id_pinjam DESC
    """)
    fun getPeminjamanByStatus(status: String): Flow<List<PeminjamanDetail>>

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
    fun getPeminjamanJatuhTempo(today: String): Flow<List<PeminjamanDetail>>

    @Query("""
        UPDATE peminjaman_sarpras 
        SET tgl_kembali_real = :tglKembaliReal, 
            kondisi_kembali = :kondisiKembali, 
            status = 'Kembali', 
            catatan = CASE 
                WHEN :catatan IS NULL OR :catatan = '' THEN catatan 
                WHEN catatan IS NULL OR catatan = '' THEN :catatan 
                ELSE catatan || ' | ' || :catatan 
            END
        WHERE id_pinjam = :idPinjam
    """)
    suspend fun updatePengembalian(
        idPinjam: Int,
        tglKembaliReal: String,
        kondisiKembali: String,
        catatan: String?
    ): Int

    @Query("SELECT COUNT(*) FROM peminjaman_sarpras")
    suspend fun getPeminjamanCount(): Int

    @Transaction
    suspend fun pinjamBarang(peminjaman: PeminjamanEntity, barangDao: BarangDao): Boolean {
        val barang = barangDao.getBarangById(peminjaman.idBarangFk) ?: return false
        if (barang.stokTersedia < peminjaman.jumlahPinjam) {
            return false
        }
        val affected = barangDao.reduceStok(peminjaman.idBarangFk, peminjaman.jumlahPinjam)
        if (affected > 0) {
            insertPeminjaman(peminjaman)
            return true
        }
        return false
    }

    @Transaction
    suspend fun kembalikanBarang(
        idPinjam: Int,
        tglKembaliReal: String,
        kondisiKembali: String,
        catatanTambahan: String?,
        barangDao: BarangDao
    ): Boolean {
        val pinjam = getPeminjamanById(idPinjam) ?: return false
        if (pinjam.status == "Kembali") return false

        val updated = updatePengembalian(idPinjam, tglKembaliReal, kondisiKembali, catatanTambahan)
        if (updated > 0) {
            barangDao.restoreStok(pinjam.idBarangFk, pinjam.jumlahPinjam)
            return true
        }
        return false
    }
}
