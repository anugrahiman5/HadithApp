package com.nusa.hadithapp.ui.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFolderBottomSheet(
    hadithTitle: String,
    availableFolders: List<String>,
    currentFolders: List<String>, // 🟢 Daftar folder yang sudah dicentang
    isAlreadyBookmarked: Boolean,
    onSaveToFolders: (List<String>) -> Unit,
    onRemoveBookmark: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Menyimpan kumpulan folder yang dicentang pengguna (tanpa "Semua Bookmark")
    val selectedFolders = remember(currentFolders) {
        mutableStateListOf<String>().apply {
            addAll(currentFolders.filter { it != "Semua Bookmark" && it.isNotBlank() })
        }
    }

    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var folderList by remember(availableFolders) {
        mutableStateOf(availableFolders.filter { it != "Semua Bookmark" && it.isNotBlank() })
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Simpan Hadits ke Koleksi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Pilih koleksi/folder (opsional). Otomatis masuk ke Semua Bookmark.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Daftar Folder Pilihan dengan CHECKBOX
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(folderList) { folderName ->
                    val isChecked = selectedFolders.contains(folderName)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) {
                                    selectedFolders.remove(folderName)
                                } else {
                                    selectedFolders.add(folderName)
                                }
                            }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) selectedFolders.add(folderName)
                                else selectedFolders.remove(folderName)
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = folderName,
                            fontSize = 14.sp,
                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                }
            }

            // Tombol Tambah Folder Baru
            TextButton(
                onClick = { showCreateFolderDialog = true },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Buat Koleksi Baru...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol Aksi Bawah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isAlreadyBookmarked) {
                    OutlinedButton(
                        onClick = {
                            onRemoveBookmark()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hapus", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        onSaveToFolders(selectedFolders.toList())
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(if (isAlreadyBookmarked) 1.5f else 1f)
                ) {
                    Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    val buttonText = if (selectedFolders.isEmpty()) "Simpan Bookmark" else "Simpan (${selectedFolders.size} Koleksi)"
                    Text(
                        text = buttonText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialog Tambah Folder Baru
    if (showCreateFolderDialog) {
        var inputFolderName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateFolderDialog = false },
            title = { Text("Buat Koleksi Baru", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputFolderName,
                    onValueChange = { inputFolderName = it },
                    placeholder = { Text("Contoh: Hafalan Arbain") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clean = inputFolderName.trim()
                        if (clean.isNotBlank() && clean != "Semua Bookmark") {
                            if (!folderList.contains(clean)) {
                                folderList = folderList + clean
                            }
                            if (!selectedFolders.contains(clean)) {
                                selectedFolders.add(clean)
                            }
                        }
                        showCreateFolderDialog = false
                    }
                ) {
                    Text("Tambah")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateFolderDialog = false }) { Text("Batal") }
            }
        )
    }
}