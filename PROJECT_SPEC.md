# SPESIFIKASI TEKNIS: INVENTARIS SARPRAS SMKN 1 DEPOK
Aplikasi Android berbasis Kotlin + Jetpack Compose + SQLite (Room Database).
Min SDK: 26, Target SDK: 34.

## 1. Identitas & Ruang Lingkup
- Nama Aplikasi: Sistem Informasi Inventaris Sarana dan Prasarana Sekolah SMKN 1 Depok
- Target User: Petugas Sarpras, Toolman Lab, Guru, dan Siswa/OSIS.
- Kategori Aset: Alat Olahraga, Multimedia/Proyektor, Kabel/Adaptor, Kamera/Dokumentasi, Toolkit.
- Palet Warna: Deep Onedex Navy (#0F243A), Slate Blue (#2563EB), Amber Gold (#D97706), Neutral Cool Gray (#F8FAFC).

## 2. Skema Basis Data (Room SQLite)
- Tabel admin_sarpras (id_admin, nip_petugas, username, password, nama_lengkap, kontak)
- Tabel kategori (id_kategori, nama_kategori, keterangan)
- Tabel barang_sarpras (id_barang, id_kategori_fk, kode_barang, nama_barang, merk_spesifikasi, stok_total, stok_tersedia, kondisi, lokasi_simpan, sumber_dana)
- Tabel peminjaman_sarpras (id_pinjam, id_barang_fk, id_admin_fk, nomor_identitas, nama_peminjam, tipe_peminjam, keperluan_kegiatan, jumlah_pinjam, tgl_pinjam, batas_kembali, tgl_kembali_real, kondisi_kembali, status, catatan)
  *Aturan Relasi:* peminjaman_sarpras.id_barang_fk -> barang_sarpras.id_barang (CASCADE).

## 3. Logika Sirkulasi Stok
- Peminjaman: Validasi jumlah_pinjam <= stok_tersedia. Jika sukses, stok_tersedia berkurang.
- Pengembalian: Status berubah menjadi 'Kembali', stok_tersedia bertambah kembali sesuai jumlah_pinjam. Catat kondisi fisik akhir (Baik/Rusak).

## 4. 5 Layar Wajib (Material Design 3 - Anti-AI Slop)
1. Splash Screen: Logo SMKN 1 Depok, Judul Sistem, Tagline "ONEDEX HIBER", delay 2 detik menuju Login.
2. Login Screen: Form NIP/Username & Password, validasi sesi ke SQLite.
3. Dashboard: Metrik Bento Grid 2x2 (Total Aset, Siap Dipinjam, Sedang Dipinjam, Perlu Servis) + Shortcut Aksi Cepat + List Jatuh Tempo Hari Ini.
4. Menu & Katalog: Filter Kategori Horizontal Chips, Card List Aset dengan indikator stok/kondisi, Search bar.
5. Pengelolaan Data & Laporan: Tab Peminjaman Aktif & Riwayat, form tambah/edit barang, ekspor rekap.