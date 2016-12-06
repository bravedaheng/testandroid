package com.example.adapter;

import java.util.List;

import com.example.activity.R;
import com.example.adapter.MyListViewDbAdapter.ViewHolder;
import com.example.domain.SearchShowBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchShowLvAdapter extends BaseAdapter {

	private Context mContext;
	private List<SearchShowBean> mdata;

	public SearchShowLvAdapter(Context context, List<SearchShowBean> data) {
		this.mContext = context;
		this.mdata = data;
	}

	@Override
	public int getCount() {
		return mdata.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// �����ͼ����null���͸�������
			convertView = View.inflate(mContext,
					R.layout.search_xianshi_lv_item, null);
			// ��ʼ��hodler
			holder = new ViewHolder();
			// ÿ�����ͼ������һ����
			holder.textView = (TextView) convertView
					.findViewById(R.id.search_xianshi_tv);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.search_xianshi_iv);
			// ��View�������,Ȼ����getTag()ȡ����
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SearchShowBean bean = mdata.get(position);
		holder.textView.setText(bean.getJingdian());
		holder.imageView.setImageDrawable(bean.getImage());

		return convertView;
	}

	/*
	 * ��ʱ�Ĵ�����
	 */
	private static class ViewHolder {
		TextView textView;
		ImageView imageView;
	}

}
