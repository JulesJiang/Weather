package activity;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;
import util.WeatherType;
import model.City;
import model.County;

import com.julse.com.R;

import android.R.array;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	/**
	 * 显示城市名
	 */
	private TextView cityNameText;
	/**
	 * 显示发布时间
	 */
	private TextView publishText;
	/**
	 * 显示天气描述信息
	 */
	private TextView weatherDespText;
	/**
	 * 显示气温1
	 */
	private TextView temp1Text;
	/**
	 * 显示气温2
	 */
	private TextView temp2Text;
	/**
	 * 显示当前日期
	 */
	private TextView currentDateText;
	/**
	 * 切换城市按钮
	 */
	private Button switchCityBtn;
	/**
	 * 更新天气按钮
	 */
	private Button refreshWeatherBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCityBtn = (Button) findViewById(R.id.switch_city);
		refreshWeatherBtn = (Button) findViewById(R.id.city_name);
		
		String countyCode = getIntent().getStringExtra(County.COUNTY_CODE);
		if (!TextUtils.isEmpty(countyCode)) {
			//有县级代号时就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.VISIBLE);
			queryWeatherCode(countyCode);
		}else {
			//没有县级代号时就显示本地天气
			showWeather();
		}
		switchCityBtn.setOnClickListener(this);
		refreshWeatherBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString(WeatherType.WEATHER_CODE, "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherCode(weatherCode);
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 * 查询县级代号对应的天气代号
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		String address ="http://www.weather.com.cn/data/list3/city"+
	countyCode+".xml";
		queryFromSever(address,County.COUNTY_CODE);
		
	}
	/**
	 * 查询天气代号所对应的天气
	 * @param weatherCode
	 */
	protected void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
		String address ="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromSever(address, WeatherType.WEATHER_CODE);
	}

	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 * @param address
	 * @param countyCode
	 */
	private void queryFromSever(String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if (County.COUNTY_CODE.equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						//从服务器返回的数据中解析天气代号
						String[] array = response.split("\\|");
						if (array!=null&&array.length==2) {
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if (WeatherType.WEATHER_CODE.equals(type)) {
					//处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread( new Runnable() {
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
		
	}
	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString(City.CITY_NAME, ""));
		temp1Text.setText(prefs.getString(WeatherType.TEMP1, ""));
		temp2Text.setText(prefs.getString(WeatherType.TEMP2, ""));
		weatherDespText.setText(prefs.getString(WeatherType.WEATHER_DESP, ""));
		publishText.setText(prefs.getString(WeatherType.PUBLISH_TIME, ""));
		currentDateText.setText(prefs.getString(WeatherType.CURRENT_DATE, ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
	}

	


}
