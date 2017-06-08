package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import model.City;
import model.County;
import model.Province;
import model.WeatherDB;

public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB, String response){
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces!=null&&allProvinces.length>0) {
				for (String p : allProvinces) {
					String[] array= p.split("\\|");
					Province province = new Province();
					province.setProvince_code(array[0]);
					province.setProvince_name(array[1]);
					//���������������ݴ洢��Province��
					weatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(WeatherDB weatherDB, String response,int provinceId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities!=null&&allCities.length>0) {
				for (String p : allCities) {
					String[] array= p.split("\\|");
					City city = new City();
					city.setCity_code(array[0]);
					city.setCity_name(array[1]);
					city.setProvince_id(provinceId);
					//���������������ݴ洢��city��
					weatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	public synchronized static boolean handleCountiesResponse(WeatherDB weatherDB, String response,int cityId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties!=null&&allCounties.length>0) {
				for (String p : allCounties) {
					String[] array= p.split("\\|");
					County county = new County();
					county.setCounty_code(array[0]);
					county.setCounty_name(array[1]);
					county.setCity_id(cityId);
					//���������������ݴ洢��Province��
					weatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * �������������ص�JSON���ݣ����������������ݴ洢������
	 */
	public static void handleWeatherResponse(Context context,String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp=weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ���
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putBoolean("city_selected", true);
			editor.putString(City.CITY_NAME,cityName);
			editor.putString(WeatherType.WEATHER_CODE,weatherCode);
			editor.putString(WeatherType.TEMP1,cityName);
			editor.putString(WeatherType.TEMP2,cityName);
			editor.putString(WeatherType.WEATHER_DESP,weatherDesp);
			editor.putString(WeatherType.PUBLISH_TIME,publishTime);
			editor.putString("current_date",sdf.format(new Date()));
			editor.commit();
	}
}
