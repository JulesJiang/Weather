package model;

public class Province {
	private int id;
	private String province_name;
	private String province_code;
	
	static final String PROVINCE_NAME="province_name";
	static final String PROVINCE_CODE="province_code";
	static final String TABLE="province";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	
	
}
