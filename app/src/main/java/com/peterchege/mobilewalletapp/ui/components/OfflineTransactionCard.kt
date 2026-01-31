package com.peterchege.mobilewalletapp.ui.components



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterchege.mobilewalletapp.core.database.entities.OfflineTransaction
import com.peterchege.mobilewalletapp.core.util.SyncStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun OfflineTransactionCard(
    transaction: OfflineTransaction,
    onSyncClick: (OfflineTransaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val isPending = transaction.syncStatus == SyncStatus.QUEUED.value
    val isSyncing = transaction.syncStatus == SyncStatus.SYNCING.value
    val isFailed = transaction.syncStatus == SyncStatus.FAILED.value

    val statusColor = when {
        isPending -> Color(0xFFFFA726) // Orange
        isSyncing -> Color(0xFF42A5F5) // Blue
        isFailed -> Color(0xFFF44336) // Red
        else -> Color(0xFF4CAF50) // Green (synced)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Status indicator and transaction info
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Status indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(statusColor)
                    )

                    // Transaction details
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Transfer to ${formatAccountNumber(transaction.accountTo)}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatDateTime(transaction.createdAt),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Text(
                                text = transaction.syncStatus,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = statusColor
                            )
                        }

                        Text(
                            text = "From: ${formatAccountNumber(transaction.accountFrom)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                // Right side - Amount
                Text(
                    text = "KSh ${formatAmount(transaction.amount)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Sync button for pending or failed transactions
            if (isPending || isFailed) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { onSyncClick(transaction) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = statusColor
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(statusColor)
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Sync",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFailed) "Retry Sync" else "Sync Now",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }
    }
}

private fun formatAccountNumber(accountNo: String): String {
    return if (accountNo.length > 4) {
        "****${accountNo.takeLast(4)}"
    } else {
        accountNo
    }
}

private fun formatDateTime(timestamp: Long): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault()
    )
    val today = LocalDateTime.now().toLocalDate()
    val transactionDate = dateTime.toLocalDate()

    return when {
        transactionDate == today -> {
            "Today, ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }
        transactionDate == today.minusDays(1) -> {
            "Yesterday, ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }
        else -> {
            dateTime.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
        }
    }
}

private fun formatAmount(amount: String): String {
    return try {
        val amountDouble = amount.toDouble()
        String.format("%,.2f", amountDouble)
    } catch (e: NumberFormatException) {
        amount
    }
}

val offlineTransactions = listOf(
    OfflineTransaction(
        clientTransactionId = "offline-001",
        accountFrom = "1234567890",
        accountTo = "9876543210",
        amount = "5000.00",
        createdAt = System.currentTimeMillis(),
        syncStatus = "SYNCING"
    ),
    OfflineTransaction(
        clientTransactionId = "offline-002",
        accountFrom = "1234567890",
        accountTo = "5555555555",
        amount = "2500.50",
        createdAt = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
        syncStatus = "FAILED"
    ),
    OfflineTransaction(
        clientTransactionId = "offline-003",
        accountFrom = "1234567890",
        accountTo = "4444444444",
        amount = "1000.00",
        createdAt = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000),
        syncStatus = "SYNCED"
    ),
    OfflineTransaction(
        clientTransactionId = "offline-004",
        accountFrom = "1234567890",
        accountTo = "3333333333",
        amount = "7500.00",
        createdAt = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000),
        syncStatus = "SYNCED"
    )

)

// Preview
@Preview(showBackground = true)
@Composable
fun OfflineTransactionCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp)
        ) {
            offlineTransactions.map {
                OfflineTransactionCard(
                    transaction = it,
                    onSyncClick = {}
                )
            }

        }
    }
}