/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */
package tool.compet.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import tool.compet.core.DkFiles
import java.io.*

/**
 * Utility class for Bitmap. Extension of Bitmap is `DkBitmapExt`.
 *
 * Ref: https://developer.android.com/codelabs/advanced-android-kotlin-training-shaders#6
 */
object DkBitmaps {
	/**
	 * Decode given bitmap-file to get bitmap's dimension (width x height).
	 * This does not allocate bitmap pixels, no bitmap was generated.
	 *
	 * Refer: https://developer.android.com/topic/performance/graphics/load-bitmap
	 *
	 * @param src Contains bitmap content.
	 */
	fun decodeDimension(src: File): IntArray {
		val opts = BitmapFactory.Options().apply {
			this.inJustDecodeBounds = true
		}

		// Decode with `inJustDecodeBounds` to get bitmap dimension
		BitmapFactory.decodeFile(src.absolutePath, opts)

		return intArrayOf(opts.outWidth, opts.outHeight)
	}

	/**
	 * Instead of loading bitmap totally into memory,
	 * we can use this to load a region of given bitmap for better memory usage.
	 */
	@Throws(IOException::class)
	fun decodeRegion(
		target: InputStream,
		left: Int, top: Int, right: Int, bottom: Int,
		opts: BitmapFactory.Options? = null
	): Bitmap? {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			BitmapRegionDecoder.newInstance(target)?.decodeRegion(Rect(left, top, right, bottom), opts)
		}
		else {
			// This method was deprecated from api 31.
			@Suppress("DEPRECATION")
			BitmapRegionDecoder.newInstance(target, false)?.decodeRegion(Rect(left, top, right, bottom), opts)
		}
	}

	@Throws(IOException::class)
	fun save(input: Bitmap, dstFilePath: String) {
		save(input, File(dstFilePath))
	}

	@Throws(IOException::class)
	fun save(bitmap: Bitmap, dst: File) {
		DkFiles.createFile(dst)
		return save(bitmap, FileOutputStream(dst))
	}

	@Throws(IOException::class)
	fun save(bitmap: Bitmap, dst: OutputStream) {
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, dst)
		dst.close()
	}

	fun load(file: File): Bitmap? {
		return load(file.path)
	}

	fun load(context: Context, imgRes: Int): Bitmap {
		val opts = BitmapFactory.Options().apply {
			this.inPreferredConfig = Bitmap.Config.ARGB_8888
		}
		return load(context, imgRes, opts)
	}

	fun load(context: Context, imgRes: Int, opts: BitmapFactory.Options): Bitmap {
		return BitmapFactory.decodeResource(context.resources, imgRes, opts)
	}

	fun load(srcFilePath: String): Bitmap? {
		val opts = BitmapFactory.Options().apply {
			this.inPreferredConfig = Bitmap.Config.ARGB_8888
		}
		return load(srcFilePath, opts)
	}

	fun load(srcFilePath: String, opts: BitmapFactory.Options): Bitmap? {
		return BitmapFactory.decodeFile(srcFilePath, opts)
	}

	@Throws(IOException::class)
	fun load(context: Context, uri: Uri): Bitmap? {
		return load(context.contentResolver.openInputStream(uri)!!)
	}

	fun load(src: InputStream): Bitmap? {
		val opts = BitmapFactory.Options().apply {
			this.inPreferredConfig = Bitmap.Config.ARGB_8888
		}
		return load(src, opts)
	}

	fun load(src: InputStream, opts: BitmapFactory.Options): Bitmap? {
		return BitmapFactory.decodeStream(src, null, opts)
	}
}
