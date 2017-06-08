package activity;

import java.util.ArrayList;
import java.util.List;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.PlaceType;
import util.Utility;
import util.WeatherType;

import com.julse.com.R;

import model.City;
import model.County;
import model.Province;
import model.WeatherDB;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Type;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY= 2 ;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private WeatherDB weatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	/**
	 * 省列表
	 */
	private List<Province> provinceList;
	/**
	 * 市列表
	 */
	private List<City> cityList;
	/**
	 * 县列表
	 */
	private List<County> countyList;
	/**
	 * 选中的省份
	 */
	private Province selectedProvince;
	/**
	 * 选中的城市
	 */
	private City selectedCity;
	/**
	 * 选中的级别
	 */
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean(WeatherType.CITY_SELECTED, false)) {
			Log.i("Life", "打开天气界面");
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText=(TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		weatherDB = WeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				if (currentLevel==LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				}else if (currentLevel==LEVEL_CITY) {
					selectedCity= cityList.get(index);
					queryCounties();
				}else if (currentLevel==LEVEL_COUNTY) {
					String countyCode = countyList.get(index).getCounty_code();
					Intent intent =new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra(County.COUNTY_CODE, countyCode);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();
	}
	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到，再去服务器上查询
	 */
	private void queryProvinces() {
		provinceList = weatherDB.loadProvinces();
		if (provinceList.size()>0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvince_name());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel= LEVEL_PROVINCE;
		}else {
			queryFromServer(null,"province");
		}
		
	}
	/**
	 * 查询全国所有的市，优先从数据库查询，如果没有查询到，再去服务器上查询
	 */
	protected void queryCities() {
		// TODO Auto-generated method stub
		cityList = weatherDB.loadCities(selectedProvince.getId());
		if (cityList.size()>0) {
			dataList.clear();
			for(City city : cityList){
				dataList.add(city.getCity_name());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvince_name());
			currentLevel= LEVEL_CITY;
		}else {
			queryFromServer(selectedProvince.getProvince_code(),"city");
		}
	}
	/**
	 * 查询全国所有的县，优先从数据库查询，如果没有查询到，再去服务器上查询
	 */
	protected void queryCounties() {
		countyList = weatherDB.loadCounties(selectedCity.getId());
		if (countyList.size()>0) {
			dataList.clear();
			for(County county : countyList){
				dataList.add(county.getCounty_name());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCity_name());
			currentLevel= LEVEL_COUNTY;
		}else {
			queryFromServer(selectedCity.getCity_code(),"county");
		}

	}
	/**
	 * 根据传入的代号和类型从服务器上查询省，市，县的数据
	 * @param city_code
	 * @param string
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			//罗列某省下面的城市，或者某城市下面的县如：190401|苏州，190402|常熟
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else {
			//罗列中国所有的省份名称及其代号如：01|北京，02|上海
			address= "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result=false;
				if (PlaceType.PROVINCE.equals(type)) {
					result= Utility.handleProvincesResponse(weatherDB, response);
				}else if(PlaceType.CITY.equals(type)) {
					result = Utility.handleCitiesResponse(weatherDB, response, selectedProvince.getId());
				}else if (PlaceType.COUNTY.equals(type)) {
					result=Utility.handleCountiesResponse(weatherDB, response, selectedCity.getId());
				}
				if (result) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							closeProgressDialog();
							if (PlaceType.PROVINCE.equals(type)) {
								queryProvinces();
							}else if (PlaceType.CITY.equals(type)) {
								queryCities();
							}else if (PlaceType.COUNTY.equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT);
						
					}
				});
			}
		});
	}
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog==null) {
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog(){
		if (progressDialog!=null) {
			progressDialog.dismiss();
		}
	}
	/**
	 * 捕获back按键，根据当前的级别来判断，此时应该返回市级列表，省列表，还是直接退出
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel==LEVEL_COUNTY) {
			queryCities();
		}else if (currentLevel==LEVEL_CITY) {
			queryProvinces();
		}else {
			finish();
		}
	}
}
