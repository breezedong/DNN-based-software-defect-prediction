/**
 * 
 */
package com.ssca.report;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jf.smali.main;

import com.ssca.analyse.smali.bound.IntentData;
import com.ssca.commonData.CommonData;
import com.ssca.redis.IntentDataToRedis;
import com.ssca.redis.JedisUtil;

/**
 * @author yujianbo
 *
 * 2016年1月21日
 */
public class GenerateReport {
	private static Logger logger = LogManager.getLogger(GenerateReport.class);

	public static void WriteReport(){
		String reportPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"report"+System.getProperty("file.separator");
		CommonData.setReportPath(reportPath);
		String apkName = new File(CommonData.getApkPath()).getName();
		String reportName = reportPath+apkName+".txt";

		try {
			File reportFile = new File(reportName);
			if(!reportFile.exists())
				reportFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(reportFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			List<IntentData> activityInBoundList = CommonData.getActivityInList();
			if(activityInBoundList!=null&&activityInBoundList.size()!=0){
				for(IntentData result:activityInBoundList){
					//暂时过滤脏数据(不考虑action和uri)
					if(!(result.getCaller().contains("Landroid/content/Context")||result.getCaller().contains("Landroid/app/Activity")))
						bw.write(result.getCaller()+"->"+result.getDesClass()+result.getUri()+" \n");
				}
			}

			List<IntentData> serviceInBoundList = CommonData.getServiceInList();
			if(serviceInBoundList!=null&&serviceInBoundList.size()!=0){
				for(IntentData result:serviceInBoundList ){
					//暂时过滤脏数据(不考虑action和uri)
					if(!(result.getCaller().contains("Landroid/content/Context")||result.getCaller().contains("Landroid/app/Activity")))
						bw.write(result.getCaller()+"->"+result.getDesClass()+result.getUri()+" \n");
				}
			}

			List<IntentData> recieverInBoundList = CommonData.getReceiverInList();
			if(recieverInBoundList!=null&&recieverInBoundList.size()!=0){
				for(IntentData result:recieverInBoundList ){
					//暂时过滤脏数据(不考虑action和uri)
					if(!(result.getCaller().contains("Landroid/content/Context")||result.getCaller().contains("Landroid/app/Activity")))
						bw.write(result.getCaller()+"->"+result.getDesClass()+result.getUri()+" \n");
				}
			}
			logger.info("report success");

			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//clear detemp
		String root = CommonData.getDecompilePath();
		clearDir(root);

	}

	private static void clearDir(String s){
		try {
			Runtime.getRuntime().exec("cmd /c rd /s/q "+s);
			logger.info("clear detemp files success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void WriteRedis(){
		List<IntentData> allResults = new ArrayList<IntentData>();
		
		List<IntentData> result1 = CommonData.getActivityInList();
		if(result1!=null&&result1.size()!=0)
			allResults.addAll(result1);
		List<IntentData> result2 = CommonData.getServiceInList();
		if(result2!=null&&result2.size()!=0)
			allResults.addAll(result2);
		List<IntentData> result3 = CommonData.getReceiverInList();
		if(result3!=null&&result3.size()!=0)
			allResults.addAll(result3);
		
		if(allResults.size()!=0)
			IntentDataToRedis.toJson(allResults);

		//clear detemp
//		String root = CommonData.getDecompilePath();
//		clearDir(root);

	}


	//	public static void main(String[] args) {
	//		clearDir("D:\\git_workspace\\RelationApk\\detemp\\2016-04-16-10-43-15");
	//	}
}


