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

public class ServiceInBound {
	private static Logger logger = LogManager.getLogger(ServiceInBound.class);
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
			List<IntentData>reList1 = Parse1.intentParse("startService");

			//save result to commonData
			if(reList1.size()>=1){
				if(CommonData.getServiceInList()==null||CommonData.getServiceInList().size()==0)
					CommonData.setServiceInList(reList1);
				else
				{
					List<IntentData> alreadyIn = CommonData.getServiceInList();
					alreadyIn.addAll(reList1);
					CommonData.setServiceInList(alreadyIn);
				}
			}
			
			IntentParse Parse2 = new IntentParse(funData);
			List<IntentData>reList2 = Parse2.intentParse("bindService");

			//save result to commonData
			if(reList2.size()>=1){
				if(CommonData.getServiceInList()==null||CommonData.getServiceInList().size()==0)
					CommonData.setServiceInList(reList2);
				else
				{
					List<IntentData> alreadyIn = CommonData.getServiceInList();
					alreadyIn.addAll(reList2);
					CommonData.setServiceInList(alreadyIn);
				}
			}
		}

	}
}
