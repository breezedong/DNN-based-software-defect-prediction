/**
 * 
 */
package com.ssca.analyse;

import com.ssca.analyse.smali.SmaliAnalyzer;

import com.ssca.analyse.manifest.ManifestAnalyser;

/**
 * @author yujianbo
 *
 * 2016年1月21日
 */
public class AnalyserController {

	public static void do_analyse(){
		ManifestAnalyser manifestAnalyser = new ManifestAnalyser();
		manifestAnalyser.doAnalyse();
		
		SmaliAnalyzer smaliAnalyzer = new SmaliAnalyzer();
		smaliAnalyzer.doSmaliAnalyse();
		
		System.gc();
	}
}
