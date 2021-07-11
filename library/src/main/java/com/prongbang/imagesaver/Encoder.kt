package com.prongbang.imagesaver

import android.util.Base64

interface Encoder {
	fun decode(str: String, flags: Int): ByteArray
}

class EncoderImpl : Encoder {
	override fun decode(str: String, flags: Int): ByteArray = Base64.decode(str, flags)
}