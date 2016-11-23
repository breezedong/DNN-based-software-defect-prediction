/**
 * 
 */
package com.ssca.analyse.smali.bound;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.swing.text.html.parser.Entity;

import org.antlr.runtime.tree.CommonTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ssca.analyse.smali.controlflow.CodeMapItemMapper;
import com.ssca.analyse.smali.controlflow.FileData;
import com.ssca.analyse.smali.controlflow.FunctionData;
import com.ssca.analyse.smali.controlflow.OredredMethod;
import com.ssca.commonData.CommonData;

/**
 * @author yujianbo
 *
 * 2016年3月2日
 */
public class ActivityInBound {
	private static Logger logger = LogManager.getLogger(ActivityInBound.class);
	private Map<String,FileData> SmaliMap;

	public void getInBound(){
		//get smali file control flow map
		SmaliMap=CodeMapItemMapper.getInstance().getSmaliFileMap();

		for(Entry<String, FileData> SmaliMapEntry:SmaliMap.entrySet()){
			//get smali filedata
			FileData SmaliFileData = SmaliMapEntry.getValue();
			logger.info("start analyse smali dataflow: "+SmaliMapEntry.getKey()+"");
			analyseSmaliFileData(SmaliFileData);
			logger.info("end   analyse smali dataflow: "+SmaliMapEntry.getKey()+"");
		}
//		SmaliMap.clear();
	}

	private void analyseSmaliFileData(FileData SmaliFileData){
		List<FunctionData> funList = SmaliFileData.getFunList();
		for(FunctionData funData:funList){
			//analyseSmaliFunData();
			IntentParse Parse = new IntentParse(funData);
			List<IntentData>reList = Parse.intentParse("startActivity");

			//save result to commonData
			if(reList.size()>=1){
				if(CommonData.getActivityInList()==null||CommonData.getActivityInList().size()==0)
					CommonData.setActivityInList(reList);
				else
				{
					List<IntentData> alreadyIn = CommonData.getActivityInList();
					alreadyIn.addAll(reList);
					CommonData.setActivityInList(alreadyIn);
				}
			}
		}
	}

}
