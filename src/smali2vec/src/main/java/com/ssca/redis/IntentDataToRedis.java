package com.ssca.redis;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ssca.analyse.smali.bound.IntentData;

public class IntentDataToRedis {

	public static void toJson(List<IntentData> intentDataList){
		for(IntentData intentData:intentDataList){
			if(intentData.getCaller().contains("Landroid/content/Context")
					||intentData.getCaller().contains("Landroid/app/Activity")
					||(intentData.getDesClass()==""&&intentData.getUri()=="")){
				continue;
			}
			JSONObject json = new JSONObject();
			json.put("sourcePkg", intentData.getSourcePkg());
			json.put("sourceClass", intentData.getCaller());
			json.put("desPkg", intentData.getDesPkg());
			json.put("desClass", intentData.getDesClass());
			json.put("uri", intentData.getUri());
			json.put("scheme", intentData.getScheme());
			json.put("host", intentData.getHost());
//			System.out.println("json:"+json.toString());
			JedisUtil.lpush("intent", json.toString());
			JedisUtil.lpush("intent_all", json.toString());
		}


	}


}
