package com.example.lint.util.ext


import android.content.res.Resources

internal fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

internal fun Int.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}