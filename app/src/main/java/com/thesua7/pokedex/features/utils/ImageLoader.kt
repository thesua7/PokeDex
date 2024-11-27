package com.thesua7.pokedex.features.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.palette.graphics.Palette
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.net.URL

@Composable
fun PicassoImageView(
    url: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        modifier = modifier,
        update = { imageView ->
            Picasso.get()
                .load(url)
                .into(imageView)
        }
    )
}


fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
    val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

    Palette.from(bmp).generate { palette ->
        palette?.dominantSwatch?.rgb?.let { colorValue ->
            onFinish(Color(colorValue))
        }
    }
}

// Helper function to load image using Picasso and return a Bitmap
fun loadImageWithPicasso(imageUrl: String, onBitmapLoaded: (Bitmap?) -> Unit) {
    Picasso.get()
        .load(imageUrl)
        .getAsync { result ->
            onBitmapLoaded(result)
        }
}

// Extension function to load Bitmap asynchronously with Picasso
fun RequestCreator.getAsync(onResult: (Bitmap?) -> Unit) {
    Thread {
        try {
            val bitmap = this.get()
            onResult(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(null)
        }
    }.start()
}