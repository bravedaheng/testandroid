package com.example.utils;

import android.R.integer;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapCompressTools {
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// 给定BitmapFactory设置解码的参数
		final BitmapFactory.Options options = new BitmapFactory.Options();

		// 从解码器中获取原始图片的高度，这样就避免了直接申请内存控件
		options.inJustDecodeBounds = true;
		// inJustDecodeBounds
		// 如果设置为true，并不会把图像的数据完全解码，亦即decodeXyz()返回值为null，
		// 但是Options的outAbc中解出了图像的基本信息。
		BitmapFactory.decodeResource(res, resId, options);

		// 属性值inSampleSize表示缩略图大小为原始图片大小的几分之一
		// options.inSampleSize = calculateInSampleSize(options, reqWidth,
		// reqHeight);
		options.inSampleSize = 10;

		// 压缩完后便可以将inJustDecodeBounds设置为false了
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/*
	 * 指定图片的缩放比例里
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		// 原始图片的宽高
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// // 这里两种压缩方式，可以选择
			// /*
			// * 方式1
			// */
			// final int halfHeight = height / 2;
			// final int halfWidth = width / 2;
			// while (((halfWidth / inSampleSize) > reqWidth)
			// && ((halfHeight / inSampleSize) > reqHeight)) {
			// inSampleSize *= 2;
			// }
			/*
			 * 方式2
			 */
			// 计算压缩的比例
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

}
