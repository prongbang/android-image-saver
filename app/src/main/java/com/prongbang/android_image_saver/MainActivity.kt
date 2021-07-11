package com.prongbang.android_image_saver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.prongbang.dexter.DexterPermissionsUtility
import com.prongbang.dexter.MultipleCheckPermissionsListenerImpl
import com.prongbang.dexter.PermissionsCheckerListenerImpl
import com.prongbang.dexter.PermissionsGranted
import com.prongbang.dexter.PermissionsUtility
import com.prongbang.dexter.SingleCheckPermissionListenerImpl
import com.prongbang.imagesaver.ImageExtension
import com.prongbang.imagesaver.ImageSaver
import java.io.File

class MainActivity : AppCompatActivity() {

	private val permissionsUtility: PermissionsUtility by lazy {
		DexterPermissionsUtility(
				Dexter.withContext(this),
				SingleCheckPermissionListenerImpl(),
				MultipleCheckPermissionsListenerImpl(),
				PermissionsCheckerListenerImpl(this)
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		permissionsUtility.checkWriteExternalGranted(object : PermissionsGranted() {
			override fun onGranted() {
				saveIt()
			}
		})
	}

	private fun saveIt() {
		ImageSaver.to(this)
				.directory("android-image-saver")
				.filename("test")
				.extension(ImageExtension.PNG)
				.listener(object : ImageSaver.Listener {
					override fun onSuccess(file: File) {
						Toast.makeText(this@MainActivity, "save it: ${file.absoluteFile}",
								Toast.LENGTH_SHORT)
								.show()
					}

					override fun onFailure(exception: Exception) {
						exception.printStackTrace()
					}
				})
				.save(ContextCompat.getDrawable(this, R.mipmap.ic_launcher), 80)
	}
}