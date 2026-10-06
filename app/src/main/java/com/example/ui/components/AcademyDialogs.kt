package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.local.FeeRecordEntity
import com.example.data.local.StudentEntity
import com.example.ui.theme.PrimaryGreen

val ACADEMY_GROUPS = listOf(
    "Hifz",
    "Nazra",
    "Tajweed",
    "Tuition",
    "Computer"
)

@Composable
fun AddTeacherDialog(
    onDismiss: () -> Unit,
    onConfirm: (fullName: String, roleTitle: String, assignedGroup: String, phone: String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var roleTitle by remember { mutableStateOf("Quran & Tajweed Teacher") }
    var assignedGroup by remember { mutableStateOf("Hifz") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Teacher", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Teacher Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = roleTitle,
                    onValueChange = { roleTitle = it },
                    label = { Text("Designation / Role") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = assignedGroup,
                    onValueChange = { assignedGroup = it },
                    label = { Text("Assigned Group (Hifz/Nazra/Tuition)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fullName.isNotBlank()) {
                        onConfirm(fullName, roleTitle, assignedGroup, phone)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = fullName.isNotBlank()
            ) {
                Text("Save Teacher")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnrollStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        fullName: String,
        fatherName: String,
        department: String,
        className: String,
        guardianPhone: String,
        address: String,
        monthlyFeePkr: Int
    ) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var department by remember { mutableStateOf(ACADEMY_GROUPS.first()) }
    var className by remember { mutableStateOf("Class 6") }
    var guardianPhone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Nasirabad Jatlan, Azad Kashmir") }
    var monthlyFeeText by remember { mutableStateOf("2000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Student",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Student Full Name *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_student_name")
                )
                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father Name (S/o) *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_father_name")
                )
                Text(
                    text = "Select Group",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryGreen
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ACADEMY_GROUPS.forEach { dept ->
                        FilterChip(
                            selected = department == dept,
                            onClick = { department = dept },
                            label = { Text(dept) }
                        )
                    }
                }
                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    label = { Text("Class (e.g. Class 6)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = guardianPhone,
                    onValueChange = { guardianPhone = it },
                    label = { Text("Guardian Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = monthlyFeeText,
                    onValueChange = { monthlyFeeText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Monthly Fee (Rs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fullName.isNotBlank() && fatherName.isNotBlank()) {
                        onConfirm(
                            fullName,
                            fatherName,
                            department,
                            className.ifBlank { "Class 6" },
                            guardianPhone,
                            address,
                            monthlyFeeText.toIntOrNull() ?: 2000
                        )
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = fullName.isNotBlank() && fatherName.isNotBlank(),
                modifier = Modifier.testTag("confirm_enroll_student_button")
            ) {
                Text("Add Student")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun PostAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, message: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General Alert") }
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Campus Alert", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Alert Title *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Alert Message *") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && message.isNotBlank()) {
                        onConfirm(title, category, message)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = title.isNotBlank() && message.isNotBlank()
            ) {
                Text("Send Alert")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CollectFeeDialog(
    record: FeeRecordEntity,
    onDismiss: () -> Unit,
    onConfirm: (paidAmountPkr: Int, paymentMethod: String) -> Unit
) {
    val remaining = (record.amountDuePkr - record.amountPaidPkr).coerceAtLeast(0)
    var amountText by remember { mutableStateOf(remaining.toString()) }
    var paymentMethod by remember { mutableStateOf("Cash") }
    val methods = listOf("Cash", "EasyPaisa", "JazzCash", "Bank")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Collect Monthly Fee", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "${record.studentName} (${record.rollNumber})",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                    label = { Text("Amount (Rs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    methods.forEach { m ->
                        FilterChip(
                            selected = paymentMethod == m,
                            onClick = { paymentMethod = m },
                            label = { Text(m) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toIntOrNull() ?: remaining
                    if (amt > 0) {
                        onConfirm(amt, paymentMethod)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Confirm Rs $amountText")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddBookEvaluationDialog(
    students: List<StudentEntity>,
    onDismiss: () -> Unit,
    onConfirm: (student: StudentEntity, subject: String, percentage: Int, remarks: String) -> Unit
) {
    if (students.isEmpty()) {
        onDismiss()
        return
    }
    var selectedStudent by remember { mutableStateOf(students.first()) }
    var subject by remember { mutableStateOf("Math") }
    var percentageText by remember { mutableStateOf("90") }
    var remarks by remember { mutableStateOf("Math me bohat acha hai, quick calculations") }
    val subjects = listOf("Math", "English", "Urdu", "Islamiat", "Science", "Computer")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Evaluate School Book Progress", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Student:", style = MaterialTheme.typography.labelLarge, color = PrimaryGreen)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    students.forEach { st ->
                        FilterChip(
                            selected = selectedStudent.id == st.id,
                            onClick = { selectedStudent = st },
                            label = { Text(st.fullName) }
                        )
                    }
                }
                Text("Book / Subject:", style = MaterialTheme.typography.labelLarge, color = PrimaryGreen)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    subjects.forEach { sub ->
                        FilterChip(
                            selected = subject == sub,
                            onClick = { subject = sub },
                            label = { Text(sub) }
                        )
                    }
                }
                OutlinedTextField(
                    value = percentageText,
                    onValueChange = { percentageText = it.filter { c -> c.isDigit() } },
                    label = { Text("Score Percentage (0 - 100%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Teacher Evaluation Remarks") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        selectedStudent,
                        subject,
                        percentageText.toIntOrNull() ?: 85,
                        remarks
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Save Evaluation")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CreateTestDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        testTitle: String,
        subject: String,
        classGroup: String,
        totalMarks: Int,
        studentName: String,
        obtainedMarks: Int
    ) -> Unit
) {
    var testTitle by remember { mutableStateOf("Monthly Academic Assessment (October)") }
    var subject by remember { mutableStateOf("Mathematics & Science") }
    var classGroup by remember { mutableStateOf("Tuition Boy (7, 8)") }
    var totalMarksText by remember { mutableStateOf("100") }
    var studentName by remember { mutableStateOf("") }
    var obtainedMarksText by remember { mutableStateOf("92") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Test / Exam", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = testTitle,
                    onValueChange = { testTitle = it },
                    label = { Text("Assessment Title *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject(s) *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = classGroup,
                    onValueChange = { classGroup = it },
                    label = { Text("Class / Group") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = totalMarksText,
                        onValueChange = { totalMarksText = it.filter { c -> c.isDigit() } },
                        label = { Text("Total Marks") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = obtainedMarksText,
                        onValueChange = { obtainedMarksText = it.filter { c -> c.isDigit() } },
                        label = { Text("Obtained Marks") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Name (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (testTitle.isNotBlank() && subject.isNotBlank()) {
                        onConfirm(
                            testTitle,
                            subject,
                            classGroup,
                            totalMarksText.toIntOrNull() ?: 100,
                            studentName,
                            obtainedMarksText.toIntOrNull() ?: 0
                        )
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Save Test")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
