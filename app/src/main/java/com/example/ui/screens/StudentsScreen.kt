package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.StudentEntity
import com.example.ui.components.ACADEMY_GROUPS
import com.example.ui.theme.ButtonGold
import com.example.ui.theme.CardWhite
import com.example.ui.theme.PrimaryGold
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SoftGoldBorder
import com.example.ui.theme.SoftMintBg
import com.example.viewmodel.AcademyUiState

@Composable
fun StudentsScreen(
    uiState: AcademyUiState,
    onSearchQueryChange: (String) -> Unit,
    onDepartmentSelect: (String) -> Unit,
    onOpenEnrollDialog: () -> Unit,
    onDeleteStudent: (StudentEntity) -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToDashboard)

    val groups = listOf("All") + ACADEMY_GROUPS
    val filteredStudents = remember(
        uiState.students,
        uiState.selectedDepartment,
        uiState.searchQuery
    ) {
        uiState.students.filter { student ->
            val matchesDept = uiState.selectedDepartment == "All" ||
                student.department.equals(uiState.selectedDepartment, ignoreCase = true)
            val q = uiState.searchQuery.trim()
            val matchesSearch = q.isEmpty() ||
                student.fullName.contains(q, ignoreCase = true) ||
                student.rollNumber.contains(q, ignoreCase = true) ||
                student.fatherName.contains(q, ignoreCase = true)
            matchesDept && matchesSearch
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search Box with 20dp radius and gold border
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = "Search by name, ID (e.g. AHAD-001), father...",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PrimaryGreen
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedBorderColor = PrimaryGold,
                    unfocusedBorderColor = SoftGoldBorder
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_search_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Group Filter Buttons (Green selected, White + Gold border unselected)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(groups) { grp ->
                    val isSelected = uiState.selectedDepartment == grp
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) PrimaryGreen else CardWhite,
                        border = BorderStroke(1.dp, if (isSelected) PrimaryGreen else PrimaryGold),
                        modifier = Modifier
                            .clickable { onDepartmentSelect(grp) }
                            .testTag("dept_chip_$grp")
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = grp,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Enrolled Students (${filteredStudents.size})",
                color = PrimaryGreen,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredStudents.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .testTag("students_empty_state_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "No Students Enrolled Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Database is clean (0 students). Tap '+ Add Student' below to enroll students into Hifz, Nazra, Tajweed, or Tuition.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = onOpenEnrollDialog,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("+ Add First Student")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("students_lazy_column"),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredStudents,
                        key = { it.id }
                    ) { student ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            border = BorderStroke(1.dp, SoftGoldBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("student_card_${student.rollNumber}")
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Circular Initials Avatar with Gold Border
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(SoftMintBg)
                                            .border(BorderStroke(1.5.dp, PrimaryGold), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = student.fullName.take(2).uppercase(),
                                            color = PrimaryGreen,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = student.fullName,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFFFFFDF5),
                                                border = BorderStroke(1.dp, PrimaryGold)
                                            ) {
                                                Text(
                                                    text = student.rollNumber,
                                                    color = PrimaryGreen,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(
                                                        horizontal = 8.dp,
                                                        vertical = 3.dp
                                                    )
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(3.dp))

                                        Text(
                                            text = "S/o ${student.fatherName}  •  ${student.className}",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Spacer(modifier = Modifier.height(3.dp))

                                        Text(
                                            text = "Group: ${student.department} | Teacher: ${student.teacherName}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ButtonGold
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Monthly: Rs ${student.monthlyFeePkr}",
                                        color = PrimaryGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = "Performance",
                                            tint = PrimaryGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = { onDeleteStudent(student) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Student",
                                                tint = Color(0xFFD32F2F),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onOpenEnrollDialog,
            containerColor = PrimaryGreen,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp),
            icon = {
                Icon(imageVector = Icons.Default.GroupAdd, contentDescription = null)
            },
            text = {
                Text(text = "Add Student", fontWeight = FontWeight.Bold)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_add_student")
        )
    }
}
