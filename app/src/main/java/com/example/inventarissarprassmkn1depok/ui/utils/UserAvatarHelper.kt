package com.example.inventarissarprassmkn1depok.ui.utils

import com.example.inventarissarprassmkn1depok.R
import com.example.inventarissarprassmkn1depok.data.local.entity.AdminEntity

object UserAvatarHelper {

    fun getAvatarRes(admin: AdminEntity?): Int {
        if (admin == null) return R.drawable.ic_avatar_admin
        val uname = admin.username.lowercase()
        val nip = admin.nipPetugas
        val nama = admin.namaLengkap

        return when {
            uname == "siswa" || nip == "22231045" || nama.contains("Siswa", ignoreCase = true) -> {
                R.drawable.ic_avatar_siswa
            }
            uname == "guru" || nip.startsWith("1979") || nama.contains("Guru", ignoreCase = true) || nama.contains("Dra.", ignoreCase = true) -> {
                R.drawable.ic_avatar_guru
            }
            uname == "toolman" || nip.startsWith("1992") || nama.contains("Toolman", ignoreCase = true) -> {
                R.drawable.ic_avatar_toolman
            }
            else -> {
                R.drawable.ic_avatar_admin
            }
        }
    }

    fun getBorrowerAvatarRes(tipePeminjam: String, nomorIdentitas: String? = null): Int {
        val tipe = tipePeminjam.trim().lowercase()
        val id = nomorIdentitas?.trim() ?: ""

        return when {
            tipe == "guru" || id.startsWith("1979") -> R.drawable.ic_avatar_guru
            tipe == "siswa" || tipe == "osis" || id == "22231045" -> R.drawable.ic_avatar_siswa
            tipe.contains("toolman") || id.startsWith("1992") -> R.drawable.ic_avatar_toolman
            tipe.contains("staf") || tipe.contains("admin") -> R.drawable.ic_avatar_admin
            else -> R.drawable.ic_avatar_siswa
        }
    }
}
