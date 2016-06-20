package DataPreProcess.Readfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** 字典的相关操作,获取hash表，字符形式的用户字典，构建特征字典 */
public class Dictionary {
	/** 1.得到哈希表形式的字典 */
	public Map<String, Integer> loadDictionary(String path) {
		try {
			String text;
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			text = br.readLine();
			if (text == null)
				return null;
			int size = Integer.parseInt(text);// 读取词典大小,用来构造map
			Map<String, Integer> map = new HashMap<String, Integer>(size);

			while ((text = br.readLine()) != null) {
				String[] word = text.split(",");
				if (word.length == 2)
					map.put(word[0], Integer.valueOf(word[1]));// 存放至map中
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

	/** 2.得到字典dictionary。txt中的字符数组 */
	public String[] loadDictionaryInv(String dicPath) {// 大辞典:词语,编号
		String[] allWords = null;
		try {
			String lineText;
			File file = new File(dicPath);
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);

			lineText = br.readLine();
			// 读取词典大小；String readLine()throws IOException读取一个文本行
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

	/** 加载特征词典 */
	public String[] loadFeatureDic(String fDicPath) {
		String[] fwords = null;
		try {
			String text;
			File file = new File(fDicPath);
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			text = br.readLine(); // 读取第一行数字，即词典大小
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

	/** 构建特征词典（清洗用户词典？），首行为词总个数，然后为每个词添加新编号，词，词在所有文档中出现的个数 */
	public String constructFeatureDictionnary(String dicPath,HashMap<String, Integer> map) {
		if (dicPath == null) return null;
		String[] words = loadDictionaryInv(dicPath);//dictionary。txt中所有词的数组
		
		int nCount = 0;
		// 第一步,判断该词是否在词典之中	判断好词（在系统词库里的用户词）个数
		//for (int i = 0; i < map.size(); i++)	删除???
			for (int j = 0; j < words.length; j++)
				if (map.containsKey(words[j]))//是一个好词
					nCount++;
		if (nCount == 0) return null;// 无好词

		// 第二步,构建（特征词典的）hashmap,储存能从字典中查到的好词
		Map<String, Integer> newMap = new HashMap<String, Integer>(nCount);
		nCount = 0;
		//for (int i = 0; i < map.size(); i++)	删除???
			for (int j = 0; j < words.length; j++)
				if (map.containsKey(words[j]))
					newMap.put(words[j], map.get(words[j]));

		// 第三步,将hashmap存入一个新的文档里；
		String featureDicPath = "dic/FeatureDic.txt";
		try {
			File file = new File(featureDicPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);//覆盖着写还是先清除了再写？？？
			//FileWriter(file,boolean append)第二个参数为 true，则将字节写入文件末尾处，而不是写入文件开始处
			
			int size = newMap.size();//=nCount?
			StringBuilder tt = new StringBuilder();
			tt.append(size).append("\r\n");
			fw.write(tt.toString());//写入首行，即好词大小数目

			Object[] keyValuePairs = newMap.entrySet().toArray();
			//Object[] toArray()返回一个包含 set 中所有元素的数组???然后可以是用数组下标来引用键值对
			for (int i = 0; i < size; i++) {
				StringBuilder sb = new StringBuilder();
				Map.Entry entry = (Map.Entry) keyValuePairs[i];//通过数组引用每个键值对
				sb.append(i + 1).append(",").append((String) entry.getKey()).append(",").append(entry.getValue()).append("\r\n");
				//改了append((entry.getValue()).toString())
				fw.write(sb.toString());
			}
			fw.flush();//void flush() throws IOException刷新该流的缓冲
			fw.close();
			/*void close() throws IOException 
			 * 关闭此流，但要先刷新它。在关闭该流之后，再调用 write() 或 flush() 将导致抛出 IOException*/
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return featureDicPath;
	}
}
