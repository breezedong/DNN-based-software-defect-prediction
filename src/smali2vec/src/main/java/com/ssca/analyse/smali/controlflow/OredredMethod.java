/**
 * 
 */
package com.ssca.analyse.smali.controlflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

/**
 * @author huge
 *
 * 2014年6月11日
 */
public class OredredMethod {  //行信息
	
	public int ProLogue;
//	int[] TrueFlag = new int[2]; 
//	int[] FalseFlag = new int[2];

	HashSet<Integer> ProgramExit = new HashSet<Integer>();
	public CommonTree statements;
	public List<String> register = new ArrayList<String>();
	String StatementStr;
	String opcode;
	
	public String getStatementStr() {
		return StatementStr;
	}
	public void setStatementStr(String statementStr) {
		StatementStr = statementStr;
	}

	public HashSet<Integer> getProgramExit() {
		return ProgramExit;
	}
	public void setProgramExit(HashSet<Integer> programExit) {
		ProgramExit = programExit;
	}
	public CommonTree getStatements() {
		return statements;
	}
	public void setStatements(CommonTree statements) {
		this.statements = statements;
	}
	public List<String> getRegister() {
		return register;
	}
	public void setRegister(List<String> register) {
		this.register = register;
	}
	
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	public int getProLogue() {
		return ProLogue;
	}
	public void setProLogue(int proLogue) {
		ProLogue = proLogue;
	}

	
	
}
