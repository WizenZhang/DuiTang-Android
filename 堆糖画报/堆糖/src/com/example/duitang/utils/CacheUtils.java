package com.example.duitang.utils;

import android.content.Context;

/**
 * ���湤����
 * 
 * @author Kevin
 * 
 */
public class CacheUtils {

	/**
	 * ���û��� key ��url, value��json
	 */
	public static void setCache(String key, String value, Context ctx) {
		PrefUtils.setString(ctx, key, value);
		//���Խ���������ļ���, �ļ�������Md5(url), �ļ�������json
	}

	/**
	 * ��ȡ���� key ��url
	 */
	public static String getCache(String key, Context ctx) {
		return PrefUtils.getString(ctx, key, null);
	}
}
