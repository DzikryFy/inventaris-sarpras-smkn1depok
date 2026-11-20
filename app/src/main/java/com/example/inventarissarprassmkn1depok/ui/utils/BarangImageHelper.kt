package com.example.inventarissarprassmkn1depok.ui.utils

import com.example.inventarissarprassmkn1depok.R

object BarangImageHelper {

    fun getDrawableRes(
        kodeBarang: String,
        namaBarang: String,
        namaKategori: String? = null
    ): Int {
        val kode = kodeBarang.trim().uppercase()
        val nama = namaBarang.trim().lowercase()
        val kat = (namaKategori ?: "").trim().lowercase()

        return when {
            // 1. Proyektor Epson EB-X500
            kode.contains("MM-001") || nama.contains("epson") -> {
                R.drawable.img_barang_proyektor_epson
            }

            // 2. Proyektor InFocus IN112xv
            kode.contains("MM-002") || nama.contains("infocus") -> {
                R.drawable.img_barang_proyektor_infocus
            }

            // 3. Bola Basket Molten GG7X
            kode.contains("OR-001") || nama.contains("molten") || nama.contains("basket") || nama.contains("bola") -> {
                R.drawable.img_barang_bola_basket
            }

            // 4. Kabel HDMI Vention
            kode.contains("KB-001") || nama.contains("vention") || nama.contains("hdmi") || nama.contains("kabel") -> {
                R.drawable.img_barang_kabel_hdmi
            }

            // 5. Kamera DSLR Canon EOS 3000D
            kode.contains("DK-001") || nama.contains("canon") || nama.contains("kamera") || nama.contains("dslr") -> {
                R.drawable.img_barang_kamera_canon
            }

            // 6. Toolkit / Perkakas
            kat.contains("toolkit") || kat.contains("perkakas") || nama.contains("obeng") || nama.contains("tang") || nama.contains("solder") -> {
                R.drawable.img_barang_toolkit
            }

            // Fallback per kategori umum
            kat.contains("multimedia") || kat.contains("proyektor") -> {
                R.drawable.img_barang_proyektor_epson
            }
            kat.contains("olahraga") -> {
                R.drawable.img_barang_bola_basket
            }
            kat.contains("kabel") || kat.contains("adaptor") -> {
                R.drawable.img_barang_kabel_hdmi
            }
            kat.contains("kamera") || kat.contains("dokumentasi") -> {
                R.drawable.img_barang_kamera_canon
            }

            // Default aset sarpras
            else -> {
                R.drawable.img_barang_default
            }
        }
    }
}
