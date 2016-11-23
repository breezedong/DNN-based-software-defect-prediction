/**
 * 
 */
package com.ssca.decompile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.*;

import com.ssca.commonData.CommonData;



/**
 * @author yujianbo
 *
 * 2016年1月21日
 */
public class Apktool {
	
	private static Logger logger = LogManager.getLogger(Apktool.class);
	private static String fileSeparator = System.getProperty("file.separator");
	
	public void do_decompile(){
		logger.info("decompile start");
		//clear dir first
		logger.info("clear detemp files...");
		clearDetemp();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String deFileName = format.format(new Date()).toString();
		
		String apktoolPath = System.getProperty("user.dir")+fileSeparator+"tools"+fileSeparator+"apktool.jar";
		String apkPath = CommonData.getApkPath();
		String dePath =System.getProperty("user.dir")+fileSeparator+"detemp"+fileSeparator+deFileName;
		String cmd = "java -jar "+apktoolPath+" d -f "+apkPath+" -o "+dePath;
		
		File defile = new File(dePath);
		defile.mkdir();
		try{
			Runtime rt = Runtime.getRuntime();    
			Process proc = rt.exec(cmd);
			BufferedInputStream inf = new BufferedInputStream(proc.getErrorStream());   
			BufferedReader inBrf = new BufferedReader(new InputStreamReader(inf));
			String lf;
			if((lf = inBrf.readLine()) !=null){
				System.out.println(lf);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("decompile success");
		CommonData.setDecompilePath(dePath);
		
	}
	

	private void clearDetemp(){
		String detmpPath = System.getProperty("user.dir")+fileSeparator+"detemp";
		File detmpFile = new File(detmpPath);
		File[]dirs = detmpFile.listFiles();
		for(File dir : dirs){
			clearDir(dir);
		}
	}
	
	private void clearDir(File root){
		if(root.isDirectory()){
			File[]files = root.listFiles();
			for(File file:files){
				clearDir(file);
			}
		}
		root.delete();
	}
}
