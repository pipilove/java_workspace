package DataPreProcess.FeatureCalculate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import DataPreProcess.Readfile.Dictionary;
import DataPreProcess.Readfile.ReadFile;
import Model.Doc;
import Model.WordFeature;

public class FeatureSelection {
	public static final int MAX_NUM =5000;//�����ʿ����map����
	
	/**ͳ�������ĵ��������ʣ����ص�һ���ַ���������*/
	public String[] FeatureCalculate(File[] file){
		HashMap<String,Integer> map=new HashMap(1000);
		try{
			ReadFile read=new ReadFile();
			int fileLen=file.length;
			for(int i=0;i<fileLen;i++){//���������ĵ�
				String[] temp=read.WordExtract(file[i]);
				for(int j=0;j<temp.length;j++){//�����ĵ������д�
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
	
	/**ͳ�������ĵ�(����ads��web�ļ�)�������ʣ����ص�һ��hashmap���û��ʵ䣿����*/
	public HashMap<String,Integer> FeatureCalculatemap(File[] webfiles,File[] adsfiles){
		HashMap<String,Integer> map=new HashMap<String, Integer>(MAX_NUM);
		try{
			ReadFile read=new ReadFile();
			int webLen=webfiles.length;
			int adsLen=adsfiles.length;
			int i,j;
			
			for(i=0;i<webLen;i++){//����web�ĵ�
				String[] webKey=read.WordExtract(webfiles[i]);
				for(j=0;j<webKey.length;j++){//������ͳ���ĵ������д�
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
			
			for(i=0;i<adsLen;i++){//����ads�ĵ�
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
	
	/**�����ĵ���������ͳ�ƣ����ص��ַ���������*/
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
	
	/**ÿ���ĵ�������������*/
	public Doc[] BuildWordVec(File[] files,String fDicPath){
		Dictionary fd=new Dictionary();
		int fileLen=files.length;
		String[] Fwords=fd.loadFeatureDic(fDicPath);//������
		Doc[] doc=new Doc[fileLen];
		FeatureSelection sd=new FeatureSelection();
		for(int i=0;i<fileLen;i++){//ѭ������ÿ���ĵ�
			doc[i]=new Doc();//��ʼ�������е�Ԫ�أ�������Doc���ʵ����
			doc[i].NumOfWords=Fwords.length;//�ĵ�i����������������
			doc[i].Did=i;
			doc[i].words=new WordFeature[Fwords.length];
			for(int m=0;m<Fwords.length;m++)//��ʼ��WordFeature�����е�Ԫ�أ���WordFeature��ʵ����
				doc[i].words[m]=new WordFeature();
			
			String[] temp=sd.SingleDocFeatureCal(files[i]);//�����ļ����еĴ�
			for(int j=0;j<temp.length;j++){//ѭ�����ĵ���ÿ����
				//�жϸô��Ƿ��ڴʵ���
				for(int k=0;k<Fwords.length;k++){
					if(Fwords[k].equalsIgnoreCase(temp[j])){
						doc[i].words[k].num++;//�������������ʿ��е�k������doc[i]�еĸ���
					}
				}
			}
		}
		return doc;
	}

}
