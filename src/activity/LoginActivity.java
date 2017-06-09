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
		//��ʼ�����׹��Ľ��
		AdManager.getInstance(this).init("8bef07fc9b629735", "3b8c10575c763646", false);
	
		setContentView(R.layout.activity_login);
		setTitle("��¼");
		
		
		weatherDB=WeatherDB.getInstance(LoginActivity.this);
		name_eText = (EditText) findViewById(R.id.edit_title);
		pwd_eText = (EditText) findViewById(R.id.editText2);
		login_btn = (Button) findViewById(R.id.btn_public);
		regist_btn = (Button) findViewById(R.id.btn_root);

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
				editor.commit();// �ύ

				if (weatherDB.loginCheck(userInfo)) {
					Log.i("Life", "��¼�ɹ�");
					Intent intent = new Intent(LoginActivity.this,WeatherActivity.class);
					startActivity(intent);
				} else {
					Log.i("Life", "��¼ʧ��");
				}
			}
		});
		regist_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
				View view=inflater.inflate(R.layout.regist_layout, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("ע��");
				builder.setIcon(R.drawable.purple_regist);
				builder.setView(view);
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String name = name_eText.getText().toString();
						String pwd = pwd_eText.getText().toString();
						String pwd_repeat = pwd_eText.getText().toString();
						if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)) {
							Toast.makeText(LoginActivity.this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
						}
						else if (!TextUtils.equals(pwd,pwd_repeat)) {
							Toast.makeText(LoginActivity.this, "�������벻һ��", Toast.LENGTH_SHORT).show();
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
