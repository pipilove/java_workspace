package DataPreProcess.Readfile;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import ICTCLAS.I3S.AC.ICTCLAS50;

/** 1.读取文件的操作；2。对字符串分词；3.对文档分词 */
public class ReadFile {
	/*** 得到目录子文件,作为文件输入 */
	public File[] GetSubPath(String path) {
		try {
			File file = new File(path);
			if (file.isDirectory()) {
				// “目录文件”指的是某些“文件”;当且仅当此抽象路径名表示的文件存在且是一个目录时，返回 true 如果只是一个文件呢？
				File[] subfile = file.listFiles();
				// 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件和目录
				return subfile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 读取字符串进行分词 */
	public String[] WordsExtract(String sInput) throws IOException {
		//demo
		ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
		String argu = ".";	// 分词所需库的路径，Default Path : .		当前目录下？？？
		if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false) {	//Initialize
			/* byte[] getBytes(String charsetName)throws UnsupportedEncodingException使用指定的字符集将此 String 编码为 byte 序列
			 * parameter：sInitDirPath: Initial Directory Path, where file Configure.xml and Data directory stored，
			 * the default value is 0, it indicates the initial directory is current working directory path
			 */
			System.out.println("Init Fail!"); // 在哪输出？？？
			return null;
		}
		//System.out.println("Init Success!");
		
		//导入用户词典前分词(analysis)
		byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 0);
		/*public native byte[] ICTCLAS_ParagraphProcess(byte[] sSrc, int eCodeType, int bPOSTagged);
		 * sSrc: The source paragraph
		 * eCodeType:  The character coding type of the string											???
		 * bPOStagged: part-of-speech tagging , or word classes or lexical categories . 说法很多其实就是词性标注
		 * 				Judge whether need POS tagging, 0 for no tag; 1 for tagging; default:1.
		 */
		String nativeStr = new String(nativeBytes, 0, nativeBytes.length,"GB2312");
		nativeStr.trim();//返回字符串的副本，忽略前导空白和尾部空白
		String[] splitString = nativeStr.split("[\\s　]+");// 去掉连续空格???
		testICTCLAS50.ICTCLAS_Exit();//释放分词组件资源
		return splitString;
	}

	/** 读取文档内容进行分词，先转换为字符串再进行分词 */
	public String[] WordExtract(File file) throws IOException {
		String sInput = null;
		String[] word = null;
		try {
			FileReader fileReader = new FileReader(file);
			// 用于读取字符流。读取原始字节流用 FileInputStream
			BufferedReader br = new BufferedReader(fileReader);
			// 无缓冲，则每次调用 read() 或readLine() 都会导致从文件中读取字节
			StringBuilder sb = new StringBuilder();
			// StringBuffer 兼容的 API，但不保证同步,用于多个线程是不安全的;比 StringBuffer 要快

			while ((sInput = br.readLine()) != null)
				sb.append(sInput); // 指定的字符串追加到此字符序列
			br.close();
			fileReader.close();

			sInput = sb.toString(); // 返回此序列中数据的字符串表示形式
			ReadFile rf = new ReadFile();
			word = rf.WordsExtract(sInput);
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return word;
	}
}
