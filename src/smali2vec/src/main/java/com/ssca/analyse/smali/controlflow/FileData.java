/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.List;

/**
 * @author huge
 *
 * 2014年6月11日
 */
public class FileData {
	public String FileFullPath;
	public String FileStaticName;
	public List<String> Access_List;
	public List<String> Super_Class;
	public List<String> Implements_Class;
	public String Source_Name;
	public List<FunctionData> funList;
	public List<FieldData> fields;
	//public List<AnnotationData> Annotations;
	public String getFileFullPath() {
		return FileFullPath;
	}
	public void setFileFullPath(String fileFullPath) {
		FileFullPath = fileFullPath;
	}
	public String getFileStaticName() {
		return FileStaticName;
	}
	public void setFileStaticName(String fileStaticName) {
		FileStaticName = fileStaticName;
	}
	public List<String> getAccess_List() {
		return Access_List;
	}
	public void setAccess_List(List<String> access_List) {
		Access_List = access_List;
	}
	public List<String> getSuper_Class() {
		return Super_Class;
	}
	public void setSuper_Class(List<String> super_Class) {
		Super_Class = super_Class;
	}
	
	public List<String> getImplements_Class() {
		return Implements_Class;
	}
	public void setImplements_Class(List<String> implements_Class) {
		Implements_Class = implements_Class;
	}
	public String getSource_Name() {
		return Source_Name;
	}
	public void setSource_Name(String source_Name) {
		Source_Name = source_Name;
	}
	public List<FieldData> getFields() {
		return fields;
	}
	public void setFields(List<FieldData> fields) {
		this.fields = fields;
	}
	public List<FunctionData> getFunList() {
		return funList;
	}
	public void setFunList(List<FunctionData> funList) {
		this.funList = funList;
	}
//	public List<AnnotationData> getAnnotations() {
//		return Annotations;
//	}
//	public void setAnnotations(List<AnnotationData> annotations) {
//		Annotations = annotations;
//	}
	
	

}
