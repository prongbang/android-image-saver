package com.prongbang.imagesaver

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

interface Saver {
	fun directory(dir: String): Saver
	fun subDirectory(subDir: String): Saver
	fun filename(filename: String): Saver
	fun extension(imageExtension: ImageExtension): Saver
	fun listener(listen: ImageSaver.Listener): Saver
	fun save(bitmap: Bitmap?, quality: Int = 100)
	fun save(base64: String?, quality: Int = 100)
	fun save(drawable: Drawable?, quality: Int = 100)
}

class SaverImpl(
		private val context: Context,
		private val permissions: Permissions,
		private val converter: Converter,
		private val bitmapUtility: BitmapUtility,
		private val encoder: Encoder
) : Saver {

	private var filename: String? = null
	private var fileExtension: String? = null
	private var directory: String = context.applicationContext.packageName
	private var subDirectory: String? = null
	private var listener: ImageSaver.Listener? = null

	override fun directory(dir: String): Saver {
		if (dir.trim().isNotEmpty()) {
			directory = dir
		} else {
			throw NullPointerException(DIRECTORY_MESSAGE)
		}
		return this
	}

	override fun subDirectory(subDir: String): Saver {
		subDirectory = if (subDir.trim().isNotEmpty()) {
			if (subDirectory != null) "$subDirectory/$subDir" else subDir
		} else {
			throw NullPointerException(SUB_DIRECTORY_MESSAGE)
		}
		return this
	}

	override fun filename(filename: String): Saver {
		if (filename.trim().isNotEmpty()) {
			val name = filename.replace(" ", "")
			this.filename = name
		} else {
			throw NullPointerException(FILE_NAME_MESSAGE)
		}
		return this
	}

	override fun extension(imageExtension: ImageExtension): Saver {
		fileExtension = imageExtension.value
		return this
	}

	override fun listener(listen: ImageSaver.Listener): Saver {
		listener = listen
		return this
	}

	override fun save(bitmap: Bitmap?, quality: Int) {
		if (!permissions.writePermissionGranted()) throw SecurityException(WRITE_PERMISSION_MESSAGE)

		if (filename == null) throw NullPointerException(FILE_NAME_MESSAGE)

		if (fileExtension == null) throw NullPointerException(FILE_EXTENSION_MESSAGE)

		if (bitmap == null) throw NullPointerException(BITMAP_MESSAGE)

		if (quality < 1 && quality <= 100) throw Exception(QUALITY_MESSAGE)

		processSaveToFile(bitmap, quality)
	}

	override fun save(base64: String?, quality: Int) {
		if (!permissions.writePermissionGranted()) throw SecurityException(WRITE_PERMISSION_MESSAGE)

		if (filename == null) throw NullPointerException(FILE_NAME_MESSAGE)

		if (fileExtension == null) throw NullPointerException(FILE_EXTENSION_MESSAGE)

		if (base64 == null || base64.isEmpty()) throw Exception(BASE64_MESSAGE)

		if (quality < 1 && quality <= 100) throw Exception(QUALITY_MESSAGE)

		val decodedString = encoder.decode(base64, Base64.DEFAULT)
		val bitmap = bitmapUtility.decodeByteArray(decodedString, 0, decodedString.size)

		processSaveToFile(bitmap, quality)
	}

	override fun save(drawable: Drawable?, quality: Int) {
		if (!permissions.writePermissionGranted()) throw SecurityException(WRITE_PERMISSION_MESSAGE)

		if (filename == null) throw NullPointerException(FILE_NAME_MESSAGE)

		if (fileExtension == null) throw NullPointerException(FILE_EXTENSION_MESSAGE)

		if (drawable == null) throw NullPointerException(DRAWABLE_MESSAGE)

		if (quality < 1 && quality <= 100) throw Exception(QUALITY_MESSAGE)

		val bitmap = converter.drawableToBitmap(drawable)

		processSaveToFile(bitmap, quality)
	}

	private fun processSaveToFile(bitmap: Bitmap, quality: Int) {
		val dir = if (subDirectory == null)
			File("${context.getExternalFilesDir(null)?.absolutePath}/$directory")
		else
			File("${context.getExternalFilesDir(null)?.absolutePath}/$directory/$subDirectory")

		if (!dir.exists()) dir.mkdirs()

		val file = File(dir, "$filename$fileExtension")

		val outStream: FileOutputStream
		try {
			outStream = FileOutputStream(file)
			compressBitmap(bitmap, quality, outStream)
			outStream.flush()
			outStream.close()
		} catch (e: FileNotFoundException) {
			listener?.onFailure(e)
		} catch (e: IOException) {
			listener?.onFailure(e)
		} finally {
			listener?.onSuccess(file)
		}
	}

	private fun compressBitmap(bitmap: Bitmap, quality: Int, outStream: OutputStream) {
		when (fileExtension) {
			ImageExtension.PNG.value -> bitmap.compress(Bitmap.CompressFormat.PNG, quality, outStream)
			ImageExtension.JPEG.value -> bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream)
			ImageExtension.WEBP.value -> bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outStream)
		}
	}

	companion object {
		private const val QUALITY_MESSAGE = "Bitmap quality must be between 1 to 100"
		private const val DIRECTORY_MESSAGE = "Directory cannot be null or empty"
		private const val SUB_DIRECTORY_MESSAGE = "Sub directory name cannot be null or empty"
		private const val FILE_NAME_MESSAGE = "File name cannot be null or empty"
		private const val WRITE_PERMISSION_MESSAGE = "Write external storage permission not granted"
		private const val BITMAP_MESSAGE = "Bitmap cannot be null or empty"
		private const val FILE_EXTENSION_MESSAGE = "File extension is null"
		private const val BASE64_MESSAGE = "Base64 encoded string should not be null or empty"
		private const val DRAWABLE_MESSAGE = "Drawable cannot be null"
	}
}