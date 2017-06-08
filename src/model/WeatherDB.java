package model;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import db.WeatherOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WeatherDB {
	/**
	 * ���ݿ���
	 */
	
	public static final String DB_NAME="weather";
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION=1;
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;
	/**
	 * �����췽��˽�л�
	 */
	private WeatherDB (Context context){
		WeatherOpenHelper dbHelper=new WeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getReadableDatabase();
	}
	/**
	 * ��ȡweatherDBʵ��
	 */
	public synchronized static WeatherDB getInstance(Context context){
		if (weatherDB==null) {
			weatherDB=new WeatherDB(context); 
		}
		return weatherDB;
	}
	
	/**
	 * ע��
	 */
	public boolean regist(UserInfo userinfo){
	
		if (userinfo!=null) {
			ContentValues values = new ContentValues();
			values.put(UserInfo.USER_NAME, userinfo.getUsername());
			values.put(UserInfo.USER_PWD, userinfo.getUserpwd());
			long i = db.insert(UserInfo.TABLE, null, values);
			if (i==1) {
				return true;
			}
		}
		return false;
	}
	/**
	 * ��¼
	 */
	public boolean loginCheck(UserInfo userInfo){
		if (userInfo!=null) {
			boolean flag=db.query(UserInfo.TABLE, null,
					UserInfo.USER_NAME+"=? and "+UserInfo.USER_PWD+"=?",
					new String[]{userInfo.getUsername(),userInfo.getUserpwd()}, null, null, null).moveToFirst();
			return true;
		}
		return false;
	}
	/**
	 * ��Provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province){
		if (province!=null) {
			ContentValues values = new ContentValues();
			values.put(Province.PROVINCE_NAME, province.getProvince_name());
			values.put(Province.PROVINCE_CODE, province.getProvince_code());
			db.insert(Province.TABLE, null, values);
		}
	}
	/**
	 * ��ȡ���ݿ���ȫ������ʡ����Ϣ
	 */
	public List<Province> loadProvinces(){
		List<Province> list =new ArrayList<Province>();
		Cursor cursor = db.query(Province.TABLE,
				null,null,null,null,null,null);
		if (cursor.moveToFirst()) {
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvince_name(cursor.getString(cursor.getColumnIndex(Province.PROVINCE_NAME)));
				province.setProvince_code(cursor.getString(cursor.getColumnIndex(Province.PROVINCE_CODE)));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
	}
	/**
	 * ��cityʵ���洢�����ݿ�
	 */
	public void saveCity(City city){
		if (city!=null) {
			ContentValues values = new ContentValues();
			values.put(City.CITY_NAME, city.getCity_name());
			values.put(City.CITY_CODE, city.getCity_code());
			values.put(City.PROVINCE_ID, city.getProvince_id());
			db.insert(City.TABLE, null, values);
		}
	}
	/**
	 * ��ȡĳʡ�ĳ�����Ϣ
	 */
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor =db.query(City.TABLE, null, City.PROVINCE_ID+"=?", 
				new String[]{String.valueOf(provinceId)} ,null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCity_name(cursor.getString(cursor.getColumnIndex(City.CITY_NAME)));
				city.setCity_code(cursor.getString(cursor.getColumnIndex(City.CITY_CODE)));
				city.setProvince_id(cursor.getInt(cursor.getColumnIndex(City.PROVINCE_ID)));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	/**
	 * ��countyʵ���洢�����ݿ�
	 */
	public void saveCounty(County county){
		if (county!=null) {
			ContentValues values = new ContentValues();
			values.put(County.COUNTY_NAME, county.getCounty_name());
			values.put(County.COUNTY_CODE, county.getCounty_code());
			values.put(County.CITY_ID, county.getCity_id());
			db.insert(County.TABLE, null, values);
		}
	}
	/**
	 *  �����ݿ��ж�ȡĳ����������������Ϣ
	 */
	public List<County> loadCounties(int cityId){
		List<County> list =new  ArrayList<County>() ;
		Cursor cursor = db.query(County.TABLE,null,County.CITY_ID+"=?",
				new String[]{String.valueOf(cityId)},null,null,null);
		if (cursor.moveToFirst()) {
			do {
				Log.i("Life", "county ��ȡ�ɹ�");
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCounty_name(cursor.getString(cursor.getColumnIndex(County.COUNTY_NAME)));
				county.setCounty_code(cursor.getString(cursor.getColumnIndex(County.COUNTY_CODE)));
				county.setCity_id(cursor.getInt(cursor.getColumnIndex(County.CITY_ID)));
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
		
	}
}
