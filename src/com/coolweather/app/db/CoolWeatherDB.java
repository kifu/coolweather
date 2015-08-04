package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
 * @author Administrator
 *
 */
public class CoolWeatherDB {
	/**
	 * ���ݿ���
	 */
	public static final String DB_NAME = "cool_weather";
	
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 2;
	
	private static  CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase database;
	
	/**
	 * ���췽��˽�л�
	 * @param context
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper openHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		database = openHelper.getWritableDatabase();
	}
	/**
	 * ��ȡCoolWeatherDBʵ��
	 * @param context
	 * @return
	 */
	public synchronized static  CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 * ��Provinceʵ�����浽���ݿ�
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			database.insert("Province", null, values);
		}
	}
	/**
	 * ��ȡ����ʡ����Ϣ
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = database.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * ��Cityʵ�����浽���ݿ�
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			database.insert("City", null, values);
		}
	}
	
	/**
	 * ��ȡĳ��ʡ�³��е���Ϣ
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = database.query("City", null, "province_id = ?", 
				new String [] {String.valueOf(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * ��Countryʵ�����浽���ݿ�
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			database.insert("County", null, values);
		}
	}
	
	/**
	 * ��ȡĳ���������ص���Ϣ
	 */
	public List<County> loadCountries(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = database.query("County", null, "city_id = ?", 
				new String [] {String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do{
				County country = new County();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				country.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				country.setCityId(cityId);
				list.add(country);
			}
			 while (cursor.moveToNext());
		}
		return list;
	}
}
