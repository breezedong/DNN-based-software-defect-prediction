/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import test.security.rules.SecRules;




/**
 * @author huge
 *
 * 2014年7月14日
 */

//存储全局变量
public class CodeMapItemMapper {
	
	private static final CodeMapItemMapper similarityMapItemMapper =
			new CodeMapItemMapper();
	
	public CodeMapItemMapper() {
	}
		
	public synchronized static CodeMapItemMapper getInstance() {
		return similarityMapItemMapper;
	}

//	=======================================================================================
	//存储所有smali文件的控制流图
	private Map<String, FileData> SmaliFileMap =
			new ConcurrentHashMap<String, FileData>();
	
	public synchronized Map<String, FileData> getSmaliFileMap() {
		return SmaliFileMap;
	}
	
	public synchronized void addSmaliFileMap(String KeyMapItem, FileData smaliFileData) {
		SmaliFileMap.put(KeyMapItem, smaliFileData);
	}
	
//  ========================================================================================
	//存储函数调用
	private Map<MethodData,List<MethodData>> InvokeMap = 
			new ConcurrentHashMap<MethodData,List<MethodData>>();
	
	public synchronized Map<MethodData,List<MethodData>> getInvokeMap() {
		return InvokeMap;
	}
	
	public synchronized void addInvokeMap(MethodData KeyMapItem, List<MethodData> methodDataList) {
		InvokeMap.put(KeyMapItem, methodDataList);		
	}
	
	public synchronized void addInvokeListMap(MethodData KeyMapItem, MethodData methodData) {
		boolean flag = true;
		for(MethodData method : InvokeMap.get(KeyMapItem)){
			if( MethodDataEquals(method,methodData) ){
				flag = false;
			}
		}
		if( flag ){
			InvokeMap.get(KeyMapItem).add(methodData);			
		}
	}
	
	private static boolean MethodDataEquals(MethodData A,MethodData B){
		boolean result = true;		
		if(! A.getMethod_Name().equals(B.getMethod_Name())){
			result = false;
		}
		else if ( ! A.getMethod_Returntype().equals(B.getMethod_Returntype()) ){
			result = false;
		}
		else if ( ! A.getMethod_Prototype().toString().equals(B.getMethod_Prototype().toString()) ){
			result = false;			
		}
		return result;		
	}
	
//=====================================================================================================
//	Map<MethodData , Map<Integer, List<String>> > registerMessage = 
//			new ConcurrentHashMap<MethodData , Map<Integer, List<String>> >();
//	
//	
//	
//	public synchronized Map<MethodData , Map<Integer, List<String>> > getRegisterMessage() {
//		return registerMessage;
//	}
//
//
//	public synchronized void addregisterMessage(MethodData KeyMapItem, Map<Integer, List<String>> rigister) {
//		registerMessage.put(KeyMapItem, rigister);
//	}
	
//=============================================================================================
//	List<SecRules> RulesPool = new ArrayList<SecRules>();
	
	
//===============================================================================================
//	Map<String , List<SecRules> > RulesPool = 
//			new ConcurrentHashMap<String,List<SecRules> >();
//	
//	public synchronized Map<String,List<SecRules>> getRulesPool() {
//		return RulesPool;
//	}
//	
//	public synchronized void addRulesPool(String rulekey , List<SecRules> rulepool){
//		RulesPool.put(rulekey, rulepool);
//	}

}
