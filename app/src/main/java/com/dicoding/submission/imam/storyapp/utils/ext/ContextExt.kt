package com.dicoding.submission.imam.storyapp.utils.ext

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showOkDialog(title: String, message: String) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("OK") { p0, _ ->
            p0.dismiss()
        }
    }.create().show()
}