package com.example.almacercaapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacercaapp.data.repository.UserRepository // <-- Importa tu repositorio
import com.example.almacercaapp.domain.validation.*
import com.example.almacercaapp.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update // <-- Asegúrate que esté importado
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() { // <-- Inyecta el repositorio

    // --- Datos del usuario (sin cambios) ---
    var user = mutableStateOf(User())

    // --- Errores individuales (sin cambios) ---
    var nameError = mutableStateOf<String?>(null)
    var emailError = mutableStateOf<String?>(null)
    var phoneError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)
    var verificationError = mutableStateOf<String?>(null)

    // --- Verificación (sin cambios) ---
    var verificationCode = mutableStateOf("")

    // --- Temporizador para reenvío (sin cambios) ---
    var timeLeft = mutableStateOf(30)
    var canResend = mutableStateOf(false)

    // --- Estados de Éxito (StateFlow) ---
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false) // <-- NUEVO para registro
    val registerSuccess = _registerSuccess.asStateFlow()   // <-- NUEVO para registro

    init {
        startCountdown()
    }

    // --- Lógica de Temporizador (sin cambios) ---
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
            startCountdown()
        }
    }

    // --- Lógica de LOGIN (MODIFICADA) ---
    fun submitLogin() { // Renombrado
        if (validateLoginFields()) { // Llama a la validación de campos
            viewModelScope.launch {
                // Opcional: Mostrar estado de carga si tienes un campo para ello
                // _uiState.update { it.copy(isSubmitting = true, errorMsg = null) }

                // Determina si se usa email o teléfono (IMPORTANTE: Repositorio actual solo usa email)
                if (!user.value.useEmail) {
                    phoneError.value = "Inicio de sesión con teléfono no implementado." // Muestra error
                    // Opcional: Ocultar estado de carga si lo mostraste
                    // _uiState.update { it.copy(isSubmitting = false) }
                    return@launch // Detiene la ejecución si se intenta con teléfono
                }

                // Llama al repositorio para intentar iniciar sesión
                val result = repository.login(
                    email = user.value.email.trim(),
                    password = user.value.password // No usar trim() en contraseñas
                )

                if (result.isSuccess) {
                    _loginSuccess.value = true // Indica éxito a la UI
                    passwordError.value = null // Limpia errores previos
                    emailError.value = null
                } else {
                    // Muestra el mensaje de error del repositorio (ej. "Credenciales inválidas")
                    passwordError.value = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                }
                // Opcional: Ocultar estado de carga
                // _uiState.update { it.copy(isSubmitting = false) }
            }
        }
    }

    // Renombrado: Valida solo que los campos no estén vacíos y tengan formato básico
    private fun validateLoginFields(): Boolean {
        // Validación visual de formato (no si existe o coincide)
        emailError.value = if (user.value.useEmail) validateEmail(user.value.email) else null
        phoneError.value = if (!user.value.useEmail) validatePhoneDigitsOnly(user.value.phoneNumber) else null
        // Validación simple de contraseña (solo si no está vacía)
        passwordError.value = if (user.value.password.isNotBlank()) null else "La contraseña es obligatoria"

        val identifierValid = if (user.value.useEmail) emailError.value == null && user.value.email.isNotBlank()
        else phoneError.value == null && user.value.phoneNumber.isNotBlank()

        return identifierValid && passwordError.value == null
    }

    // Función para resetear el estado de éxito del login después de navegar
    fun onLoginNavigationDone() {
        _loginSuccess.value = false
    }


    // --- Lógica de REGISTRO (MODIFICADA) ---
    fun submitSignUp() { // Renombrado
        if (validateSignUpFields()) { // Llama a la validación de campos
            viewModelScope.launch {
                // Opcional: Mostrar estado de carga
                // _uiState.update { it.copy(isSubmitting = true, errorMsg = null) }

                // Llama al repositorio para intentar registrar
                val result = repository.register(
                    name = user.value.username.trim(),
                    // Asegúrate de pasar el valor correcto según useEmail
                    email = if (user.value.useEmail) user.value.email.trim() else "",
                    phone = if (!user.value.useEmail) user.value.phoneNumber.trim() else "",
                    password = user.value.password // No usar trim()
                )

                if (result.isSuccess) {
                    _registerSuccess.value = true // Indica éxito a la UI
                    // Limpia errores previos
                    nameError.value = null
                    emailError.value = null
                    phoneError.value = null
                    passwordError.value = null
                } else {
                    // Muestra el mensaje de error del repositorio (ej. "El correo ya está registrado")
                    // Decide dónde mostrar el error (campo específico o general)
                    if (user.value.useEmail) {
                        emailError.value = result.exceptionOrNull()?.message ?: "Error en el registro"
                    } else {
                        phoneError.value = result.exceptionOrNull()?.message ?: "Error en el registro"
                    }
                    // También podrías usar un estado de error general si prefieres
                    // _uiState.update { it.copy(errorMsg = result.exceptionOrNull()?.message) }
                }
                // Opcional: Ocultar estado de carga
                // _uiState.update { it.copy(isSubmitting = false) }
            }
        }
    }

    // Renombrado: Valida solo formato y reglas de los campos
    private fun validateSignUpFields(): Boolean {
        nameError.value = validateNameLettersOnly(user.value.username)
        emailError.value = if (user.value.useEmail) validateEmail(user.value.email) else null
        phoneError.value = if (!user.value.useEmail) validatePhoneDigitsOnly(user.value.phoneNumber) else null
        // Usa la validación de contraseña fuerte aquí
        passwordError.value = validateStrongPass(user.value.password)

        // Devuelve true si TODOS los errores son nulos (campos válidos)
        return listOf(
            nameError.value,
            emailError.value,
            phoneError.value,
            passwordError.value
        ).all { it == null }
    }

    // Función para resetear el estado de éxito del registro después de navegar
    fun onRegisterNavigationDone() {
        _registerSuccess.value = false
    }

    // --- Lógica de VERIFICACIÓN (sin cambios, ya que no interactúa con el repo aún) ---
    fun validateVerification(): Boolean {
        verificationError.value = validateVerificationCode(verificationCode.value)
        return verificationError.value == null
    }

    // --- ACTUALIZADORES DE CAMPOS (sin cambios) ---
    fun toggleMethod() { user.value = user.value.copy(useEmail = !user.value.useEmail) }
    fun updateUsername(value: String) { user.value = user.value.copy(username = value) }
    fun updateEmail(value: String) { user.value = user.value.copy(email = value) }
    fun updatePhone(value: String) { user.value = user.value.copy(phoneNumber = value) }
    fun updatePassword(value: String) { user.value = user.value.copy(password = value) }
    fun updateVerificationCode(value: String) { verificationCode.value = value }
}