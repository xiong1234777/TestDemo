package com.xiong.mdapplication.utils;
import android.util.Log;

/**
 * LogUtil 日志管理工具类
 * @author zxh
 *
 */
public class LogUtils {
	private static final int V=1;
	private static final int D=2;
	private static final int I=3;
	private static final int W=4;
	private static final int E=5;
	private static final int NOTING=6;
	private static final int LEVEL=V;
	
	
	public static void  v(String tag,String msg){
		if(LEVEL<=V){
			Log.v(tag, msg);
		}
	}
	public static void  d(String tag,String msg){
		if(LEVEL<=D){
			Log.d(tag, msg);
		}
	}
	public static void  i(String tag,String msg){
		if(LEVEL<=I){
			Log.i(tag, msg);
		}
	}
	public static void  w(String tag,String msg){
		if(LEVEL<=W){
			Log.w(tag, msg);
		}
	}
	public static void  e(String tag,String msg){
		if(LEVEL<=E){
			Log.e(tag, msg);
		}
	}
}
