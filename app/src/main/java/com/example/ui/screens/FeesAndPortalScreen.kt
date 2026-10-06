package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FeeRecordEntity
import com.example.ui.components.CollectFeeDialog
import com.example.ui.theme.ButtonGold
import com.example.ui.theme.CardWhite
import com.example.ui.theme.PlayfairDisplayFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SoftGoldBorder
import com.example.ui.theme.StatusAbsentBg
import com.example.ui.theme.StatusAbsentText
import com.example.ui.theme.StatusPresentBg
import com.example.ui.theme.StatusPresentText
import com.example.viewmodel.AcademyUiState

@Composable
fun FeesAndPortalScreen(
    uiState: AcademyUiState,
    onRecordPayment: (FeeRecordEntity, Int, String) -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToDashboard)

    val context = LocalContext.current
    var feeRecordToCollect by remember { mutableStateOf<FeeRecordEntity?>(null) }

    val totalCollected = uiState.feeRecords.sumOf { it.amountPaidPkr }
    val totalBilled = uiState.feeRecords.sumOf { it.amountDuePkr }
    val totalPending = (totalBilled - totalCollected).coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Fee Collection & Vouchers",
            fontFamily = PlayfairDisplayFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
        Text(
            text = "Monthly Tuition & Hifz Fee Ledger • Al Hadid Academy",
            fontSize = 13.sp,
            color = ButtonGold,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Summary Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            border = BorderStroke(1.dp, SoftGoldBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Fee Collected",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Rs $totalCollected",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Pending Balance",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Rs $totalPending",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (totalPending > 0) StatusAbsentText else PrimaryGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.feeRecords.isEmpty()) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                border = BorderStroke(1.dp, SoftGoldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fees_empty_state_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = "Zero Fee Records (Rs 0)",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Add a student from Home or Students tab to automatically create their monthly fee voucher.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("fee_records_list"),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = uiState.feeRecords,
                    key = { "fee_${it.id}" }
                ) { record ->
                    val balance = (record.amountDuePkr - record.amountPaidPkr).coerceAtLeast(0)
                    val isPaid = balance == 0
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = BorderStroke(1.dp, SoftGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = record.studentName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${record.rollNumber} • ${record.department}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Surface(
                                    color = if (isPaid) StatusPresentBg else StatusAbsentBg,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (isPaid) "PAID" else "PENDING",
                                        color = if (isPaid) StatusPresentText else StatusAbsentText,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Paid: Rs ${record.amountPaidPkr} / Rs ${record.amountDuePkr}",
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (!isPaid) {
                                    Button(
                                        onClick = { feeRecordToCollect = record },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Payments,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Collect Fee")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                OutlinedButton(
                                    onClick = {
                                        val receipt = "Al Hadid Academy Nasirabad Jatlan\nStudent: ${record.studentName} (${record.rollNumber})\nPaid: Rs ${record.amountPaidPkr}/${record.amountDuePkr}\nStatus: ${record.status}"
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, receipt)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Share Receipt"))
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share Receipt",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    feeRecordToCollect?.let { record ->
        CollectFeeDialog(
            record = record,
            onDismiss = { feeRecordToCollect = null },
            onConfirm = { amount, method ->
                onRecordPayment(record, amount, method)
            }
        )
    }
}
