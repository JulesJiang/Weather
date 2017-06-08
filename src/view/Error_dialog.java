package view;

import com.julse.com.R;

import android.app.AlertDialog;
import android.content.Context;

public class Error_dialog extends AlertDialog{

	protected Error_dialog(Context context,String message) {
		super(context);
		setIcon(R.drawable.purple_error);
		setMessage(message);
		setCancelable(false);
		show();
	}

}
