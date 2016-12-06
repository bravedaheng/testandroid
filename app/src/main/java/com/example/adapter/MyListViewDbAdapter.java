package com.example.adapter;

import java.util.List;

import com.example.activity.MainActivity;
import com.example.activity.R;
import com.example.activity.SingleActivity;
import com.example.domain.Person;
import com.example.utils.BitmapCompressTools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListViewDbAdapter extends BaseAdapter {
	private List<Person> mlist;
	private Context mcontext;
	private LayoutInflater mlayoutInflater;
	private Bitmap mbitmap = null;

	public MyListViewDbAdapter(Context context, List<Person> list, Bitmap bitmap) {
		mlayoutInflater = LayoutInflater.from(context);
		this.mcontext = context;
		this.mlist = list;
		this.mbitmap = bitmap;
	}

	// 刷新适配器,更新数据
	public void refresh(List<Person> list) {
		this.mlist = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Person p = mlist.get(position);
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.list_item, null);
			holder.mingcheng = (TextView) convertView
					.findViewById(R.id.tv_mingcheng);
			holder.jieshao = (TextView) convertView
					.findViewById(R.id.tv_jieshao);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageview);
			// holder.leixing = (TextView) convertView
			// .findViewById(R.id.tv_leixing);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mingcheng.setText(p.getMingcheng());
		holder.jieshao.setText(p.getJieshao());
		// holder.imageView.setImageDrawable(getResources().getDrawable(
		// p.getImageView()));
		mbitmap = BitmapCompressTools.decodeSampledBitmapFromResource(
				mcontext.getResources(), p.getImageView(), 100, 100);
		holder.imageView.setImageBitmap(mbitmap);

		return convertView;
	}

	public class ViewHolder {
		public TextView mingcheng;
		public TextView jieshao;
		public TextView id;
		public ImageView imageView;
	}

}
