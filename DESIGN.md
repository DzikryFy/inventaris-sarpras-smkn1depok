---
name: SMKN 1 Depok Sarpras Circulation
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#44474d'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#74777d'
  outline-variant: '#c4c6cd'
  surface-tint: '#4c6079'
  primary: '#000e1f'
  on-primary: '#ffffff'
  primary-container: '#0f243a'
  on-primary-container: '#788ca6'
  inverse-primary: '#b4c8e5'
  secondary: '#0051d5'
  on-secondary: '#ffffff'
  secondary-container: '#316bf3'
  on-secondary-container: '#fefcff'
  tertiary: '#190900'
  on-tertiary: '#ffffff'
  tertiary-container: '#391b00'
  on-tertiary-container: '#d07100'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d2e4ff'
  primary-fixed-dim: '#b4c8e5'
  on-primary-fixed: '#061d32'
  on-primary-fixed-variant: '#354860'
  secondary-fixed: '#dbe1ff'
  secondary-fixed-dim: '#b4c5ff'
  on-secondary-fixed: '#00174b'
  on-secondary-fixed-variant: '#003ea8'
  tertiary-fixed: '#ffdcc3'
  tertiary-fixed-dim: '#ffb77d'
  on-tertiary-fixed: '#2f1500'
  on-tertiary-fixed-variant: '#6e3900'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
  surface-bg: '#F8FAFC'
  surface-card: '#FFFFFF'
  card-border: '#E2E8F0'
  status-available-text: '#065F46'
  status-available-bg: '#D1FAE5'
  status-borrowed-text: '#92400E'
  status-borrowed-bg: '#FEF3C7'
  status-damaged-text: '#991B1B'
  status-damaged-bg: '#FEE2E2'
typography:
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.25rem
  gutter-desktop: 1.5rem
  margin: 1rem
  margin-tablet: 1.5rem
  margin-desktop: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system delivers an institutional, high-utility operational management interface for vocational education (SMKN 1 Depok). Inspired by Material Design 3 (Material You) fused with structured modern dashboard aesthetics, the interface balances institutional authority with tactile mobile-first ergonomics.

### Persona & Target Audience
Built for lab managers, department heads, teachers, and student inventory stewards navigating asset checkout, return, tracking, and incident logging across school laboratories and workshops.

### Emotional Response & Visual Grammar
- **Tactile Utility:** Solid, physical surface metaphors over ephemeral glassmorphism; decisive touches, high affordance, zero AI-slop or generic blur.
- **Operational Clarity:** High-contrast status feedback, disciplined Bento grid modules, and dense scannability designed for high-throughput daily physical equipment circulation.
- **Institutional Rigor:** Grounded by authoritative deep navy and technical slate blue, balanced with immediate contextual indicators for equipment conditions.

## Colors

The palette establishes strict functional delineation between administrative structure, dynamic interaction, and inventory condition:

- **Primary (`#0F243A` - Onedex Navy Blue):** Used for top app bars, primary navigational surfaces, authoritative display headers, and dark action triggers.
- **Secondary (`#2563EB` - Crisp Slate Blue):** Interactive touchpoints, active states, active tab indicators, and primary submit buttons.
- **Tertiary (`#D97706` - Warm Golden Amber):** Operational alerts, pending validation notices, asset barcode scanner accents, and caution highlights.
- **Neutral (`#64748B` - Slate Neutral):** Structural icons, secondary labels, and neutral metadata.

### Surface Hierarchy
- **Canvas Base:** `#F8FAFC` provides a neutral, glare-free working area that separates active content blocks.
- **Surface Elevation Containers:** Solid `#FFFFFF` cards bounded by a crisp 1px `#E2E8F0` border.

### Status Semantic Chips
- **Tersedia (Ready/In-Stock):** Text `#065F46` on `#D1FAE5`.
- **Sedang Dipinjam (Active Loan):** Text `#92400E` on `#FEF3C7`.
- **Rusak / Pemeliharaan (Damaged/Maintenance):** Text `#991B1B` on `#FEE2E2`.

## Typography

The typography leverages Plus Jakarta Sans across all roles to ensure geometric clarity and rapid numeric scannability for asset serial codes, room identifiers, and circulation dates.

- **Headlines:** Display tracking is kept tight (`-0.02em`) for punchy titles in Bento containers and mobile top sheets.
- **Data & Tables:** Tabular figures are enforced for inventory codes (`SKN1-LAB-XXXX`), serials, and timestamps.
- **Badges & Labels:** `label-sm` utilizes uppercase formatting with `0.04em` tracking for instant triage of asset states.

## Layout & Spacing

The circulation engine applies a tactile Bento Grid layout system adaptable from handheld barcode scanning workflows to desktop administrative workstations.

### Structural Grid
- **Mobile (<640px):** Single-column stacked stream, 4-column sub-grid for quick-action metrics. Margin: `1rem`, Gutters: `1rem`. Floating Action Bar (FAB) pinned for quick check-in/out.
- **Tablet (640px - 1024px):** 8-column layout. 2-column asymmetric Bento distribution separating real-time scan feed from equipment queue.
- **Desktop (>1024px):** 12-column fixed grid (max-width `1280px`). Asset filtering sidebar spans 3 columns; inventory matrix spans 9 columns.

### Density Rules
Every component is sized on a strict 4px/8px rhythm. Compact rows maintain a minimum 44px touch target on mobile to eliminate scanning and checkout input errors in workshop conditions.

## Elevation & Depth

This design system avoids blurry layered neumorphism and heavily tinted shadows, adhering to Material 3 tactile layering:

- **Level 0 (Canvas Base):** Flat `#F8FAFC`, non-elevated background.
- **Level 1 (Bento Cards & Containers):** Solid `#FFFFFF`, fortified with a 1px boundary of `#E2E8F0` and subtle directional ambient drop: `box-shadow: 0px 1px 3px rgba(15, 36, 58, 0.05), 0px 1px 2px rgba(15, 36, 58, 0.03)`.
- **Level 2 (Dropdown Menus & Persistent Bottom Sheets):** `#FFFFFF` surface elevated above backdrop using `box-shadow: 0px 4px 6px -1px rgba(15, 36, 58, 0.08), 0px 2px 4px -2px rgba(15, 36, 58, 0.05)`.
- **Level 3 (Modal Dialogs & Circulation Scan Overlays):** `box-shadow: 0px 12px 24px -4px rgba(15, 36, 58, 0.12), 0px 4px 8px -2px rgba(15, 36, 58, 0.06)`, overlayed on a `#0F243A` scrim at 40% opacity.

## Shapes

The roundedness language implements an Android M3 form factor centered on an authentic `rounded-2xl` (16px / `1rem`) foundation for content cards and containers:

- **Bento Modules & Master Cards:** `rounded-2xl` (`1rem` / 16px) for an ergonomic, friendly hand feel.
- **Buttons, Form Inputs, & Action Dialogs:** `rounded-xl` (`0.75rem` / 12px) balancing structural alignment with thumb-friendly curves.
- **Chips, Badges, & Avatar Pills:** Fully rounded / pill shape (`rounded-full` / `9999px`) to immediately denote non-interactive or filtered status tags.

## Components

### Buttons
- **Primary Action (Check-out / Save):** Background `#2563EB`, text `#FFFFFF`, height 44px, padding 0 20px, font `label-lg`, radius 12px. Active press state deepens to `#1D4ED8`.
- **Administrative Action:** Background `#0F243A`, text `#FFFFFF`, radius 12px.
- **Outlined / Secondary:** Background `#FFFFFF`, 1px solid border `#E2E8F0`, text `#0F243A`.

### Status Badges & Chips
- Formed with pill radius (`rounded-full`), height 24px, padding `0 10px`, typography `label-sm`.
- **Tersedia:** `#D1FAE5` background with `#065F46` label and a 6px dot indicator `#059669`.
- **Sedang Dipinjam:** `#FEF3C7` background with `#92400E` label and a 6px dot indicator `#D97706`.
- **Rusak:** `#FEE2E2` background with `#991B1B` label and a 6px dot indicator `#DC2626`.

### Inventory Cards & Bento Grid Cells
- Built with solid white background (`#FFFFFF`), 1px outline (`#E2E8F0`), and Level 1 elevation.
- Internal padding is locked to `space-md` (16px) or `space-lg` (24px) depending on grid density.
- Header holds asset name (`title-md`) and category tag; center holds serialized code in monospace/tabular styling; footer separates location, quantity counter, and current status chip.

### Input Fields & Barcode Readers
- Default: Height 48px, background `#FFFFFF`, border 1px solid `#CBD5E1`, text `#0F243A`, font `body-md`, radius 12px.
- Focused: 2px solid `#2563EB`, background `#FFFFFF`.
- Barcode/QR Trigger Input: Includes an embedded right-aligned tactile icon button with `#F1F5F9` background that initiates camera scanner overlay.

### Lists & Loan Logs
- Row items separated by a 1px border-bottom `#F1F5F9`.
- Each row contains borrower identity, room/lab designation, timestamp, and a contextual quick-action icon button (Return / Verify).

## Anti-AI Slop Directives & Engineering Guardrails

Dokumen ini melarang keras pembuatan antarmuka generik hasil halusinasi AI ("AI Slop"). Seluruh implementasi antarmuka wajib mengutamakan utilitas fisik, keterbacaan tinggi di kondisi lapangan (gudang/lapangan/lab), dan ergonomi jempol satu tangan pada perangkat Android.

### 1. Daftar Elemen Terlarang (Strictly Prohibited Anti-Patterns)
- **DILARANG Efek Kaca / Glassmorphism:** Jangan gunakan `backdrop-filter: blur()`, `Modifier.blur()`, kartu transparan berkabut, atau container semi-tembus pandang. Seluruh permukaan kartu wajib solid (`#FFFFFF`) di atas kanvas netral (`#F8FAFC`).
- **DILARANG Gradien Neon & Glow Melayang:** Hindari drop-shadow berwarna cerah (seperti *blue/purple outer glow*), gradien pelangi dekoratif, atau aksen futuristik fiksi ilmiah. Gunakan bayangan terarah natural Material 3 (`Level 1 elevation`).
- **DILARANG Widget Dekoratif Nirfaedah:** Jangan buat kartu metrik kosmetik tanpa data riil (misal: "Efisiensi 99.8%", grafik gelombang tanpa sumbu, atau ilustrasi 3D mengambang). Setiap komponen harus terikat langsung ke kolom database SQLite/Room (`stok_total`, `stok_tersedia`, `kondisi`, `batas_kembali`).
- **DILARANG Sudut Lengkung Inkonsisten:** Dilarang mengacak radius sudut kartu. Gunakan secara disiplin:
    - Kartu Bento / Kontainer Utama: `16dp` (`rounded-2xl`).
    - Input Teks, Tombol, dan Dialog: `12dp` (`rounded-xl`).
    - Badge Status / Filter Chips: `9999px` (`rounded-full`).
- **DILARANG Teks Lorem Ipsum & Data Fiktif Asing:** Seluruh label data wajib mencerminkan sarana prasarana nyata SMKN 1 Depok (misal: *Epson EB-X500*, *Kabel HDMI 15m*, *Bola Basket Molten GG7X*, *Sony Alpha a6400*, *Ruang Gudang Sarpras Rak A1*, *XII PPLG 1*).

### 2. Standar Utilitas Taktil & Ergonomi Fisik (Real-World Utility)
- **Batas Sentuh Minimum (Touch Targets):** Setiap target klik (tombol, checkbox, icon scanner, baris sirkulasi) wajib memiliki dimensi area sentuh minimal **48 x 48 dp** agar tidak memicu salah tekan saat digunakan oleh petugas di area gudang.
- **Zona Ergonomi Jempol (Thumb-Zone Priority):**
    - Tombol aksi primer (Check-out barang, scan QR, FAB tambah peminjaman) wajib diletakkan pada area 40% bawah layar agar mudah dijangkau satu tangan.
    - Form pencarian dan tombol filter kategori diletakkan di area atas yang mudah dipindai mata secara sekilas.
- **Kejelasan Batas Fisik (1px Crisp Border):** Seluruh kartu putih (`#FFFFFF`) wajib dipertegas dengan garis batas tepi 1px solid `#E2E8F0` (`BorderStroke(1.dp, CardBorder)`). Pemisahan konten tidak boleh hanya mengandalkan bayangan buram.
- **Tipografi Data Bertipe Tabular (Monospace Numeric):** Kode inventaris (`SKN1-SPR-XXXX`), nomor serial, NIS/NIP, dan format tanggal-jam transaksi wajib menggunakan font angka berjarak seragam (*tabular/monospace figures*) agar tidak bergeser saat data diperbarui.

### 3. Kontras Status & Hirarki Visual (WCAG AAA Compliance)
- **Status Chip Tidak Boleh Tipis:** Status barang tidak boleh hanya berupa titik kecil. Wajib berupa kapsul penuh (*pill badge*) dengan kontras teks tajam:
    - **Tersedia:** Latar `#D1FAE5` dengan teks `#065F46` dan indikator titik `#059669`.
    - **Sedang Dipinjam:** Latar `#FEF3C7` dengan teks `#92400E` dan indikator titik `#D97706`.
    - **Rusak / Butuh Servis:** Latar `#FEE2E2` dengan teks `#991B1B` dan indikator titik `#DC2626`.
- **Ketegasan Judul:** Warna teks utama adalah Dark Slate `#0B1C30` pada permukaan terang, menghasilkan rasio kontras > 7:1 (bebas dari teks abu-abu pudar yang sulit dibaca di bawah sinar matahari luar ruangan).

### 4. Aturan Implementasi Jetpack Compose (Kotlin Android)
```kotlin
// Contoh penerapan standar kartu anti-AI-slop di Jetpack Compose:
Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
        containerColor = Color(0xFFFFFFFF) // Solid White, NO opacity blur
    ),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)), // Batas tegas 1px
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // Elevasi tactile natural
) {
    // Konten fungsional terstruktur dengan padding 16.dp
}