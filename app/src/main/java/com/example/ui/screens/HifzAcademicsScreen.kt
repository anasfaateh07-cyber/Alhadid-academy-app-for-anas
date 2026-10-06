package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.StudentEntity
import com.example.ui.theme.BrightActionGreen
import com.example.ui.theme.ButtonGold
import com.example.ui.theme.CardWhite
import com.example.ui.theme.PlayfairDisplayFamily
import com.example.ui.theme.PrimaryGold
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SoftGoldBorder
import com.example.ui.theme.SoftMintBg
import com.example.ui.theme.StatusLeaveBg
import com.example.ui.theme.StatusLeaveText
import com.example.ui.theme.StatusPakkaBg
import com.example.ui.theme.StatusPakkaText
import com.example.ui.theme.StatusPresentBg
import com.example.ui.theme.StatusPresentText
import com.example.viewmodel.AcademyUiState

@Composable
fun QuranScreen(
    uiState: AcademyUiState,
    onUpdateQuranSabaq: (StudentEntity, String, String, String, String) -> Unit,
    onOpenEnrollDialog: () -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToDashboard)

    var selectedGroup by remember { mutableStateOf("Hifz Group") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("quran_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Quran Sabaq & Hifz Record",
                        fontFamily = PlayfairDisplayFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "Real-time Sabaq evaluation, audio recording & auto-save",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Surface(
                    color = StatusPresentBg,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Auto\n● Save\nActive",
                        color = StatusPresentText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select Class:",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Hifz Group", "Nazra").forEach { grp ->
                        val isSel = selectedGroup == grp
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSel) PrimaryGreen else CardWhite,
                            border = BorderStroke(1.dp, if (isSel) PrimaryGreen else PrimaryGold),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedGroup = grp }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
                                Text(
                                    text = grp,
                                    color = if (isSel) Color.White else Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        if (uiState.students.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "No Quran Sabaq Records Yet",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Enroll a student first to evaluate daily Sabaq (Yaad, Kacha, Pakka), Sabaqi, and Manzil.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onOpenEnrollDialog,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("+ Add Student")
                        }
                    }
                }
            }
        } else {
            items(
                items = uiState.students,
                key = { "quran_${it.id}" }
            ) { student ->
                var sabaqText by remember(student.todaySabaq) { mutableStateOf(student.todaySabaq) }
                var status by remember(student.sabaqStatus) { mutableStateOf(student.sabaqStatus) }
                var manzil by remember(student.manzilText) { mutableStateOf(student.manzilText) }
                var revType by remember(student.revisionType) { mutableStateOf(student.revisionType) }

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
                                        fontSize = 17.sp
                                    )
                                    Text(
                                        text = "S/o ${student.fatherName} • ${student.className} • ${student.rollNumber}",
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

                        Text("Today's Sabaq:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OutlinedTextField(
                            value = sabaqText,
                            onValueChange = {
                                sabaqText = it
                                onUpdateQuranSabaq(student, it, status, manzil, revType)
                            },
                            placeholder = { Text("Para 2, Ruku 5") },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGold,
                                unfocusedBorderColor = SoftGoldBorder
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("Sabaq Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    status = "Yaad"
                                    onUpdateQuranSabaq(student, sabaqText, "Yaad", manzil, revType)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrightActionGreen),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Yaad", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    status = "Kacha"
                                    onUpdateQuranSabaq(student, sabaqText, "Kacha", manzil, revType)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StatusLeaveBg,
                                    contentColor = StatusLeaveText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Warning, null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Kacha", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    status = "Pakka"
                                    onUpdateQuranSabaq(student, sabaqText, "Pakka", manzil, revType)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StatusPakkaBg,
                                    contentColor = StatusPakkaText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Verified, null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Pakka", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Manzil:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = manzil,
                                    onValueChange = {
                                        manzil = it
                                        onUpdateQuranSabaq(student, sabaqText, status, it, revType)
                                    },
                                    placeholder = { Text("Para 1") },
                                    shape = RoundedCornerShape(14.dp),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Revision Type:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("Sabaqi", "Manzil").forEach { rt ->
                                        val sel = revType == rt
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (sel) PrimaryGreen else CardWhite,
                                            border = BorderStroke(1.dp, PrimaryGold),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    revType = rt
                                                    onUpdateQuranSabaq(student, sabaqText, status, manzil, rt)
                                                }
                                        ) {
                                            Box(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = rt,
                                                    color = if (sel) Color.White else Color.Gray,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = SoftMintBg,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Mic, "Record Audio", tint = PrimaryGreen)
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Record Audio",
                                    color = PrimaryGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = ButtonGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Last 7 Days  •  Auto Save ✓",
                                    color = ButtonGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BooksScreen(
    uiState: AcademyUiState,
    onOpenAddBookEvalDialog: () -> Unit,
    onOpenEnrollDialog: () -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToDashboard)

    var selectedStudentId by remember(uiState.students) {
        mutableStateOf(uiState.students.firstOrNull()?.id ?: 0)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("books_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "School Books Wise Progress",
                        fontFamily = PlayfairDisplayFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "Apka Bacha Kis Book Me Acha Hai - Real-time Subject Evaluation",
                        fontSize = 12.sp,
                        color = ButtonGold,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (uiState.students.isNotEmpty()) {
                    Button(
                        onClick = onOpenAddBookEvalDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+ Evaluate")
                    }
                }
            }
        }

        if (uiState.students.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    border = BorderStroke(1.dp, SoftGoldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "No Subject Evaluations Yet",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Add a student first to evaluate their progress in Math, English, Urdu, Science, and Islamiat.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onOpenEnrollDialog,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("+ Add Student")
                        }
                    }
                }
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.students) { st ->
                        val isSel = st.id == selectedStudentId
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSel) PrimaryGreen else CardWhite,
                            border = BorderStroke(1.dp, PrimaryGold),
                            modifier = Modifier.clickable { selectedStudentId = st.id }
                        ) {
                            Text(
                                text = "${st.fullName} (${st.className})",
                                color = if (isSel) Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }

            val evals = uiState.bookEvaluations.filter {
                selectedStudentId == 0 || it.studentId == selectedStudentId
            }

            if (evals.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Tap '+ Evaluate' to add Math, English, or Urdu evaluation.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            } else {
                items(evals, key = { "book_${it.id}" }) { ev ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.fillMaxWidth()
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
                                    text = ev.subjectName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${ev.percentage}%",
                                        color = PrimaryGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = BrightActionGreen,
                                        shape = CircleShape
                                    ) {
                                        Text(
                                            text = ev.grade,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            LinearProgressIndicator(
                                progress = { (ev.percentage / 100f).coerceIn(0f, 1f) },
                                color = BrightActionGreen,
                                trackColor = Color(0xFFECF3EE),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Text(
                                text = ev.remarks,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Evaluated by: ${ev.evaluatedBy}  •  ${ev.date}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TestsScreen(
    uiState: AcademyUiState,
    onOpenCreateTestDialog: () -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToDashboard)

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("tests_screen_list"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 92.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Assessments & Examination Center",
                        fontFamily = PlayfairDisplayFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "Manage Weekly Tests, Monthly Tests & Generate Result Card PDFs with Logo",
                        fontSize = 12.5.sp,
                        color = Color.Gray
                    )
                }
            }

            if (uiState.examResults.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "No Tests or Exams Created Yet",
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "Tap '+ Create Test / Exam' below to schedule an assessment and enter student marks.",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(uiState.examResults, key = { "test_${it.id}" }) { test ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.5.dp, SoftGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = test.testTitle,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${test.subject}  •  ${test.date}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                                Surface(
                                    color = Color(0xFFFFFBF0),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "Total:\n${test.totalMarks}\nMarks",
                                        color = PrimaryGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Class/Group: ${test.classGroup}",
                                    color = ButtonGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Created by: ${test.createdBy}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            if (test.studentName.isNotBlank()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = test.studentName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = "Marks: ${test.obtainedMarks}/${test.totalMarks} | Pos: 1st",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Surface(
                                        color = BrightActionGreen,
                                        shape = CircleShape
                                    ) {
                                        Text(
                                            text = test.grade,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = onOpenCreateTestDialog,
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Enter / Update Marks for Students",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onOpenCreateTestDialog,
            containerColor = PrimaryGreen,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp),
            icon = {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            },
            text = {
                Text(text = "Create Test / Exam", fontWeight = FontWeight.Bold)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_create_test")
        )
    }
}
