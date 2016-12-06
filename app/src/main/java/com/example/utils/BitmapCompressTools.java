package com.example.utils;

import android.R.integer;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapCompressTools {
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// ����BitmapFactory���ý���Ĳ���
		final BitmapFactory.Options options = new BitmapFactory.Options();

		// �ӽ������л�ȡԭʼͼƬ�ĸ߶ȣ������ͱ�����ֱ�������ڴ�ؼ�
		options.inJustDecodeBounds = true;
		// inJustDecodeBounds
		// �������Ϊtrue���������ͼ���������ȫ���룬�༴decodeXyz()����ֵΪnull��
		// ����Options��outAbc�н����ͼ��Ļ�����Ϣ��
		BitmapFactory.decodeResource(res, resId, options);

		// ����ֵinSampleSize��ʾ����ͼ��СΪԭʼͼƬ��С�ļ���֮һ
		// options.inSampleSize = calculateInSampleSize(options, reqWidth,
		// reqHeight);
		options.inSampleSize = 10;

		// ѹ��������Խ�inJustDecodeBounds����Ϊfalse��
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/*
	 * ָ��ͼƬ�����ű�����
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		// ԭʼͼƬ�Ŀ��
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// // ��������ѹ����ʽ������ѡ��
			// /*
			// * ��ʽ1
			// */
			// final int halfHeight = height / 2;
			// final int halfWidth = width / 2;
			// while (((halfWidth / inSampleSize) > reqWidth)
			// && ((halfHeight / inSampleSize) > reqHeight)) {
			// inSampleSize *= 2;
			// }
			/*
			 * ��ʽ2
			 */
			// ����ѹ���ı���
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

}
