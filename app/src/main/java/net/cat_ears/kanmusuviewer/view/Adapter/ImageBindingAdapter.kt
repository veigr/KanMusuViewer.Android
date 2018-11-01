package net.cat_ears.kanmusuviewer.view.Adapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageBindingAdapter {
    @BindingAdapter("imageBitmap")
    @JvmStatic
    fun loadImage(view: ImageView, bitmap: Bitmap?) {
        // たったこれだけのことに Adapter が必要とかマジか
        if (bitmap != null)
            view.setImageBitmap(bitmap)
    }
}