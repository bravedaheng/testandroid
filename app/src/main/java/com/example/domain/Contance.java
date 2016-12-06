package com.example.domain;

import android.database.sqlite.SQLiteDatabase;

/**
 * 存放常量的类
 */
public class Contance {
	public static final String IMPORT_DB_NAME = "jingdian_db";
	public static final String IMPORT_DB_TABLENAME = "jingdian";
	public static final String TAG_INFO = "import_db";

	// 搜索记录的数据库以及表的创建
	public static final String DB_NAME = "shiyong.db";
	public static final String HISTORY_TABLENAME = "history";
	public static final String CREAT_HISTORY = "create table " + HISTORY_TABLENAME
			+ "(_id integer primary key autoincrement, h_name text not null)";

	// 打开数据库
	public static final SQLiteDatabase SQLITE = SQLiteDatabase
			.openOrCreateDatabase(
					"data/data/com.example.activity/files/jingdian_db", null);
}
