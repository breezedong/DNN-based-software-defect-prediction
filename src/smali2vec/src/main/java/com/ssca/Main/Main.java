
package com.ssca.Main;

import java.io.File;

import com.ssca.analyse.AnalyserController;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.ssca.commonData.CommonData;
import com.ssca.decompile.Apktool;
import com.ssca.report.GenerateReport;

/**
 * @author yujianbo
 *
 * 2016年1月21日
 */
public class Main {


	public static void main(String[] args) {
		//main传参
		new Scheduler().mainScheduler(args[0], null);
	

//		new Scheduler().mainScheduler("E:\\work\\test\\200apk\\done\\f59de6d552d8e2f68c3a2cac497b0b55.apk", null);


//		File apkRootPath = new File("E:\\work\\test\\picked_20160409wandoujia\\50");
//		for(File f : apkRootPath.listFiles()){
//			System.out.println(f.getAbsolutePath());
//			new Scheduler().mainScheduler(f.getAbsolutePath(), null);
//		}
		//		Server s = new Server();
		//		for(File f : apkRootPath.listFiles()){
		//			Task task = new Task(f.getAbsolutePath());
		//			s.executeTask(task);
		//		}
		//		
	}
}

class Task implements Runnable{
	private String apkPath;
	public Task(String apkPath){
		this.apkPath=apkPath;
	}
	@Override
	public void run() {
		new Scheduler().mainScheduler(apkPath, null);
	}
}

class Server{
	private ThreadPoolExecutor executor;
	public Server(){
		executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	public void executeTask(Task task){
		executor.execute(task);
	}
	public void endServer(){
		executor.shutdown();
	}
}
