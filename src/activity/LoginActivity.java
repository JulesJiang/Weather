package activity;

import net.youmi.android.AdManager;
import model.UserInfo;
import model.WeatherDB;

import com.julse.com.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText name_eText;
	private EditText pwd_eText;
	private Button login_btn;
	private Button regist_btn;
	private UserInfo userInfo;
	private WeatherDB weatherDB ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化有米广告的接口
		AdManager.getInstance(this).init("8bef07fc9b629735", "3b8c10575c763646", false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		
		
		weatherDB=WeatherDB.getInstance(LoginActivity.this);
		name_eText = (EditText) findViewById(R.id.edit_name);
		pwd_eText = (EditText) findViewById(R.id.edit_pwd);
		login_btn = (Button) findViewById(R.id.btn_login);
		regist_btn = (Button) findViewById(R.id.btn_regist);

		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String name = name_eText.getText().toString();
				String pwd = pwd_eText.getText().toString();
				userInfo= UserInfo.getInstance();
				userInfo.setUsername(name);
				userInfo.setUserpwd(pwd);
				SharedPreferences pref = getSharedPreferences(UserInfo.TABLE,
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(UserInfo.USER_NAME, userInfo.getUsername());
				editor.putString(UserInfo.USER_PWD, userInfo.getUserpwd());
				editor.commit();// 提交

				if (weatherDB.loginCheck(userInfo)) {
					
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,ChooseAreaActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		regist_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
				View view=inflater.inflate(R.layout.regist_layout, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("注册");
				builder.setIcon(R.drawable.purple_regist);
				builder.setView(view);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String name = name_eText.getText().toString();
						String pwd = pwd_eText.getText().toString();
						String pwd_repeat = pwd_eText.getText().toString();
						if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)) {
							Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
						}
						else if (!TextUtils.equals(pwd,pwd_repeat)) {
							Toast.makeText(LoginActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
						}
						else {
							userInfo= UserInfo.getInstance();
							userInfo.setUsername(name);
							userInfo.setUserpwd(pwd);
							weatherDB.regist(userInfo);
						}
						
					}
				});
				
				builder.create().show();
				
			}
		});
		SharedPreferences sharedPref = getSharedPreferences(UserInfo.TABLE,
				Context.MODE_PRIVATE);
		String name = sharedPref.getString(UserInfo.USER_NAME, "");
		String pwd = sharedPref.getString(UserInfo.USER_PWD, "");
		name_eText.setText(name);
		pwd_eText.setText(pwd);
	}
}
