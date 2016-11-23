/**
 * 
 */
package com.ssca.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ssca.commonData.CommonData;

/**
 * @author yujianbo
 *
 * 2016年1月25日
 */
public class Config {

	public static void setWhiteList(){
		List<String> whiteList = new ArrayList<String>();
		String fileSeparator = System.getProperty("file.separator");
		String rulepath = System.getProperty("user.dir") + fileSeparator + "conf" + fileSeparator + "whiteAPI.txt";
		
		File file = new File(rulepath);
		BufferedReader reader = null;
        try {
       //     System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
        //       System.out.println("line " + line + ": " + tempString);
                line++;
                whiteList.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }		
        
        CommonData.setWhiteList(whiteList);
	}
}
