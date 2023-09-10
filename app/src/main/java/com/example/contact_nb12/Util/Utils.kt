package com.example.contact_nb12.Util

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.contact_nb12.R

object Utils {
    // 기본프로필이미지 Uri
    fun getDefaultImgUri(context: Context): Uri {
        val resId = R.drawable.dialog_profile
        val res = context.resources
        val imgUri =
            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${res.getResourcePackageName(resId)}/${
                res.getResourceTypeName(resId)
            }/${res.getResourceEntryName(resId)}"
        return Uri.parse(imgUri)!!
    }

    // ImageView띄우기(Glide)
    fun getImageForUri(context: Context, uri: Uri, view: ImageView) {
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(view)
    }
}