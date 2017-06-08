package model;

import android.content.Context;

public class UserInfo {
	private int id;
	private String username;
	private String userpwd;
	private static UserInfo userInfo;
	
	public final static String USER_NAME= "user_name";
	public final static String USER_PWD= "user_pwd";
	public final static String TABLE="userinfo";
	
	/**
	 * »ñÈ¡userinfoÊµÀý
	 */
	public synchronized static UserInfo getInstance(){
		if (userInfo==null) {
			userInfo=new UserInfo(); 
		}
		return userInfo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	
}
