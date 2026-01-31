package com.peterchege.mobilewalletapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.peterchege.mobilewalletapp.core.models.responses.RemoteTransaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun TransactionCard(
    transaction: RemoteTransaction,
    modifier: Modifier = Modifier
) {
    val isCredit = transaction.debitOrCredit.equals("CREDIT", ignoreCase = true)

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side - Type indicator, Description & DateTime
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Type indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isCredit)
                                Color(0xFF4CAF50)
                            else
                                Color(0xFFF44336)
                        )
                )

                // Description and DateTime
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = transaction.transactionType,
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
//                        Text(
//                            text = formatDateTime(dateTime),
//                            style = MaterialTheme.typography.bodySmall.copy(
//                                fontSize = 12.sp
//                            ),
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                        )

                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = "Bal: KSh ${String.format("%,.2f", transaction.balance)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Right side - Amount
            Text(
                text = formatAmount(transaction.amount, isCredit),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = if (isCredit)
                    Color(0xFF4CAF50)
                else
                    Color(0xFFF44336)
            )
        }
    }
}



private fun formatAmount(amount: Double, isCredit: Boolean): String {
    val prefix = if (isCredit) "+" else "-"
    return "$prefix KSh ${String.format("%,.2f", amount)}"
}

val remoteTransactions = listOf(
    RemoteTransaction(
        accountNo = "1234567890",
        amount = 2500.50,
        balance = 15000.00,
        customerId = "CUST001",
        debitOrCredit = "DEBIT",
        id = 1,
        transactionId = "TXN001",
        transactionType = "Grocery Store"
    ),
    RemoteTransaction(
        accountNo = "1234567890",
        amount = 85000.00,
        balance = 100500.00,
        customerId = "CUST001",
        debitOrCredit = "CREDIT",
        id = 2,
        transactionId = "TXN002",
        transactionType = "Salary Deposit"
    ),
    RemoteTransaction(
        accountNo = "1234567890",
        amount = 1250.00,
        balance = 14250.00,
        customerId = "CUST001",
        debitOrCredit = "DEBIT",
        id = 3,
        transactionId = "TXN003",
        transactionType = "Restaurant Payment"
    )
)

// Preview
@Preview(showBackground = true)
@Composable
fun TransactionCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp)
        ) {
            remoteTransactions.map {
                TransactionCard(
                    transaction = it,
                )
            }
        }
    }
}