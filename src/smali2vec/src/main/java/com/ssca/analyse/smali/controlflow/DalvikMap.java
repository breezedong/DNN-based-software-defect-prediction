/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huge
 *
 * 2015年1月5日
 */
public class DalvikMap {
	
	
	private static final DalvikMap DalvikMapItemMapper =
			new DalvikMap();
	
	public DalvikMap() {
	}
		
	public synchronized static DalvikMap getInstance() {
		return DalvikMapItemMapper;
	}
	
	public Map<MethodData,String> dal = new HashMap<MethodData,String>();

	public Map<MethodData, String> getDal() {
		return dal;
	}
	
	public void addDal(MethodData m, String s){
		dal.put(m, s);
	}

}
