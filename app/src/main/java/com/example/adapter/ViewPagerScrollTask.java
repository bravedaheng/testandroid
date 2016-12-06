package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

public class ViewPagerScrollTask implements Runnable {

	private ViewPager mviewPager; // android-support-v4中的滑动组件
	private List<ImageView> mimageViews; // 滑动的图片集合
	private int mcurrentItem = 0; // 当前图片的索引号

	private Context mcontext;

	public ViewPagerScrollTask(Context context, ViewPager viewPager,
			List<ImageView> imageViews, int currentItem) {
		this.mcontext = context;
		this.mviewPager = viewPager;
		this.mimageViews = imageViews;
		this.mcurrentItem = currentItem;
	}

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mviewPager.setCurrentItem(mcurrentItem);// 切换当前显示的图片
		};
	};

	@Override
	public void run() {
		synchronized (mviewPager) {
			System.out.println("mcurrentItem: " + mcurrentItem);
			mcurrentItem = (mcurrentItem + 1) % mimageViews.size();
			handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
		}
	}

}
