package com.nusa.hadithapp.ui.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val KALANGAN_OF_RAWI = listOf(
    "Semua Kalangan" to "",
    "Shahabat" to "Shahabat",
    "Shahabiyah" to "Shahabiyah",
    "Tabi'ut Tabi'in Kalangan Biasa" to "Tabi'ut Tabi'in Kalangan Biasa",
    "Tabi'ut Tabi'in Kalangan Tua" to "Tabi'ut Tabi'in Kalangan Tua",
    "Tabi'in Kalangan Pertengahan" to "Tabi'in Kalangan Pertengahan",
    "Tabi'in Kalangan Biasa" to "Tabi'in Kalangan Biasa",
    "Tabi'in (Tdk Jumpa Shahabat)" to "Tabi'in (Tdk Jumpa Shahabat)",
    "Tabi'ut Tabi'in Kalangan Pertengahan" to "Tabi'ut Tabi'in Kalangan Pertengahan",
    "Tabi'in Kalangan Tua" to "Tabi'in Kalangan Tua",
    "Tabi'ul Atba' Kalangan Tua" to "Tabi'ul Atba' Kalangan Tua",
    "Tabi'ul Atba' Kalangan Pertengahan" to "Tabi'ul Atba' Kalangan Pertengahan",
    "Tabi'ul Atba' Kalangan Biasa" to "Tabi'ul Atba' Kalangan Biasa"
)

val LEVEL_OF_RAWI = listOf(
    "Semua Level" to "",
    "1. Shahabat" to "1. ",
    "2. Tsiqah Tsiqah / Tsiqah Hafidh" to "2. ",
    "3. Tsiqah 'Adil" to "3. ",
    "4. Shaduq La Ba'sa Bih" to "4. ",
    "5. Buruk Hafalannya" to "5. ",
    "6. Maqbul" to "6. ",
    "7. Majhul Hal" to "7. ",
    "8. Dha'if" to "8. ",
    "9. Tidak Dipercaya / Majhul" to "9. ",
    "10. Matruk" to "10. ",
    "11. Tertuduh Berdusta" to "11. ",
    "12. Kadzab (Pendusta)" to "12. "
)

@Composable
fun RawiFilterPanel(
    kuniyahValue: String,
    onKuniyahChange: (String) -> Unit,
    classifyValue: String,
    onClassifyChange: (String) -> Unit,
    levelValue: String,
    onLevelChange: (String) -> Unit,
    onApplyFilter: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    var classifyDropdownExpanded by remember { mutableStateOf(false) }
    var levelDropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Filter Khusus Perawi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                TextButton(onClick = onReset) {
                    Text("Reset", color = MaterialTheme.colorScheme.error)
                }
            }

            AnimatedVisibility(visible =  isExpanded) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Input Kunyah
                    OutlinedTextField(
                        value = kuniyahValue,
                        onValueChange = onKuniyahChange,
                        label = { Text("KUNIYAH / GELAR PANGGILAN") },
                        placeholder = { Text("Contoh: Abu Bakr, Abu Hurairah") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    //2. Dropdown Kalangan
                    Column {
                        val displayClassifyText = KALANGAN_OF_RAWI.find { it.second == classifyValue }?.first ?: "Semua Kalangan"

                        OutlinedTextField(
                            value = displayClassifyText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("KALANGAN PERAWI (THABAQAH)") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { classifyDropdownExpanded = true }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { classifyDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp)
                        )

                        DropdownMenu(
                            expanded = classifyDropdownExpanded,
                            onDismissRequest = { classifyDropdownExpanded = false }
                        ) {
                            KALANGAN_OF_RAWI.forEach { (label, value) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        onClassifyChange(value)
                                        classifyDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    //3. Dropdown Level Jarh
                    Column {
                        val displayLevelText = LEVEL_OF_RAWI.find { it.second == levelValue }?.first ?: "Semua Level"

                        OutlinedTextField(
                            value = displayLevelText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("TINGKAT JARH WA TA'DIL") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { levelDropdownExpanded = true }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { levelDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp)
                        )

                        DropdownMenu(
                            expanded = levelDropdownExpanded,
                            onDismissRequest = { levelDropdownExpanded = false }
                        ) {
                            LEVEL_OF_RAWI.forEach { (label, value) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        onLevelChange(value)
                                        levelDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // 4. Tombol Terapkan Filter
                    Button(
                        onClick = onApplyFilter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text(
                            text = "Terapkan Filter",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            }
        }
    }
}