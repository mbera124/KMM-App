package org.example.project

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File


class AndroidImageRepository(private val context: Context) : ImageRepository {

//    override suspend fun captureImage(): ImageResult {
//        val file = File(
//            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//            "captured_${System.currentTimeMillis()}.jpg"
//        )
//        val uri: Uri = FileProvider.getUriForFile(
//            context,
//            "${context.packageName}.provider",
//            file
//        )
//
//        // Launch camera
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//            putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        }
//
//        // ⚠️ This part must be launched from an Activity/Fragment using ActivityResultLauncher
//        // For now, just return the file path
//        return ImageResult(path = file.absolutePath)
//    }
//
//    override suspend fun saveImage(image: ImageResult): Boolean {
//        return File(image.path).exists()
//    }


    fun createImageFile(): Pair<File, Uri> {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "captured_${System.currentTimeMillis()}.jpg"
        )
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        return Pair(file, uri)
    }

    override suspend fun captureImage(): ImageResult {
        // We'll never directly call this in Android — handled by ActivityResult
        throw NotImplementedError("Use ActivityResultLauncher in MainActivity")
    }

    override suspend fun saveImage(image: ImageResult): Boolean {
        return File(image.path).exists()
    }
}