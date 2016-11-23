package com.ssca.analyse.smali.dataflow;
///**
// * 
// */
//package control.data.analysis;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Set;
//import java.util.regex.Pattern;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//
//import analyze.result.AndroidStaticSmaliResult;
//import analyze.result.CommonDataSet;
//import analyze.result.ConstantResult;
//import test.security.analyze.SingleAPIAnalyzer;
//import test.security.analyze.SingleAPIAnalyzer.Content;
//import test.security.it.IContent;
//import test.security.rules.FunctionIdentifier;
//import test.security.rules.ParamInfo;
//import test.security.rules.SecRules;
//import control.flow.analysis.CodeMapItemMapper;
//import control.flow.analysis.FileData;
//import control.flow.analysis.FunctionData;
//import control.flow.analysis.GetControlFlow;
//import control.flow.analysis.MethodData;
//import control.flow.analysis.OredredMethod;
//
//
///**
// * @author huge
// *
// * 2014年7月19日
// */
//public class GetDataFlow {   
//	/* 进行规则匹配后得到当前敏感API，进一步寻找参数信息
//	 * 参数信息
//	 * */
//	public static Logger logger = LogManager.getLogger(GetDataFlow.class);
//	public void ParameterMessage(){
////		初始化
//		SingleAPIAnalyzer singleAPIAnalyzer = new SingleAPIAnalyzer();
//		Content sr = null;
//		List<String> paramList = new ArrayList<String>();
//		List<ParamInfo> paramInfoList = new ArrayList<ParamInfo>();
//		FunctionIdentifier checknum = new FunctionIdentifier();
//		List<AndroidStaticSmaliResult> ResultPool = new ArrayList<AndroidStaticSmaliResult>();
//		
//		for(FileData fileData : CodeMapItemMapper.getInstance().getSmaliFileMap().values()){			
//			List<FunctionData> funList = fileData.getFunList();
//			for(FunctionData func : funList){ //
//	//			test(func);
////				System.out.println("testtesttest!!!!");
//				Map<Integer , OredredMethod > Method_Line = func.getMethod_Line();
//				Map<Integer , List<String>> registerMessage = func.getRegisterMessage();
//				for(OredredMethod ored : Method_Line.values()){
//					//进行规则比对，命中了进行定值分析					
//					for(SecRules rule : CodeMapItemMapper.getInstance().getRulesPool().get("singleRulesPool")){
//				
//						sr = singleAPIAnalyzer.analyzeAPIByRule(rule);					
//						checknum = sr.getCheckFunc();
//						String functionName = checknum.getFunctionName();
//						String ClassName = checknum.getClassName();
//						paramInfoList = sr.getParamInfoList();
//						paramList = checknum.getParamTypeList();
//						
//						Pattern apiPartern = Pattern.compile(functionName );
//						Pattern classNamePartern = Pattern.compile(ClassName);		
//						
//						if( (apiPartern.matcher(ored.getStatementStr()).find() ) && (classNamePartern.matcher(ored.getStatementStr()).find())){
//					//	if( (ored.getStatementStr().contains(ApiName)) && (ored.getStatementStr().contains(ClassName)) ){ //分析
//							List<String> parameterRegister = registerMessage.get(ored.getProLogue());
//						
//							//进行三类规则匹配 如果最上层为用户行为，则忽略
//							
//							if(sr.getSingleCheck().equals("true") && !func.getMethodData().userinterface){ //不进行参数分析，就此结束
//								AndroidStaticSmaliResult smaliResult = new AndroidStaticSmaliResult();
//								smaliResult.setFileName(fileData.getFileFullPath());
//								smaliResult.setFuncName(func.getMethodData().getMethod_Name());
//								smaliResult.setRiskType(rule.getRiskType());
//								smaliResult.setRiskLevel(rule.getRiskLevel());
//								smaliResult.setLine(ored.getProLogue());
//								smaliResult.setRuleAPIName(functionName);
//								smaliResult.setRuleID(rule.getRuleID());
//								smaliResult.setRuleName(rule.getRuleName());
//								smaliResult.setInvokeString(ored.getStatementStr());
//								smaliResult.setDescription(rule.getDecription());
//								ResultPool.add(smaliResult);
//								logger.info("filename : " + smaliResult.getFileName());
//								logger.info("FuncName : " + smaliResult.getFuncName());
//								logger.info("RiskType : " + smaliResult.getRiskType());
//								logger.info("Line : " + smaliResult.getLine());
//								logger.info("RuleAPIName : " + smaliResult.getRuleAPIName());
//								logger.info("RuleID : " + smaliResult.getRuleID());
//								logger.info("RuleName : " + smaliResult.getRuleName());
//								logger.info("InvokeString : " + smaliResult.getInvokeString());
//								logger.info("Description : " + smaliResult.getDescription());								
//							}
//							
//						
//						
//							//分为两类信息
//							List<ConstantResult> constantResult = new ArrayList<ConstantResult>();
//							if(sr.getSingleCheck().equals("false") && !func.getMethodData().userinterface ){ //进行参数分析
//								logger.info("进行参数分析。");	
//							//	List<Integer> registerResult = new ArrayList<Integer>();
//								if(rule.getRuleID().equals("1-4-1-1")){
//									if(parameterRegister.size()>=1){//参数在1个之上
//										System.out.println("Intent 参数分析！");
//										String[] resultString = InvokeRegister(parameterRegister.get(0),func,ored.getProLogue());
//										String analysisDingzhiParam = resultString[1];
//										System.out.println("Intent 参数 ： " + analysisDingzhiParam);
//										for(SecRules SecondRule : CodeMapItemMapper.getInstance().getRulesPool().get("intentRulesPool")){ // 2类规则
//											if((paramList.get(0)).equals(SecondRule.getContent().getStrReferConstantContraintType())){
//												IContent Secondcontent = SecondRule.getContent();
//												Pattern regPartern = Pattern.compile("([\\s\\S]*)(\")(" + Secondcontent.getStrRegexConstraint() + ")(\")([\\s\\S]*)");
//											//	System.out.println("22222");
//												if((analysisDingzhiParam!=null)&&(regPartern.matcher(analysisDingzhiParam).find())){															
//													System.out.println("命中Intent规则 " );
//													//进行后续定值分析
//													for(int secRegister=1;secRegister<parameterRegister.size();secRegister++){
//														String[] secResultString = InvokeRegister(parameterRegister.get(secRegister),func,ored.getProLogue());
//														if((secResultString[0]).equals("1")){// 定值
//															for(SecRules SecRule : CodeMapItemMapper.getInstance().getRulesPool().get("dingzhiRulesPool")){ // 2类规则
//																if((paramList.get(0)).equals(SecRule.getContent().getStrReferConstantContraintType())){
//																	IContent Seccontent = SecRule.getContent();
//																	Pattern SecregPartern = Pattern.compile("([\\s\\S]*)(\")(" + Seccontent.getStrRegexConstraint() + ")(\")([\\s\\S]*)");
//																//	System.out.println("22222");
//																	String secanalysisDingzhiParam = secResultString[1];
//																	if(SecregPartern.matcher(secanalysisDingzhiParam).find()){																
//																		ConstantResult constantresult = new ConstantResult();
//																		constantresult.setRuleID(SecRule.getRuleID());
//																		constantresult.setRuleName(SecRule.getRuleName());
//																		constantresult.setDescription(SecRule.getDecription());
//																		constantresult.setRiskType(SecRule.getRiskType());
//																		constantresult.setRiskLevel(SecRule.getRiskLevel());
//																		constantresult.setCode(secanalysisDingzhiParam);
//																		System.out.println("定值命中规则 : " + SecRule.getRuleID());
//																		
//																	
//																		AndroidStaticSmaliResult smaliResult = new AndroidStaticSmaliResult();
//																		smaliResult.setFileName(fileData.getFileFullPath());
//																		smaliResult.setFuncName(func.getMethodData().getMethod_Name());
//																		smaliResult.setRiskType(rule.getRiskType());
//																		smaliResult.setRiskLevel(rule.getRiskLevel());
//																		smaliResult.setLine(ored.getProLogue());
//																		smaliResult.setRuleAPIName(functionName);
//																		smaliResult.setRuleID(rule.getRuleID());
//																		smaliResult.setRuleName(rule.getRuleName());
//																		smaliResult.setInvokeString(ored.getStatementStr());
//																		smaliResult.setDescription(rule.getDecription());
//																		constantResult.add(constantresult);
//																		smaliResult.setConstantResult(constantResult);
//																		ResultPool.add(smaliResult);
//																		logger.info("filename : " + smaliResult.getFileName());
//																		logger.info("FuncName : " + smaliResult.getFuncName());
//																		logger.info("RiskType : " + smaliResult.getRiskType());
//																		logger.info("Line : " + smaliResult.getLine());
//																		logger.info("RuleAPIName : " + smaliResult.getRuleAPIName());
//																		logger.info("RuleID : " + smaliResult.getRuleID());
//																		logger.info("RuleName : " + smaliResult.getRuleName());
//																		logger.info("InvokeString : " + smaliResult.getInvokeString());
//																		logger.info("Description : " + smaliResult.getDescription());
//																	}												
//																}
//															}	
//														}
//													}
//												}												
//											}
//										}
//										
//									}
//								}
//								else
//								for(String s : paramList){
//									//得到需要进行参数检测的具体内容 是否为定值
//									if(parameterRegister.size()>=paramInfoList.size()){
//										logger.info("目标参数与规则参数一致，进入分析流程");
//										for(ParamInfo paraminfo : paramInfoList){
//											int paramIndex = paraminfo.getParamIndex();//具体参数
//											String checkType = paraminfo.getCheckType();
//											if(checkType.equals("constants")){
//												String[] resultString = InvokeRegister(parameterRegister.get(paramIndex-1),func,ored.getProLogue());
//												String ParamResult = resultString[0];	
//												if(ParamResult.equals("1")){ //需要获得const后面的内容
//													System.out.println(parameterRegister.get(paramIndex-1) + " const: " + resultString[1]);
//													System.out.println("进行定值规则比对！");
//													for(SecRules SecondRule : CodeMapItemMapper.getInstance().getRulesPool().get("dingzhiRulesPool")){ // 2类规则
//														if(s.equals(SecondRule.getContent().getStrReferConstantContraintType())){
//															IContent Secondcontent = SecondRule.getContent();
//														//	Pattern regPartern = Pattern.compile("([\\s\\S]*)(\")(" + Secondcontent.getStrRegexConstraint() + ")(\")([\\s\\S]*)");
//															Pattern regPartern = Pattern.compile( Secondcontent.getStrRegexConstraint() );
//														//	System.out.println("22222");
//															String analysisDingzhiParam = resultString[1];
//															if(regPartern.matcher(analysisDingzhiParam).find()){
//																ConstantResult constantresult = new ConstantResult();
//																constantresult.setRuleID(SecondRule.getRuleID());
//																constantresult.setRuleName(SecondRule.getRuleName());
//																constantresult.setDescription(SecondRule.getDecription());
//																constantresult.setRiskType(SecondRule.getRiskType());
//																constantresult.setRiskLevel(SecondRule.getRiskLevel());
//																constantresult.setCode(analysisDingzhiParam);;
//																System.out.println("定值命中规则 : " + SecondRule.getRuleID());
//																
//																AndroidStaticSmaliResult smaliResult = new AndroidStaticSmaliResult();
//																smaliResult.setFileName(fileData.getFileFullPath());
//																smaliResult.setFuncName(func.getMethodData().getMethod_Name());
//																smaliResult.setRiskType(rule.getRiskType());
//																smaliResult.setRiskLevel(rule.getRiskLevel());
//																smaliResult.setLine(ored.getProLogue());
//																smaliResult.setRuleAPIName(functionName);
//																smaliResult.setRuleID(rule.getRuleID());
//																smaliResult.setRuleName(rule.getRuleName());
//																smaliResult.setInvokeString(ored.getStatementStr());
//																smaliResult.setDescription(rule.getDecription());
//																constantResult.add(constantresult);
//																smaliResult.setConstantResult(constantResult);
//																ResultPool.add(smaliResult);
//																logger.info("filename : " + smaliResult.getFileName());
//																logger.info("FuncName : " + smaliResult.getFuncName());
//																logger.info("RiskType : " + smaliResult.getRiskType());
//																logger.info("Line : " + smaliResult.getLine());
//																logger.info("RuleAPIName : " + smaliResult.getRuleAPIName());
//																logger.info("RuleID : " + smaliResult.getRuleID());
//																logger.info("RuleName : " + smaliResult.getRuleName());
//																logger.info("InvokeString : " + smaliResult.getInvokeString());
//																logger.info("Description : " + smaliResult.getDescription());
//															}												
//														}
//													}										
//												}												
//												else if(ParamResult.equals("-1")){
//													System.out.println("不是定值！！！");
//												}
//											}
//										}
//										
//									}
//								}
//								
//							}
//							
//							
//						}
//					}
//				}
//				
//			}
//		}
//		CommonDataSet.setResultPool(ResultPool);
//		SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
//		sdf.applyPattern("yyyy年MM月dd日HH时mm分ss秒");
//		String timeStr = sdf.format(new Date());
//		CommonDataSet.setTime(timeStr);
//	}
//	
//	
//
//	private String[] InvokeRegister(String reg,FunctionData func,int oredProLogue ){ //reg为寄存器名字(eg p0 v1)
//		Map<Integer , List<String>> registerMessage = func.getRegisterMessage();
//		Map<Integer , OredredMethod > Method_Line = func.getMethod_Line();
//		Map<Integer , List<Integer>> registerLastll = func.getRegisterLast();
//		String result = "0";
//		String[] resultString = new String[2];
////		Object[] key = registerMessage.keySet().toArray();   
////		Arrays.sort(key); 
//	//	int s = InvokeLine(registerMessage,oredProLogue,reg); //得到最近一次寄存器出现的地方，进行指令分析
//	//	System.out.println("reg: " + reg + "	function static name : " + func.getMethodData().staticFileName);
//		List<Integer> rs = InvokeLine(registerMessage,oredProLogue,reg,registerLastll);
//		for(int s : rs){
//			String opcode = "";
//			if(Method_Line.containsKey(s)){
//				//opcode = Method_Line.get(s).getOpcode();
//				opcode = Method_Line.get(s).getStatementStr();
//			}
////			else if(reg.equals("p0")){
////				opcode = "this";
////			}
//			else if(reg.startsWith("p")&&(!reg.equals("p0"))){ 
//				//分情况，可能是之前中间有变化，那么需要追踪的是当前值还不是去调用函数中寻找
//				opcode = "pramer"; //应该是到之前的调用函数中寻找
//			}
//			
//			String[] resultTemp = new String[2];
//			
//			switch(ConstJudge(opcode)){ //判断上一层指令类型
//			case 0 : //定值
//			//	System.out.println("当前参数为定值");
//				String[] resultConst = opcode.split(" ");
//				String resultSecond = "";
//				for(int i=2;i<resultConst.length;i++){
//					resultSecond = resultSecond + " " + resultConst[i];
//				}
//				resultString[1] = resultSecond;
//				result = "1";
//				break;
//			case 1 : //向上查找,进行递归,注意停止条件
////				System.out.println("向上查找");
////				System.out.println(func.getMethod_Line().get(s).getStatementStr());
//				resultTemp = (InvokeRegister(reg,func,s));
//				result = resultTemp[0];
//				resultString[1] = resultTemp[1];
//				break;
//			case 2 : //重新寻找寄存器定值信息,寻找上一个语句，然后查找参数(move-result系列 )
//				int keyindex = registerLastll.get(s).get(0);
////				System.out.println("重新寻找寄存器定值信息");
////				System.out.println(func.getMethod_Line().get(s).getStatementStr());
////				System.out.println("oredProLogue: " + oredProLogue + "	keyindex: " + keyindex);
//
////				int index=key.length-1;
////				while( (int)  key[index] >=s){
////					index--;
////				}
//				List<String> returnRegister = registerMessage.get(keyindex); //目标语句的所有的register，进行分析
////				System.out.println("重新定位的寄存器");
////				System.out.println(func.getMethod_Line().get(keyindex).getStatementStr());
//				//当前重新定位的寄存器中有一个为定值则为定值
//				for(String register : returnRegister){
//					resultTemp = (InvokeRegister(register,func,keyindex));
//					if( resultTemp[0].equals("1")){
//						result="1";
//						resultString[1] = resultTemp[1];
//					}
//				}
//				if(!result.equals("1")){
//					result="-1";
//				}
//				break;
//			case 3 : //move 寻找第二个寄存器存储值
////				System.out.println("move寄存器，只寻找第二个寄存器内容");
////				System.out.println(func.getMethod_Line().get(s).getStatementStr() + "	寄存器： " + func.getMethod_Line().get(s).getRegister().get(0));
//				resultTemp = (InvokeRegister(func.getMethod_Line().get(s).getRegister().get(1),func,s));
//				result = resultTemp[0];
//				resultString[1] = resultTemp[1];
//				break;
//				
//				
//			case 4 : 
////				System.out.println(  "上一层函数寻找 4444444444");
//				
//				for(MethodData m : CodeMapItemMapper.getInstance().getInvokeMap().keySet()){
//					if( MethodDataEquals(m,func.getMethodData()) ){ 
////						System.out.println("获取调用函数！");
//				//		List<MethodData> invokem = CodeMapItemMapper.getInstance().getInvokeMap().get(m);
//						//每个调用地点都需要进行上溯查询 InvokeRegister(String reg,FunctionData func,int oredProLogue )
//						
//						for(MethodData inMethod : CodeMapItemMapper.getInstance().getInvokeMap().get(m) ){ //inMethod 为父类method
//							for(FileData fileData : CodeMapItemMapper.getInstance().getSmaliFileMap().values()){
//								for(FunctionData invokeFunc : fileData.funList ){								
//									if( MethodDataEquals(invokeFunc.getMethodData(),inMethod) ){
//								//		System.out.println("4444444444444");
//										for(String in : invokeFunc.getMethod_Line().get(inMethod.ProLogue).getRegister()){											
//									//		String resultTemps = (InvokeRegister(in,invokeFunc,inMethod.ProLogue))[0];
//											resultTemp = (InvokeRegister(in,invokeFunc,inMethod.ProLogue));
//											if( resultTemp[0].equals("1")){
//												result="1";
//												resultString[1] = resultTemp[1];
//											}
//										}
//										if(!result.equals("1")){
//											result="-1";
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//				
//				break;			
//				
//			case 5 : //aput/sput 寻找第一个寄存器存储值
//				resultTemp = (InvokeRegister(func.getMethod_Line().get(s).getRegister().get(0),func,s));
//				result = resultTemp[0];
//				resultString[1] = resultTemp[1];
//				break;
//			
//				//TODO field信息相关变化，未完成
////			case 6 ://iget 寻找第二个寄存器存储值，需找field的信息.根据当前获取的信息去寻找前面的信息
////				String staStr = Method_Line.get(s).getStatementStr();
////				String[] fieldStrTemp = staStr.split(";");
////				String fieldStr = fieldStrTemp[fieldStrTemp.length-2] + ":" + fieldStrTemp[fieldStrTemp.length-2];								
////				break;
//				
//			case 7 ://aget			
//				List<String> registerAget = Method_Line.get(s).getRegister();
//				if(reg.equals(registerAget.get(registerAget.size()-1))){//第三个指令，上溯查找
//					resultTemp = (InvokeRegister(reg,func,s));
//					result = resultTemp[0];
//					resultString[1] = resultTemp[1];
//				}
//				else{//寻找第二个指令
//					resultTemp = (InvokeRegister(registerAget.get(registerAget.size()-2),func,s));
//					result = resultTemp[0];
//					resultString[1] = resultTemp[1];
//				}				
//				break;
//			case 8: //aput 第一个和第三个寄存器上溯寻找 第二个寄存器则寻找第一个寄存器内容
//				List<String> registerAput = Method_Line.get(s).getRegister();
//				if( (reg.equals(registerAput.get(registerAput.size()-1))) || (reg.equals(registerAput.get(registerAput.size()-3))) ){//第三个指令，上溯查找
//					resultTemp = (InvokeRegister(reg,func,s));
//					result = resultTemp[0];
//					resultString[1] = resultTemp[1];
//				}
//				else{
//					resultTemp = (InvokeRegister(registerAput.get(registerAput.size()-3),func,s));
//					result = resultTemp[0];
//					resultString[1] = resultTemp[1];
//				}				
//				break;
//			case -1 :
//				result = "-1";
////				System.out.println(opcode + "确定不是定值");
//				break;
//				
//			default :
//				System.out.println("default!");
//				break;
//				
//			}
//			
//		}
//		resultString[0] = result;
//		return resultString;
//	}
//	
//	
//
//	private List<Integer> InvokeLine( Map<Integer , List<String>> registerMessage,int invokeLine,String reg  ,Map<Integer,List<Integer>> registerLast){
//		Set<Integer> keySet = registerLast.keySet();
//        Iterator<Integer> iter = keySet.iterator();
//		
//	//	List<Integer> lastline = registerLast.get(invokeLine);
//		List<Integer> lastl = new ArrayList<Integer>();
////		if(lastline != null && !lastline.isEmpty()){
////			for(int lastll : lastline){
//				while(iter.hasNext()){
//					 int key = iter.next();
//					if( ( key<invokeLine )&&( registerMessage.get(key)!=null )&&( !registerMessage.get(key).isEmpty()) ){					
//						if(  registerMessage.get(key).contains(reg)){
//							lastl.add(key);	
//							break;
//						}
////					}
////				}
//				
//			}
//		}
//				if(lastl.isEmpty()){
//					lastl.add(-1);
//				}			
//		
//		return lastl;
//	}
//	
//	private int ConstJudge(String opcode){//分几种情况，需要对指令们进行详细分类
//		
//		int JudgeResult = -1;
//		
//		if(opcode.contains("const")){ //为定值
//			JudgeResult = 0;
//		}
////		else if(opcode.contains("this")){  //p0 表示this，无关属性，直接定为确定非定值
////			JudgeResult = -1;
////		}
//		else if(opcode.contains("pramer")){ //可能出现v3 但是为pramer的情况
//			JudgeResult = 4;
//		}
//		/* 需要进上一步查找,这种情况是 寄存器在这些指令中没有变化或者变化为类型，运算变化。
//		 * -to- neg- not- 为类型转换, cmp为比较。 
//		 * 
//		 * */
//		else if( (opcode.contains("-to-")) || (opcode.contains("neg-")) || (opcode.contains("not-")) || (opcode.contains("cmp")) || (opcode.contains("invoke"))				 
//				){ 
//			JudgeResult = 1;
//		}
////		else if(opcode.contains("new-instance")){ //确定为非定值
////			JudgeResult = -1;
////		}
//		
//		/* move 分两种情况。第一种是不管是哪个寄存器都是向上寻找源寄存器的值  永远是寻找源寄存器的值
//		 * 第二种为包括result/exception的情况
//		 * return一般为最后一条语句，不会成为敏感调用或者 ，需要进一步*/
//		else if( (opcode.contains("move")) || (opcode.contains("iget")) ){
//			if( (opcode.contains("-result")) || (opcode.contains("-exception")) ){ //前一句调用得到的结果，需要判断之前调用的参数是否为定值
//				JudgeResult = 2;
//			}
//			else {
//				JudgeResult = 3;
//			}
//		}
//		
//		/* 实例操作指令  暂定为 确定为非定值  
//		 * check-cast 转换指令，可能抛出异常
//		 * instance-of 判断能否进行转换
//		 * new-instance 构造新实例，确定为非定值
//		 * */
//		
//		/* 字段操作指令
//		 * iget 为获取指令，但是获取数据为field，从field中去追寻
//		 * iget/sget  分为目标寄存器和源寄存器  filed有的为定值
//		 * iput/sput  两种都是带field，也就是说是实例赋值，为非定值
//		 *  */
//		
//		else if( (opcode.contains("iput")) || (opcode.contains("sput")) ){
//			JudgeResult = 5;
//		}
//		
////		else if((opcode.contains("sget"))){
////			JudgeResult = 6;			
////		}
//		
//		else if((opcode.contains("aget"))){
//			JudgeResult = 7;
//		}
//		else if((opcode.contains("aput"))){
//			JudgeResult = 8;
//		}
//	
//		/* 数组操作指令
//		 * array-length 获取数组长度。
//		 * new-array 构造新数组
//		 * filled-new-array/range 构造数组并填充
//		 * */
//
//		return JudgeResult;
//	}
//	
////	private boolean InvokeFunc(String reg,FunctionData func ){
////		
////	}
//	
//	private static boolean MethodDataEquals(MethodData A,MethodData B){
//		boolean result = true;		
////		String sa = A.getMethod_Prototype().toString();
////		String sb = B.getMethod_Prototype().toString();
//		if(! A.getMethod_Name().equals(B.getMethod_Name())){
//			result = false;
//		}
//		else if ( ! A.getMethod_Returntype().equals(B.getMethod_Returntype()) ){
//			result = false;
//		}
//		else if ( ! A.getMethod_Prototype().toString().equals(B.getMethod_Prototype().toString()) ){
//			result = false;			
//		}
//		else if ( ! A.getStaticFileName().toString().equals(B.getStaticFileName().toString()) ){
//			result = false;			
//		}
//		return result;		
//	}
//	
//	
//	public void test(FunctionData func){
//		Map<Integer , OredredMethod > Method_Line = func.getMethod_Line();
//		Map<Integer , List<String>> registerMessage = func.getRegisterMessage();
//		for(int oredp : Method_Line.keySet()){
//		//	if(Method_Line.get(oredp).getOpcode().startsWith("iput")){
//				List<String> registerList = registerMessage.get(oredp); 
//				for(String reg : registerList){			
//					InvokeRegister(reg,func,oredp);					
//					
//		//		}				
//			}
//		}
//	}
//	
//
//}
