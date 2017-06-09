package activity;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import service.AutoUpdateService;
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
import android.util.Log;
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
		refreshWeatherBtn = (Button) findViewById(R.id.refresh_weather);
		
		String countyCode = getIntent().getStringExtra(County.COUNTY_CODE);
		if (!TextUtils.isEmpty(countyCode)) {
			//有县级代号时就去查询天气
			publishText.setText("查询天气同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else {
			//没有县级代号时就显示本地天气
			showWeather();
		}
		switchCityBtn.setOnClickListener(this);
		refreshWeatherBtn.setOnClickListener(this);
		
		
		//广告
		//实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		//获取要嵌入广告条的布局
		LinearLayout adyLayout = (LinearLayout) findViewById(R.id.adLayout);
		//将广告条加入到布局中
		adyLayout.addView(adView);
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
//			Log.i("Life", "weathercode="+weatherCode);
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
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
//		Log.i("Life","countyCode"+countyCode);
		queryFromServer(address,County.COUNTY_CODE);
		

	}
	/**
	 * 查询天气代号所对应的天气
	 * @param weatherCode
	 */
	protected void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
//		Log.i("Life","weatherCode "+ weatherCode);
		String address ="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address, WeatherType.WEATHER_CODE);
	}

	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 * @param address
	 * @param countyCode
	 */
	private void queryFromServer(final String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			//response的内容包含的是一个html页面
			@Override
			public void onFinish(final String response) {
				if (County.COUNTY_CODE.equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						//从服务器返回的数据中解析天气代号
//						Log.i("Life","response="+response);
						String[] array = response.split("\\|");
//						Log.i("Life","array="+array.length+array[0]);
						if (array!=null&&array.length==2) {
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
//							Log.i("Life","查询queryWeatherInfo(weatherCode)");

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
		publishText.setText(prefs.getString(WeatherType.PUBLISH_TIME, "")+"发布");
		currentDateText.setText(prefs.getString(WeatherType.CURRENT_DATE, ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
		Intent intent = new Intent(this,AutoUpdateService.class);
		startService(intent);
		
	}

	


}
