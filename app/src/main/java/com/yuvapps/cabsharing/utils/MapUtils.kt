package com.yuvapps.cabsharing.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.yuvapps.cabsharing.R

object MapUtils {

    fun getCarBitmap(context: Context): Bitmap {

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_car)
        return Bitmap.createScaledBitmap(bitmap, Constants.BITMAP_WIDTH, Constants.BITMP_HEIGHT,false)
    }
}
