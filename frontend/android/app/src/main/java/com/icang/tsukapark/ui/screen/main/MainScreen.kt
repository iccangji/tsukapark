package com.icang.tsukapark.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.icang.tsukapark.R
import com.icang.tsukapark.data.network.ParkUiState
import com.icang.tsukapark.data.network.Slot
import com.icang.tsukapark.data.network.UiState
import com.icang.tsukapark.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    mainViewModel: MainViewModel = viewModel(
        factory = MainViewModel.Factory
    )
) {

    val uiState = mainViewModel.parkUiState.collectAsState().value
    val userCheckinState = mainViewModel.checkinUiState.collectAsState().value
    val isConnectedState = mainViewModel.isConnectedState.collectAsState().value

    val checkoutDialogState = remember { mutableStateOf(false) }
    val checkinDialogState = remember { mutableStateOf(false) }
    val checkinState = remember { mutableStateOf(true) }
    val slotIndexState = remember { mutableStateOf(0) }
    val slotState = remember{
        mutableStateListOf(
            Slot("L1", false),
            Slot("R1", false),
            Slot("L2", false),
            Slot("R2", false),
            Slot("L3", false),
            Slot("R3", false),
        )
    }

    when (uiState) {
        is ParkUiState.Success -> {
            slotState.clear()
            slotState.addAll(uiState.data)
        }

        else -> {}
    }

    if (userCheckinState != 0){
        checkinState.value = false
        slotIndexState.value = userCheckinState - 1
    }else{
        checkinState.value = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_directions_car_24), // Icon mobil
                            contentDescription = "Car Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "T-SukaPark",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                },
                actions = {
                    OutlinedButton(
                        modifier = Modifier
                            .padding(8.dp),
                        onClick = { mainViewModel.logout() },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Profile Icon",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome Message
            Text(
                text = "T-SUKAPARK",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Selamat Datang di T-SUKAPARK",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Parking Slot Grid
            Card(
                modifier = Modifier,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(32.dp),
                        painter = painterResource(id = R.drawable.baseline_wifi_24),
                        contentDescription = "Wi-Fi",
                        tint = if (isConnectedState) MaterialTheme.colorScheme.primary else Color.Gray,
                    )

                    // Parking Slots
                    ParkingSlotGrid(
                        modifier = Modifier
                            .weight(1f),
                        checkinClick = {
                            if(checkinState.value) {
                                checkinDialogState.value = true
                                slotIndexState.value = it
                            }
                        },
                        slots = slotState,
                    )

                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(32.dp),
                        painter = painterResource(id = R.drawable.baseline_wifi_24),
                        contentDescription = "Wi-Fi",
                        tint = if (isConnectedState) MaterialTheme.colorScheme.primary else Color.Gray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Reserved Parking and Checkout
            if (!checkinState.value) {
                ReservationStatusCard(
                    checkoutClick = {
                        checkoutDialogState.value = true
                    },
                    label = slotState[slotIndexState.value].label
                )
            }

            when {
                checkoutDialogState.value -> {
                    MainDialog(
                        onDismissRequest = { checkoutDialogState.value = false },
                        onConfirmation = {
                            checkoutDialogState.value = false
                            mainViewModel.setPark(slotIndexState.value, false)
//                            checkinState.value = true
//                            slotState.toMutableList().apply {
//                                this[slotIndexState.value].isFilled = false
//                            }
                        },
                        confirmationText = "Checkout",
                        dialogTitle = "Checkout Dialog",
                        dialogText = "Check-out parkiran saat ini?",
                    )
                }
                checkinDialogState.value -> {
                    MainDialog(
                        onDismissRequest = { checkinDialogState.value = false },
                        onConfirmation = {
                            checkinDialogState.value = false
//                            checkinState.value = false
//                            slotState.toMutableList().apply {
//                                this[slotIndexState.value].isFilled = true
//                            }
                            mainViewModel.setPark(
                                slotIndexState.value, true
                            )
                        },
                        confirmationText = "Checkin",
                        dialogTitle = "Checkin Dialog",
                        dialogText = "Check-in parkiran ini?",
                    )
                }
            }
        }
    }

}

@Composable
fun ParkingSlotGrid(
    modifier: Modifier,
    checkinClick: (Int) -> Unit,
    slots: List<Slot>,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        slots.mapIndexed { index, item ->
            index to item
        }.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                row.forEach { (globalIndex, item) ->
                    ParkingSlot(
                        slot = item.label,
                        isFilled = item.isFilled,
                    ) {
                        checkinClick(globalIndex)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ParkingSlot(
    slot: String,
    isFilled: Boolean,
    onClick: () -> Unit,
){
    Button(
        modifier = Modifier
            .size(72.dp),
        onClick = {
            if (!isFilled) {
                onClick()
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFilled) Color(0xFF4CAF50) else Color(0xFFF44336),
            contentColor = Color.White
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier,
                text = slot,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 12.sp
            )
        }
    }

}

@Composable
fun ReservationStatusCard(
    checkoutClick: () -> Unit,
    label: String
){
    OutlinedCard(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Parkiran yang dipesan: $label",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = checkoutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Check-out")
            }
        }
    }
}

@Composable
fun MainDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmationText: String
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(confirmationText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Batalkan")
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        MainScreen(modifier = Modifier)
    }
}