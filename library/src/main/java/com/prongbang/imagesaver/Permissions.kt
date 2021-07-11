package com.prongbang.imagesaver

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

interface Permissions {
	fun readPermissionGranted(): Boolean
	fun writePermissionGranted(): Boolean
}

class RuntimePermissions(
		private val context: Context
) : Permissions {
	override fun readPermissionGranted(): Boolean {
		return ContextCompat.checkSelfPermission(context,
				android.Manifest.permission.READ_EXTERNAL_STORAGE
		) == PermissionChecker.PERMISSION_GRANTED
	}

	override fun writePermissionGranted(): Boolean {
		return ContextCompat.checkSelfPermission(context,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE
		) == PermissionChecker.PERMISSION_GRANTED
	}
}