package com.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.domain.Contance;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

public class SplashActivity extends Activity {

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// �жϲ��������ݿ�
				importDB();
				Log.i(Contance.TAG_INFO, "����ɹ�");
				break;
			default:
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �趨�ޱ�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ���ò���
		setContentView(R.layout.splash_activity);

		Message message = new Message();
		message.what = 1;
		handler.sendMessage(message);

		Log.i(Contance.TAG_INFO, "��תǰ");

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				enterHome();
				Log.i(Contance.TAG_INFO, "��ת��");
			}
		}, 1000);
	}

	/*
	 * �жϲ��������ݿ�
	 */
	private void importDB() {
		File file = new File(getFilesDir(), Contance.IMPORT_DB_NAME);
		// �ж��Ƿ����
		if (file.exists() && file.length() > 0) {
			Log.i(Contance.TAG_INFO, "�Ѵ���");
		} else {
			// ʹ��AssetManager��������assets�ļ���
			AssetManager asset = getAssets();
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = asset.open(Contance.IMPORT_DB_NAME);
				fos = new FileOutputStream(file);
				int len = 0;
				byte[] buf = new byte[1024];
				while ((len = is.read(buf)) != -1) {
					fos.write(buf, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.i(Contance.TAG_INFO, "�����");
	}

	/*
	 * ��ת��������
	 */
	private void enterHome() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		// ������ǰҳ��
		SplashActivity.this.finish();
	}

}
