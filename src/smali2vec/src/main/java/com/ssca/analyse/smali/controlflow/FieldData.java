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
public class FieldData {
	/* .field < 访问权限> [ 修饰关键字] < 字段名>:< 字段类型> */
	public String FieldName; //字段名
	public List<String> AccessList; //访问权限 + 修饰关键字
	public String Field_Type; //字段类型
	public String Field_Initial_value = "";//static_field后面会有固定值 =X,在字段类型后面
	public List<AnnotationData> Annotations; //标注
	
	
	public String getFieldName() {
		return FieldName;
	}
	public void setFieldName(String fieldName) {
		FieldName = fieldName;
	}
	public List<String> getAccessList() {
		return AccessList;
	}
	public void setAccessList(List<String> accessList) {
		AccessList = accessList;
	}
	public String getField_Type() {
		return Field_Type;
	}
	public void setField_Type(String field_Type) {
		Field_Type = field_Type;
	}	
	public String getField_Initial_value() {
		return Field_Initial_value;
	}
	public void setField_Initial_value(String field_Initial_value) {
		Field_Initial_value = field_Initial_value;
	}
	public List<AnnotationData> getAnnotations() {
		return Annotations;
	}
	public void setAnnotations(List<AnnotationData> annotations) {
		Annotations = annotations;
	}
	

}
