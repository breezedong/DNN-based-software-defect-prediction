/**
 * 
 */
package com.ssca.commonData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huge
 *
 * 2015年6月3日
 */
public class Android_API_DataSet {
	
	public static List<API_Data> api = new ArrayList<API_Data>();

	public static List<API_Data> getApi() {
		return api;
	}

	public static void setApi(List<API_Data> api) {
		Android_API_DataSet.api = api;
	}
	
	public static void addApi(API_Data one_api){
		api.add(one_api);
	}


	
	
	
	

}
