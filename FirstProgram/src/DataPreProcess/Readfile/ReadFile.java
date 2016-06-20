package DataPreProcess.Readfile;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import ICTCLAS.I3S.AC.ICTCLAS50;

/** 1.��ȡ�ļ��Ĳ�����2�����ַ����ִʣ�3.���ĵ��ִ� */
public class ReadFile {
	/*** �õ�Ŀ¼���ļ�,��Ϊ�ļ����� */
	public File[] GetSubPath(String path) {
		try {
			File file = new File(path);
			if (file.isDirectory()) {
				// ��Ŀ¼�ļ���ָ����ĳЩ���ļ���;���ҽ����˳���·������ʾ���ļ���������һ��Ŀ¼ʱ������ true ���ֻ��һ���ļ��أ�
				File[] subfile = file.listFiles();
				// ����һ������·�������飬��Щ·������ʾ�˳���·������ʾ��Ŀ¼�е��ļ���Ŀ¼
				return subfile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** ��ȡ�ַ������зִ� */
	public String[] WordsExtract(String sInput) throws IOException {
		//demo
		ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
		String argu = ".";	// �ִ�������·����Default Path : .		��ǰĿ¼�£�����
		if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false) {	//Initialize
			/* byte[] getBytes(String charsetName)throws UnsupportedEncodingExceptionʹ��ָ�����ַ������� String ����Ϊ byte ����
			 * parameter��sInitDirPath: Initial Directory Path, where file Configure.xml and Data directory stored��
			 * the default value is 0, it indicates the initial directory is current working directory path
			 */
			System.out.println("Init Fail!"); // �������������
			return null;
		}
		//System.out.println("Init Success!");
		
		//�����û��ʵ�ǰ�ִ�(analysis)
		byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 0);
		/*public native byte[] ICTCLAS_ParagraphProcess(byte[] sSrc, int eCodeType, int bPOSTagged);
		 * sSrc: The source paragraph
		 * eCodeType:  The character coding type of the string											???
		 * bPOStagged: part-of-speech tagging , or word classes or lexical categories . ˵���ܶ���ʵ���Ǵ��Ա�ע
		 * 				Judge whether need POS tagging, 0 for no tag; 1 for tagging; default:1.
		 */
		String nativeStr = new String(nativeBytes, 0, nativeBytes.length,"GB2312");
		nativeStr.trim();//�����ַ����ĸ���������ǰ���հ׺�β���հ�
		String[] splitString = nativeStr.split("[\\s��]+");// ȥ�������ո�???
		testICTCLAS50.ICTCLAS_Exit();//�ͷŷִ������Դ
		return splitString;
	}

	/** ��ȡ�ĵ����ݽ��зִʣ���ת��Ϊ�ַ����ٽ��зִ� */
	public String[] WordExtract(File file) throws IOException {
		String sInput = null;
		String[] word = null;
		try {
			FileReader fileReader = new FileReader(file);
			// ���ڶ�ȡ�ַ�������ȡԭʼ�ֽ����� FileInputStream
			BufferedReader br = new BufferedReader(fileReader);
			// �޻��壬��ÿ�ε��� read() ��readLine() ���ᵼ�´��ļ��ж�ȡ�ֽ�
			StringBuilder sb = new StringBuilder();
			// StringBuffer ���ݵ� API��������֤ͬ��,���ڶ���߳��ǲ���ȫ��;�� StringBuffer Ҫ��

			while ((sInput = br.readLine()) != null)
				sb.append(sInput); // ָ�����ַ���׷�ӵ����ַ�����
			br.close();
			fileReader.close();

			sInput = sb.toString(); // ���ش����������ݵ��ַ�����ʾ��ʽ
			ReadFile rf = new ReadFile();
			word = rf.WordsExtract(sInput);
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return word;
	}
}
