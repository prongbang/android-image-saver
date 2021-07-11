package com.prongbang.imagesaver

import android.graphics.Bitmap
import android.graphics.BitmapFactory

interface BitmapUtility {
	fun decodeByteArray(data: ByteArray, offset: Int, length: Int): Bitmap
}

class BitmapUtilityImpl : BitmapUtility {
	override fun decodeByteArray(data: ByteArray, offset: Int, length: Int): Bitmap =
			BitmapFactory.decodeByteArray(data, offset, length)
}