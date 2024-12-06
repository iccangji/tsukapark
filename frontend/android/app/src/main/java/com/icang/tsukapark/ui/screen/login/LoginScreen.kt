package com.icang.tsukapark.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.icang.tsukapark.R
import com.icang.tsukapark.data.network.UiState
import com.icang.tsukapark.ui.theme.AppTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory
    )
) {

    val uiState = loginViewModel.uiState.collectAsState().value
    val btnLoginState = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_directions_car_24),
                contentDescription = "Car Icon",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "T-SukaPark",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        Text(
            text = "Login into your account",
            fontSize = 16.sp,
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        LoginForm(
            modifier = Modifier,
            email = emailState.value,
            onEmailChange = {
                emailState.value = it
            },
            password = passwordState.value,
            onPasswordChange = {
                passwordState.value = it
            },
            onSubmitClick = {
                if (emailState.value != "" && passwordState.value != "") {
                    loginViewModel.auth(emailState.value, passwordState.value)
                    btnLoginState.value = false
                }
            },
            loginButtonEnable = btnLoginState.value
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "or", modifier = Modifier.padding(horizontal = 8.dp))
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Sign up",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        when (uiState) {
            UiState.Error -> {
                LoginDialog(
                    onDismissRequest = { loginViewModel.hideModal() },
                    dialogTitle = "Login Gagal",
                    dialogText = "Username atau password yang dimasukkan salah. Harap coba lagi",
                )
                btnLoginState.value = true
            }
            UiState.Initial -> {}

            else -> {}
        }
    }
}

@Composable
fun LoginForm(
    modifier: Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    loginButtonEnable: Boolean
){
    Column (
        modifier = modifier.fillMaxWidth()
    ){
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text("Enter your email") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            label = { Text("Enter your password") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password icon",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Forgot password link
        Text(
            text = "Forgot password?",
            color = MaterialTheme.colorScheme.secondary, // Blue color,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Login button
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = loginButtonEnable
        ) {
            Text(
                text = "Login now",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoginDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
){
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
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("OK")
            }
        },
        confirmButton = {},
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_XL)
@Composable
fun LoginPreview() {
    AppTheme {
        LoginScreen()
    }
}