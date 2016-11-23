/**
 * 
 */
package com.ssca.analyse.smali.bound;

import java.io.Serializable;

/**
 * @author yujianbo
 *
 * 2016年4月13日
 */
public class IntentData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String callApi="";
	private String caller="";
	private String sourcePkg="";
	private String desPkg="";
	private String desClass="";
	private String action="";
	private String uri="";
	private String scheme="";
	private String host="";
	private int newInstanceLine=-1;
	private int startActivityLine=-1;
	private int initLine=-1;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return caller+"->"+"desClass:["+desClass+"]+action:["+action+"]+uri:["+uri+"]";
	}
	public String getSourcePkg() {
		return sourcePkg;
	}
	public void setSourcePkg(String sourcePkg) {
		this.sourcePkg = sourcePkg;
	}
	public String getDesPkg() {
		return desPkg;
	}

	public void setDesPkg(String desPkg) {
		this.desPkg = desPkg;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getCallApi() {
		return callApi;
	}
	public void setCallApi(String callApi) {
		this.callApi = callApi;
	}
	public int getInitLine() {
		return initLine;
	}
	public void setInitLine(int initLine) {
		this.initLine = initLine;
	}
	public int getNewInstanceLine() {
		return newInstanceLine;
	}
	public void setNewInstanceLine(int newInstanceLine) {
		this.newInstanceLine = newInstanceLine;
	}
	public int getStartActivityLine() {
		return startActivityLine;
	}
	public void setStartActivityLine(int startActivityLine) {
		this.startActivityLine = startActivityLine;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getDesClass() {
		return desClass;
	}
	public void setDesClass(String desClass) {
		this.desClass = desClass;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
