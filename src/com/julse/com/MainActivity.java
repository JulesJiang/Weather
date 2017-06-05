package com.julse.com;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private EditText name_eText;
	private EditText pwd_eText;
	private Button login_btn;
	private Button regist_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("µÇÂ¼");
		name_eText = (EditText) findViewById(R.id.edit_title);
		pwd_eText = (EditText) findViewById(R.id.editText2);
		login_btn = (Button) findViewById(R.id.btn_public);
		regist_btn = (Button) findViewById(R.id.btn_root);
		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences pref = getSharedPreferences("info",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("name", name_eText.getText().toString());
				editor.putString("pwd", pwd_eText.getText().toString());
				editor.commit();// Ìá½»
				name_eText.setText("");
				pwd_eText.setText("");

			}
		});
		regist_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		SharedPreferences sharedPref = getSharedPreferences("info",
				Context.MODE_PRIVATE);
		String name = sharedPref.getString("name", "");
		String pwd = sharedPref.getString("pwd", "");
		name_eText.setText(name);
		pwd_eText.setText(pwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
