package model;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import db.WeatherOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDB {
	/**
	 * 数据库名
	 */
	
	public static final String DB_NAME="weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION=1;
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;
	/**
	 * 将构造方法私有话
	 */
	private WeatherDB (Context context){
		WeatherOpenHelper dbHelper=new WeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getReadableDatabase();
	}
	/**
	 * 获取weatherDB实例
	 */
	public synchronized static WeatherDB getInstance(Context context){
		if (weatherDB==null) {
			weatherDB=new WeatherDB(context); 
		}
		return weatherDB;
	}
	/**
	 * 将userinfo实例存储到数据库
	 */
	public void saveUserinfo(UserInfo userinfo){
	
		if (userinfo!=null) {
			ContentValues values = new ContentValues();
			values.put("username", userinfo.getUsername());
			values.put("userpwd", userinfo.getUserpwd());
			db.insert("userinfo", null, values);
		}
	}
	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province){
		if (province!=null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvince_name());
			values.put("province_code", province.getProvince_code());
			db.insert("province", null, values);
		}
	}
	/**
	 * 读取数据库中全国所哟省份信息
	 */
	public List<Province> loadProvinces(){
		List<Province> list =new ArrayList<Province>();
		Cursor cursor = db.query("Province",
				null,null,null,null,null,null);
		if (cursor.moveToFirst()) {
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvince_name(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvince_code(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
	}
	/**
	 * 将city实例存储到数据库
	 */
	public void saveCity(City city){
		if (city!=null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCity_name());
			values.put("city_code", city.getCity_code());
			values.put("province_id", city.getProvince_id());
			db.insert("City", null, values);
		}
	}
	/**
	 * 获取某省的城市信息
	 */
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor =db.query("City", null, "province_id=?", 
				new String[]{String.valueOf(provinceId)} ,null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	/**
	 * 将county实例存储到数据库
	 */
	public void saveCounty(County county){
		if (county!=null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCounty_name());
			values.put("county_code", county.getCounty_code());
			values.put("city_id", county.getCity_id());
			db.insert("County", null, values);
		}
	}
	/**
	 *  从数据库中读取某城市下面所有县信息
	 */
	public List<County> loadCounties(int cityId){
		List<County> list =new  ArrayList<County>() ;
		Cursor cursor = db.query("County",null,"city_id=?",
				new String[]{String.valueOf(cityId)},null,null,null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCounty_name(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCounty_code(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
		
	}
}
