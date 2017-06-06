package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * user�������
	 */
	public static final String CREATE_USERINFO="create table userinfo ("
	 +"id integer primary key autoincrement,"
	 +"user_name text,"
	 +"user_pwd text)";
	/**
	 * province�������
	 */
	public static final String CREATE_PROVINCE="create table province ("
			 +"id integer primary key autoincrement,"
			 +"province_name text,"
			 +"province_code text)";
	/**
	 * city�������
	 */
	public static final String CREATE_CITY="create table city ("
			 +"id integer primary key autoincrement,"
			 +"city_name text,"
			 +"city_code text," 
			 +"province_id integer)";
	/**
	 * county�������
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
		db.execSQL(CREATE_USERINFO);//�����û���
		db.execSQL(CREATE_PROVINCE);//����province��
		db.execSQL(CREATE_CITY);//����city��
		db.execSQL(CREATE_COUNTY);//����county��
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
