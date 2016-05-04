package com.xiong.mdapplication;

import android.app.Application;
import android.content.Context;

import com.xiong.mdapplication.service.LocationService;
import com.xiong.mdapplication.service.MyBDLocationListener;
import com.xiong.mdapplication.service.WriteLog;


public class MyApplication extends Application {
	private static Context context;
	public LocationService locationService;
	
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		 /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(new MyBDLocationListener());
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        WriteLog.getInstance().init(); // 初始化日志


	}
	
	public static Context getContext(){
		return context;
	}
	
}
