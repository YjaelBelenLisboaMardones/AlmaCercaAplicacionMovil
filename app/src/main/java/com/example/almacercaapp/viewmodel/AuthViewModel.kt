package com.example.almacercaapp.viewmodel



//El ViewModel es la capa que maneja la lógica y controla los datos del modelo para la interfaz de usuario
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacercaapp.domain.validation.*
import com.example.almacercaapp.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // --- Datos del usuario ---
    var user = mutableStateOf(User())

    // --- Errores individuales ---
    var nameError = mutableStateOf<String?>(null)
    var emailError = mutableStateOf<String?>(null)
    var phoneError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)
    var verificationError = mutableStateOf<String?>(null)

    // --- Verificación ---
    var verificationCode = mutableStateOf("")

    // --- Temporizador para reenvío ---
    var timeLeft = mutableStateOf(30)
    var canResend = mutableStateOf(false)

    var selectedCountry = mutableStateOf("")
    var selectedArea = mutableStateOf("")
    var userLocation = mutableStateOf("")

    init {
        startCountdown()
    }

    // 🕒 Temporizador de reenvío
    fun startCountdown() {
        canResend.value = false
        timeLeft.value = 30
        viewModelScope.launch {
            while (timeLeft.value > 0) {
                delay(1000)
                timeLeft.value -= 1
            }
            canResend.value = true
        }
    }

    fun resendCode() {
        if (canResend.value) {
            // Aquí podrías llamar a una API para reenviar el código
            startCountdown()
        }
    }

    // VALIDACIONES

    //vali registro
    fun validateSignUp(): Boolean {
        nameError.value = validateNameLettersOnly(user.value.username)
        emailError.value = if (user.value.useEmail) validateEmail(user.value.email) else null
        phoneError.value = if (!user.value.useEmail) validatePhoneDigitsOnly(user.value.phoneNumber) else null
        passwordError.value = validateStrongPass(user.value.password)

        return listOf(
            nameError.value,
            emailError.value,
            phoneError.value,
            passwordError.value
        ).all { it == null }
    }

    //vali inicio de sesion
    fun validateLogin(): Boolean {
        emailError.value = if (user.value.useEmail) validateEmail(user.value.email) else null
        phoneError.value = if (!user.value.useEmail) validatePhoneDigitsOnly(user.value.phoneNumber) else null
        passwordError.value = validateStrongPass(user.value.password)

        return listOf(
            emailError.value,
            phoneError.value,
            passwordError.value
        ).all { it == null }
    }

    // vali verificar
    fun validateVerification(): Boolean {
        verificationError.value = validateVerificationCode(verificationCode.value)
        return verificationError.value == null
    }

    // --- ACTUALIZADORES DE CAMPOS ---
    fun toggleMethod() { user.value = user.value.copy(useEmail = !user.value.useEmail) }
    fun updateUsername(value: String) { user.value = user.value.copy(username = value) }
    fun updateEmail(value: String) { user.value = user.value.copy(email = value) }
    fun updatePhone(value: String) { user.value = user.value.copy(phoneNumber = value) }
    fun updatePassword(value: String) { user.value = user.value.copy(password = value) }
    fun updateVerificationCode(value: String) { verificationCode.value = value }

    fun updateCountry(country: String) {
        selectedCountry.value = country
    }

    fun updateArea(area: String) {
        selectedArea.value = area
    }
    fun updateLocation(location: String) {
        userLocation.value = location
    }
}