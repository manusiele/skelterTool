import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.Duration
import android.annotation.SuppressLint
import android.widget.Toast

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


// First, add the ExpiryManager object
object ExpiryManager {
    private var expiryJob: Job? = null
    private val _expiryTime = MutableStateFlow("00:00:00")
    val expiryTime: StateFlow<String> = _expiryTime.asStateFlow()

    @SuppressLint("NewApi")
    fun startExpiryCheck(
        expiryDate: LocalDateTime,
        onExpired: () -> Unit
    ) {
        expiryJob?.cancel()
        expiryJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val now = LocalDateTime.now()
                if (now.isAfter(expiryDate)) {
                    withContext(Dispatchers.Main) {
                        onExpired()
                    }
                    break
                }

                val duration = Duration.between(now, expiryDate)
                val hours = duration.toHours()
                val minutes = duration.toMinutes() % 60
                val seconds = duration.seconds % 60

                _expiryTime.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                delay(1000) // Update every second
            }
        }
    }

    fun stopExpiryCheck() {
        expiryJob?.cancel()
        _expiryTime.value = "00:00:00"
    }
}

// Then your LoginScreen composable
@SuppressLint("NewApi")
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val expiryTime by ExpiryManager.expiryTime.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ... other UI elements ...

        Button(
            onClick = {
                when {
                    username.isEmpty() -> {
                        isError = true
                        errorMessage = "Username cannot be empty"
                    }
                    password.isEmpty() -> {
                        isError = true
                        errorMessage = "Password cannot be empty"
                    }
                    else -> {
                        isLoading = true
                        scope.launch {
                            try {
                                withContext(Dispatchers.IO) {
                                    val dbHelper = DatabaseHelper()
                                    val userData = dbHelper.getUserData(username)

                                    withContext(Dispatchers.Main) {
                                        if (userData != null && dbHelper.validateCredentials(username, password)) {
                                            if (LocalDateTime.now().isAfter(userData.expiryDate)) {
                                                isError = true
                                                errorMessage = "Account has expired"
                                            } else {
                                                ExpiryManager.startExpiryCheck(
                                                    userData.expiryDate
                                                ) {
                                                    navController.navigate("login") {
                                                        popUpTo(0) { inclusive = true }
                                                    }
                                                    Toast.makeText(
                                                        context,
                                                        "Session expired",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                                navController.navigate("main") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                        } else {
                                            isError = true
                                            errorMessage = "Invalid credentials"
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    isError = true
                                    errorMessage = "Connection error: ${e.message}"
                                    Toast.makeText(
                                        context,
                                        "Connection error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("Sign In")
            }
        }

        // Display expiry time
        Text(
            text = "Expiry: $expiryTime",
            fontSize = 20.sp,
            color = Color(0xFFE50914),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}