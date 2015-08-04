package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
/**
 * 返回数据"代号|城市,代号|城市"
 */
public class Utility {
	/**
	 * 解析省级数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB
			coolWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0){
				for (String p : allProvinces) {
					String[] arr = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(arr[0]);
					province.setProvinceName(arr[1]);
					coolWeatherDB.saveProvince(province);
				}	
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析城市数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public synchronized static boolean handleCitiesResopnse(CoolWeatherDB
			coolWeatherDB, String response, int provinceId) {
		String[] allCities = response.split(",");
		if (allCities != null && allCities.length >0) {
			for (String c : allCities) {
				String[] arr = c.split("\\|");
				City city = new City();
				city.setCityCode(arr[0]);
				city.setCityName(arr[1]);
				city.setProvinceId(provinceId);
				coolWeatherDB.saveCity(city);
			}	
			return true;
		}
		return false;
	}
	
	/**
	 * 解析县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public synchronized static boolean handleCountiesResopnse(CoolWeatherDB
			coolWeatherDB, String response, int cityId) {
		String[] allCounties = response.split(",");
		if (allCounties != null && allCounties.length >0) {
			for (String c : allCounties) {
				String[] arr = c.split("\\|");
				County county = new County();
				county.setCountyCode(arr[0]);
				county.setCountyName(arr[1]);
				county.setCityId(cityId);
				coolWeatherDB.saveCounty(county);
			}	
			return true;
		}
		return false;
	}
	
	/**
	 *解析天气数据
	 */
	
	public static void handleWeatherResponse(Context context, String response) {
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, 
					weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 将服务器返回的所有的天气信息储存到SharedPreferences文件
	 * 
	 */
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
		edit.putBoolean("city_selected", true);
		edit.putString("city_name", cityName);
		edit.putString("weather_code", weatherCode);
		edit.putString("temp1", temp1);
		edit.putString("temp2", temp2);
		edit.putString("weather_desp", weatherDesp);
		edit.putString("publish_time", publishTime);
		edit.putString("current_date", sdf.format(new Date()));
		edit.commit();
	}

}
