package org.example.project

interface ImageRepository {
    suspend fun captureImage(): ImageResult
    suspend fun saveImage(image: ImageResult): Boolean
}