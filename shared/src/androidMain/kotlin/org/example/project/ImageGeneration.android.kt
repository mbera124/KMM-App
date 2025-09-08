package org.example.project

import android.content.Context

actual fun provideImageRepository(context: Any?): ImageRepository {
    require(context is Context)
    return AndroidImageRepository(context)
}
