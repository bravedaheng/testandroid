package com.example.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.domain.Contance;
import com.example.domain.ContanceKey;
import com.example.domain.Image;
import com.example.domain.ImportContance;
import com.example.domain.Person;

import com.example.Listener.MyPageChangeListener;
import com.example.activity.R;
import com.example.adapter.MyListViewDbAdapter;
import com.example.adapter.MyViewPagerAdapter;
import com.example.adapter.ViewPagerScrollTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	// 打开数据库
	// public SQLiteDatabase SQLITE = SQLiteDatabase.openOrCreateDatabase(
	// "data/data/com.example.activity/files/jingdian_db", null);
	private static final String TAG = "info";
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点
	private TextView tv_title;
	private int currentItem = 0; // 当前图片的索引号

	private EditText et_search; // 搜索框

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	private ListView listView;
	private MyListViewDbAdapter adapter;
	List<Person> bookList = new ArrayList<Person>();

	private Bitmap bitmap = null;
	private Integer[] images = Image.images;
	Person person;

	private long exitTime = 0; // 返回键推出的时间

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// 重新刷新适配器，并重新查询
				adapter.refresh(queryData());
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置为无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		Log.i(TAG, "11111");
		initViewPager(); // ViewPager的相关内容初始化
		initListView(); // ListView的相关内容初始化
		Log.i(TAG, "22222");

	}

	private void initListView() {
		// 获取ListView
		listView = (ListView) findViewById(R.id.main_listview);
		Log.i(TAG, "33333");
		bookList = queryData();
		Log.i(TAG, "44444");
		// loadingImage(p);
		// 实例化DbAdapter
		adapter = new MyListViewDbAdapter(getApplication(), bookList, bitmap);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	/*
	 * 初始化资源
	 */
	private void initViewPager() {

		imageResId = new int[] { R.drawable.a_jinci, R.drawable.a_mengshan,
				R.drawable.a_fenheerku, R.drawable.a_fenhegongyuan,
				R.drawable.a_senlingongyuan };
		titles = new String[imageResId.length];
		titles[0] = "晋祠";
		titles[1] = "蒙山大佛";
		titles[2] = "汾河二库";
		titles[3] = "汾河公园";
		titles[4] = "森林公园";

		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(titles[0]);//

		et_search = (EditText) findViewById(R.id.et_custom_title);
		et_search.setOnClickListener(this);

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyViewPagerAdapter(this, imageViews,
				imageResId));// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener(this,
				titles, dots, tv_title, currentItem));

	}

	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每3秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ViewPagerScrollTask(
				this, viewPager, imageViews, currentItem), 1, 3,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	// 查询数据库，将每一行的数据封装成一个person 对象，然后将对象添加到List中
	private List<Person> queryData() {
		Log.i(TAG, "55555");
		List<Person> list = new ArrayList<Person>();
		Log.i(TAG, "66666");
		// 调用query()获取Cursor
		Log.i(TAG, "77777");
		// Cursor c =Contance.SQLITE.query(Contance.IMPORT_DB_TABLENAME, null,
		// null, null,
		// null, null, null, null);
		Cursor c = Contance.SQLITE.rawQuery("select * from jingdian", null);
		Log.i(TAG, "88888");
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Log.i(TAG, "99999");
			int _id = c.getInt(c.getColumnIndex("_id"));
			String mingcheng = c.getString(c.getColumnIndex("mingcheng"));
			String jieshao = c.getString(c.getColumnIndex("jieshao"));
			Log.i(TAG, "aaaaa");
			// 用一个Person对象来封装查询出来的数据
			final Person p = new Person();
			p.set_id(_id);
			p.setMingcheng(mingcheng);
			p.setJieshao(jieshao);
			p.setImageView(images[c.getPosition()]);
			Log.i(TAG, "bbbbb");
			Log.i("ResultActivity", "" + c.getPosition());
			// loadingImage(p);
			list.add(p);
			Log.i(TAG, "ccccc");
			c.moveToNext();
			Log.i(TAG, "ddddd");
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_custom_title:
			Intent intent = new Intent(MainActivity.this, SearchActivity.class);
			startActivity(intent);
			MainActivity.this.finish(); // 结束当前界面
			break;

		default:
			break;
		}
	}

	/*
	 * 每一个item的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Person person = bookList.get(position);
		queryItemSql(person);

	}

	private void queryItemSql(Person person) {
		String sql = "select * from jingdian where _id = " + person.get_id();
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

		Intent intent = new Intent(getApplicationContext(),
				SingleActivity.class);
		intent.putExtra(ContanceKey.KEY_MINGCHENG, ImportContance.MINGCHENG);
		intent.putExtra(ContanceKey.KEY_JIESHAO, ImportContance.JIESHAO);
		intent.putExtra(ContanceKey.KEY_LEIXING, ImportContance.LEIXING);
		intent.putExtra(ContanceKey.KEY_JIJIE, ImportContance.JIJIE);
		intent.putExtra(ContanceKey.KEY_JIANYI, ImportContance.JIANYI);
		intent.putExtra(ContanceKey.KEY_MENPIAO, ImportContance.MENPIAO);
		intent.putExtra(ContanceKey.KEY_SHIJIAN, ImportContance.SHIJIAN);
		intent.putExtra(ContanceKey.KEY_DIZHI, ImportContance.DIZHI);
		startActivity(intent);
		MainActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}