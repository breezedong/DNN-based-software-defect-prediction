/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.antlr.runtime.tree.CommonTree;
import org.apache.logging.log4j.*;

import com.ssca.analyse.smali.dataflow.RegisterMessage;
import com.ssca.commonData.API_Data;
import com.ssca.commonData.Android_API_DataSet;

//import test.security.analyze.UserInterfaceAnalyzer;
//import test.security.analyze.UserInterfaceAnalyzer.Content;
////import test.security.analyze.SingleAPIAnalyzer.Content;
//import test.security.it.IContent;
//import test.security.rules.FunctionIdentifier;
//import test.security.rules.ParamInfo;
//import test.security.rules.ReadWhiteList;
//import test.security.rules.SecRules;

/**
 * @author huge
 *
 * 2014年6月23日
 */
public class GetControlFlow {
	public static Logger logger = LogManager.getLogger(GetControlFlow.class);
	
	
	public static void GetFlow(CommonTree t,String fileName,List<String> whiteList){ //输入为当前smali文件的语法树
		
		if(t.getChildren().toString().contains("I_METHODS")){ //没有method 不显示后续流程了		
			String fileFullPath = t.getChild(0).getText();
//		Map<MethodData,String> dal = new HashMap<MethodData,String>();
			if(!WhiteAPI(fileFullPath,whiteList)){
				//	if(true){
				FileData filedata = new FileData();
				filedata.setFileFullPath(fileFullPath);
				filedata.setFileStaticName(fileName);
				logger.info("smali file :" + fileFullPath + "  start!");
				String sTemp ;
				//System.out.println(t.getChildCount());
				for(int i=1;i<t.getChildCount();i++){
					CommonTree tChild =(CommonTree) t.getChild(i);
					sTemp = tChild.token.getText();
					switch(sTemp){
					case "I_ACCESS_LIST":
						List<String> access_list = new ArrayList<String>();
						for(int j=0;j<tChild.getChildCount();j++){
							access_list.add(tChild.getChild(j).getText());
						}
						filedata.setAccess_List(access_list);
						p(access_list);
								logger.info("access_list end!");
						break;
					case "I_SUPER"://此类的super类的相关信息
						List<String> super_list = new ArrayList<String>();
						for(int j=0;j<tChild.getChildCount();j++){
							super_list.add(tChild.getChild(j).getText());
						}
						filedata.setSuper_Class(super_list);
						p(super_list);
								logger.info("super_list end!");
						break;
					case "I_IMPLEMENTS": 
						List<String> implements_list = new ArrayList<String>();
						for(int j=0;j<tChild.getChildCount();j++){
							implements_list.add(tChild.getChild(j).getText());
						}
						filedata.setImplements_Class(implements_list);
						p(implements_list);
									logger.info("implements end!");
						break;
					case "I_SOURCE":
						filedata.setSource_Name(tChild.getChild(0).getText());
									logger.info("source end!");
						break;
					case "I_METHODS"://使用子函数进行分析
						List<FunctionData> funList = new ArrayList<FunctionData>();
						for(int j=0;j<tChild.getChildCount();j++){
							funList.add( GetMethodsData((CommonTree) tChild.getChild(j),fileFullPath) );
						}
						filedata.setFunList(funList);
								logger.info("methods end!");
						break;
					case "I_FIELDS":
						List<FieldData> fields = new ArrayList<FieldData>();
						for(int j=0;j<tChild.getChildCount();j++){
							fields.add( GetFieldsData((CommonTree) tChild.getChild(j)) );
						}
						filedata.setFields(fields);
									logger.info("fields end!");
						break;
						
					case "I_ANNOTATIONS": //TODO 结构复杂，需要重新写
//				List<AnnotationData> annotations = new ArrayList<AnnotationData>();
//				for(int j=0;j<tChild.getChildCount();j++){
//					annotations.add( GetAnnotationData((CommonTree) tChild.getChild(j)) );
//				}
//				filedata.setAnnotations(annotations);
						break;
						
					default:
						logger.error("smali file error!");
						//System.out.println( sTemp + " error!");
						break;
						
					} //不是白名单
					
				}		
				logger.info("smali file end!");
				//控制流分析的结果存在filedata中
				CodeMapItemMapper.getInstance().addSmaliFileMap(fileFullPath, filedata);//将smalifile解析后的内容存入map
			}
		}
	}
	
	
	public static FunctionData GetMethodsData(CommonTree methodTree, String fileFullPath){  
		FunctionData functionData = new FunctionData();
		MethodData methodData = new MethodData();
	//	functionData.setMethod_Name(methodTree.getChild(0).getText());
		methodData.setMethod_Name(methodTree.getChild(0).getText());
		methodData.setStaticFileName(fileFullPath);
		
		//加入指令变化,记录指令所在行数，进行到达定值分析时，追寻上一层参数是否为定值。
		Map<Integer , List<String>> registerMessage = new ConcurrentHashMap<Integer , List<String>>();
		Map<String ,List<Integer>> registerCall = new ConcurrentHashMap<String,List<Integer>>();
		//TODO api 分析注释掉
//		methodData.setUserinterface(GetUserInterfaceFlag(methodData.getMethod_Name()));
//		logger.info("setUserinterface");
//		CodeMapItemMapper.getInstance().getRulesPool().get("guiAnalyzePool");
		for(int index=1;index<methodTree.getChildCount();index++){
			String dalvik = "";
			String method_flag = methodTree.getChild(index).getText();
		//	logger.debug("method_flag" + method_flag);
			switch(method_flag){
			case "I_METHOD_PROTOTYPE":
			//	functionData.setMethod_Returntype(methodTree.getChild(index).getChild(0).getChild(0).getText());		
				methodData.setMethod_Returntype(methodTree.getChild(index).getChild(0).getChild(0).getText());
				List<String> Method_Prototype = new ArrayList<String>();//
				for(int j=1;j<methodTree.getChild(index).getChildCount();j++){
					Method_Prototype.add(methodTree.getChild(index).getChild(j).getText());
				}
			//	functionData.setMethod_Prototype(Method_Prototype);
				methodData.setMethod_Prototype(Method_Prototype);  
				break;
			case "I_ACCESS_LIST":
				List<String> Access_List = new ArrayList<String>();
				for(int j=0;j<methodTree.getChild(index).getChildCount();j++){
					Access_List.add(methodTree.getChild(index).getChild(j).getText());
				}
				functionData.setAccess_List(Access_List);
				break;
			case "I_REGISTERS":
				functionData.setRegisters_Number( Integer.parseInt( methodTree.getChild(index).getChild(0).getText() ));
				break;
			case "I_ORDERED_METHOD_ITEMS": 
				//I_ORDERED_METHOD_ITEMS method语句的存储 一般格式是指令+寄存器+XX  具体的return和参数需要进一步细化
			
				RegisterMessage regNum = new RegisterMessage();
				for(int i=0;i<methodTree.getChild(index).getChildCount();i++){
					CommonTree StateChild = (CommonTree) methodTree.getChild(index).getChild(i);
					String DMname = StateChild.getText();
					if(DMname.startsWith("I_STATEMENT_FORMAT")){//表示为指令执行语句 TODO I_STATEMENT_ARRAY_DATA需要用其他的格式来存储,I_LABEL一般对应着一些跳转信息。						
				//		logger.debug("I_STATEMENT_FORMAT" + StateChild.getChildren().toString());
//						if( ("I_STATEMENT_FORMAT" + StateChild.getChildren().toString()).contains("invoke exception:") ){
//							logger.debug("error!");
//						}
						OredredMethod oredredMethod = new OredredMethod();
						String stateTemp = "";
						HashSet<Integer> ProgramExit = new HashSet<Integer>(); 
					
						oredredMethod.setProLogue( StateChild.token.getLine());
						oredredMethod.setStatements(StateChild);
						oredredMethod.setOpcode( StateChild.getChild(0).getText());
						dalvik = dalvik + StateChild.getChild(0).getText() + " ";
						List<String> register = new ArrayList<String>();
						String registerCallName="";
					
						for(int j=0;j<StateChild.getChildCount();j++){
							
							if(StateChild.getChild(j).getText().contains("I_REGISTER_")){
								for(int registerNum=0;registerNum<StateChild.getChild(j).getChildCount();registerNum++){
									register.add(StateChild.getChild(j).getChild(registerNum).getText());
								}
								stateTemp = stateTemp+register.toString();
							} 
							else{
								stateTemp=stateTemp+StateChild.getChild(j).getText()+" ";
							}
						}						
						//针对指令进行寄存器处理
						
						if( (register.size()==0)&&(StateChild.getChildCount()>=2)&&(!StateChild.getChildren().toString().contains("I_REGISTER_")) ){
							int reNum = regNum.RegisterNumber(StateChild.getChild(0).getText());
						//	System.out.print(StateChild.getChild(0).getText() + "  "+reNum);
							for(int tereNum = 0 ;tereNum<reNum;tereNum++){
								register.add(StateChild.getChild(tereNum+1).getText());
							}
						}
				
						//test register
						List<String> registerConst = new ArrayList<String>();
						registerConst.addAll(register);
						
						//加入API_Data 的内容
					//	String sss = StateChild.getChildren().toString();
						if(stateTemp.startsWith("invoke") ) {
							API_Data api = new API_Data();
							api.setNamespace(StateChild.getChild(2).getText());
							api.setAPIName(StateChild.getChild(3).getText());
							api.setReturn_Name("");
							int reNumber = 0;
							for(int j=0;j<StateChild.getChildCount();j++){
								if(StateChild.getChild(j).getText().contains("I_METHOD_PROTOTYPE")){
									for(int cj=0;cj<StateChild.getChild(j).getChildCount();cj++){
										if(!StateChild.getChild(j).getChild(cj).getText().contains("I_METHOD_RETURN_TYPE")){
											reNumber++;
											stateTemp = stateTemp + StateChild.getChild(j).getChild(cj).getText();
										}
										else{
											stateTemp = stateTemp + StateChild.getChild(j).getChild(cj).getChild(0).getText() + " ";
											api.setReturn_Name(StateChild.getChild(j).getChild(cj).getChild(0).getText());
//											if(api.getAPIName().equals("<init>") && api.getNamespace().equals("Ljava/lang/StringBuilder;") && api.getReturn_Name().equals("V")){
//										//		System.out.println(api.getAPIName() + "    " + api.getNamespace() + "    " + api.getReturn_Name());												
//											}
										}
									}									
								} 
							}
							stateTemp = stateTemp.replaceAll("I_METHOD_PROTOTYPE", "");
							
							Android_API_DataSet.addApi(api);
							registerConst.clear();
		//					System.out.println(StateChild.getLine() + "    " +stateTemp );
							if(StateChild.getChildren().toString().contains("/range")){
								if(reNumber>0){
									if(register.size()>1){
										String pmax = register.get(1);
										char[] rmax = pmax.toCharArray();							
										String rtemp = String.valueOf(rmax[1]);
										for(int min =  Integer.parseInt(rtemp)-reNumber+1 ; min<=Integer.parseInt(rtemp);min++){
											String pm = String.valueOf(rmax[0])+min;
											registerConst.add(pm);
										}																	
									}
									else{
										registerConst.addAll(register);
									}
								}								
							}
							else{
								//get registerMessageMap
								for(int min = register.size()-reNumber;min<register.size();min++){
									registerConst.add(register.get(min));
								}
								//get and set registerCallMap
								if(register.size()>=1){
									registerCallName=register.get(0);
									List callList = registerCall.get(registerCallName);
									if(callList==null||callList.size()==0){
										callList = new ArrayList<Integer>();
									}
									callList.add(StateChild.getLine());
									registerCall.put(registerCallName, callList);
								}
								
							}
							
						}
						
			
						//CodeMapItemMapper.getInstance().addregisterMessage(StateChild.getLine(), register);
						registerMessage.put(StateChild.getLine(), registerConst);
						
						
						oredredMethod.setStatementStr(stateTemp);
						oredredMethod.setRegister(register);
						boolean plusFlag = false; int plusInt = 1;
//   					System.out.println("oredredMethod.str : " + oredredMethod.getStatementStr());
						//	if( (i+plusInt<methodTree.getChild(4).getChildCount()) ){
						while( (plusFlag == false) && ( i+plusInt<methodTree.getChild(4).getChildCount() )){
							if(methodTree.getChild(4).getChild(i+plusInt).getText().startsWith("I_STATEMENT_FORMAT") ){
							//	if( (i+1<methodTree.getChild(4).getChildCount()) && (methodTree.getChild(4).getChild(i+1).getText().startsWith("I_LABEL")) 
//										&&( (methodTree.getChild(4).getChild(i+1).getChild(0).getText().startsWith("cond_")) 
//												|| (methodTree.getChild(4).getChild(i+1).getChild(0).getText().startsWith("goto_")) ) 
								if(	stateTemp.startsWith("return")){
									//	System.out.println("return: " + methodTree.getChild(4).getChild(i).getLine() + " www " + methodTree.getChild(4).getChild(i+1).getLine() );
									ProgramExit.add(0); 
									plusFlag=true;																		
								}
								else{
									ProgramExit.add(methodTree.getChild(4).getChild(i+plusInt).getLine());
									plusFlag=true;
								}
							}
							else{
								plusInt++;
							}						
						}
						//	}
						if(plusFlag==false){
							//	TrueFlag[1]=0;
							ProgramExit.add(0);
						}
						
						//TODO 分析FALSEflag对应地址，有跳转的指令加入跳转指令.目前包括了if和goto,其他的跳转指令再添加。
						if(stateTemp.startsWith("if-")){ //根据指令分析跳转条件，if-XX进行条件跳转
							String JumpAddress = StateChild.getChild(StateChild.getChildCount()-1).getText();//跳转指令flag,cond形式，表示地址
							for(int t=0;t<methodTree.getChild(index).getChildCount();t++){
								//此语句对应的下一句tree就是跳转的地方
								String s = methodTree.getChild(index).getChild(t).getText();
								if(s.startsWith("I_LABEL"))
									if(methodTree.getChild(index).getChild(t).getChild(0).getText().startsWith(JumpAddress) ){				
										if(t+1<methodTree.getChild(index).getChildCount()){
											//	FalseFlag[1] = methodTree.getChild(index).getChild(t+1).getLine();	
											ProgramExit.add(methodTree.getChild(index).getChild(t+1).getLine());
										}
										else{
											//	FalseFlag[1]=0;
											ProgramExit.add(0);
										}						
									}
							}				
						}
						else if(stateTemp.startsWith("goto")){//goto进行无条件跳转  TODO catch中有时也会有goto进行跳转，但是没有放入methodLine。
							String JumpAddress = StateChild.getChild(StateChild.getChildCount()-1).getText();//跳转指令flag,cond形式，表示地址
							for(int t=0;t<methodTree.getChild(index).getChildCount();t++){
								//此语句对应的下一句tree就是跳转的地方
								String s = methodTree.getChild(index).getChild(t).getText();
								if(s.startsWith("I_LABEL"))
								if(methodTree.getChild(index).getChild(t).getChild(0).getText().startsWith(JumpAddress) ){				
									if(t+1<methodTree.getChild(index).getChildCount()){
									//	FalseFlag[1] = methodTree.getChild(index).getChild(t+1).getLine();		
										ProgramExit.add(methodTree.getChild(index).getChild(t+1).getLine());
									}
									else{
									//	FalseFlag[1]=0;
										ProgramExit.add(0);
									}						
								}
							}
					//	System.out.println("goto~");	
						}
						//switch 多方跳转
						else if(stateTemp.startsWith("packed-switch")){ 
//							System.out.println("packed-switch!"); 
							String switchLabel = methodTree.getChild(index).getChild(i).getChild(2).getText();
							List<String> switchElements = new ArrayList<String>();	
							for(int t=i;t<methodTree.getChild(index).getChildCount();t++){
								if(methodTree.getChild(index).getChild(t).getText().startsWith("I_STATEMENT_PACKED_SWITCH")){
									if( (methodTree.getChild(index).getChild(t-1).getChild(0).getText()).equals(switchLabel) ){
										// I_PACKED_SWITCH_START_KEY      I_PACKED_SWITCH_ELEMENTS
//										String switchStartKey = methodTree.getChild(index).getChild(t).getChild(0).getChild(0).getText();
										for(int switchNumbers=0;switchNumbers<methodTree.getChild(index).getChild(t).getChild(1).getChildCount();switchNumbers++){
											switchElements.add(methodTree.getChild(index).getChild(t).getChild(1).getChild(switchNumbers).getText());
										}
									}
								}
							}	
							for(String swstr : switchElements){
								for(int t=i;t<methodTree.getChild(index).getChildCount();t++){
									if(methodTree.getChild(index).getChild(t).getChild(0).getText().equals(swstr)){
										ProgramExit.add(methodTree.getChild(index).getChild(t+1).getLine());
									}
								}								
							}
//							System.out.println("packed-switch end !");
						}
						else if(stateTemp.startsWith("sparse-switch")){
//							System.out.println("sparse-switch!"); 
							String switchLabel = methodTree.getChild(index).getChild(i).getChild(2).getText();
							List<String> switchElements = new ArrayList<String>();	
							for(int t=i;t<methodTree.getChild(index).getChildCount();t++){
								if(methodTree.getChild(index).getChild(t).getText().startsWith("I_STATEMENT_SPARSE_SWITCH")){
									if( (methodTree.getChild(index).getChild(t-1).getChild(0).getText()).equals(switchLabel) ){
										// I_PACKED_SWITCH_START_KEY      I_PACKED_SWITCH_ELEMENTS
									//	String switchStartKey = methodTree.getChild(index).getChild(t).getChild(0).getChild(0).getText();
										for(int switchNumbers=1;switchNumbers<methodTree.getChild(index).getChild(t).getChild(0).getChildCount();switchNumbers++){
											switchElements.add(methodTree.getChild(index).getChild(t).getChild(0).getChild(switchNumbers).getText());
											switchNumbers++;
										}
									}
								}
							}	
							for(String swstr : switchElements){
								for(int t=i;t<methodTree.getChild(index).getChildCount();t++){
									if(methodTree.getChild(index).getChild(t).getChild(0).getText().equals(swstr)){
										ProgramExit.add(methodTree.getChild(index).getChild(t+1).getLine());
									}
								}								
							}
//							System.out.println("sparse-switch end !");
						}
						
						else{								
							//得到函数调用关系图		
							if(stateTemp.startsWith("invoke")){ //调用函数指令  StateChild 	
								methodData.setProLogue( StateChild.token.getLine() );
								GetInvokeList(StateChild,methodData,fileFullPath);											
							}					
						}
	//					oredredMethod.setFalseFlag(ProgramExit);
						oredredMethod.setProgramExit(ProgramExit);
					
					//	Method_Line.add(oredredMethod);
						functionData.addMethod_Line(StateChild.token.getLine(),oredredMethod);
//						logger.debug(StateChild.getLine() + "    " +stateTemp + "end");
					}
				}
				break;
			case "I_CATCHES":
				List< List<String> > catches = new ArrayList< List<String> >();
				for(int j=0;j<methodTree.getChild(index).getChildCount();j++ ){
					List<String> Catch = new ArrayList<String>();
					for(int k=0;k<methodTree.getChild(index).getChild(j).getChildCount();k++){
						Catch.add(methodTree.getChild(index).getChild(j).getChild(k).getText());
					}
					catches.add(Catch);
				}
				functionData.setCatches(catches);				
				break;
			case "I_PARAMETERS": //参数寄存器,得到初始的寄存器状态
				//I_PARAMETERS
				List<Parameter> parameters = new ArrayList<Parameter>();
				for(int j=0;j<methodTree.getChild(index).getChildCount();j++){ //methodTree.getChild(6).getChild(j)
					parameters.add( GetParameter((CommonTree) methodTree.getChild(index).getChild(j)) );			
				}
				functionData.setParameters(parameters);
				registerMessage.put(methodTree.getChild(index).getLine(), ParameRegister((CommonTree) methodTree.getChild(index)));
				break;
			case "I_ANNOTATIONS": 
				//I_ANNOTATIONS TODO  结构较复杂，需要进一步细化
//				List<AnnotationData> annotations = new ArrayList<AnnotationData>();
//				for(int j=0;j<methodTree.getChild(index).getChildCount();j++){
//					annotations.add( GetAnnotationData((CommonTree) methodTree.getChild(index).getChild(j)) );
//				}
//				functionData.setAnnotations(annotations);
				break;
			case "I_LOCALS" : //当前method位置
//				for(int j=0;j<methodTree.getChild(index).getChildCount();j++){
//					System.out.println("locals : " + methodTree.getChild(index).getChild(j).getText());
//				}
				break;
			default :
		//		System.out.println("method error flag : " + method_flag);
				logger.error("method error flag : " + method_flag);	
			}
			if(dalvik!=null&&!dalvik.isEmpty()){
		//		System.out.println("method name : " + methodData.Method_Name + "  dal :  " + dalvik);
				DalvikMap.getInstance().addDal(methodData, dalvik);				
			}
		}
	
		
		Map<Integer, List<Integer>> registerLast = new TreeMap<Integer, List<Integer>>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    }
                });
	
	//	logger.info("OredredMethod register start!");
		for(OredredMethod oredred : functionData.getMethod_Line().values()){
			HashSet<Integer> ProgramExit = oredred.getProgramExit();
			for(int line : ProgramExit){
				if( !registerLast.keySet().contains(line)){
					List<Integer> last = new ArrayList<Integer>();
					last.add(oredred.ProLogue);
					registerLast.put(line, last);
					//		System.out.println("NewFlaseRegisterKey : " + falseFlag[1] + "	value : " + oredred.ProLogue);
				}
				else{
					if(!registerLast.get(line).contains(oredred.ProLogue)){
						registerLast.get(line).add(oredred.ProLogue);
						//			System.out.println("FlaseRegisterKey : " + falseFlag[1] + "	value : " + oredred.ProLogue);
					}
				}
				
			}			
		}
	
	//	logger.info("OredredMethod register end!");
		
	//	CodeMapItemMapper.getInstance().addregisterMessage(methodData, registerMessage);
		functionData.setRegisterLast(registerLast);
		functionData.setRegisterMessage(registerMessage);
		functionData.setMethodData(methodData);
		functionData.setRegisterCall(registerCall);
		
	//	logger.info("functionData end!");
		return functionData;		
	}
	
	public static FieldData GetFieldsData(CommonTree fieldTree){ //传参为field的tree
	
		FieldData fieldData = new FieldData(); 
		fieldData.setFieldName(  fieldTree.getChild(0).getText() ); //字段名
	//	System.out.println("name : " + fieldTree.getChild(0).getText());
		for(int index=1;index<fieldTree.getChildCount();index++){			
			CommonTree tChild =(CommonTree) fieldTree.getChild(index);
			String sTemp = tChild.token.getText();
			switch(sTemp){   
			case "I_ACCESS_LIST": //访问权限
				List<String> Access_List = new ArrayList<String>();
				for(int j=0;j<tChild.getChildCount();j++){
					Access_List.add(tChild.getChild(j).getText());
				}
				fieldData.setAccessList(Access_List);
				break;
			case "I_FIELD_TYPE": //字段类型
				fieldData.setField_Type(tChild.getChild(0).getText());
				break;
			case "I_ANNOTATIONS": //字段说明
				List<AnnotationData> annotationData = new ArrayList<AnnotationData>();
				for(int j=0;j<tChild.getChildCount();j++){
					annotationData.add( GetAnnotationData((CommonTree) fieldTree.getChild(3).getChild(j)));
				}
				fieldData.setAnnotations(annotationData);
				break;
			case "I_FIELD_INITIAL_VALUE"://static_field后面会有固定值 =X
				fieldData.setField_Initial_value(tChild.getChild(0).getText());
				break;
			default:
				System.out.println("field : "+tChild.getText());
				break;
			}
			
		}	
		
		return fieldData;
		
	}
	
	public static AnnotationData GetAnnotationData( CommonTree AnnotationTree){ 
		AnnotationData annotation = new AnnotationData();
	//	System.out.println(" child number : "+ AnnotationTree.getChildCount());
		if(AnnotationTree.getChildCount()>=2){ //有的时候没有子节点
			annotation.setAnnotation_Name(AnnotationTree.getChild(0).getText());
			annotation.setSubAnnotation(AnnotationTree.getChild(1).getChild(0).getText());
			Map<String,List<String>> AnnotationValue = new ConcurrentHashMap<String,List<String>>();
			//	System.out.println(AnnotationTree.getChild(0).getText() + "   " + AnnotationTree.getChild(1).getChild(0).getText() );
			for(int i=1;i<AnnotationTree.getChild(1).getChildCount();i++){
				String key = AnnotationTree.getChild(1).getChild(i).getChild(0).getText();
				List<String> values = new ArrayList<String>();
				if(AnnotationTree.getChild(1).getChild(i).getChild(1).getChildCount()==0){//直接value
					values.add(AnnotationTree.getChild(1).getChild(i).getChild(1).getText());
				}
				else{
					for(int j=0;j<AnnotationTree.getChild(1).getChild(i).getChild(1).getChildCount();j++){
						values.add(AnnotationTree.getChild(1).getChild(i).getChild(1).getChild(j).getText());
					}				
				}
		//		System.out.println("key : " + key + "  value :" + values.toString());
				AnnotationValue.put(key, values);
			}
			annotation.setAnnotationValue(AnnotationValue);				
		}
		return annotation;		
	}
	
	public static Parameter GetParameter(CommonTree ParameterTree ){
		Parameter parameter = new Parameter();
		List<String> params = new ArrayList<String>();
		for(int j=0;j<ParameterTree.getChildCount()-1;j++){
			params.add(ParameterTree.getChild(j).getText());
		}
		parameter.setParams(params);
		
		List<AnnotationData> annotations = new ArrayList<AnnotationData>();
		for(int j=0;j<ParameterTree.getChild(ParameterTree.getChildCount()-1).getChildCount();j++){
			annotations.add( GetAnnotationData((CommonTree) ParameterTree.getChild(ParameterTree.getChildCount()-1).getChild(j)));
		}
		parameter.setAnnotations( annotations );
		return parameter;
		
	}
	
	public static List<String> ParameRegister(CommonTree ParametersTree){
		List<String> register = new ArrayList<String>();
		for(int j=0;j<ParametersTree.getChildCount();j++){
			register.add(ParametersTree.getChild(j).getChild(0).getText());
		}
		return register;		
	}
	
	//得到调用信息
	public static void GetInvokeList(CommonTree InvokeTree,MethodData fatherMethodData, String fileFullPath){ //输入为当前invoke树和当前method的信息
		//invokeTree 的结构-- 指令 + 使用的寄存器 + 调用函数的父类 + 被调用的函数 + （参数类型+被调用函数的返回类型）
	//	可能存在多线程的问题，但是这里不一定含有run的那个类
		
		MethodData methodData = new MethodData();	
		
		
		methodData.setMethod_Name(InvokeTree.getChild(3).getText());
		methodData.setMethod_Returntype(InvokeTree.getChild(4).getChild(0).getChild(0).getText());
		methodData.setStaticFileName(InvokeTree.getChild(2).getText());
		List<String> protoType = new ArrayList<String>();
		if( InvokeTree.getChild(4).getChildCount()>1 ){
			for(int i=1;i<InvokeTree.getChild(4).getChildCount();i++){
				protoType.add(InvokeTree.getChild(4).getChild(i).getText());
			}			
		}
		methodData.setMethod_Prototype(protoType);
		methodData.setProLogue(0);//初始化为0，表示为被调用的子函数
		if(InvokeTree.getChild(3).getText().equals("start")){	
			methodData.setMethod_Name("run()");
			methodData.setMethod_Returntype("V");
			methodData.setStaticFileName(InvokeTree.getChild(2).getText());
		}
		
		boolean codeMethodFlag = true;
		for(MethodData method : CodeMapItemMapper.getInstance().getInvokeMap().keySet()){
			if( (MethodDataEquals(method,methodData)==true)&&(MethodDataEquals(methodData,fatherMethodData)==false) ){
				CodeMapItemMapper.getInstance().addInvokeListMap(method, fatherMethodData);	
				codeMethodFlag = false;
			}
		}
		if( (codeMethodFlag)&&(MethodDataEquals(methodData,fatherMethodData)==false) ){
			List<MethodData> methodDataList = new ArrayList<MethodData>();
			methodDataList.add(fatherMethodData);
			CodeMapItemMapper.getInstance().addInvokeMap(methodData, methodDataList);			
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
		else if ( ! A.getStaticFileName().toString().equals(B.getStaticFileName().toString()) ){
			result = false;			
		}
		return result;		
	}
	

	private static boolean WhiteAPI(String name,List<String> whiteList){
		boolean result = false;
		
		
//		whiteList.add("com/google/"); whiteList.add("com/admob/"); whiteList.add("com/paypal/"); whiteList.add("com/scoreloop/"); whiteList.add("com/umpay/"); 
//		whiteList.add("com/netqin/"); whiteList.add("com/mobclick/"); whiteList.add("com/mappn/"); whiteList.add("backport/android/"); whiteList.add("com/papaya/"); 
//		whiteList.add("com/openfeint/"); whiteList.add("com/mobclix/"); whiteList.add("com/greystripe/"); whiteList.add("net/youmi/"); whiteList.add("com/wooboo/"); 
//		whiteList.add("com/flurry/"); whiteList.add("com/gfan/"); whiteList.add("com/adwhirl/"); whiteList.add("com/tapjoy/"); whiteList.add("org/meteoroid/"); 
//		whiteList.add("com/socialin/"); whiteList.add("net/box/"); whiteList.add("com/android/mms"); whiteList.add("jp/rbweb/"); whiteList.add("org/appforce/"); 
//		whiteList.add("com/nuance/"); whiteList.add("com/millennialmedia/"); whiteList.add("com/vdopia/"); whiteList.add("com/lotaris/"); whiteList.add("com/android/providers"); 
//		whiteList.add("org/sipdroid/"); whiteList.add("org/doubango/"); whiteList.add("com/xtify/"); whiteList.add("org/achartengine/"); whiteList.add("com/smobile/"); 
//		whiteList.add("com/ovozo/"); whiteList.add("cn/com/android/"); whiteList.add("com/medialets/"); whiteList.add("com/pontiflex/"); whiteList.add("com/csipsimple/"); 
//		whiteList.add("com/tapjoy/"); whiteList.add("com/zong/"); whiteList.add("com/myspace/"); whiteList.add("com/facebook/"); whiteList.add("com/eoemobile/"); 
//		whiteList.add("pep/com/lbs"); whiteList.add("org/freecoder/"); whiteList.add("org/appcelerator/"); whiteList.add("j2ab/android/"); whiteList.add("com/wiyun/"); 
//		whiteList.add("com/vpon/"); whiteList.add("com/urbanairship/"); whiteList.add("com/transpera/"); whiteList.add("com/streamezzo/"); whiteList.add("com/android/vending"); 
//		whiteList.add("engine/scoreloop/"); whiteList.add("com/quickoffice/"); whiteList.add("com/phonegap/"); whiteList.add("com/openet/"); whiteList.add("com/mobfox/"); 
//		whiteList.add("com/storm8/"); whiteList.add("com/tencent/"); whiteList.add("org/acra/CrashReportDialog/"); whiteList.add("com/appflight/"); 
//		whiteList.add("org/apache/"); whiteList.add("org/slf4j/"); whiteList.add("com/alipay/"); whiteList.add("com/chinaMobile/");  whiteList.add("com/amazon/");
//		whiteList.add("android/support/"); whiteList.add("/R$"); whiteList.add("/R;");//whiteList.add("com/dianping/");
		
		
		
		for(String whiteapi : whiteList){
			if(name.contains(whiteapi)){
				result = true;
				return result;
			}
		}
		
		return result;
		
	}
	
	public static void p(List<String> list){
		for(int k=0;k<list.size();k++){
			//System.out.println(list.getClass()+":::::");
			System.out.println(list.get(k));
		}
	}
	
//	private static boolean GetUserInterfaceFlag(String method_name){
//		boolean r = false;
//		UserInterfaceAnalyzer userInterface = new UserInterfaceAnalyzer();
//		Content sr = null;
////		List<String> paramList = new ArrayList<String>();
////		List<ParamInfo> paramInfoList = new ArrayList<ParamInfo>();
//		FunctionIdentifier checknum = new FunctionIdentifier();
//		//标记method类型,用户类型时进行忽略 
//		for(SecRules rule : CodeMapItemMapper.getInstance().getRulesPool().get("guiAnalyzePool")){
//			sr = userInterface.analyzeGuiByRule(rule);
//			checknum = sr.getCheckFunc();
//			String functionName = checknum.getFunctionName();
//			String ClassName = checknum.getClassName();
//			Pattern apiPartern = Pattern.compile(functionName );
////			Pattern classNamePartern = Pattern.compile(ClassName);	
//			if(apiPartern.matcher(method_name).find())	{
//	//			System.out.println("onclick : " + method_name);
//				r=true;
//			}	
//		}		
//		return r;
//	}
	
}
