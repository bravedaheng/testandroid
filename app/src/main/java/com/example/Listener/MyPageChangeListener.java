package com.example.Listener;

import java.util.List;

import com.example.activity.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ��ViewPager��ҳ���״̬�����ı�ʱ����
 */
public class MyPageChangeListener implements OnPageChangeListener {

	private Context mContext;

	private String[] mtitles; // ͼƬ����
	private List<View> mdots; // ͼƬ�������ĵ���Щ��
	private TextView mtv_title;
	private int mcurrentItem = 0; // ��ǰͼƬ��������

	private int oldPosition = 0;

	public MyPageChangeListener(Context context, String[] titles,
			List<View> dots, TextView tv_title, int currentItem) {
		this.mContext = context;
		this.mtitles = titles;
		this.mdots = dots;
		this.mtv_title = tv_title;
		this.mcurrentItem = currentItem;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		mcurrentItem = position;
		mtv_title.setText(mtitles[position]);
		mdots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
		mdots.get(position).setBackgroundResource(R.drawable.dot_focused);
		oldPosition = position;
	}

}
