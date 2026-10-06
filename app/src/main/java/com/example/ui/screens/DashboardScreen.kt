package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightGold
import com.example.ui.theme.ButtonGold
import com.example.ui.theme.CardWhite
import com.example.ui.theme.DarkForestGreen
import com.example.ui.theme.DeepCardGreen
import com.example.ui.theme.MintAccentGreen
import com.example.ui.theme.PlayfairDisplayFamily
import com.example.ui.theme.PrimaryGold
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SoftGoldBorder
import com.example.viewmodel.AcademyTab
import com.example.viewmodel.AcademyUiState

@Composable
fun DashboardScreen(
    uiState: AcademyUiState,
    onNavigateTab: (AcademyTab) -> Unit,
    onOpenAddTeacherDialog: () -> Unit,
    onOpenEnrollDialog: () -> Unit,
    onOpenAlertDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalStudents = uiState.students.size
    val totalFeeCollected = uiState.feeRecords.sumOf { it.amountPaidPkr }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_lazy_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Dark Green Welcome Card with 80dp Gold Border Logo (NO QURAN AYAT)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkForestGreen),
                border = BorderStroke(1.5.dp, PrimaryGold),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("welcome_hero_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlHadidLogoBadge(
                        size = 80.dp,
                        borderWidth = 3.dp,
                        showLabelInside = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Welcome to Al Hadid Academy",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PlayfairDisplayFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Nasirabad Jatlam, Azad Kashmir",
                        color = BrightGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Al Hadid Academy is a complete Islamic & Modern Education System. We provide Hifz-ul-Quran, Nazra, Tajweed, Tuition (Class 5-8), Computer Courses and full Fee & Attendance Management. Our mission is to provide quality Deeni & Dunyavi taleem under the supervision of Principal Awais Mustafa and Teacher Anas Mustafa.",
                        color = Color.White.copy(alpha = 0.92f),
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // 2. Second Card: "Assalam-o-Alaikum, Teacher Anas Mustafa" + DUAL ADMIN badge gold
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCardGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dual_admin_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Assalam-o-Alaikum,",
                                color = BrightGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            color = BrightGold,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("Teacher ")
                                    }
                                    withStyle(
                                        SpanStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("Anas ")
                                    }
                                    withStyle(
                                        SpanStyle(
                                            color = MintAccentGreen,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("Mustafa")
                                    }
                                },
                                fontSize = 23.sp
                            )
                        }

                        Surface(
                            color = PrimaryGold,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "DUAL ADMIN",
                                color = Color(0xFF1B1B1B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }

                    Text(
                        text = "Al Hadid Academy • Nasirabad Jatlan, Azad Kashmir",
                        color = BrightGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Full administrative control over Hifz, Nazra, Tajweed, Tuition & Financials.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // 3. Three Action Buttons: + Add Teacher, + Add Student, + Alert
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenAddTeacherDialog,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_add_teacher")
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+ Add Teacher",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = onOpenEnrollDialog,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_add_student")
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+ Add Student",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = onOpenAlertDialog,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonGold),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_add_alert")
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+ Alert",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }

        // 4. Stats Row: Total Students 0/100, Fee Collected Rs 0 (White cards 20dp radius + gold border 1dp)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Students Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTab(AcademyTab.STUDENTS) }
                        .testTag("stat_total_students_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Students",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDDF2F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = "Students",
                                    tint = Color(0xFF0277BD),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = "$totalStudents / 100",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreen
                        )

                        LinearProgressIndicator(
                            progress = { (totalStudents / 100f).coerceIn(0f, 1f) },
                            color = PrimaryGreen,
                            trackColor = Color(0xFFE6ECE8),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Text(
                            text = "Capacity target",
                            fontSize = 12.sp,
                            color = ButtonGold,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Fee Collected Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTab(AcademyTab.FEES) }
                        .testTag("stat_fee_collected_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fee Collected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDFF5E8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = "Fee Collected",
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = "Rs $totalFeeCollected",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreen
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "October cycle",
                            fontSize = 12.sp,
                            color = ButtonGold,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Optional Teachers / Alerts list if user added any
        if (uiState.teachers.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Faculty Members (${uiState.teachers.size})",
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold
                        )
                        uiState.teachers.forEach { t ->
                            Text(
                                text = "• ${t.fullName} — ${t.roleTitle} (${t.assignedGroup})",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // 5. Footer at bottom of Home Screen
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Developed by ANAS DEVELOPRS • Contact: anas.dev",
                    color = Color(0xFF757575),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("home_footer_developer_text")
                )
            }
        }
    }
}
