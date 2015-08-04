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
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 2;
	
	private static  CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase database;
	
	/**
	 * 构造方法私有化
	 * @param context
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper openHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		database = openHelper.getWritableDatabase();
	}
	/**
	 * 获取CoolWeatherDB实例
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
	 * 将Province实例储存到数据库
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
	 * 读取所有省份信息
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
	 * 将City实例储存到数据库
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
	 * 读取某个省下城市的信息
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
	 * 将Country实例储存到数据库
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
	 * 读取某个城市下县的信息
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
