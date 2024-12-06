package com.icang.tsukapark.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.icang.tsukapark.ui.screen.login.LoginScreen
import com.icang.tsukapark.ui.screen.main.MainScreen
import com.icang.tsukapark.ui.screen.OnboardScreen

@Composable
fun TsukaparkApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = viewModel(
        factory = AppViewModel.Factory
    )
) {

    when (appViewModel.showEmail.collectAsState().value) {
        "" -> {
            OnboardScreen()
        }
        "null" -> {
            LoginScreen()
        }
        else -> {
            MainScreen(modifier = modifier)
        }
    }
}