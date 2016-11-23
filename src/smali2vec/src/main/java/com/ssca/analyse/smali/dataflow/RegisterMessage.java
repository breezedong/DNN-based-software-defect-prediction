/**
 * 
 */
package com.ssca.analyse.smali.dataflow;

/**
 * @author huge
 *
 * 2014年9月11日
 */
public class RegisterMessage {
	
	public int RegisterNumber(String opcode){
		int number = 1; //默认为1
		
		if( ((opcode.contains("move")) || (opcode.contains("instance-of")))
				&& (!opcode.contains("move-result")) && (!opcode.contains("move-exception")) ){
			number=2;
		}
		else if( (opcode.contains("goto")) ){
			number=0;
		}
		else if ( (opcode.equals("if-eq")) || (opcode.equals("if-ne")) || (opcode.equals("if-lt")) || 
				(opcode.equals("if-ge")) || (opcode.equals("if-gt")) || (opcode.equals("if-le")) ){
			number=2;
		}
		else if ( (opcode.contains("cmp")) || (opcode.contains("aget")) || (opcode.contains("aput")) ){
			number=3;
		}
		else if ( (opcode.contains("iget")) || (opcode.contains("iput")) || (opcode.contains("array-length")) ){
			number=2;
		}
		else if ( (opcode.contains("-to-")) || (opcode.contains("neg-")) || (opcode.contains("not-")) ){
			number=2;
		}
		else if ( ((opcode.contains("add-")) || (opcode.contains("sub-")) || (opcode.contains("mul-")) ||
				opcode.contains("div-") || (opcode.contains("rem-")) || (opcode.contains("and-")) ||
				opcode.contains("or-") || (opcode.contains("xor-")) || (opcode.contains("shl-")) ||
				opcode.contains("shr-") || (opcode.contains("ushr-")) )
				&& (!opcode.contains("2addr")) && (!opcode.contains("lit")) && (!opcode.contains("monitor-")) ){
			number=3;
		}
		else if ( ((opcode.contains("add-")) || (opcode.contains("sub-")) || (opcode.contains("mul-")) ||
				opcode.contains("div-") || (opcode.contains("rem-")) || (opcode.contains("and-")) ||
				opcode.contains("or-") || (opcode.contains("xor-")) || (opcode.contains("shl-")) ||
				opcode.contains("shr-") || (opcode.contains("ushr-"))) 
				&& (!opcode.contains("monitor-")) ){
			number=2;
		}
		
		return number;
	}
	
//	public static void main(String[] args){
//		int registerNumber = RegisterNumber("move-result");
//		System.out.print(registerNumber);
//	}

}
