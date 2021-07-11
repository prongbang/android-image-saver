package com.prongbang.imagesaver

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

interface Converter {
	fun drawableToBitmap(drawable: Drawable): Bitmap
}

class ConverterImpl : Converter {

	override fun drawableToBitmap(drawable: Drawable): Bitmap {
		if (drawable is BitmapDrawable) {
			return drawable.bitmap
		}
		var width = drawable.intrinsicWidth
		width = if (width > 0) width else 1
		var height = drawable.intrinsicHeight
		height = if (height > 0) height else 1

		val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		drawable.setBounds(0, 0, canvas.width, canvas.height)
		drawable.draw(canvas)
		return bitmap
	}
}