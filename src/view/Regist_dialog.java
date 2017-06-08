package view;

import model.UserInfo;

import com.julse.com.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Regist_dialog extends View{
	private EditText e_name;
	private EditText e_pwd;
	private EditText e_pwd_repeat;
	private Context context ;
	public Regist_dialog(Context context) {
		super(context);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.regist_layout, null);
		
		e_name=(EditText) findViewById(R.id.editText1);
		e_pwd=(EditText) findViewById(R.id.editText2);
		e_pwd_repeat=(EditText) findViewById(R.id.editText3);
		
		e_pwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	class listener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			String name=e_name.getText().toString();
			String pwd=e_pwd.getText().toString();
			String pwd_repeat= e_pwd_repeat.getText().toString();
			
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd_repeat)) {
				Toast.makeText(context, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			}
			else if (pwd.equals(pwd_repeat)) {
				UserInfo userInfo = UserInfo.getInstance();
				userInfo.setUsername(name);
				userInfo.setUserpwd(pwd);
				Log.i("Life", "注册成功");
			}
		}
	
	}
}
