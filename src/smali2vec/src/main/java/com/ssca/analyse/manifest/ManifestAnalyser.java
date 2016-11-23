/**
 * 
 */
package com.ssca.analyse.manifest;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ssca.Main.Main;
import com.ssca.commonData.CommonData;

/**
 * @author yujianbo
 *
 * 2016年1月21日
 */
public class ManifestAnalyser {

	private static String fileSeparator = System.getProperty("file.separator");
	private String manifestPath;

	public void doAnalyse(){
		manifestPath= CommonData.getDecompilePath()+fileSeparator+"AndroidManifest.xml";
		try {
			//创建一个SAXBuilder对象
			SAXBuilder saxBuilder = new SAXBuilder();            
			//读取xml资源
			Document doc = saxBuilder.build(manifestPath);
			//获取根元素(root)
			Element root = doc.getRootElement();   
			
			//获取app packageName
			String appPckName =root.getAttributeValue("package").toString();
			CommonData.setApkPkg(appPckName);
			
			//获取application
			Element application = root.getChild("application");

			//获取activity
			activityParse(application);

			//get receiver
			serviceParse(application);

			//get receiver
			receiverParse(application);

			//get provider
			providerParse(application);


		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private void activityParse(Element application){
		List<String> activityNameList=new ArrayList<String>();
		List activityList = application.getChildren("activity");
		for(int i=0;i<activityList.size();i++){
			String activityName="";
			String activityData = "";
			String scheme = "";
			String host = "";
			String path = "";
			Element activity = (Element)activityList.get(i);
			//get activity name

//			activityNameList.add(activity.getAttributeValue("name", activity.getNamespace("android")).toString());
			activityName=activity.getAttributeValue("name", activity.getNamespace("android")).toString();
			
			List intentList = activity.getChildren("intent-filter");
			if(intentList.size()!=0){
				for(int i1=0;i1<intentList.size();i1++){
					//鸽子君要的activity入口
					Element category = ((Element)intentList.get(i1)).getChild("category");
					if(category!=null){
						if(category.getNamespace("android")!=null){
							if(category.getAttribute("name", category.getNamespace("android"))!=null){
								if(category.getAttribute("name", category.getNamespace("android")).toString().contains("LAUNCHER")){
									System.out.println("entrance:"+activityName);
								}
										
							}
						}
					}
					
					Element action = ((Element)intentList.get(i1)).getChild("action");
					if(action != null){
						if(action.getNamespace("android")!=null){
							if(action.getAttribute("name", action.getNamespace("android"))!=null){
//								activityNameList.add(action.getAttributeValue("name", action.getNamespace("android")).toString());
//								activityData=action.getAttributeValue("name", action.getNamespace("android")).toString();
								activityName = activityName+"|"+action.getAttributeValue("name", action.getNamespace("android")).toString();
							}
						}
					}
					Element data = ((Element)intentList.get(i1)).getChild("data");
					if(data!=null){
						if(data.getNamespace("android")!=null){
							if( data.getAttribute("scheme", action.getNamespace("android"))!=null){
								scheme = data.getAttribute("scheme", data.getNamespace("android")).getValue();
								activityName+=(" scheme:"+scheme);
							}
							if(data.getAttribute("host", action.getNamespace("android"))!=null){
								host = data.getAttribute("host", data.getNamespace("android")).getValue();
								activityName+=(" host:"+host);
							}
							if(data.getAttribute("path", action.getNamespace("android"))!=null){
								path = data.getAttribute("path", data.getNamespace("android")).getValue();
								activityName+=(" path:"+path);
							}
						}
					}

				}
			}
			
			activityNameList.add(activityName);

		}

		// set commondataset
		CommonData.setActivityOutList(activityNameList);
		for(String s:activityNameList)
			System.out.println(s);
	}

	private void serviceParse(Element application){
		List<String> serviceNameList=new ArrayList<String>();
		List serviceList = application.getChildren("service");
		for(int i=0;i<serviceList.size();i++){
			Element service = (Element)serviceList.get(i);
			//get activity name

			serviceNameList.add(service.getAttributeValue("name", service.getNamespace("android")).toString());

			List intentList = service.getChildren("intent-filter");
			if(intentList.size()!=0){
				for(int i1=0;i1<intentList.size();i1++){
					Element action = ((Element)intentList.get(i1)).getChild("action");
					if(action != null){
						if(action.getNamespace("android")!=null){
							if(action.getAttribute("name", action.getNamespace("android"))!=null){
								serviceNameList.add(action.getAttributeValue("name", action.getNamespace("android")).toString());
							}
						}
					}

				}
			}
		}

		// set commondataset
		CommonData.setServiceOutList(serviceNameList);
	}

	private void receiverParse(Element application){
		List<String> receiverNameList=new ArrayList<String>();
		List receiverList = application.getChildren("receiver");
		for(int i=0;i<receiverList.size();i++){
			Element receiver = (Element)receiverList.get(i);
			//get activity name

			receiverNameList.add(receiver.getAttributeValue("name", receiver.getNamespace("android")).toString());

			List intentList = receiver.getChildren("intent-filter");
			if(intentList.size()!=0){
				for(int i1=0;i1<intentList.size();i1++){
					Element action = ((Element)intentList.get(i1)).getChild("action");
					if(action != null){
						if(action.getNamespace("android")!=null){
							if(action.getAttribute("name", action.getNamespace("android"))!=null){
								receiverNameList.add(action.getAttributeValue("name", action.getNamespace("android")).toString());
							}
						}
					}

				}
			}
		}

		// set commondataset
		CommonData.setReceiverOutList(receiverNameList);
	}

	private void providerParse(Element application){
		List<String> providerNameList=new ArrayList<String>();
		List providerList = application.getChildren("provider");
		for(int i=0;i<providerList.size();i++){
			Element provider = (Element)providerList.get(i);
			//get provider name

			providerNameList.add(provider.getAttributeValue("authorities", provider.getNamespace("android")).toString());

		}

		// set commondataset
		CommonData.setProviderOutList(providerNameList);
	}


	public static void main(String[] args){
		CommonData.setDecompilePath("C:\\Users\\huge\\Desktop\\xml");
		ManifestAnalyser analyser = new ManifestAnalyser();
		analyser.doAnalyse();
	}

}
