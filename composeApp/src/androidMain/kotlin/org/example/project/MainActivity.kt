package org.example.project

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            App()
//        }
//    }
//}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

class MainActivity : ComponentActivity() {

    private lateinit var imageRepo: AndroidImageRepository
    private var latestImagePath: String? = null
    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    // Register launcher
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && latestImagePath != null) {
            lifecycleScope.launch {
                val saved = imageRepo.saveImage(ImageResult(latestImagePath!!))
                Toast.makeText(
                    this@MainActivity,
                    "Image saved: $saved at $latestImagePath",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(this, "Capture cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        imageRepo = AndroidImageRepository(this)

        setContent {
            Column(modifier = Modifier.padding(56.dp)) {

                Button(onClick = {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                    val (file, uri) = imageRepo.createImageFile()
                    latestImagePath = file.absolutePath
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                        putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    }
                    takePictureLauncher.launch(intent)
                }) {
                    Text("Capture Image")
                }
            }
        }
    }
    private fun launchCamera() {
        val (file, uri) = imageRepo.createImageFile()
        latestImagePath = file.absolutePath
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        takePictureLauncher.launch(intent)
    }
}