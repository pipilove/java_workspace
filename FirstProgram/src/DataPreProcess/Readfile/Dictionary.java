package DataPreProcess.Readfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** �ֵ����ز���,��ȡhash���ַ���ʽ���û��ֵ䣬���������ֵ� */
public class Dictionary {
	/** 1.�õ���ϣ����ʽ���ֵ� */
	public Map<String, Integer> loadDictionary(String path) {
		try {
			String text;
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			text = br.readLine();
			if (text == null)
				return null;
			int size = Integer.parseInt(text);// ��ȡ�ʵ��С,��������map
			Map<String, Integer> map = new HashMap<String, Integer>(size);

			while ((text = br.readLine()) != null) {
				String[] word = text.split(",");
				if (word.length == 2)
					map.put(word[0], Integer.valueOf(word[1]));// �����map��
			}
			br.close();
			fileReader.close();

			return map;
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return null;

	}

	/** 2.�õ��ֵ�dictionary��txt�е��ַ����� */
	public String[] loadDictionaryInv(String dicPath) {// ��ǵ�:����,���
		String[] allWords = null;
		try {
			String lineText;
			File file = new File(dicPath);
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);

			lineText = br.readLine();
			// ��ȡ�ʵ��С��String readLine()throws IOException��ȡһ���ı���
			if (lineText == null) return null;
			int dicSize = Integer.parseInt(lineText);
			allWords = new String[dicSize];

			while ((lineText = br.readLine()) != null) {
				String[] word = lineText.split(",");
				if (word.length == 2) {
					allWords[Integer.parseInt(word[1])] = word[0];
				}
			}
			br.close();
			fileReader.close();
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return allWords;
	}

	/** ���������ʵ� */
	public String[] loadFeatureDic(String fDicPath) {
		String[] fwords = null;
		try {
			String text;
			File file = new File(fDicPath);
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			text = br.readLine(); // ��ȡ��һ�����֣����ʵ��С
			if (text == null)
				return null;
			int size = Integer.parseInt(text);
			fwords = new String[size];
			while ((text = br.readLine()) != null) {
				String[] word = text.split(",");
				if (word.length == 3)
					fwords[Integer.parseInt(word[0]) - 1] = word[1];
			}
			br.close();
			fileReader.close();
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return fwords;
	}

	/** ���������ʵ䣨��ϴ�û��ʵ䣿��������Ϊ���ܸ�����Ȼ��Ϊÿ��������±�ţ��ʣ����������ĵ��г��ֵĸ��� */
	public String constructFeatureDictionnary(String dicPath,HashMap<String, Integer> map) {
		if (dicPath == null) return null;
		String[] words = loadDictionaryInv(dicPath);//dictionary��txt�����дʵ�����
		
		int nCount = 0;
		// ��һ��,�жϸô��Ƿ��ڴʵ�֮��	�жϺôʣ���ϵͳ�ʿ�����û��ʣ�����
		//for (int i = 0; i < map.size(); i++)	ɾ��???
			for (int j = 0; j < words.length; j++)
				if (map.containsKey(words[j]))//��һ���ô�
					nCount++;
		if (nCount == 0) return null;// �޺ô�

		// �ڶ���,�����������ʵ�ģ�hashmap,�����ܴ��ֵ��в鵽�ĺô�
		Map<String, Integer> newMap = new HashMap<String, Integer>(nCount);
		nCount = 0;
		//for (int i = 0; i < map.size(); i++)	ɾ��???
			for (int j = 0; j < words.length; j++)
				if (map.containsKey(words[j]))
					newMap.put(words[j], map.get(words[j]));

		// ������,��hashmap����һ���µ��ĵ��
		String featureDicPath = "dic/FeatureDic.txt";
		try {
			File file = new File(featureDicPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);//������д�������������д������
			//FileWriter(file,boolean append)�ڶ�������Ϊ true�����ֽ�д���ļ�ĩβ����������д���ļ���ʼ��
			
			int size = newMap.size();//=nCount?
			StringBuilder tt = new StringBuilder();
			tt.append(size).append("\r\n");
			fw.write(tt.toString());//д�����У����ôʴ�С��Ŀ

			Object[] keyValuePairs = newMap.entrySet().toArray();
			//Object[] toArray()����һ������ set ������Ԫ�ص�����???Ȼ��������������±������ü�ֵ��
			for (int i = 0; i < size; i++) {
				StringBuilder sb = new StringBuilder();
				Map.Entry entry = (Map.Entry) keyValuePairs[i];//ͨ����������ÿ����ֵ��
				sb.append(i + 1).append(",").append((String) entry.getKey()).append(",").append(entry.getValue()).append("\r\n");
				//����append((entry.getValue()).toString())
				fw.write(sb.toString());
			}
			fw.flush();//void flush() throws IOExceptionˢ�¸����Ļ���
			fw.close();
			/*void close() throws IOException 
			 * �رմ�������Ҫ��ˢ�������ڹرո���֮���ٵ��� write() �� flush() �������׳� IOException*/
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return featureDicPath;
	}
}
