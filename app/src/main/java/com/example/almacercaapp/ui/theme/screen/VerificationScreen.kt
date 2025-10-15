package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.almacercaapp.viewmodel.AuthViewModel
import com.example.almacercaapp.navigation.Routes
@Composable
fun VerificationScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val user = viewModel.user.value
    val code = viewModel.verificationCode.value
    val timeLeft = viewModel.timeLeft.value
    val canResend = viewModel.canResend.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Verificación",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            val destino = if (user.useEmail) user.email else user.phoneNumber
            Text(
                text = "Hemos enviado un código de verificación a $destino",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { viewModel.updateVerificationCode(it) },
                label = { Text("Código de verificación") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.verificationError.value != null) {
                Text(
                    text = viewModel.verificationError.value!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (viewModel.validateVerification()) {
                        navController.navigate(Routes.Location.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6FCF97)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Verificar", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (canResend) {
                Text(
                    text = "¿No recibiste el código? Reenviar",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { viewModel.resendCode() },
                    fontSize = 15.sp
                )
            } else {
                Text(
                    text = "Reenviar código en ${timeLeft}s",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    }
}