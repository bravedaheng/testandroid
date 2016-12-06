package com.example.activity;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.example.adapter.SearchHistoryAdapter;
import com.example.adapter.SearchShowLvAdapter;
import com.example.db.HistoryHelper;
import com.example.domain.Contance;
import com.example.domain.ContanceKey;
import com.example.domain.HistoryShowBean;
import com.example.domain.ImportContance;
import com.example.domain.JingdianName;
import com.example.domain.SearchShowBean;
import com.example.utils.SearchClearEditText;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private ImageButton ib_back_normal; // ��������෵�ذ�ť
	private TextView tv_search; // �������ұߵ���������
	SearchClearEditText clear_et;
	private ListView lv_search_xianshi, lv_search_history;
	private Button clear_search_history_btn;

	// ��ʼ�����ݿ����
	HistoryHelper helper;
	private SearchHistoryAdapter his_adapter;

	private List<SearchShowBean> sea_data;
	private List<HistoryShowBean> his_data;
	static List<String> list = new ArrayList<String>();

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// ��������¼��ť��ˢ�½���
				his_adapter.refresh(queryHistoryData());
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ��⣬ʹ���Լ��Ĳ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.clear_et_activity);

		init();

	}

	/*
	 * ��ʼ�����
	 */
	private void init() {
		ib_back_normal = (ImageButton) findViewById(R.id.ib_back_normal);
		tv_search = (TextView) findViewById(R.id.tv_search);
		clear_et = (SearchClearEditText) findViewById(R.id.et_clear);
		clear_search_history_btn = (Button) findViewById(R.id.clear_search_history_btn);

		lv_search_xianshi = (ListView) findViewById(R.id.search_xianshi_lv);
		lv_search_history = (ListView) findViewById(R.id.search_history_lv);

		his_data = queryHistoryData();

		his_adapter = new SearchHistoryAdapter(his_data, this);
		lv_search_history.setAdapter(his_adapter);

		ib_back_normal.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		clear_search_history_btn.setOnClickListener(this);

		lv_search_history.setOnItemClickListener(this);
		lv_search_xianshi.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back_normal:
			Intent intent = new Intent(SearchActivity.this, MainActivity.class);
			startActivity(intent);
			SearchActivity.this.finish(); // ������ǰ����
			// lv_search_xianshi.setVisibility(View.GONE);
			break;
		case R.id.tv_search: // ���������ť
			/*
			 * ���������ť����գ�����Ϊ����ʹ��list���ϱ���ģ��������һ������������ݺ�
			 * ֱ��������һ�����ͻ���ӣ���������գ�����ÿ����ʾ�Ķ������µ�
			 */
			list.removeAll(list);
			// ���������ť��ʹ����ʾ���������listview��ʾ����
			lv_search_xianshi.setVisibility(View.VISIBLE);
			lv_search_history.setVisibility(View.GONE);
			clear_search_history_btn.setVisibility(View.GONE);

			String search = clear_et.getText().toString().trim();
			int count = 0;
			for (int i = 0; i < JingdianName.names.length; i++) {
				if (JingdianName.names[i].contains(search) == true) {
					list.add(JingdianName.names[i]);
					count++;
				}
			}
			if (count == 0) { // ���һ��Ҳû�в鵽������ʾû�о���
				list.removeAll(list); // ��ռ������ݣ����²�ѯ
				getData(list, lv_search_xianshi);
				Toast.makeText(getApplicationContext(), "û�иþ���",
						Toast.LENGTH_SHORT).show();
			} else { // �����ݾ���ʾ
				getData(list, lv_search_xianshi);
			}

			// �������ݿ�����
			insertHistory(search);

			break;

		case R.id.clear_search_history_btn: // ��������ʷ��¼��ť
			deleteHistory();
			break;
		default:
			break;
		}
	}

	/*
	 * ɾ����ʷ��¼
	 */
	private void deleteHistory() {
		helper = new HistoryHelper(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		// ɾ����
		db.execSQL("delete from " + Contance.HISTORY_TABLENAME);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// �˴�handler����һ��message����������ui
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
		// his_adapter.refresh(queryHistoryData());
		db.close();
	}

	/*
	 * ������ʷ��¼�������ť�����edittext��ֵ��д�����ݿ�
	 */
	private void insertHistory(String search) {
		helper = new HistoryHelper(getApplicationContext());
		// �������ݿ�
		SQLiteDatabase db = helper.getWritableDatabase();
		Log.i("create", "���ݿ��history�����ɹ�");
		int count = 0;
		// ��ѯ���ݿ⣬�ж�edittext�������Ƿ��Ѿ����ڣ���������ˣ��Ͳ�д�ˣ���������ڣ��Ͳ������ݿ�
		// ȡ�ز�ѯ���history���h_name�е�list����
		List<String> list = queryHistorySql();
		for (int i = 0; i < list.size(); i++) {
			// ��ȡ��������������ݣ��������Ѿ����ڵļ�¼�ȶԣ������һ���ģ���count���ӣ�
			if (list.get(i).equals(search)) {
				count++;
			}
		}
		// ���count == 0��˵��û���ظ������ݣ��Ϳ��Բ������ݿ�history����
		if (count == 0) {
			db.execSQL("insert into " + Contance.HISTORY_TABLENAME
					+ " values(?,?)", new Object[] { null, search });
			Log.i("create", "���ݿ��history���ݲ���ɹ�");
		} else {
			// Toast.makeText(getApplicationContext(), "�Ѵ���",
			// Toast.LENGTH_SHORT)
			// .show();
		}

		db.close();
	}

	/*
	 * ��ѯ���ݿ��h_nameһ�У�Ȼ��ŵ�list�����У������ж��Ƿ��������
	 */
	private List<String> queryHistorySql() {
		helper = new HistoryHelper(getApplicationContext());
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ Contance.HISTORY_TABLENAME, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// ��ѯ���ݿ⣬ȡ��h_name��һ�У�Ȼ��ȫ���ŵ�list�����У���ǰ����ô˷�����ʱ�������ж�
			String name = cursor.getString(cursor.getColumnIndex("h_name"));
			list.add(name);
			cursor.moveToNext();
		}

		db.close();
		// ����һ��list����
		return list;
	}

	/*
	 * ���������ť�󣬲�ѯ�����Ľ��
	 */
	private void getData(List<String> list, ListView listView) {
		sea_data = new ArrayList<SearchShowBean>();
		for (int i = 0; i < list.size(); i++) {
			SearchShowBean bean = new SearchShowBean();
			bean.setJingdian(list.get(i));
			bean.setImage(getResources().getDrawable(
					R.drawable.umeng_socialize_search_icon));
			sea_data.add(bean);
		}
		listView.setAdapter(new SearchShowLvAdapter(this, sea_data));
	}

	// ��ÿһ�е����ݷ�װ��һ��HistoryShowBean����Ȼ��ŵ�his_list��
	private List<HistoryShowBean> queryHistoryData() {
		helper = new HistoryHelper(getApplicationContext());
		List<HistoryShowBean> his_list = new ArrayList<HistoryShowBean>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor his_c = db.rawQuery("select * from "
				+ Contance.HISTORY_TABLENAME, null);

		his_c.moveToFirst();
		while (!his_c.isAfterLast()) {
			int h_id = his_c.getInt(his_c.getColumnIndex("_id"));
			String h_name = his_c.getString(his_c.getColumnIndex("h_name"));

			// ��һ��HistoryShowBean������װ�õ�������
			final HistoryShowBean his_bean = new HistoryShowBean();
			his_bean.setJingdian(h_name);
			his_bean.setImage(getResources().getDrawable(
					R.drawable.history_icon));
			his_list.add(his_bean);
			his_c.moveToNext();
		}
		if (his_list.size() == 0) {
			clear_search_history_btn.setVisibility(View.GONE);
		}
		db.close();
		return his_list;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {// ���µ������BACK��ͬʱû���ظ�
			Intent intent = new Intent(SearchActivity.this, MainActivity.class);
			startActivity(intent);
			SearchActivity.this.finish();
			// ���ﲻ��Ҫִ�и���ĵ���¼�������ֱ��return
			return true;
		}
		// ����ִ�и������������¼�
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) { // ���listview��id
		case R.id.search_xianshi_lv: // ��ʾlistview
			// ��õ�ǰ�����item
			view = lv_search_xianshi.getChildAt(position);
			// ���item�е�textview�ؼ�������ʼ��
			TextView tv = (TextView) view.findViewById(R.id.search_xianshi_tv);
			// ���textview��ֵ
			String str = tv.getText().toString().trim();
			// �������ݿ��ѯ������ת
			listViewJump(str);
			break;
		case R.id.search_history_lv:
			view = lv_search_history.getChildAt(position);
			TextView tv1 = (TextView) view.findViewById(R.id.search_history_tv);
			String str1 = tv1.getText().toString().trim();
			String string = null;
			// ֮ǰ�Ѿ������ݿ���С�mingcheng����ֵ�Ѿ��ŵ���һ��������
			// �жϵ����item�е�textview��ֵ��mingcheng���Ƿ���ڣ�������ڣ��Ͱ��Ǹ�ֵ�������ݿ��ѯ
			Log.i("searchAcitivity", "11111");
			// Boolean boolean1 = null;
			int count1 = 0;
			for (int i = 0; i < JingdianName.names.length; i++) {
				Log.i("searchAcitivity", "22222");
				if (JingdianName.names[i].equals(str1)) {
					Log.i("searchAcitivity", "33333");
					string = JingdianName.names[i];
					// ��������forѭ�������������
					listViewJump(string);
					Log.i("searchAcitivity", "44444");
					// boolean1 = true;
					count1++;
					break;
				}
			}
			if (count1 == 0) {
				Log.i("searchAcitivity", "88888");
				Toast.makeText(getApplicationContext(), "û�иþ���",
						Toast.LENGTH_SHORT).show();
			}
			Log.i("searchAcitivity", "99999");

			break;
		default:
			break;
		}
	}

	private void listViewJump(String str) {
		String sql = "select * from jingdian where mingcheng = '" + str + "'";
		Cursor c = Contance.SQLITE.rawQuery(sql, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ImportContance.MINGCHENG = c.getString(c
					.getColumnIndex("mingcheng"));
			ImportContance.JIESHAO = c.getString(c.getColumnIndex("jieshao"));
			ImportContance.LEIXING = c.getString(c.getColumnIndex("leixing"));
			ImportContance.JIJIE = c.getString(c.getColumnIndex("jijie"));
			ImportContance.JIANYI = c.getString(c.getColumnIndex("jianyi"));
			ImportContance.MENPIAO = c.getString(c.getColumnIndex("menpiao"));
			ImportContance.SHIJIAN = c.getString(c.getColumnIndex("shijian"));
			ImportContance.DIZHI = c.getString(c.getColumnIndex("dizhi"));
			c.moveToNext();
		}

		c.close();

		Intent intent = new Intent(SearchActivity.this, SingleActivity.class);
		intent.putExtra(ContanceKey.KEY_MINGCHENG, ImportContance.MINGCHENG);
		intent.putExtra(ContanceKey.KEY_JIESHAO, ImportContance.JIESHAO);
		intent.putExtra(ContanceKey.KEY_LEIXING, ImportContance.LEIXING);
		intent.putExtra(ContanceKey.KEY_JIJIE, ImportContance.JIJIE);
		intent.putExtra(ContanceKey.KEY_JIANYI, ImportContance.JIANYI);
		intent.putExtra(ContanceKey.KEY_MENPIAO, ImportContance.MENPIAO);
		intent.putExtra(ContanceKey.KEY_SHIJIAN, ImportContance.SHIJIAN);
		intent.putExtra(ContanceKey.KEY_DIZHI, ImportContance.DIZHI);
		startActivity(intent);
		SearchActivity.this.finish();
	}
}
