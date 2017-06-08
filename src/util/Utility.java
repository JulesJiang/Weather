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
	 * 解析和处理服务器返回的省级数据
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
					//将解析出来的数据存储到Province表
					weatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
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
					//将解析出来的数据存储到city表
					weatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析和处理服务器返回的县级数据
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
					//将解析出来的数据存储到Province表
					weatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
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
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中
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
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
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
