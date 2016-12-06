package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.activity.R;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * ÃÓ≥‰ViewPager“≥√Êµƒ  ≈‰∆˜
 */
public class ViewPagerMyAdapter extends PagerAdapter {

	private int[] imageResId;// Õº∆¨ID
	// ª¨∂ØµƒÕº∆¨ºØ∫œ
	private List<ImageView> imageViews;

	private Context context;

	public ViewPagerMyAdapter(Context context, int[] imageResId,
			List<ImageView> imageViews) {
		this.context = context;
		imageResId = this.imageResId;
		this.imageViews = imageViews;
	}

	@Override
	public int getCount() {
		return imageResId.length;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(imageViews.get(arg1));
		return imageViews.get(arg1);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}
}
