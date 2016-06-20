package DataPreProcess.WriteFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import DataPreProcess.FeatureCalculate.FeatureSelection;
import DataPreProcess.Readfile.Dictionary;
import Model.Doc;
import Model.WordFeature;

public class WriteDoc {
	/** ���ĵ�������Ĵ���������Ϣ�����ĵ� */
	public boolean writeFile(Doc[] doc, String docVecPath) {
		try {
			File file = new File(docVecPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			for (int i = 0; i < doc.length; i++) {
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < doc[i].words.length; j++) {// doc�ı��дʵ���Ŀwords.length=Fwords.length��
					sb.append(",").append(doc[i].words[j].num);
				}
				sb.append("\r\n");
				fw.write(sb.toString());
			}
			fw.flush();
			fw.close();
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return true;// д�ĵ��ɹ�
	}

	/** ��������Ϣ�����ĵ� */
	public boolean writeFile(double[][] matrix, String TF_IDF_Path) {
		try {
			File file = new File(TF_IDF_Path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			for (int i = 0; i < matrix.length; i++) {
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < matrix[i].length; j++)
					sb.append(matrix[i][j]).append(",");
				sb.append("\r\n");
				fw.write(sb.toString());
			}
			fw.flush();
			fw.close();
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return true;
	}

	/** ���ĵ���Ĵ���num��Ϣ���뵽������ */
	public int[][] loadDoc2Vec(Doc[] doc) {
		int lengthOfWords = doc[0].words.length;
		int lengthOfDoc = doc.length;
		int[][] Vec = new int[lengthOfDoc][lengthOfWords];
		for (int i = 0; i < lengthOfDoc; i++) {
			for (int j = 0; j < lengthOfWords; j++) {
				Vec[i][j] = doc[i].words[j].num;
			}
		}
		return Vec;
	}

	/** ���ĵ���Ĵ����weight��Ϣ���뵽������ */
	public Doc[] BuildWordVec(File[] file, String FeatureDic) {
		Dictionary fd = new Dictionary();
		// loadFeatureDic
		int nDoc = file.length;
		String[] Fword = fd.loadFeatureDic(FeatureDic);// �����ʱ�Ŵ�1��ʼ
		System.out.println(Fword.length);
		Doc[] doc = new Doc[nDoc];
		FeatureSelection sd = new FeatureSelection();
		for (int i = 0; i < nDoc; i++) {// ѭ��ÿ���ĵ�
			doc[i] = new Doc();
			doc[i].NumOfWords = Fword.length;
			doc[i].Did = i + 1;
			doc[i].words = new WordFeature[Fword.length];
			for (int m = 0; m < Fword.length; m++)
				// ��ʼ��WordFeature����
				doc[i].words[m] = new WordFeature();
			String[] temp = sd.SingleDocFeatureCal(file[i]);
			for (int j = 0; j < temp.length; j++) {// ѭ�����ĵ���ÿ���ʣ�
				// �жϸô��Ƿ��ڴʵ���
				for (int k = 0; k < Fword.length; k++) {
					if (Fword[k].equalsIgnoreCase(temp[j])) {
						doc[i].words[k].num++;
					}
				}
			}
		}
		return doc;
	}

	public ArrayList<Doc> loadDoc2ArrayList(Doc[] doc) {
		int length = doc.length;//�ļ�����
		ArrayList<Doc> result = new ArrayList<Doc>();
		for (int i = 0; i < length; i++)
			result.add(doc[i]);
		return result;
	}

	public void Write_result(String desfile, File[] webfile, File[] adsfile,
			double[][] PLSA_VSM, double d, int K) {
		// TODO Auto-generated method stub
		String filepath = null;
		int i, j;
		boolean[] ads = new boolean[adsfile.length];
		for (i = 0; i < webfile.length; i++) {
			// ��ȡ��ҳ���ƣ���������ҳ�����������ļ���
			filepath = desfile + "/" + webfile[i].getName();
			if (CreateFolder(filepath)) {
				// ����ļ���
				Clear_file(filepath);
				CopyFile(webfile[i].getAbsolutePath(), filepath + "/"
						+ webfile[i].getName());
				// ���
				adsfalse(ads);
				// ������������
				check_ads(i, K, PLSA_VSM, ads, d);
				for (j = 0; j < PLSA_VSM[i].length; j++) {
					if (ads[j])
						CopyFile(adsfile[j].getAbsolutePath(), filepath + "/"
								+ adsfile[j].getName());
				}
			}
		}
	}

	private void adsfalse(boolean[] ads) {
		// TODO Auto-generated method stub
		for (int i = 0; i < ads.length; i++)
			ads[i] = false;
	}

	// ����
	private void check_ads(int i, int K, double[][] pLSA_VSM, boolean[] result,
			double d) {
		// TODO Auto-generated method stub
		int j, n, length, count = 0;
		length = pLSA_VSM[i].length;
		double[] temp = new double[pLSA_VSM[i].length];
		for (j = 0; j < length; j++)
			if (pLSA_VSM[i][j] >= d)
				count++;
		if (count > K) {
			for (j = 0; j < length; j++)
				if (pLSA_VSM[i][j] >= d)
					result[j] = true;
		} else {
			count = 0;
			for (j = 0; j < length; j++)
				temp[j] = pLSA_VSM[i][j];
			while (count < K) {
				for (n = 0, j = 0; j < length; j++)
					if (temp[n] < temp[j])
						n = j;
				count++;
				result[n] = true;
				temp[n] = -1;
			}
		}
	}

	/*
	 * ����ļ��е����ļ�
	 */
	private void Clear_file(String filepath) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		if (!file.exists())
			return;
		if (!file.isDirectory())
			return;
		String[] filelist = file.list();
		File temp = null;
		for (int i = 0; i < filelist.length; i++) {
			if (filepath.endsWith(File.separator))
				temp = new File(filepath + filelist[i]);
			else
				temp = new File(filepath + File.separator + filelist[i]);
			if (temp.isFile())
				temp.delete();
			if (temp.isDirectory()) {
				Clear_file(filepath + "/" + filelist[i]);
				temp.delete();
			}
		}
	}

	/*
	 * �����ļ���
	 */
	boolean CreateFolder(String fpath) {
		try {
			File file = new File(fpath);
			if (!file.exists())
				file.mkdir();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * �ض��ļ�����
	 */
	boolean CopyFile(String sourcefile, String desfile) {
		try {
			FileInputStream in = new FileInputStream(sourcefile);
			FileOutputStream out = new FileOutputStream(desfile);
			byte[] buffer = new byte[1024];
			int readed = -1;
			while ((readed = in.read(buffer)) > 0)
				out.write(buffer, 0, readed);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void write_vsm(String desfile, File[] webfile, File[] adsfile,
			double[][] VSM, int K) {
		String filepath = null;
		int i, j;
		boolean[] ads = new boolean[adsfile.length];
		for (i = 0; i < webfile.length; i++) {
			// ��ȡ��ҳ���ƣ���������ҳ�����������ļ���
			filepath = desfile + "/" + webfile[i].getName();
			if (CreateFolder(filepath)) {
				// ����ļ���
				Clear_file(filepath);
				CopyFile(webfile[i].getAbsolutePath(), filepath + "/"
						+ webfile[i].getName());
				// ���
				adsfalse(ads);
				// ������������
				VSM_ads(i, K, VSM, ads);
				for (j = 0; j < VSM[i].length; j++) {
					if (ads[j])
						CopyFile(adsfile[j].getAbsolutePath(), filepath + "/"
								+ adsfile[j].getName());
				}
			}
		}
	}

	private void VSM_ads(int i, int k, double[][] VSM, boolean[] ads) {
		// TODO Auto-generated method stub
		int j, n, count = 0, length = VSM[0].length;
		double[] temp = new double[length];
		for (j = 0; j < length; j++)
			temp[j] = VSM[i][j];
		while (count < k) {
			for (n = 0, j = 0; j < length; j++)
				if (temp[n] < temp[j])
					n = j;
			count++;
			ads[n] = true;
			temp[n] = -1;
		}
	}
}
