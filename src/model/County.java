package model;

public class County {
	private int id;
	private String couty_name;
	private String couty_code;
	private int city_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCouty_name() {
		return couty_name;
	}
	public void setCouty_name(String couty_name) {
		this.couty_name = couty_name;
	}
	public String getCouty_code() {
		return couty_code;
	}
	public void setCouty_code(String couty_code) {
		this.couty_code = couty_code;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	
}
