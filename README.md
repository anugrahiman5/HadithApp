# 📖 HadithApp — Aplikasi Hadits Android

> Aplikasi Android untuk membaca, menelusuri, dan mempelajari hadits-hadits Nabi Muhammad ﷺ dari kitab-kitab hadits utama (Kutubussittah). Dibangun dengan Kotlin, Jetpack Compose, dan arsitektur modern.

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?logo=android" />
  <img src="https://img.shields.io/badge/Min_SDK-26_(Android_8.0)-blue" />
  <img src="https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose-orange" />
  <img src="https://img.shields.io/badge/Version-1.0-lightgrey" />
</p>

---

## ✨ Fitur Utama

### 📚 Perpustakaan Hadits Lengkap
- Membaca hadits dari **9 kitab utama** (Shahih Bukhari, Shahih Muslim, Sunan Abu Dawud, Sunan Tirmidzi, Sunan Nasai, Sunan Ibnu Majah, Musnad Ahmad, Muwatta Malik, Sunan Darimi) dan **5 kitab tambahan** (Sunan Daruquthni, Shahih Ibnu Khuzaimah, Shahih Ibnu Hibban, Al-Mustadrak Al-Hakim, Musnad Syafi'i)
- Navigasi antar hadits dengan **swipe gesture** (kanan/kiri)
- Lompat ke nomor hadits tertentu (Jump to Hadith)
- Penomoran hadits standar dan penomoran alternatif kitab penerbit

### 🔍 Pencarian Canggih
- Cari hadits berdasarkan **kata kunci Arab & terjemahan Indonesia**
- **Keyboard Arab virtual in-app** (tidak perlu install bahasa Arab di perangkat)
  - Hijaiyah lengkap + tanda baca + harakat
  - Tombol backspace, spasi, dan angka Arab
- Riwayat pencarian tersimpan

### 🌿 Pohon Sanad (Silsilah Perawi)
- Visualisasi pohon sanad bercabang secara grafis
- Warna perawi berdasarkan tingkat kualitas (`Tsiqah`, `Matruk`, `Kadzab`, dll.)
- Keterangan warna yang dinamis (hanya tampilkan level yang ada dalam sanad)
- Detail profil setiap perawi (kalangan, wafat, dll.)
- Klik nama perawi di teks terjemahan → tampil profil detail

### 💬 Asisten AI Syarah Hadits (Mode Ulama)
- Tanya penjelasan syarah, makna kosakata sulit, asbabul wurud, faedah fiqih, dan hikmah hadits
- Didukung **Google Gemini API** (gratis hingga 1.500 req/hari via Google AI Studio)
- Konsep **BYOK (Bring Your Own Key)** — kunci API disimpan lokal di perangkat, tanpa server developer
- Persona **"Mode Ulama"**: jawaban bersumber dari metodologi ahlussunnah (Fathul Bari, Syarah An-Nawawi, Tuhfatul Ahwadzi)
- **Guardrails**: menolak pertanyaan di luar topik keislaman/hadits dengan sopan
- Quick prompt: faedah utama, kosakata sulit, asbabul wurud, hukum fiqih, adab nabawi

### 🗂️ Klasifikasi Tematik
- Telusuri hadits berdasarkan **Tema** (iman, ibadah, akhlak, dll.)
- Telusuri berdasarkan **Kategori** tematis
- Hadits serupa/terkait

### 👤 Profil Perawi & Biografi Imam
- Biografi lengkap imam-imam hadits (Imam Bukhari, Muslim, dll.)
- Profil detail perawi: guru-murid, penilaian ulama rijal, komentar ulama
- Navigasi hadits per perawi

### 🔖 Bookmark & Catatan
- Simpan hadits favorit ke **folder bookmark** yang dapat dikustomisasi
- Tulis **catatan tadabbur** pribadi per hadits
- Riwayat bacaan otomatis

### 🎨 Tampilan yang Dapat Dikustomisasi
- **Tipografi Arab**: 6+ pilihan font (Uthmani Hafs, Scheherazade, Noto Naskh, Lateef, dll.)
- Ukuran font Arab & Indonesia yang dapat diatur
- Pengaturan harakat (tampil penuh / tersembunyi)
- Spasi baris teks Arab
- **Mode tema**: Terang, Gelap, dan Hangat
- **Warna aksen**: Teal, Amber, Green, dll.

### 🃏 Kartu Kutipan & Berbagi
- Generate **kartu gambar** hadits (Quote Card) untuk dibagikan
- Bagikan teks lengkap hadits (Arab + terjemahan + sumber)

### 🔔 Pengingat Hadits Harian
- Atur waktu pengingat harian untuk membaca hadits

### 🎙️ Text-to-Speech (TTS)
- Dengarkan bacaan teks Arab (bahasa Arab)
- Dengarkan terjemahan Indonesia

### 🧠 Ensiklopedi Musthalah Hadits
- Pelajari istilah-istilah ilmu hadits (`Shahih`, `Hasan`, `Dhaif`, `Mutawatir`, dll.)
- Quiz interaktif musthalah
- Navigasi ke detail hadits dari keterangan istilah

### 📊 Perbandingan Hadits
- Bandingkan redaksi hadits yang sama dari berbagai kitab secara berdampingan

---

## 🏗️ Arsitektur & Teknologi

Proyek ini menerapkan **Clean Architecture** dengan pola MVVM:

```
app/
├── data/
│   ├── data_source/
│   │   ├── local/          # Room Database (DAO, Entity)
│   │   └── remote/         # Ktor HTTP Client (HadithApiService, GeminiApiService)
│   ├── model/              # DTO (Data Transfer Object)
│   ├── mapper/             # Konversi DTO → Domain Model
│   ├── preferences/        # DataStore Preferences
│   ├── repository/         # Implementasi repository
│   └── session/            # Session Manager (pencarian, perawi)
├── domain/
│   ├── model/              # Domain model (Hadith, SanadPerawi, dll.)
│   ├── repository/         # Interface repository
│   └── use_case/           # Use case bisnis
└── ui/
    ├── home/               # Halaman beranda
    ├── detail/             # Detail hadits (HadithDetailScreen)
    ├── search/             # Pencarian + Keyboard Arab Virtual
    ├── explore/            # Jelajah kitab
    ├── saved/              # Bookmark & catatan
    ├── biography/          # Biografi imam
    ├── musthalah/          # Ensiklopedi musthalah
    ├── compare/            # Perbandingan hadits
    ├── classification/     # Tema & kategori
    ├── settings/           # Pengaturan & AI
    ├── profile/            # Profil pengguna & statistik
    └── navigation/         # NavGraph (Compose Navigation)
```

---

## 📦 Tech Stack

| Kategori | Library / Tool |
|---|---|
| **Bahasa** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **DI** | Dagger Hilt |
| **Database** | Room (SQLite) |
| **Network** | Ktor Client (CIO Engine) |
| **Serialisasi** | Kotlinx Serialization |
| **Preferensi** | AndroidX DataStore Preferences |
| **Navigasi** | Compose Navigation |
| **Font** | Bundled TTF (Uthmani Hafs, Scheherazade, Noto Naskh, Lateef, dll.) |
| **AI** | Google Gemini API (`gemini-1.5-flash` / `gemini-2.0-flash`) |
| **Build** | Gradle KTS + AGP |

---

## 🌐 Sumber Data

### API Hadits
Seluruh data hadits (teks Arab, terjemahan, sanad, derajat) diambil dari:

- **API**: `https://api.haditssoft.workers.dev`  
  *(Cloudflare Workers endpoint dari proyek [haditssoft](https://github.com/haditssoft/haditssoft-api))*


### Data Kitab (Legacy / Referensi)
File JSON mentah untuk referensi lokal tersedia di folder `legacy_assets/`:

| File | Kitab |
|---|---|
| `bukhari.json` | Shahih Al-Bukhari |
| `muslim.json` | Shahih Muslim |
| `abu-dawud.json` | Sunan Abu Dawud |
| `tirmidzi.json` | Sunan At-Tirmidzi |
| `nasai.json` | Sunan An-Nasai |
| `ibnu-majah.json` | Sunan Ibnu Majah |
| `ahmad.json` | Musnad Ahmad |
| `malik.json` | Muwatta Malik |
| `darimi.json` | Sunan Ad-Darimi |

### Font Aksara Arab
Font-font Arab yang digunakan (bundled dalam APK):

| Font | Sumber |
|---|---|
| **Uthmani Hafs** | Kompleks Percetakan Al-Qur'an Raja Fahd |
| **Scheherazade New** | SIL International (OFL) |
| **Noto Naskh Arabic** | Google Fonts (Apache 2.0) |
| **Lateef** | SIL International (OFL) |
| **Amiri Naskh** | Google Fonts (OFL) |

### AI (Opsional)
Fitur Asisten AI menggunakan **Google Gemini API** yang dikelola langsung oleh pengguna:
- Daftar & ambil kunci API gratis di: [Google AI Studio](https://aistudio.google.com/app/apikey)
- Kuota gratis: **1.500 request/hari, 15 RPM** (per pengguna)
- Kunci API disimpan **lokal di perangkat** — tidak dikirim ke server manapun selain Google

---

## 🚀 Cara Menjalankan

### Prasyarat
- Android Studio Meerkat atau lebih baru
- JDK 17+
- Android SDK 37 (API Level 37)
- Perangkat/Emulator dengan Android 8.0+ (API 26+)

### Clone & Build

```bash
git clone https://github.com/<username>/HadithApp.git
cd HadithApp
```

Buka proyek dengan Android Studio, tunggu sync Gradle selesai, lalu jalankan ke perangkat/emulator.

> **Catatan**: Proyek menggunakan `--offline` Gradle cache. Jika pertama kali clone dan cache belum ada, jalankan tanpa flag `--offline` agar dependency dapat diunduh.

### Setup Fitur AI (Opsional)
1. Buka menu **Pengaturan** di aplikasi
2. Gulir ke bagian **Asisten AI Hadits**
3. Masukkan kunci Gemini API dari [Google AI Studio](https://aistudio.google.com/app/apikey)
4. Pilih model (`gemini-1.5-flash` direkomendasikan untuk gratis)

---

## 📁 Struktur Proyek Singkat

```
HadithApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/nusa/hadithapp/
│   │   │   ├── data/            # Data layer
│   │   │   ├── domain/          # Domain layer
│   │   │   ├── ui/              # UI layer (Compose screens)
│   │   │   ├── util/            # Utilities & helpers
│   │   │   └── di/              # Hilt modules
│   │   └── res/
│   │       ├── font/            # Font Arab & Latin
│   │       └── ...
│   └── build.gradle.kts
├── legacy_assets/               # JSON hadits mentah (referensi)
├── gradle/libs.versions.toml    # Version catalog
└── README.md
```

---

## 📜 Lisensi & Kredit

Proyek ini dibuat untuk keperluan pribadi dan edukasi. Data hadits dan terjemahan merupakan warisan keilmuan Islam yang bersifat terbuka.

- Konten hadits: domain publik (teks klasik)
- Font: masing-masing memiliki lisensi OFL / Apache 2.0 (lihat keterangan di atas)
- Kode aplikasi: silakan gunakan sebagai referensi belajar

---

> *"Barangsiapa menempuh suatu jalan untuk mencari ilmu, Allah akan mudahkan baginya jalan menuju surga."*  
> — HR. Muslim no. 2699
