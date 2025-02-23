package com.watuke.watu.screens

import android.annotation.SuppressLint
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.content.Intent
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.icu.util.TimeUnit
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.google.zxing.qrcode.encoder.QRCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var serialNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val expiryTime by ExpiryManager.expiryTime.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Skelter Tool",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 120.dp, bottom = 40.dp)
                )

                Text(
                    text = "Expiry: $expiryTime",
                    fontSize = 20.sp,
                    color = Color(0xFFE50914),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 100.dp)
                )

                // Serial Number Input
                OutlinedTextField(
                    value = serialNumber,
                    onValueChange = { serialNumber = it },
                    label = { Text("Enter Serial Number", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 90.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFFE50914),
                        focusedBorderColor = Color(0xFFE50914),
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A)
                    )
                )

                // Main Buttons
                Button(
                    onClick = {
                        navController.navigate("QrCodeScreen")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE50914)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Remove KG 2025",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("QrCodeScreen")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE50914)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Remove MDM Tecno/Itel/Infinix",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Remove MDM Nokia - Coming Soon", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color(0xFFE50914)
                    ),
                    border = BorderStroke(2.dp, Color(0xFFE50914)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Remove MDM Nokia",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bottom Row Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Enable ADB Android 14+ - Coming Soon", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color(0xFFE50914)
                    ),
                    border = BorderStroke(2.dp, Color(0xFFE50914)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Enable ADB\nAndroid 14+",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Enable ADB Android 13+ - Coming Soon", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color(0xFFE50914)
                    ),
                    border = BorderStroke(2.dp, Color(0xFFE50914)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Enable ADB\nAndroid 13+",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Enable Wireless Debugging - Coming Soon", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color(0xFFE50914)
                    ),
                    border = BorderStroke(2.dp, Color(0xFFE50914)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Enable\nWireless Debug",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                }
            }

            // Footer
            Text(
                text = "© 2025 Skelter Tool",
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A))
                    .padding(16.dp)
            )
        }
    }
}

// QR Code generation function
fun generateQRCode(data: String, context: Context): Bitmap {
    val qrCodeWriter = QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 600, 600)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color(0xFFE50914).toArgb() else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}


// Add ExpiryManager object in the same file or in a separate file
object ExpiryManager {
    private val _expiryTime = MutableStateFlow("00:00:00")
    val expiryTime: StateFlow<String> = _expiryTime.asStateFlow()

    @SuppressLint("NewApi")
    fun startCountdown(expiryDate: Calendar) {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val now = Calendar.getInstance()
                if (now.after(expiryDate)) {
                    _expiryTime.value = "EXPIRED"
                    break
                }

                val diffInMillis = expiryDate.timeInMillis - now.timeInMillis

                // Calculate days, hours, and minutes
                val days = diffInMillis / (24 * 60 * 60 * 1000)
                val hours = (diffInMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
                val minutes = (diffInMillis % (60 * 60 * 1000)) / (60 * 1000)

                _expiryTime.value = String.format("%02d/%02d/%02d", days, hours, minutes)
                delay(1000)
            }
        }
    }

}