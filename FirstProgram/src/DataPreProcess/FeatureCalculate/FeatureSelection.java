package DataPreProcess.FeatureCalculate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import DataPreProcess.Readfile.Dictionary;
import DataPreProcess.Readfile.ReadFile;
import Model.Doc;
import Model.WordFeature;

public class FeatureSelection {
	public static final int MAX_NUM =5000;//特征词库最大map数量
	
	/**统计所有文档的特征词，加载到一个字符串数组中*/
	public String[] FeatureCalculate(File[] file){
		HashMap<String,Integer> map=new HashMap(1000);
		try{
			ReadFile read=new ReadFile();
			int fileLen=file.length;
			for(int i=0;i<fileLen;i++){//遍历所有文档
				String[] temp=read.WordExtract(file[i]);
				for(int j=0;j<temp.length;j++){//遍历文档的所有词
					if(map.containsKey(temp[j])){
						int n=map.get(temp[j]);
						map.remove(temp[j]);
						map.put(temp[j], n+1);
					}
					else{
						map.put(temp[j], 1);
					}
				}
			}
		}catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		Object[] t=map.entrySet().toArray();
		String[] wd=new String[t.length];
		for(int i=0;i<t.length;i++){
			wd[i]=t.toString();
		}
		System.out.println(wd[1]);
		return wd;
	}
	
	/**统计所有文档(包括ads和web文件)的特征词，加载到一个hashmap（用户词典？）里*/
	public HashMap<String,Integer> FeatureCalculatemap(File[] webfiles,File[] adsfiles){
		HashMap<String,Integer> map=new HashMap<String, Integer>(MAX_NUM);
		try{
			ReadFile read=new ReadFile();
			int webLen=webfiles.length;
			int adsLen=adsfiles.length;
			int i,j;
			
			for(i=0;i<webLen;i++){//遍历web文档
				String[] webKey=read.WordExtract(webfiles[i]);
				for(j=0;j<webKey.length;j++){//遍历并统计文档的所有词
					if(map.containsKey(webKey[j])){
						/*int n=map.get(webKey[j]);
						map.remove(webKey[j]);
						map.put(webKey[j], n+1);*/
						map.put(webKey[j], map.get(webKey[j])+1);
					}
					else{
						map.put(webKey[j], 1);
					}
				}
			}
			
			for(i=0;i<adsLen;i++){//遍历ads文档
				String[] adsKey=read.WordExtract(adsfiles[i]);
				for(j=0;j<adsKey.length;j++){
					if(map.containsKey(adsKey[j])){
						int n=map.get(adsKey[j]);
						map.remove(adsKey[j]);
						map.put(adsKey[j], n+1);
					}
					else{
						map.put(adsKey[j], 1);
					}
				}
			}
			
		}catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return map;
	} 
	
	/**单个文档的特征词统计，加载到字符串数组中*/
	public String[] SingleDocFeatureCal(File file){
			try{
				ReadFile read=new ReadFile();
				String[] output=read.WordExtract(file);
				return output;
			}catch (IOException err) {
				System.out.println(err.toString());
				err.printStackTrace();
			}
			return null; 
	}
	
	/**每个文档建立词组向量*/
	public Doc[] BuildWordVec(File[] files,String fDicPath){
		Dictionary fd=new Dictionary();
		int fileLen=files.length;
		String[] Fwords=fd.loadFeatureDic(fDicPath);//特征词
		Doc[] doc=new Doc[fileLen];
		FeatureSelection sd=new FeatureSelection();
		for(int i=0;i<fileLen;i++){//循环遍历每个文档
			doc[i]=new Doc();//初始化数组中的元素（即创建Doc类的实例）
			doc[i].NumOfWords=Fwords.length;//文档i中最多的特征词数？
			doc[i].Did=i;
			doc[i].words=new WordFeature[Fwords.length];
			for(int m=0;m<Fwords.length;m++)//初始化WordFeature数组中的元素（即WordFeature的实例）
				doc[i].words[m]=new WordFeature();
			
			String[] temp=sd.SingleDocFeatureCal(files[i]);//单个文件含有的词
			for(int j=0;j<temp.length;j++){//循环该文档的每个词
				//判断该词是否在词典中
				for(int k=0;k<Fwords.length;k++){
					if(Fwords[k].equalsIgnoreCase(temp[j])){
						doc[i].words[k].num++;//即代表在特征词库中第k个词在doc[i]中的个数
					}
				}
			}
		}
		return doc;
	}

}
