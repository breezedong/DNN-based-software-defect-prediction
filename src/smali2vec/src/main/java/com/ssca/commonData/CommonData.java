/**
 * 
 */
package com.ssca.commonData;

import java.util.List;

import com.ssca.analyse.smali.bound.IntentData;

/**
 * @author yujianbo
 *
 * 2016年1月21日
 */
public class CommonData {

	private static String apkPath;
	private static String reportPath;
	private static String decompilePath;
	private static String apkPkg;
	private static List<String> activityOutList;
	private static List<String> serviceOutList;
	private static List<String> receiverOutList;
	private static List<String> providerOutList;
	private static List<IntentData> activityInList;
	private static List<IntentData> serviceInList;
	private static List<IntentData> receiverInList;
	private static List<String> providerInList;
	
	private static List<String> whiteList;
	
	public static String getApkPkg() {
		return apkPkg;
	}

	public static void setApkPkg(String apkPkg) {
		CommonData.apkPkg = apkPkg;
	}
	public static List<String> getWhiteList() {
		return whiteList;
	}

	public static void setWhiteList(List<String> whiteList) {
		CommonData.whiteList = whiteList;
	}

	public static List<IntentData> getActivityInList() {
		return activityInList;
	}

	public static void setActivityInList(List<IntentData> activityInList) {
		CommonData.activityInList = activityInList;
	}

	public static List<IntentData> getServiceInList() {
		return serviceInList;
	}

	public static void setServiceInList(List<IntentData> serviceInList) {
		CommonData.serviceInList = serviceInList;
	}

	public static List<IntentData> getReceiverInList() {
		return receiverInList;
	}

	public static void setReceiverInList(List<IntentData> receiverInList) {
		CommonData.receiverInList = receiverInList;
	}

	public static List<String> getProviderInList() {
		return providerInList;
	}

	public static void setProviderInList(List<String> providerInList) {
		CommonData.providerInList = providerInList;
	}
	
	public static List<String> getServiceOutList() {
		return serviceOutList;
	}

	public static void setServiceOutList(List<String> serviceOutList) {
		CommonData.serviceOutList = serviceOutList;
	}

	public static List<String> getReceiverOutList() {
		return receiverOutList;
	}

	public static void setReceiverOutList(List<String> receiverOutList) {
		CommonData.receiverOutList = receiverOutList;
	}

	public static List<String> getProviderOutList() {
		return providerOutList;
	}

	public static void setProviderOutList(List<String> providerOutList) {
		CommonData.providerOutList = providerOutList;
	}

	public static List<String> getActivityOutList() {
		return activityOutList;
	}

	public static void setActivityOutList(List<String> activityList) {
		CommonData.activityOutList = activityList;
	}

	public static String getDecompilePath() {
		return decompilePath;
	}

	public static void setDecompilePath(String decompilePath) {
		CommonData.decompilePath = decompilePath;
	}

	public static String getReportPath() {
		return reportPath;
	}

	public static void setReportPath(String reportPath) {
		CommonData.reportPath = reportPath;
	}

	public static String getApkPath() {
		return apkPath;
	}

	public static void setApkPath(String apkPath) {
		CommonData.apkPath = apkPath;
	}
}
