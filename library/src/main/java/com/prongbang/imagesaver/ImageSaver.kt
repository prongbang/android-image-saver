package com.prongbang.imagesaver

import android.content.Context
import java.io.File

object ImageSaver {

	fun to(context: Context): Saver =
			SaverImpl(
					context,
					RuntimePermissions(context),
					ConverterImpl(),
					BitmapUtilityImpl(),
					EncoderImpl()
			)

	fun convert(): Converter = ConverterImpl()

	interface Listener {
		fun onSuccess(file: File)
		fun onFailure(exception: Exception)
	}
}