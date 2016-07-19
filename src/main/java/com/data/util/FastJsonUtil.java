package com.data.util;

import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public final class FastJsonUtil {
	private FastJsonUtil() {

	}

	public static String toJsonString(Object object) {
		return JSON.toJSONString(object);
	}

	public static final <T> String objToString(T obj) {
		return JSON.toJSONString(obj);
	}

	public static JSONObject toJsonObject(String text) {
		return JSON.parseObject(text);
	}

	public static final <T> T stringToObj(String data, T obj) {
		return (T) JSON.parseObject(data, obj.getClass());
	}

	/**
	 * 将JsonArray转换为对应的List
	 */
	public static <T> List<T> parseObjectList(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * 将JsonArray转换为对应的对象
	 */
	public static <T> T parseObject(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	/**
	 * 配合属性filter的转json方法
	 */
	public static String toJsonString(Object object, SimplePropertyPreFilter filter) {
		return JSON.toJSONString(object, filter);
	}

	/**
	 * 只包含指定属性
	 * 
	 * @param properties
	 *            属性名数组
	 */
	public static String toJsonStringWithIncludes(Object object, String... properties) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		for (String p : properties) {
			filter.getIncludes().add(p);
		}
		return toJsonString(object, filter);
	}

	/**
	 * 只包含指定属性
	 * 
	 * @param properties
	 *            属性名集合
	 */
	public static String toJsonStringWithIncludes(Object object, List<String> properties) {
		return toJsonStringWithIncludes(object, properties.toArray(new String[0]));
	}

	/**
	 * 忽略object中的指定属性
	 * 
	 * @param properties
	 *            属性名
	 */
	public static String toJsonStringWithExcludes(Object object, String... properties) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		for (String p : properties) {
			filter.getExcludes().add(p);
		}
		return toJsonString(object, filter);
	}

	/**
	 * 忽略object中的指定属性
	 * 
	 * @param properties
	 *            属性名
	 */
	public static String toJsonStringWithExcludes(Object object, List<String> properties) {
		return toJsonStringWithExcludes(object, properties.toArray(new String[0]));
	}

}
