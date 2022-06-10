package com.example.link.util.resource

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formArgs: Any): String =
        context.getString(resId, *formArgs)

    override fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getColorStateList(@ColorRes resId: Int): ColorStateList =
        context.getColorStateList(resId)

    override fun getDrawable(@DrawableRes resId: Int): Drawable? =
        ContextCompat.getDrawable(context, resId)


}