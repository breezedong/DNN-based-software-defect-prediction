package com.ssca.analyse.smali.bound;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ssca.analyse.smali.controlflow.CodeMapItemMapper;
import com.ssca.analyse.smali.controlflow.FileData;
import com.ssca.analyse.smali.controlflow.FunctionData;
import com.ssca.commonData.CommonData;

public class RecieverInBound {

	private static Logger logger = LogManager.getLogger(RecieverInBound.class);
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
			IntentParse Parse1 = new IntentParse(funData);
			List<IntentData>reList1 = Parse1.intentParse("sendBroadcast");

			//save result to commonData
			if(reList1.size()>=1){
				if(CommonData.getReceiverInList()==null||CommonData.getReceiverInList().size()==0)
					CommonData.setReceiverInList(reList1);
				else
				{
					List<IntentData> alreadyIn = CommonData.getReceiverInList();
					alreadyIn.addAll(reList1);
					CommonData.setReceiverInList(alreadyIn);
				}
			}
		}

	}

}
