/**
 * 
 */
package com.ssca.analyse.smali;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.logging.log4j.*;
import org.jf.smali.LexerErrorInterface;
import org.jf.smali.smaliFlexLexer;
import org.jf.smali.smaliParser;

import com.ssca.Main.Config;
import com.ssca.analyse.smali.bound.ActivityInBound;
import com.ssca.analyse.smali.bound.IntentData;
import com.ssca.analyse.smali.bound.RecieverInBound;
import com.ssca.analyse.smali.bound.ServiceInBound;
import com.ssca.analyse.smali.controlflow.CodeMapItemMapper;
import com.ssca.analyse.smali.controlflow.FileData;
import com.ssca.analyse.smali.controlflow.FunctionData;
import com.ssca.analyse.smali.controlflow.GetControlFlow;
import com.ssca.commonData.CommonData;
import com.ssca.report.GenerateReport;

/**
 * @author yujianbo
 *
 *         2016年1月25日
 */
public class SmaliAnalyzer {

	private static List<File> smaliFileList = new ArrayList();
	private static String fileSeparator = System.getProperty("file.separator");
	private static Logger logger = LogManager.getLogger(SmaliAnalyzer.class);
	private static int N = 7;
	private static int count = 0;
	private static Map<Integer, String> SmaliTokens = new HashMap<Integer, String>();
	private Map<String, FileData> SmaliMap;
	private List<FunctionData> flist;
	private Map<Integer, List<Integer>> ineed;
	private List<Integer> temp;

	public void doflow() {
		getSmaliFiles(CommonData.getDecompilePath());
		int k = 1;
		for (File smaliFile : smaliFileList) {
			try {
				logger.info("start analyse " + smaliFile.getAbsolutePath());
				CommonTree t = getCommonTree(smaliFile, false, CommonData.getWhiteList());
				System.out.println(t.getChildCount());
				// 输出语法树
				// FileWriter fileWriter = new
				// FileWriter("D:\\PaperW\\result\\tree\\" + (k++) +".txt");
				// fileWriter.write(t.toStringTree());
				// fileWriter.flush();
				// fileWriter.close();
				GetControlFlow.GetFlow(t, smaliFile.getName(), CommonData.getWhiteList());
				SmaliMap = CodeMapItemMapper.getInstance().getSmaliFileMap();
				for (Entry<String, FileData> SmaliMapEntry : SmaliMap.entrySet()) {
					// get smali filedata
					FileData SmaliFileData = SmaliMapEntry.getValue();
					flist = SmaliFileData.getFunList();
					Iterator it = flist.iterator();
					while (it.hasNext()) {
						FunctionData fd = (FunctionData) it.next();
						ineed = fd.getRegisterLast();
						// System.out.println(":::::::::::" + ineed.size());
						Iterator itmap = ineed.entrySet().iterator();
						int key;
						while (itmap.hasNext()) {
							Map.Entry entry = (Map.Entry) itmap.next();
							key = (int) entry.getKey();
							temp = (List<Integer>) entry.getValue();
							System.out.print(key + "<--");
							Iterator it2 = temp.iterator();
							while (it2.hasNext()) {
								int value = (int) it2.next();
								System.out.print(value);
								System.out.print(' ');
							}
							System.out.println();
						}
					}
				}
				// CodeMapItemMapper.getInstance().getSmaliFileMap();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void doWordFreq() {
		getSmaliFiles(CommonData.getDecompilePath());
		String flaw = this.readCheckmarx();
		StringBuilder labelski = new StringBuilder();
		StringBuilder labeldnn = new StringBuilder();
		int k = 1;
		int classcount = 0;
		int defective = 0;
		//对于每一个smali文件，先生成features，再与checkmarx库对比生成label
		for (File smaliFile : smaliFileList) {
			classcount++;
			String s = this.readSmali(smaliFile.getAbsolutePath());
			N = SmaliTokens.size();
			Pattern[] p = new Pattern[N];
			int[] apr = new int[N];
			for (int i = 0; i < N; i++) {
				p[i] = Pattern.compile(SmaliTokens.get(i + 1));
			}
			for (int i = 0; i < apr.length; i++) {
				Matcher m = p[i].matcher(s);
				while (m.find()) {
					apr[i]++;
				}
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < apr.length; i++) {
				sb.append(apr[i]);
				if (i != apr.length - 1)
					sb.append(' ');
			}
			File file = new File("D:\\PaperW\\exper\\marxrt\\gtalksms", "train5.txt");
			try {
				FileWriter fw = new FileWriter(file, true);
				fw.write(sb.toString() + '\n');
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//生成对应的label
			String str = smaliFile.getAbsolutePath();
			int loc1 = str.lastIndexOf("\\");
			int loc2 = str.indexOf(".smali");
			str = str.substring(loc1 + 1, loc2);
			// 查找checkmarx结果，生成label
			Pattern pt = Pattern.compile(str);
			Matcher mc = pt.matcher(flaw);
			// System.out.println(mc.find());
			//label for ski-learn
			if (mc.find() == true) {
				defective++;
				labelski.append(1);
				labelski.append(' ');
				labeldnn.append(1);
				labeldnn.append(' ');
				labeldnn.append(0);
				labeldnn.append('\n');
			} else {
				labelski.append(0);
				labelski.append(' ');
				labeldnn.append(0);
				labeldnn.append(' ');
				labeldnn.append(1);
				labeldnn.append('\n');
			}
			//label for Tensorflow
//			if (mc.find() == true) {
//				defective++;
//				label.append(1);
//				label.append(' ');
//				label.append(0);
//				label.append('\n');
//			} else {
//				label.append(0);
//				label.append(' ');
//				label.append(1);
//				label.append('\n');
//			}
		}
		System.out.println("Total classes: "+classcount);
		System.out.println("Defective: "+defective);
		//将label写入文件
		File file1 = new File("D:\\PaperW\\exper\\marxrt\\gtalksms", "labelski5.txt");
		try {
			FileWriter fw = new FileWriter(file1, true);
			fw.write(labelski.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file11 = new File("D:\\PaperW\\exper\\marxrt\\gtalksms", "labeldnn5.txt");
		try {
			FileWriter fw = new FileWriter(file11, true);
			fw.write(labeldnn.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doSmaliAnalyse() {
		// commondata是静态类
		getSmaliFiles(CommonData.getDecompilePath());
		logger.info("smali start!");
		int k = 1;
		StringBuilder label = new StringBuilder();
		// 读取checkmarx的信息
		String flaw = this.readCheckmarx();
		for (File smaliFile : smaliFileList) {
			count++;
			logger.info("start analyse " + smaliFile.getAbsolutePath());
			// 获得此smali文件名
			String str = smaliFile.getAbsolutePath();
			int loc1 = str.lastIndexOf("\\");
			int loc2 = str.indexOf(".smali");
			str = str.substring(loc1 + 1, loc2);
			// 查找checkmarx结果，生成label
			Pattern pt = Pattern.compile(str);
			Matcher mc = pt.matcher(flaw);
			// System.out.println(mc.find());
			if (mc.find() == true) {
				label.append(1);
				label.append(' ');
			} else {
				label.append(0);
				label.append(' ');
			}
			logger.info(str);
			int[] apr = new int[N];
			try {
				// 解析每个smali文件，获取语法树
				CommonTree t = getCommonTree(smaliFile, false, CommonData.getWhiteList());
				// System.out.println(t.getChildCount());
				String tempString = t.toStringTree();
				// 写入文件
				// System.out.println(t.toStringTree());
				// System.out.println(t.getChildCount());
				// CommonTree tChild =(CommonTree) t.getChild(0);
				// String sTemp = tChild.token.getText();
				// System.out.println(sTemp);
				// FileWriter fileWriter = new
				// FileWriter("D:\\githubWS\\smali\\result\\Result" + (k++) +
				// ".txt");
				// fileWriter.write(t.toStringTree());
				// fileWriter.flush();
				// fileWriter.close();
				// 统计数目
				Pattern[] p = new Pattern[N];
				p[0] = Pattern.compile("I_METHOD");
				p[1] = Pattern.compile("I_FIELD");
				p[2] = Pattern.compile("invoke");
				p[3] = Pattern.compile("if");
				p[4] = Pattern.compile("goto");
				p[5] = Pattern.compile("put");
				p[6] = Pattern.compile("get");
				for (int i = 0; i < apr.length; i++) {
					Matcher m = p[i].matcher(tempString);
					while (m.find()) {
						apr[i]++;
					}
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < apr.length; i++) {
					sb.append(apr[i]);
					if (i != apr.length - 1)
						sb.append(' ');
				}
				File file = new File("D:\\PaperW\\exper\\marxrt\\AnkiDroid", "train1.txt");
				try {
					FileWriter fw = new FileWriter(file, true);
					fw.write(sb.toString() + '\n');
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// String fileFullPath = t.getChild(0).getText();
				// System.out.println(fileFullPath);
				// 根据smali语法树，获取控制流
				// GetControlFlow.GetFlow(t, smaliFile.getName(),
				// CommonData.getWhiteList());
				t = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 随机生成label
		// int[] label = new int[count];
		// c
		// File file1 = new File("D:\\PaperW\\result", "label.txt");
		// for (int i = 0; i < count; i++) {
		// if (Math.random() >= 0.8)
		// label[i] = 1;
		// sb.append(label[i]);
		// if (i != label.length - 1)
		// sb.append(' ');
		// }
		File file1 = new File("D:\\PaperW\\exper\\marxrt\\flym", "label1.txt");
		try {
			FileWriter fw = new FileWriter(file1, true);
			fw.write(label.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// logger.info("smali end!");
		//
		// logger.info("start get activity bound!");
		// //根据控制流，解析activity的Bound
		// ActivityInBound activityBound = new ActivityInBound();
		// activityBound.getInBound();
		// logger.info("activity end!");
		//
		// logger.info("start get service bound!");
		// //根据控制流，解析activity的Bound
		// ServiceInBound serviceInBound = new ServiceInBound();
		// serviceInBound.getInBound();
		// logger.info("service end!");
		//
		// logger.info("start get reciever bound!");
		// //根据控制流，解析activity的Bound
		// RecieverInBound recieverInBound = new RecieverInBound();
		// recieverInBound.getInBound();
		// logger.info("reciever end!");

		// if(CommonData.getActivityInList()==null)
		// return ;
		// for(IntentData s : CommonData.getActivityInList())
		// System.out.println(s);

	}

	private static void inflateSmaliMap() {
		int k = 1;
		SmaliTokens.put(k++, "field");
		SmaliTokens.put(k++, "method");
		SmaliTokens.put(k++, "nop");
		SmaliTokens.put(k++, "nop");
		SmaliTokens.put(k++, "move");
		SmaliTokens.put(k++, "return");
		SmaliTokens.put(k++, "const");
		SmaliTokens.put(k++, "moniter");
		SmaliTokens.put(k++, "check-cast");
		SmaliTokens.put(k++, "instance-of");
		SmaliTokens.put(k++, "array-length");
		SmaliTokens.put(k++, "fill");
		SmaliTokens.put(k++, "throw");
		SmaliTokens.put(k++, "goto");
		SmaliTokens.put(k++, "switch");
		SmaliTokens.put(k++, "cmp");
		SmaliTokens.put(k++, "if");
		SmaliTokens.put(k++, "aget");
		SmaliTokens.put(k++, "aput");
		SmaliTokens.put(k++, "iget");
		SmaliTokens.put(k++, "iput");
		SmaliTokens.put(k++, "sget");
		SmaliTokens.put(k++, "sput");
		SmaliTokens.put(k++, "invoke");
		SmaliTokens.put(k++, "neg");
		SmaliTokens.put(k++, "not");
		SmaliTokens.put(k++, "int-to");
		SmaliTokens.put(k++, "long-to");
		SmaliTokens.put(k++, "float-to");
		SmaliTokens.put(k++, "double-to");
		SmaliTokens.put(k++, "int-to");
		SmaliTokens.put(k++, "add");
		SmaliTokens.put(k++, "sub");
		SmaliTokens.put(k++, "mul");
		SmaliTokens.put(k++, "div");
		SmaliTokens.put(k++, "rem");
		SmaliTokens.put(k++, "and");
		SmaliTokens.put(k++, "or");
		SmaliTokens.put(k++, "sh");
	}

	private void getSmaliFiles(String fileRoot) {
		File fileroot = new File(fileRoot);
		for (File file : fileroot.listFiles()) {
			if (file.isDirectory()) {
				getSmaliFiles(file.getAbsolutePath());
			} else {
				if (file.getName().endsWith(".smali"))
					smaliFileList.add(file);
			}
		}
	}

	private static String readCheckmarx() {
		StringBuffer str = new StringBuffer("");
		File file = new File("D:\\PaperW\\exper\\marxrt\\gtalksms\\dp-gtalksms5.txt");
		try {
			FileReader fr = new FileReader(file);
			int ch = 0;
			while ((ch = fr.read()) != -1) {
				str.append((char) ch);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File reader出错");
		}
		return str.toString();
	}

	private static String readSmali(String path) {
		StringBuffer str = new StringBuffer("");
		File file = new File(path);
		try {
			FileReader fr = new FileReader(file);
			int ch = 0;
			while ((ch = fr.read()) != -1) {
				str.append((char) ch);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File reader出错");
		}
		return str.toString();
	}

	private CommonTree getCommonTree(File smaliFile, boolean verboseErrors, List<String> whiteList) throws Exception {
		// TOKENs应该是节点
		CommonTokenStream tokens;
		// 词法分析器
		LexerErrorInterface lexer;

		FileInputStream fis = new FileInputStream(smaliFile.getAbsolutePath());
		InputStreamReader reader = new InputStreamReader(fis, "UTF-8");

		lexer = new smaliFlexLexer(reader);
		((smaliFlexLexer) lexer).setSourceFile(smaliFile);
		// 生成节点
		tokens = new CommonTokenStream((TokenSource) lexer);

		smaliParser parser = new smaliParser(tokens);
		parser.setVerboseErrors(verboseErrors);
		// parser.setAllowOdex(allowOdex);
		// parser.setApiLevel(apiLevel);
		// 得到树
		smaliParser.smali_file_return result = parser.smali_file();

		if (parser.getNumberOfSyntaxErrors() > 0 || lexer.getNumberOfSyntaxErrors() > 0) {
			return null;
		}

		CommonTree t = result.getTree();

		// GetControlFlow.GetFlow(t, smaliFile.getName(),whiteList);

		reader.close();
		fis.close();
		return t;

	}

	public static void main(String[] args) {
		Config.setWhiteList();
		// CommonData.setDecompilePath("D:\\workspace\\RelationApk\\detemp\\2016-03-11-09-58-20\\smali\\cn\\jingling\\motu\\advertisement\\providers");
		// CommonData.setDecompilePath("D:\\workspace\\RelationApk\\detemp\\2016-04-06-13-30-10\\smali\\com\\baidu\\dq\\advertise\\d");
		//
		// CommonData.setDecompilePath("D:\\git_workspace\\RelationApk\\detemp\\2016-04-26-08-59-24\\smali\\com\\zkmm\\adsdk");
		CommonData.setDecompilePath("D:\\SoftSec\\Tools\\AndroidKiller_v1.3.1\\projects\\gtalksms-2.2");
		SmaliAnalyzer analyzer = new SmaliAnalyzer();
		// analyzer.doSmaliAnalyse();
		// analyzer.doflow();
		analyzer.inflateSmaliMap();
		analyzer.doWordFreq();
		// System.out.println(SmaliTokens.size());
		// System.out.println(analyzer.readString());
	}
}
