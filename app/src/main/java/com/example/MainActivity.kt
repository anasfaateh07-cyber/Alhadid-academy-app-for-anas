package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddBookEvaluationDialog
import com.example.ui.components.AddTeacherDialog
import com.example.ui.components.CreateTestDialog
import com.example.ui.components.EnrollStudentDialog
import com.example.ui.components.PostAlertDialog
import com.example.ui.screens.AlHadidLogoBadge
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.BooksScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FeesAndPortalScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StudentsScreen
import com.example.ui.screens.TestsScreen
import com.example.ui.theme.AlHadidAcademyTheme
import com.example.ui.theme.ButtonGold
import com.example.ui.theme.CardWhite
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.PlayfairDisplayFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SoftGoldBg
import com.example.viewmodel.AcademyTab
import com.example.viewmodel.AcademyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlHadidAcademyTheme {
                AlHadidAcademyApp()
            }
        }
    }
}

private data class BottomNavItem(
    val tab: AcademyTab,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun AlHadidAcademyApp(
    viewModel: AcademyViewModel = viewModel()
) {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(
            onSplashFinished = { showSplash = false }
        )
        return
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val bannerMessage by viewModel.statusBannerMessage.collectAsStateWithLifecycle()

    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var showEnrollDialog by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var showBookEvalDialog by remember { mutableStateOf(false) }
    var showCreateTestDialog by remember { mutableStateOf(false) }

    val navItems = remember {
        listOf(
            BottomNavItem(AcademyTab.HOME, "Home", Icons.Default.Mosque, "nav_tab_home"),
            BottomNavItem(AcademyTab.STUDENTS, "Students", Icons.Default.Groups, "nav_tab_students"),
            BottomNavItem(AcademyTab.FEES, "Fees", Icons.Default.Receipt, "nav_tab_fees"),
            BottomNavItem(AcademyTab.ATTENDANCE, "Attendance", Icons.Default.CheckCircle, "nav_tab_attendance"),
            BottomNavItem(AcademyTab.QURAN, "Quran", Icons.AutoMirrored.Filled.MenuBook, "nav_tab_quran"),
            BottomNavItem(AcademyTab.BOOKS, "Books", Icons.Default.AutoStories, "nav_tab_books"),
            BottomNavItem(AcademyTab.TESTS, "Tests", Icons.AutoMirrored.Filled.Assignment, "nav_tab_tests")
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        containerColor = CreamBackground,
        topBar = {
            AcademyTopBar(
                alertCount = uiState.announcements.size,
                onBellClick = { showAlertDialog = true },
                onLogoutClick = { showSplash = true }
            )
        },
        bottomBar = {
            AcademySevenTabBottomBar(
                items = navItems,
                currentTab = currentTab,
                onSelectTab = viewModel::selectTab
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamBackground)
        ) {
            AnimatedVisibility(visible = bannerMessage != null) {
                bannerMessage?.let { msg ->
                    Surface(
                        color = SoftGoldBg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("status_feedback_banner")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryGreen,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { viewModel.clearStatusBanner() }) {
                                Text("OK", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (currentTab) {
                    AcademyTab.HOME -> DashboardScreen(
                        uiState = uiState,
                        onNavigateTab = viewModel::selectTab,
                        onOpenAddTeacherDialog = { showAddTeacherDialog = true },
                        onOpenEnrollDialog = { showEnrollDialog = true },
                        onOpenAlertDialog = { showAlertDialog = true }
                    )

                    AcademyTab.STUDENTS -> StudentsScreen(
                        uiState = uiState,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onDepartmentSelect = viewModel::updateSelectedDepartment,
                        onOpenEnrollDialog = { showEnrollDialog = true },
                        onDeleteStudent = viewModel::deleteStudent,
                        onBackToDashboard = { viewModel.selectTab(AcademyTab.HOME) }
                    )

                    AcademyTab.FEES -> FeesAndPortalScreen(
                        uiState = uiState,
                        onRecordPayment = viewModel::recordFeePayment,
                        onBackToDashboard = { viewModel.selectTab(AcademyTab.HOME) }
                    )

                    AcademyTab.ATTENDANCE -> AttendanceScreen(
                        uiState = uiState,
                        todayDate = viewModel.todayFormatted,
                        onSelectDate = viewModel::updateSelectedDate,
                        onMarkAttendance = viewModel::markStudentAttendance,
                        onMarkAllPresent = viewModel::markAllPresentForSelectedDate,
                        onBackToDashboard = { viewModel.selectTab(AcademyTab.HOME) }
                    )

                    AcademyTab.QURAN -> QuranScreen(
                        uiState = uiState,
                        onUpdateQuranSabaq = viewModel::updateStudentQuranSabaq,
                        onOpenEnrollDialog = { showEnrollDialog = true },
                        onBackToDashboard = { viewModel.selectTab(AcademyTab.HOME) }
                    )

                    AcademyTab.BOOKS -> BooksScreen(
                        uiState = uiState,
                        onOpenAddBookEvalDialog = { showBookEvalDialog = true },
                        onOpenEnrollDialog = { showEnrollDialog = true },
                        onBackToDashboard = { viewModel.selectTab(AcademyTab.HOME) }
                    )

                    AcademyTab.TESTS -> TestsScreen(
                        uiState = uiState,
                        onOpenCreateTestDialog = { showCreateTestDialog = true },
                        onBackToDashboard = { viewModel.selectTab(AcademyTab.HOME) }
                    )
                }
            }
        }
    }

    if (showAddTeacherDialog) {
        AddTeacherDialog(
            onDismiss = { showAddTeacherDialog = false },
            onConfirm = viewModel::addTeacher
        )
    }

    if (showEnrollDialog) {
        EnrollStudentDialog(
            onDismiss = { showEnrollDialog = false },
            onConfirm = viewModel::enrollNewStudent
        )
    }

    if (showAlertDialog) {
        PostAlertDialog(
            onDismiss = { showAlertDialog = false },
            onConfirm = viewModel::postAlert
        )
    }

    if (showBookEvalDialog) {
        AddBookEvaluationDialog(
            students = uiState.students,
            onDismiss = { showBookEvalDialog = false },
            onConfirm = viewModel::saveBookEvaluation
        )
    }

    if (showCreateTestDialog) {
        CreateTestDialog(
            onDismiss = { showCreateTestDialog = false },
            onConfirm = viewModel::createTestOrExam
        )
    }
}

@Composable
private fun AcademyTopBar(
    alertCount: Int,
    onBellClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Surface(
        color = CardWhite,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AlHadidLogoBadge(
                        size = 48.dp,
                        borderWidth = 2.dp,
                        showLabelInside = false
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Al Hadid Academy",
                            fontFamily = PlayfairDisplayFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Text(
                            text = "Nasirabad Jatlan, Azad Kashmir",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ButtonGold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBellClick,
                        modifier = Modifier.testTag("topbar_bell_button")
                    ) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = PrimaryGreen,
                                modifier = Modifier.size(26.dp)
                            )
                            if (alertCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 3.dp, y = (-2).dp)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE53935)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$alertCount",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.testTag("topbar_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout / Replay Splash",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEAE5D5))
        }
    }
}

@Composable
private fun AcademySevenTabBottomBar(
    items: List<BottomNavItem>,
    currentTab: AcademyTab,
    onSelectTab: (AcademyTab) -> Unit
) {
    Surface(
        color = CardWhite,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bottom_navigation_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentTab == item.tab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectTab(item.tab) }
                        .padding(vertical = 2.dp)
                        .testTag(item.tag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(48.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) PrimaryGreen else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Color.White else Color(0xFF5F6E6E),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PrimaryGreen else Color(0xFF5F6E6E),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
