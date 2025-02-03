package io.github.sustainow.presentation.ui.utils

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun uriToImageBitmap(context: Context, imageUri: Uri): ImageBitmap {
    var bitmap: ImageBitmap? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                imageUri!!
            )
        ).asImageBitmap()
        return bitmap
    } else {
        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            .asImageBitmap()
        return bitmap
    }
}