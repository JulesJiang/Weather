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
	 * ��ʾ������
	 */
	private TextView cityNameText;
	/**
	 * ��ʾ����ʱ��
	 */
	private TextView publishText;
	/**
	 * ��ʾ����������Ϣ
	 */
	private TextView weatherDespText;
	/**
	 * ��ʾ����1
	 */
	private TextView temp1Text;
	/**
	 * ��ʾ����2
	 */
	private TextView temp2Text;
	/**
	 * ��ʾ��ǰ����
	 */
	private TextView currentDateText;
	/**
	 * �л����а�ť
	 */
	private Button switchCityBtn;
	/**
	 * ����������ť
	 */
	private Button refreshWeatherBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//��ʼ�����ؼ�
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
			//���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.VISIBLE);
			queryWeatherCode(countyCode);
		}else {
			//û���ؼ�����ʱ����ʾ��������
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
			publishText.setText("ͬ����...");
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
	 * ��ѯ�ؼ����Ŷ�Ӧ����������
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		String address ="http://www.weather.com.cn/data/list3/city"+
	countyCode+".xml";
		queryFromSever(address,County.COUNTY_CODE);
		
	}
	/**
	 * ��ѯ������������Ӧ������
	 * @param weatherCode
	 */
	protected void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
		String address ="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromSever(address, WeatherType.WEATHER_CODE);
	}

	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ
	 * @param address
	 * @param countyCode
	 */
	private void queryFromSever(String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if (County.COUNTY_CODE.equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						//�ӷ��������ص������н�����������
						String[] array = response.split("\\|");
						if (array!=null&&array.length==2) {
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if (WeatherType.WEATHER_CODE.equals(type)) {
					//������������ص�������Ϣ
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
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
		
	}
	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ��������
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
