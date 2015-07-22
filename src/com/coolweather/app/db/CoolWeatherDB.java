package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
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
	public static final int VERSION = 1;
	
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
			values.put("province_code", province.getProviceCode());
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
			Province province = new Province();
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProviceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
		} while (cursor.moveToNext());
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
			values.put("provice_id", city.getProviceId());
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
			City city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setProviceId(provinceId);
			list.add(city);
		} while (cursor.moveToNext());
		return list;
	}
	
	/**
	 * ��Countryʵ�����浽���ݿ�
	 */
	public void saveCountry(Country country) {
		if (country != null) {
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			database.insert("Country", null, values);
		}
	}
	
	/**
	 * ��ȡĳ���������ص���Ϣ
	 */
	public List<Country> loadCountries(int cityId) {
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = database.query("Country", null, "city_id = ?", 
				new String [] {String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			Country country = new Country();
			country.setId(cursor.getInt(cursor.getColumnIndex("id")));
			country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
			country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
			country.setCityId(cityId);
			list.add(country);
		} while (cursor.moveToNext());
		return list;
	}
	
	
	
	
	
	
	
}
