package com.nusa.hadithapp.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.ui.profile.components.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onNavigateToHadith: (bookSlug: String, number: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.restoreDataFromUri(
                context = context,
                uri = uri,
                onSuccess = { bm, notes, hist ->
                    Toast.makeText(
                        context,
                        "Pemulihan berhasil!\n$bm bookmark, $notes catatan, $hist riwayat bacaan dipulihkan.",
                        Toast.LENGTH_LONG
                    ).show()
                },
                onError = { errorMsg ->
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showReadingHistorySheet by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showClearCacheConfirmDialog by remember { mutableStateOf(false) }
    var showResetProfileConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ProfileTopBar(
                onBackClick = onBackClick,
                onHelpClick = { showHelpDialog = true },
                onEditProfileClick = { showEditProfileDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. KARTU IDENTITAS PENUNTUT ILMU
            ProfileHeaderCard(
                name = uiState.userProfile.name,
                roleTitle = uiState.userProfile.roleTitle
            )

            // 2. RINGKASAN AKTIVITAS KAJIAN NYATA (3 Pilar)
            ProfileSummarySection(
                bookmarkCount = uiState.bookmarkCount,
                noteCount = uiState.noteCount,
                totalReadHadithCount = uiState.totalReadHadithCount,
                distinctBooksCount = uiState.distinctBooksCount,
                onReadingHistoryClick = { showReadingHistorySheet = true }
            )

            // 2.5 PETA KONSISTENSI BELAJAR HADIS ALA GITHUB (Heatmap)
            ProfileHeatmapCard(
                heatmapWeeks = uiState.studyHeatmap,
                currentStreakDays = uiState.currentStreakDays,
                totalRecentReadCount = uiState.totalRecentReadCount
            )

            // 3. PUSAT CADANGAN & PEMULIHAN MANDIRI (100% Nyata & Local-First)
            ProfileBackupCard(
                onBackupClick = { viewModel.exportAllData(context) },
                onRestoreClick = {
                    try {
                        restoreLauncher.launch("application/json")
                    } catch (e: Exception) {
                        try {
                            restoreLauncher.launch("*/*")
                        } catch (e2: Exception) {
                            Toast.makeText(context, "Tidak dapat membuka pengelola berkas", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            // 4. KEAMANAN & PEMELIHARAAN LOKAL
            ProfileSecurityCard(
                isPinEnabled = uiState.userProfile.isPinEnabled,
                onPinClick = { showPinDialog = true },
                onPrivacyClick = { showPrivacyDialog = true },
                onClearCacheClick = { showClearCacheConfirmDialog = true }
            )

            // 5. ATUR ULANG PROFIL & FOOTER
            ProfileFooterSection(
                onResetProfileClick = { showResetProfileConfirmDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // ==================== DIALOGS & BOTTOM SHEETS ====================

    if (showReadingHistorySheet) {
        ReadingHistoryBottomSheet(
            booksProgress = uiState.booksProgress,
            totalReadCount = uiState.totalReadHadithCount,
            onNavigateToHadith = onNavigateToHadith,
            onDismiss = { showReadingHistorySheet = false }
        )
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentProfile = uiState.userProfile,
            onSave = { name, role ->
                viewModel.updateProfile(name, role)
                Toast.makeText(context, "Nama dan profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showEditProfileDialog = false }
        )
    }

    if (showPinDialog) {
        PinDialog(
            onSavePin = { pin ->
                viewModel.updatePin(pin)
                Toast.makeText(
                    context,
                    if (pin.isBlank()) "PIN keamanan dinonaktifkan" else "PIN keamanan aktif",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onDismiss = { showPinDialog = false }
        )
    }

    if (showPrivacyDialog) {
        PrivacyDialog(onDismiss = { showPrivacyDialog = false })
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Ruang Belajar Penuntut Ilmu", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Halaman ini adalah ruang kendali pribadi Anda untuk memantau progres membaca hadis dari 14 kitab muktabar, mengamankan catatan kajian, serta mencadangkan data secara mandiri ke format berkas JSON tanpa ketergantungan server pihak ketiga.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(onClick = { showHelpDialog = false }, shape = RoundedCornerShape(10.dp)) {
                    Text("Tutup")
                }
            }
        )
    }

    if (showClearCacheConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheConfirmDialog = false },
            title = { Text("Bersihkan Cache Hadis?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Ini akan menghapus hadis sementara yang tersimpan saat dibuka secara online untuk melegakan memori HP. Bookmark dan catatan kajian Anda TIDAK akan terhapus.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearCacheConfirmDialog = false
                        viewModel.clearCache {
                            Toast.makeText(context, "Cache hadis berhasil dibersihkan", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Bersihkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showResetProfileConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetProfileConfirmDialog = false },
            title = { Text("Atur Ulang Profil?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Nama profil dan tingkatan akan dikembalikan ke setelan bawaan (Thalibul Ilmi). Seluruh hadis tersimpan dan catatan kajian Anda tetap aman.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetProfileConfirmDialog = false
                        viewModel.resetProfile {
                            Toast.makeText(context, "Profil dikembalikan ke bawaan", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Atur Ulang")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetProfileConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
