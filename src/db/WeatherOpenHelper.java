package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * user表建表语句
	 */
	public static final String CREATE_USERINFO="create table userinfo ("
	 +"id integer primary key autoincrement,"
	 +"user_name text,"
	 +"user_pwd text)";
	/**
	 * province表建表语句
	 */
	public static final String CREATE_PROVINCE="create table province ("
			 +"id integer primary key autoincrement,"
			 +"province_name text,"
			 +"province_code text)";
	/**
	 * city表建表语句
	 */
	public static final String CREATE_CITY="create table city ("
			 +"id integer primary key autoincrement,"
			 +"city_name text,"
			 +"city_code text," 
			 +"province_id integer)";
	/**
	 * county表建表语句
	 */
	public static final String CREATE_COUNTY="create table county ("
			 +"id integer primary key autoincrement,"
			 +"county_name text,"
			 +"county_code text,"
			 +"city_id integer)";
	public WeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USERINFO);//创建用户表
		db.execSQL(CREATE_PROVINCE);//创建province表
		db.execSQL(CREATE_CITY);//创建city表
		db.execSQL(CREATE_COUNTY);//创建county表
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
