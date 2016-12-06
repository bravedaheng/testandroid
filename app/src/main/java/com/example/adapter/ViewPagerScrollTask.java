package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

public class ViewPagerScrollTask implements Runnable {

	private ViewPager mviewPager; // android-support-v4�еĻ������
	private List<ImageView> mimageViews; // ������ͼƬ����
	private int mcurrentItem = 0; // ��ǰͼƬ��������

	private Context mcontext;

	public ViewPagerScrollTask(Context context, ViewPager viewPager,
			List<ImageView> imageViews, int currentItem) {
		this.mcontext = context;
		this.mviewPager = viewPager;
		this.mimageViews = imageViews;
		this.mcurrentItem = currentItem;
	}

	// �л���ǰ��ʾ��ͼƬ
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mviewPager.setCurrentItem(mcurrentItem);// �л���ǰ��ʾ��ͼƬ
		};
	};

	@Override
	public void run() {
		synchronized (mviewPager) {
			System.out.println("mcurrentItem: " + mcurrentItem);
			mcurrentItem = (mcurrentItem + 1) % mimageViews.size();
			handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
		}
	}

}
