/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.List;

/**
 * @author huge
 *
 * 2014年7月16日
 */
public class MethodData { //应该加上file的相关信息，根据当前的信息不一定能定位到fatherMethod的位置
	
	public String Method_Name;
	public List<String> Method_Prototype;
	public String Method_Returntype;
	public int ProLogue;
	public String staticFileName;
	public boolean userinterface;//true表明是用户行为
	//public String 
	
	
	public String getMethod_Name() {
		return Method_Name;
	}
	public void setMethod_Name(String method_Name) {
		Method_Name = method_Name;
	}
	public List<String> getMethod_Prototype() {
		return Method_Prototype;
	}
	public void setMethod_Prototype(List<String> method_Prototype) {
		Method_Prototype = method_Prototype;
	}
	public String getMethod_Returntype() {
		return Method_Returntype;
	}
	public void setMethod_Returntype(String method_Returntype) {
		Method_Returntype = method_Returntype;
	}
	public int getProLogue() {
		return ProLogue;
	}
	public void setProLogue(int proLogue) {
		ProLogue = proLogue;
	}
	public String getStaticFileName() {
		return staticFileName;
	}
	public void setStaticFileName(String staticFileName) {
		this.staticFileName = staticFileName;
	}
	public boolean isUserinterface() {
		return userinterface;
	}
	public void setUserinterface(boolean userinterface) {
		this.userinterface = userinterface;
	}
	

}
