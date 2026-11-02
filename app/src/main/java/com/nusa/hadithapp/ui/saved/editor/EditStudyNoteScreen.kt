package com.nusa.hadithapp.ui.saved.editor

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.slideIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.ReferencedHadith
import com.nusa.hadithapp.ui.saved.editor.components.AttachHadithBottomSheet
import com.nusa.hadithapp.ui.saved.editor.components.ReferencedHadithCard
import com.nusa.hadithapp.util.extractArabicMatan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudyNoteScreen(
    viewModel: StudyNoteEditorViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val bookmarks by viewModel.bookmarksFlow.collectAsState()
    val readingHistory by viewModel.readingHistoryFlow.collectAsState()
    val lastRead by viewModel.lastReadFlow.collectAsState()
    val quickSearchResult by viewModel.quickSearchResult.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val canRedo by viewModel.canRedo.collectAsState()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showAddTagDialog by remember { mutableStateOf(false) }
    var binderMenuExpanded by remember { mutableStateOf(false) }
    var showAttachHadithSheet by remember { mutableStateOf(false) }
    var targetHadithToSwap by remember { mutableStateOf<ReferencedHadith?>(null) }
    var showNoteMenu by remember { mutableStateOf(false) }
    var showArabicSymbolMenu by remember { mutableStateOf(false) }

    val availableBinders =
        listOf("Syarah Fathul Bari", "Kaidah Musthalah", "Tafsir Ahkam & Fiqih", "Materi Khutbah")

    // Hitung jumlah kata
    val wordCount = remember(state.content) {
        state.content.trim().split("\\s+".toRegex()).count { it.isNotBlank() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Edit Catatan Kajian",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2E7D32))
                            )
                            Text(
                                text = "Tersimpan otomatis ke draft lokal",
                                fontSize = 11.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(0,10,0,0),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                },
                actions = {
                    // Tombol Simpan Hijau
                    Button(
                        onClick = { viewModel.saveStudyNote(onSuccess = onBackClick) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Simpan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Box {
                        IconButton(onClick = { showNoteMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showNoteMenu,
                            onDismissRequest = { showNoteMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Bagikan Catatan") },
                                leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                                onClick = {
                                    showNoteMenu = false
                                    val shareBody = buildString {
                                        if (state.title.isNotBlank()) {
                                            appendLine(state.title)
                                            appendLine()
                                        }
                                        appendLine(state.content)
                                        if (state.references.isNotEmpty()) {
                                            appendLine()
                                            appendLine("--- Referensi Hadits ---")
                                            state.references.forEach {
                                                appendLine("${it.bookName} No. ${it.number}")
                                            }
                                        }
                                    }
                                    val intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, shareBody)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Bagikan Catatan"))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Salin Semua Teks") },
                                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                                onClick = {
                                    showNoteMenu = false
                                    clipboardManager.setText(AnnotatedString(state.content))
                                    Toast.makeText(context, "Teks catatan berhasil disalin", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (state.isPinned) "Lepas Sematan" else "Sematkan Catatan") },
                                leadingIcon = {
                                    Icon(
                                        if (state.isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showNoteMenu = false
                                    viewModel.togglePinned()
                                    Toast.makeText(
                                        context,
                                        if (!state.isPinned) "Catatan disematkan" else "Sematkan catatan dilepas",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Bersihkan Teks", color = MaterialTheme.colorScheme.error) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.DeleteOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    showNoteMenu = false
                                    viewModel.clearContent()
                                    Toast.makeText(context, "Teks catatan dibersihkan", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Sticky Toolbar Format di atas Keyboard
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.undo() },
                            enabled = canUndo,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Undo,
                                contentDescription = "Undo",
                                modifier = Modifier.size(18.dp),
                                tint = if (canUndo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                        IconButton(
                            onClick = { viewModel.redo() },
                            enabled = canRedo,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Redo,
                                contentDescription = "Redo",
                                modifier = Modifier.size(18.dp),
                                tint = if (canRedo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }

                    VerticalDivider(
                        modifier = Modifier
                            .height(20.dp)
                            .padding(horizontal = 4.dp)
                    )

                    TextButton(
                        onClick = { viewModel.updateContent(state.content + "\n### ") },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Text("H", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    TextButton(
                        onClick = { viewModel.updateContent(state.content + "**teks**") },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Text("B", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    TextButton(
                        onClick = { viewModel.updateContent(state.content + "*teks*") },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Text(
                            "I",
                            fontStyle = FontStyle.Italic,
                            fontSize = 15.sp
                        )
                    }
                    TextButton(
                        onClick = { viewModel.updateContent(state.content + "\n- ") },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Text("•=", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    TextButton(
                        onClick = { viewModel.updateContent(state.content + "\n> ") },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Text("❝", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Box {
                        TextButton(
                            onClick = { showArabicSymbolMenu = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("ع", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        DropdownMenu(
                            expanded = showArabicSymbolMenu,
                            onDismissRequest = { showArabicSymbolMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("ﷺ (Shallallahu 'Alaihi Wasallam)") },
                                onClick = {
                                    showArabicSymbolMenu = false
                                    viewModel.insertText("ﷺ")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("رضي الله عنه (Radhiyallahu 'Anhu)") },
                                onClick = {
                                    showArabicSymbolMenu = false
                                    viewModel.insertText("رضي الله عنه")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("عليه السلام ('Alaihissalam)") },
                                onClick = {
                                    showArabicSymbolMenu = false
                                    viewModel.insertText("عليه السلام")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("رحمه الله (Rahimahullah)") },
                                onClick = {
                                    showArabicSymbolMenu = false
                                    viewModel.insertText("رحمه الله")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("سبحانه وتعالى (Subhanahu wa Ta'ala)") },
                                onClick = {
                                    showArabicSymbolMenu = false
                                    viewModel.insertText("سبحانه وتعالى")
                                }
                            )
                        }
                    }

                    //Tombol Hijau + Hadits
                    Button(
                        onClick = { showAttachHadithSheet = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "📖 + Hadits",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. KONTROL META (Binder Dropdown + Pin Switch)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown Binder Koleksi
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.clickable { binderMenuExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "📁 ${state.categoryBinder}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = binderMenuExpanded,
                        onDismissRequest = { binderMenuExpanded = false }
                    ) {
                        availableBinders.forEach { binder ->
                            DropdownMenuItem(
                                text = { Text(binder) },
                                onClick = {
                                    viewModel.updateCategoryBinder(binder)
                                    binderMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Switch Pin / Sematkan
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (state.isPinned) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                        0.4f
                    ),
                    modifier = Modifier.clickable { viewModel.togglePinned() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (state.isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (state.isPinned) "Tersemat" else "Sematkan",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 2. CHIP TAGAR (#Hashtag)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.tags) { tag ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "#$tag",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { viewModel.removeTag(tag) },
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        color = Color.Transparent,
                        modifier = Modifier.clickable { showAddTagDialog = true }
                    ) {
                        Text(
                            text = "+ Tambah Tag",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 3. JUDUL KAJIAN (Besar & Tebal)
            BasicTextField(
                value = state.title,
                onValueChange = { viewModel.updateTitle(it) },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 28.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (state.title.isEmpty()) {
                        Text(
                            "Judul Catatan Kajian...",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "🕒 Terakhir disunting: Hari ini • $wordCount kata",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 4. DAFTAR KARTU HADITS RUJUKAN UTAMA TERSEMAT
            state.references.forEachIndexed { index, referencedHadith ->
                ReferencedHadithCard(
                    ref = referencedHadith,
                    isPrimary = index == 0,
                    onSwapClick = { showAttachHadithSheet = true },
                    onDeleteClick = { viewModel.removeReferencedHadith(referencedHadith) }
                )
            }

            //5. TOMBOL PUTUS-PUTUS: SEMATKAN HADITS LAIN / TAKHRIJ
            Surface(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAttachHadithSheet = true }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.references.isEmpty()) "+ Sematkan Hadis Rujukan Utama" else "+ Sematkan Hadis Lain / Bandingkan Takhrij",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        // 6. TEXT AREA UTAMA (ISI KAJIAN)
        BasicTextField(
            value = state.content,
            onValueChange = { viewModel.updateContent(it) },
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                if (state.content.isEmpty()) {
                    Text(
                        "Tulis uraian faidah, analisis lughawi, intisari fiqih, atau faidah dari para ulama...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline,
                        lineHeight = 22.sp
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 350.dp)
        )
    }
}


    // Dialog Tambah Tagar
    if (showAddTagDialog) {
        var inputTag by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddTagDialog = false },
            title = { Text("Tambah Tagar") },
            text = {
                OutlinedTextField(
                    value = inputTag,
                    onValueChange = { inputTag = it },
                    placeholder = { Text("Contoh: Bab Niat") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addTag(inputTag)
                    showAddTagDialog = false
                }) {
                    Text("Tambah", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTagDialog = false }) { Text("Batal") }
            }
        )
    }

    //BOTTOM SHEET SEMATKAN HADIS
    if (showAttachHadithSheet) {
        AttachHadithBottomSheet(
            bookmarks = bookmarks,
            readingHistory = readingHistory,
            lastRead = lastRead,
            quickSearchResult = quickSearchResult,
            targetHadithToSwap = targetHadithToSwap,
            onSearchHadith = { slug, num ->
                viewModel.lookupHadith(slug, num)
            },
            onHadithConfirmed = { chosenHadith ->
                val target = targetHadithToSwap
                if (target != null) {
                    viewModel.swapReferencedHadith(target, chosenHadith)
                } else {
                    viewModel.addReferencedHadith(chosenHadith)
                }
            },
            onDismiss = {
                showAttachHadithSheet = false
                targetHadithToSwap = null
            }
        )
    }
}