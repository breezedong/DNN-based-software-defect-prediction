package com.ssca.analyse.smali.bound;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.antlr.runtime.tree.CommonTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ssca.analyse.smali.controlflow.FunctionData;
import com.ssca.analyse.smali.controlflow.OredredMethod;
import com.ssca.commonData.CommonData;

public class IntentParse {
	private static Logger logger =LogManager.getLogger(IntentParse.class);
	private List<IntentData> intentDataList = new ArrayList<IntentData>();
	private FunctionData funData;
	private String api;
	private IntentData intentData;
	private Map<Integer,OredredMethod> Method_Lines;
	private Map<Integer , List<String>> registerMessage;
	private Map<Integer,List<Integer>> registerLast;
	private Map<String,List<Integer>> registerCall;

	public IntentParse(FunctionData fundata) {
		//根据传入的FunctionData获取控制流和数据流
		this.funData=fundata;
		Method_Lines = fundata.getMethod_Line();
		registerMessage = fundata.getRegisterMessage();
		registerLast = fundata.getRegisterLast();
		registerCall = fundata.getRegisterCall();
	}


	/**
	 * smali解析：针对smali文件中method区域的数据（FunctionData）指定api，解析intent参数
	 */
	public List<IntentData> intentParse(String api){
		this.api = api;
		analyseSmaliFunData();

		return intentDataList;
	}

	private void analyseSmaliFunData(){
		for(Map.Entry<Integer, OredredMethod> LineEntry:Method_Lines.entrySet()){
			int lineNumber = LineEntry.getKey();//该行行号
			OredredMethod line = LineEntry.getValue();
			if(line.getStatementStr().contains(api)){//匹配关键api的行
				intentData = new IntentData();
				intentData.setSourcePkg(CommonData.getApkPkg());
				intentData.setCallApi(api);
				intentData.setStartActivityLine(lineNumber);

				//获取startActivity调用者信息
				//				intentData.setCaller(line.getStatements().getChild(2).toString());

				String caller = getCallerInfo(line.getRegister().get(0), lineNumber, line);
				intentData.setCaller(caller);

				//获取startActivity()中的参数intent的寄存器
				List<String> startActivityParams = registerMessage.get(lineNumber);
				if(startActivityParams==null||startActivityParams.size()==0)
					return;
				String intentRegister = registerMessage.get(lineNumber).get(0);

				if(registerCall.get(intentRegister)==null)
					logger.info("intent not found!");
				else{
					logger.info("intent has found at:"+intentData.getStartActivityLine());
					//获取intent构造时或者是setClass的参数信息
					// new Intent(MainActivity.this,NextActivity.class) ||  intent.setClass(MainActivity.this,NextActivity.class)
					getIntentParams(intentRegister,intentData.getStartActivityLine());
				}
				intentDataList.add(intentData);
			}
		}
	}


	/**
	 * @param lineNumber 搜索的起始行数
	 * @param Register  需要搜索的寄存器名字
	 * @return 从lineNumber开始向上追踪最近一次出现寄存器Register的位置
	 * 
	 */
	private int getLastLine(int lineNumber,String Register){
		Queue<Integer> queue = new ArrayDeque<Integer>();
		queue.add(lineNumber);
		while(!queue.isEmpty()){
			int line = queue.remove();
			if(Method_Lines.get(line).register.size()>0
					&&Method_Lines.get(line).register.get(0).equals(Register)
					&&line!=lineNumber
					&&line!=intentData.getStartActivityLine()){
				if(line<intentData.getStartActivityLine())
					return line;
			}
			List<Integer> LastLineNumberList = registerLast.get(line);
			if(LastLineNumberList==null||LastLineNumberList.size()==0)
				break; 
			for(int lastLine:LastLineNumberList){
				if(lastLine!=lineNumber&&lastLine<lineNumber){
					queue.add(lastLine);
				}
			}
		}
		return -1;

	}

	/**
	 * @param intentRegister  intent的寄存器
	 * @param intentLine startActivity(intent)的位置
	 * @return 获取intent构造时或者是setClass第二个参数class,或者component的第二个参数
	 */
	private void getIntentParams(String intentRegister,int intentLine){

		//从intentLine开始往上搜，获取startActivity的intent的定义位置：new-instance v*  Landroid/content/Intent;
		int lineTemp = getLastLine(intentLine, intentRegister);
		OredredMethod oredredMethodTemp = Method_Lines.get(lineTemp);
		while(lineTemp!=-1){
			oredredMethodTemp = Method_Lines.get(lineTemp);
			if(oredredMethodTemp.getOpcode().equals("new-instance")&&oredredMethodTemp.getRegister().get(0).equals(intentRegister)){
				logger.info("get intent new-instance at:"+lineTemp);
				intentData.setNewInstanceLine(lineTemp);
				break;
			}else{
				lineTemp = getLastLine(lineTemp, intentRegister);
			}
		}
		if(intentData.getNewInstanceLine()==-1){
			logger.info("can not found intent new-instance");
			return;
		}

		//在intentNewinstanceLine和intentLine之间查找intent的参数
		/* 查找思路：
		 * 1.找到intent<init>，分析内部参数，如果参数满足需求，则返回结果
		 * 2.在intent<init>和startAvtivity(intent)之间找setClass/setClassName或者setData…/setAction
		 * 3.继续追踪set API内部参数是否为const，如果为const，则返回
		 */
		//get intent <init>
		List<Integer> intentRegisterCallLines = registerCall.get(intentRegister);
		for(int intetregisterCallLine:intentRegisterCallLines){
			//把搜索范围控制在intentNewinstanceLine和intentLine之间
			//TODO 可以优化
			if(intetregisterCallLine>intentData.getStartActivityLine())
				break;
			if(intetregisterCallLine<intentData.getNewInstanceLine())
				continue;
			OredredMethod lineOredredMethod = Method_Lines.get(intetregisterCallLine);
			String lineString = lineOredredMethod.getStatementStr();
			if(lineString.contains("Landroid/content/Intent;")
					&&lineOredredMethod.getOpcode().equals("invoke-direct")
					&&lineString.contains("<init>")
					&&lineOredredMethod.getRegister().contains(intentRegister)){
				//匹配上intent <init>
				intentData.setInitLine(intetregisterCallLine);
				List<String> initRegisterList = lineOredredMethod.getRegister();
				Map<String,String> initParamsMap = new HashMap<String,String>();
				for(int childIndex=0;childIndex<lineOredredMethod.getStatements().getChildCount();childIndex++){
					CommonTree tempTree = (CommonTree) lineOredredMethod.getStatements().getChild(childIndex);
					if(tempTree.getText().equals("I_METHOD_PROTOTYPE")){
						int paraCount = tempTree.getChildCount();
						for(int i=0;i<paraCount;i++){
							if(!tempTree.getChild(i).toString().equals("I_METHOD_RETURN_TYPE")){
								initParamsMap.put(initRegisterList.get(i), tempTree.getChild(i).toString());
							}
						}
					}
				}
				logger.info("intent<init> params:"+initParamsMap.size());
				if(initParamsMap.size()>0){
					for(Map.Entry<String, String>entry:initParamsMap.entrySet()){
						switch(entry.getValue()){
						case "Ljava/lang/String;":
							intentData.setAction(getConstValue(entry.getKey(), intentData.getInitLine(), intentData.getNewInstanceLine()));
							break;
						case "Landroid/net/Uri;":
							intentData.setUri(getConstValue(entry.getKey(), intentData.getInitLine(), intentData.getNewInstanceLine()));
							break;
						case "Ljava/lang/Class;":
							intentData.setDesClass(getConstValue(entry.getKey(), intentData.getInitLine(), intentData.getNewInstanceLine()));
							break;
						default:
							break;
						}
					}
				}
				logger.info("end parse intent init");
				break;
			}//end of match intent <init>

		}

		//start parse intent api
		logger.info("start parse intent api");
		for(int line=intentData.getNewInstanceLine();line<intentData.getStartActivityLine();line++){
			if(Method_Lines.containsKey(line)){
				OredredMethod lineInfo = Method_Lines.get(line);
				String lineString = lineInfo.getStatementStr();
				if(lineString.contains("setData")){
					String uri = getConstValue(registerMessage.get(line).get(0), line, intentData.getNewInstanceLine());
					intentData.setUri(uri);
					String scheme = uri.split(":")[0];
					intentData.setScheme(scheme);
					logger.error("uri:"+uri);
				}else if(lineString.contains("setClass")){
					intentData.setDesClass(getConstValue(registerMessage.get(line).get(1), line, intentData.getNewInstanceLine()));
				}else if(lineString.contains("setAction")){
					intentData.setAction(getConstValue(registerMessage.get(line).get(0), line, intentData.getNewInstanceLine()));
				}else if(lineString.contains("<init>")&&lineString.contains("ComponentName")){
					intentData.setDesPkg(getConstValue(registerMessage.get(line).get(0), line, intentData.getNewInstanceLine()));
					intentData.setDesClass(getConstValue(registerMessage.get(line).get(1), line, intentData.getNewInstanceLine()));
					logger.error("pkg:"+intentData.getDesPkg()+"+class:"+intentData.getDesClass());
				}
			}
		}

	}

	/**
	 * @param registerName 追踪的寄存器的名字
	 * @param cur_line	当前行
	 * @param limitLine 往上追踪的截止的截止行
	 * @return 返回追踪到的定值const
	 */
	private String getConstValue(String registerName,int cur_line,int limitLine){
		int lastLine = getLastLine(cur_line, registerName);
		//16-04-21: 调整为上一个指令如果不是const 则放弃追踪 return空
		OredredMethod lineInfo = Method_Lines.get(lastLine);
		if(lineInfo!=null){
			if(lineInfo.getOpcode().equals("const-string")){
				String reslut = lineInfo.getStatementStr().split(registerName)[1].trim();
				return reslut.substring(1, reslut.length()-1);
			}else if(lineInfo.getOpcode().contains("const")){
				return lineInfo.getStatementStr().split(registerName)[1];
			}
		}
		//		if(lastLine!=-1&&lastLine>limitLine){
		//			OredredMethod lineInfo = Method_Lines.get(lastLine);
		//			if(lineInfo.getOpcode().contains("const")){
		//				return lineInfo.getStatementStr().split(registerName)[1];
		//			}else{
		//				return getConstValue(registerName, lastLine, limitLine);
		//			}
		//		}
		return "";
	}


	private String getCallerInfo(String contextRegister,int startLine,OredredMethod line){
		String caller = "Landroid/content/Context;";
		String curInfo = line.getStatements().getChild(2).toString();
		if((!curInfo.contains("Landroid/content/Context;"))&&(!curInfo.contains("Landroid/app/Activity"))){
			return curInfo;
		}else{
			int lastLine = getLastLine(startLine, contextRegister);
			if(lastLine!=-1){
				OredredMethod lastLineInfo = Method_Lines.get(lastLine);
				String lastLineOp = lastLineInfo.getOpcode();
				if(lastLineOp.equals("sget-object")){
					caller = lastLineInfo.getStatements().getChild(2).toString();
				}else if(lastLineOp.equals("move-result-object")){
					//修改追踪的方法，不用getLastLine，直接通过行数向上找
					while(Method_Lines.get(--lastLine)==null)
						;
					if(Method_Lines.get(lastLine).getOpcode().startsWith("invoke")){
						caller = Method_Lines.get(lastLine).getStatements().getChild(2).toString();
					}
//					int getContextLine = getLastLine(lastLine, contextRegister);
//					if(getContextLine!=-1&&Method_Lines.get(getContextLine).getOpcode().startsWith("invoke")){
//						caller = Method_Lines.get(getContextLine).getStatements().getChild(2).toString();
//					}
				}
			}
			return caller;
		}


	}
}
