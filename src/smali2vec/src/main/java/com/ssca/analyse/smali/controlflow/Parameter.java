/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.List;

/**
 * @author huge
 *
 * 2014年6月23日
 */
public class Parameter {

	public List<String> params;
	public List<AnnotationData> annotations;
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	public List<AnnotationData> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<AnnotationData> annotations) {
		this.annotations = annotations;
	}
	
	
	
}
