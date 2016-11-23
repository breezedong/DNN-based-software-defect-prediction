/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.List;
import java.util.Map;

/**
 * @author huge
 *
 * 2014年6月23日
 */
public class AnnotationData {  //需要重新写 结构较复杂
	
	public String Annotation_Name;
	public String SubAnnotation;
//	public String Annotation_Element;
//	public List<String> Encoded_Array;
	Map<String , List<String>> AnnotationValue;
 	public Map<String, List<String>> getAnnotationValue() {
		return AnnotationValue;
	}
	public void setAnnotationValue(Map<String, List<String>> annotationValue) {
		AnnotationValue = annotationValue;
	}
	public String getAnnotation_Name() {
		return Annotation_Name;
	}
	public void setAnnotation_Name(String annotation_Name) {
		Annotation_Name = annotation_Name;
	}
	public String getSubAnnotation() {
		return SubAnnotation;
	}
	public void setSubAnnotation(String subAnnotation) {
		SubAnnotation = subAnnotation;
	}
	

}
