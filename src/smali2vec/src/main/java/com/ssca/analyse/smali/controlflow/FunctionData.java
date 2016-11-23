/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huge
 *
 * 2014年6月11日
 */
public class FunctionData {
	
	MethodData methodData;
//	public String Method_Name;
//	public List<String> Method_Prototype;
//	public String Method_Returntype;	
	public List<String> Access_List;
	public int Registers_Number;
	public Map<Integer , OredredMethod > Method_Line = new ConcurrentHashMap<Integer , OredredMethod>();
	public List< List<String> > catches;
	public List<Parameter> Parameters;
	public List<AnnotationData> Annotations;	
	public Map<Integer , List<String>> registerMessage = new ConcurrentHashMap<Integer , List<String>>();	
	public Map<Integer,List<Integer>> registerLast = new ConcurrentHashMap<Integer,List<Integer>>();
	//TODO 添加一个寄存器调用map
	public Map<String,List<Integer>> registerCall = new ConcurrentHashMap<String,List<Integer>>();
	
	public Map<String, List<Integer>> getRegisterCall() {
		return registerCall;
	}
	public void setRegisterCall(Map<String, List<Integer>> registerCall) {
		this.registerCall = registerCall;
	}
	public MethodData getMethodData() {
		return methodData;
	}
	public void setMethodData(MethodData methodData) {
		this.methodData = methodData;
	}
	
	public List<List<String>> getCatches() {
		return catches;
	}
	public void setCatches(List<List<String>> catches) {
		this.catches = catches;
	}
	public List<String> getAccess_List() {
		return Access_List;
	}
	public void setAccess_List(List<String> access_List) {
		Access_List = access_List;
	}
	public int getRegisters_Number() {
		return Registers_Number;
	}
	public void setRegisters_Number(int registers_Number) {
		Registers_Number = registers_Number;
	}

	public Map<Integer, OredredMethod> getMethod_Line() {
		return Method_Line;
	}
	public void setMethod_Line(Map<Integer, OredredMethod> method_Line) {
		Method_Line = method_Line;
	}
	public void addMethod_Line(int line,OredredMethod ored){
		Method_Line.put(line, ored);
	}
	public List<Parameter> getParameters() {
		return Parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		Parameters = parameters;
	}
	public List<AnnotationData> getAnnotations() {
		return Annotations;
	}
	public void setAnnotations(List<AnnotationData> annotations) {
		Annotations = annotations;
	}
	public Map<Integer, List<String>> getRegisterMessage() {
		return registerMessage;
	}
	public void setRegisterMessage(Map<Integer, List<String>> registerMessage) {
		this.registerMessage = registerMessage;
	}
	public Map<Integer, List<Integer>> getRegisterLast() {
		return registerLast;
	}
	public void setRegisterLast(Map<Integer, List<Integer>> registerLast) {
		this.registerLast = registerLast;
	}	
	
	

}
