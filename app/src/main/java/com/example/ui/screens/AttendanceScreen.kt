package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.StudentEntity
import com.example.ui.theme.BrightActionGreen
import com.example.ui.theme.CardWhite
import com.example.ui.theme.PrimaryGold
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SoftGoldBorder
import com.example.ui.theme.SoftMintBg
import com.example.ui.theme.StatusAbsentBg
import com.example.ui.theme.StatusAbsentText
import com.example.ui.theme.StatusLeaveBg
import com.example.ui.theme.StatusLeaveText
import com.example.ui.theme.StatusPresentBg
import com.example.ui.theme.StatusPresentText
import com.example.viewmodel.AcademyUiState

@Composable
fun AttendanceScreen(
    uiState: AcademyUiState,
    todayDate: String,
    onSelectDate: (String) -> Unit,
    onMarkAttendance: (StudentEntity, String) -> Unit,
    onMarkAllPresent: () -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToDashboard)

    val context = LocalContext.current
    var localSearch by remember { mutableStateOf("") }

    val recordsForDate = remember(uiState.attendanceRecords, uiState.selectedDate) {
        uiState.attendanceRecords
            .filter { it.date == uiState.selectedDate }
            .associateBy { it.studentId }
    }

    val totalStudents = uiState.students.size
    val presentCount = recordsForDate.values.count { it.status == "Present" }
    val absentCount = recordsForDate.values.count { it.status == "Absent" }
    val leaveCount = recordsForDate.values.count { it.status == "Leave" }

    val filteredStudents = remember(uiState.students, localSearch) {
        uiState.students.filter {
            localSearch.isBlank() ||
                it.fullName.contains(localSearch, ignoreCase = true) ||
                it.rollNumber.contains(localSearch, ignoreCase = true)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("attendance_lazy_column"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 92.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Attendance Summary Card (matches Screenshot 4)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Attendance Summary",
                                color = PrimaryGreen,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = StatusPresentBg,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Instant Auto-Save ON ✓",
                                    color = StatusPresentText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SummaryStatBox(
                                label = "Total",
                                count = totalStudents,
                                bgColor = Color(0xFFFFFBF0),
                                textColor = Color(0xFF1F2937),
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Present",
                                count = presentCount,
                                bgColor = StatusPresentBg,
                                textColor = StatusPresentText,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Absent",
                                count = absentCount,
                                bgColor = StatusAbsentBg,
                                textColor = StatusAbsentText,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Leave",
                                count = leaveCount,
                                bgColor = StatusLeaveBg,
                                textColor = StatusLeaveText,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Date & Class/Group Selector Boxes
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Date", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = uiState.selectedDate.ifEmpty { todayDate },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Class / Group", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = "Hifz & All",
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Search Bar
            item {
                OutlinedTextField(
                    value = localSearch,
                    onValueChange = { localSearch = it },
                    placeholder = { Text("Search by student name or roll #...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = PrimaryGreen
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = CardWhite,
                        focusedBorderColor = PrimaryGold,
                        unfocusedBorderColor = SoftGoldBorder
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 4. Filter: All Students + Mark All Present Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CardWhite,
                        border = BorderStroke(1.dp, PrimaryGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Filter: All Students",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = onMarkAllPresent,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("mark_all_present_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mark All Present", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            if (filteredStudents.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("attendance_empty_state_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(26.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Zero Attendance Records Today",
                                style = MaterialTheme.typography.titleMedium,
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Enroll students first to mark daily Present, Absent, or Leave attendance.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(
                    items = filteredStudents,
                    key = { "att_${it.id}" }
                ) { student ->
                    val status = recordsForDate[student.id]?.status ?: "Present"
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(SoftMintBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = student.fullName.take(1).uppercase(),
                                            color = PrimaryGreen,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = student.fullName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "S/o ${student.fatherName} • ID: ${student.rollNumber}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Surface(
                                    color = StatusPresentBg,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = status.uppercase(),
                                        color = StatusPresentText,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Present Button
                                Button(
                                    onClick = { onMarkAttendance(student, "Present") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BrightActionGreen
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Present", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                // Absent Button
                                Button(
                                    onClick = { onMarkAttendance(student, "Absent") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StatusAbsentBg,
                                        contentColor = StatusAbsentText
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Absent", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                // Leave Button
                                Button(
                                    onClick = { onMarkAttendance(student, "Leave") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StatusLeaveBg,
                                        contentColor = StatusLeaveText
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Leave", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Fixed "Generate Attendance Report PDF" Button
        Button(
            onClick = {
                val reportText = buildString {
                    appendLine("Al Hadid Academy - Nasirabad Jatlan, Azad Kashmir")
                    appendLine("Attendance Report (${uiState.selectedDate})")
                    appendLine("Total: $totalStudents | Present: $presentCount | Absent: $absentCount | Leave: $leaveCount")
                }
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, reportText)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Generate Attendance Report"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(54.dp)
                .testTag("generate_attendance_pdf_button")
        ) {
            AlHadidLogoBadge(size = 26.dp, borderWidth = 1.dp, showLabelInside = false)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Generate Attendance Report PDF",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun SummaryStatBox(
    label: String,
    count: Int,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = label, fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Medium)
            Text(text = "$count", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.ExtraBold)
        }
    }
}
