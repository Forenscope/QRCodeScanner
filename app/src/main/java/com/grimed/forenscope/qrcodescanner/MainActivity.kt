package com.grimed.forenscope.qrcodescanner

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.grimed.forenscope.qrcodescanner.ui.theme.QRCodeScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRCodeScannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var scannedValue by remember { mutableStateOf("") }
                    var permissionGranted by remember { mutableStateOf(false) }

                    RequestCameraPermission(
                        onPermissionGranted = { permissionGranted = true },
                        onPermissionDenied = {
                            // Handle the case when permission is denied
                        }
                    )

                    if (permissionGranted) {
                        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                            QRCodeScanner(
                                onQRCodeScanned = { qrCodeValue ->
                                    scannedValue = qrCodeValue
                                },
                                modifier = Modifier.fillMaxSize()
                            )

                            if (scannedValue.isNotEmpty()) {
                                Text(
                                    text = scannedValue,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .clickable {
                                            openLinkInBrowser(scannedValue)
                                        }
                                )
                            }
                        }
                    } else {
                        // Show a message or UI indicating that permission is required
                        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                            Text(
                                text = "Camera permission is required to use this app.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openLinkInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setPackage("com.android.chrome")  // Or any other browser package
            }
            try {
                startActivity(browserIntent)
            } catch (e: Exception) {
                Log.e("OpenLinkInBrowser", "No browser found to handle this intent", e)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QRCodeScannerTheme {
        Greeting("Android")
    }
}