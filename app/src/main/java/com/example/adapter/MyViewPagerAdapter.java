package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
/**
 * ÃÓ≥‰ViewPager“≥√Êµƒ  ≈‰∆˜
 */
public class MyViewPagerAdapter extends PagerAdapter {

	private List<ImageView> mimageViews; // ª¨∂ØµƒÕº∆¨ºØ∫œ
	private int[] mimageResId; // Õº∆¨ID

	private Context mContext;

	public MyViewPagerAdapter(Context Context, List<ImageView> imageViews,
			int[] imageResId) {
		this.mContext = Context;
		this.mimageResId = imageResId;
		this.mimageViews = imageViews;
	}

	@Override
	public int getCount() {
		return mimageResId.length;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mimageViews.get(arg1));
		return mimageViews.get(arg1);
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
