package com.nusa.hadithapp.ui.settings.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.nusa.hadithapp.data.preferences.SettingsState
import com.nusa.hadithapp.ui.theme.AppAccentColor
import com.nusa.hadithapp.ui.theme.AppPalette
import com.nusa.hadithapp.ui.theme.AppThemeMode
import com.nusa.hadithapp.util.HadithTextColor
import com.nusa.hadithapp.util.formatHadithText
import com.nusa.hadithapp.util.getArabicFontFamily
import com.nusa.hadithapp.util.getIndonesianFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReminderSection(
    settings: SettingsState,
    onReminderEnabledChange: (Boolean) -> Unit,
    onReminderTimeChange: (hour: Int, minute: Int) -> Unit,
    onTestNotification: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showTimePickerDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onReminderEnabledChange(true)
            val success = onTestNotification()
            Toast.makeText(
                context,
                if (success) "Izin diberikan! Notifikasi pengingat dikirim ke bilah atas." else "Izin diberikan! Pengingat aktif.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Izin notifikasi ditolak. Harap izinkan notifikasi di setelan HP agar pengingat dapat muncul.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val requestPermissionAndRun: (() -> Unit) -> Unit = { action ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val status = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            if (status != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                action()
            }
        } else {
            action()
        }
    }

    val formattedTime = remember(settings.reminderHour, settings.reminderMinute) {
        String.format(java.util.Locale.US, "%02d:%02d WIB", settings.reminderHour, settings.reminderMinute)
    }

    SectionContainer(
        title = "Pengingat Hadits Harian",
        icon = Icons.Outlined.NotificationsActive,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Switch Aktifkan Pengingat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = "Aktifkan Notifikasi Harian",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Menerima petikan hadis pilihan dan mutiara ilmu setiap hari",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = settings.isDailyReminderEnabled,
                    onCheckedChange = { checked ->
                        if (checked) {
                            requestPermissionAndRun {
                                onReminderEnabledChange(true)
                                Toast.makeText(context, "Pengingat harian diaktifkan", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            onReminderEnabledChange(false)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            if (settings.isDailyReminderEnabled) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                // Jam Pengingat Saat Ini
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Waktu Pengingat",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formattedTime,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    OutlinedButton(
                        onClick = { showTimePickerDialog = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pilih Jam Bebas", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Rekomendasi Waktu Berkah Penuntut Ilmu
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Rekomendasi Waktu Berkah Penuntut Ilmu:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    val recommendations = listOf(
                        Triple(5, 30, "🌅 05:30 • Ba'da Subuh (Awal Hari Penuh Berkah)"),
                        Triple(9, 0, "☀️ 09:00 • Waktu Dhuha (Penyegar Hati)"),
                        Triple(16, 0, "🌇 16:00 • Ba'da Ashar (Pengingat Sore)"),
                        Triple(20, 0, "🌙 20:00 • Ba'da Isya (Kajian Malam)")
                    )

                    recommendations.forEach { (h, m, label) ->
                        val isSelected = (settings.reminderHour == h && settings.reminderMinute == m)
                        Surface(
                            onClick = {
                                requestPermissionAndRun {
                                    onReminderTimeChange(h, m)
                                    Toast.makeText(
                                        context,
                                        "Waktu disetel ke ${String.format(java.util.Locale.US, "%02d:%02d", h, m)} WIB",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Tombol Uji Tampil Notifikasi
                OutlinedButton(
                    onClick = {
                        requestPermissionAndRun {
                            val success = onTestNotification()
                            if (success) {
                                Toast.makeText(context, "Notifikasi pengingat dikirim ke bilah atas!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Notifikasi dinonaktifkan di setelan HP. Harap aktifkan izin notifikasi HadithApp.", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Uji Tampil Notifikasi Sekarang", fontSize = 12.sp)
                }
            }
        }
    }

    if (showTimePickerDialog) {
        val timePickerState = rememberTimePickerState(
            initialHour = settings.reminderHour,
            initialMinute = settings.reminderMinute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showTimePickerDialog = false
                        requestPermissionAndRun {
                            onReminderTimeChange(timePickerState.hour, timePickerState.minute)
                            Toast.makeText(
                                context,
                                "Waktu pengingat disetel ke ${String.format(java.util.Locale.US, "%02d:%02d", timePickerState.hour, timePickerState.minute)} WIB",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text("Batal")
                }
            },
            title = {
                Text("Atur Jam Pengingat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }
}

/**
 * 6. Bagian Penyimpanan & Cache Lokal
 */
