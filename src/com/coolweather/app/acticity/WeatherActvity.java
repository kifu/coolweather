package com.coolweather.app.acticity;

import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActvity extends Activity implements OnClickListener {
	
	private LinearLayout weatherInfoLayout;
	
	private TextView cityNameText;
	
	private TextView publishText;
	
	private TextView weatherDespText;
	
	private TextView temp1Text;
	
	private TextView temp2Text;
	
	private TextView currentDateText;
	/**
	 * �л�����
	 */
	private Button switchCity;
	/**
	 * ����������ť
	 */
	private Button refreshWeathre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.example.coolweather.R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(com.example.coolweather.R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(com.example.coolweather.R.id.city_name);
		publishText = (TextView) findViewById(com.example.coolweather.R.id.publish_text);
		weatherDespText = (TextView) findViewById(com.example.coolweather.R.id.weather_desp);
		temp1Text = (TextView) findViewById(com.example.coolweather.R.id.templ1);
		temp2Text = (TextView) findViewById(com.example.coolweather.R.id.templ2);
		currentDateText = (TextView) findViewById(com.example.coolweather.R.id.current_data);
		switchCity = (Button) findViewById(com.example.coolweather.R.id.switch_city);
		refreshWeathre = (Button) findViewById(com.example.coolweather.R.id.refresh_weather);
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)) {
			publishText.setText("ͬ����...");
			//����
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.VISIBLE);
			queryWeatherCode(countyCode);
		} else {
			//û���ؼ�����ֱ����ʾ��������
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeathre.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case com.example.coolweather.R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case com.example.coolweather.R.id.refresh_weather:
			publishText.setText("ͬ����...");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = preferences.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 *��ѯ�������Ŷ�Ӧ������
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFormServer(address,"countyCode");
	}
	/**
	 *��ѯ�������Ŷ�Ӧ������
	 * @param weatherCode
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFormServer(address,"weatherCode");
	}
	
	private void queryFormServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|");
						if(array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if("weatherCode".equals(type)) {
					//������������ص�����
					Utility.handleWeatherResponse(WeatherActvity.this, response);
					runOnUiThread(new  Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new  Runnable() {
					public void run() {
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	
	/**
	 * ��sharedPreferences�ļ��ж�ȡ�洢��������Ϣ;����ʾ������
	 */
	private void showWeather() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(preferences.getString("city_name", ""));
		temp1Text.setText(preferences.getString("temp2", ""));
		temp2Text.setText(preferences.getString("temp1", ""));
		weatherDespText.setText(preferences.getString("weather_desp", ""));
		publishText.setText("����" + preferences.getString("publish_time", "") + "����");
		currentDateText.setText(preferences.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
	}
}
