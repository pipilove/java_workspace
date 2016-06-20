import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DataPreProcess.FeatureCalculate.DBSCAN;
import DataPreProcess.FeatureCalculate.DocAlike;
import DataPreProcess.FeatureCalculate.FeatureSelection;
import DataPreProcess.FeatureCalculate.TF_IDF;
import DataPreProcess.Readfile.Dictionary;
import DataPreProcess.Readfile.ReadFile;
import DataPreProcess.WriteFile.WriteDoc;
import DataProcess.PLSA.PLSA;
import Model.Doc;

public class Window extends JFrame implements ActionListener {
	Box baseBox, box1, box2;
	JButton b1, b2, b3, b4;
	TextField text1, text2, text3, textK;

	String webAbsolutePath, adsAbsolutePath, resAbsolutePath, numk;

	Window(String s) {
		super(s); // �½����ڵ�����
		b1 = new JButton("�����ı�ѡȡ");
		b1.addActionListener(this);
		b2 = new JButton("����ı�ѡȡ");
		b2.addActionListener(this);
		b3 = new JButton("������λ��");
		b3.addActionListener(this);
		b4 = new JButton("��ʼ����");
		b4.addActionListener(this);

		text1 = new TextField(30); // �������ָ���������¿��ı��ֶ�
		text2 = new TextField(30);
		text3 = new TextField(30);
		textK = new TextField(30);
		textK.setText("�˴�������ҳƥ�����������");
		box1 = Box.createVerticalBox(); // ����һ�����ϵ�����ʾ������� Box��
		box2 = Box.createVerticalBox();

		box1.add(Box.createVerticalStrut(30)); // ����һ�����ɼ��ġ��̶��߶ȵ����;�൱��������һ���̶���϶
		box1.add(b1);
		box1.add(Box.createVerticalStrut(8));
		box1.add(b2);
		box1.add(Box.createVerticalStrut(8));
		box1.add(b3);
		box1.add(Box.createVerticalStrut(15));
		box1.add(b4);

		box2.add(Box.createVerticalStrut(30));
		box2.add(text1);
		box2.add(Box.createVerticalStrut(8));
		box2.add(text2);
		box2.add(Box.createVerticalStrut(8));
		box2.add(text3);
		box2.add(Box.createVerticalStrut(15));
		box2.add(textK);

		baseBox = Box.createHorizontalBox(); // ����һ����������ʾ������� Box
		baseBox.add(Box.createHorizontalStrut(20));
		baseBox.add(box1);
		baseBox.add(Box.createHorizontalStrut(8));
		baseBox.add(box2);
		// baseBox.add(Box.createHorizontalStrut(20));

		setLayout(new FlowLayout());
		add(baseBox);
		setBackground();
		setBounds(333, 333, 500, 250); // setBounds(x,y,width,height) �ƶ����(x �� y
										// ָ�����Ͻ���ʾ���ϵ���λ��)���������С
		setVisible(true);
		// setResizable(false);

		this.addWindowListener(new WindowAdapter() { // �رհ�ť��������
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void setBackground() {
		((JPanel) this.getContentPane()).setOpaque(false); // ��������������Ϊ͸��,��ʾ��ײ��ı�������

		ImageIcon img = new ImageIcon("Pic/����.png"); // ���ͼƬ������ͼ����󴴽�һ��
														// ImageIcon
		JLabel background = new JLabel(img); // ��������ָ��ͼ��� JLabel ʵ��
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());

		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		// ���� java.awt.Container �̳еķ�������������ӵ���ײ���
	}

	// TODO Auto-generated method stub����࣬�������
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) { // ��ҳ�ı�ѡȡ��java.util.EventObject �̳еķ���,����������� Evente�Ķ���
			JFileChooser window = new JFileChooser(); // JFileChooser Ϊ�û�ѡ���ļ��ṩ��һ�ּ򵥵Ļ���
			window.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// ���� JFileChooser���������û�FILES_ONLY��DIRECTORIES_ONLY������FILES_AND_DIRECTORIES
			int returnVal = window.showOpenDialog(null);
			/* showOpenDialog(Component parent)throws HeadlessException����һ�� "Open File" �ļ�ѡ�����Ի���;
			 * parent- �öԻ���ĸ����������Ϊ null
			 * ����״̬�� JFileChooser.CANCEL_OPTION JFileChooser.APPROVE_OPTION JFileChooser.ERROR_OPTION 
			 */
			if (returnVal == JFileChooser.APPROVE_OPTION) { // ѡ��ȷ�ϣ�yes��ok���󷵻ظ�ֵ
				webAbsolutePath = window.getSelectedFile().getAbsolutePath();
				text1.setText(webAbsolutePath);
				text1.setEditable(false);
				if (Str_check(webAbsolutePath) == null) {
					// ʹ�ù��������棿�����μ�JFileChoser API;���Ŀ¼�к��в���txt�ļ���ʱ�������������ʾ
					JOptionPane.showMessageDialog(this, "�в�����Ҫ����ĵ����ڣ�", "����",JOptionPane.ERROR_MESSAGE);
					/*
					 * ȷ����������ʾ�Ի���� Frame,���Ϊ null ���� parentComponent ������ Frame����ʹ��Ĭ�ϵ� Frame;
					 * ...;�Ի���ı����ַ���;
					 * Ҫ��ʾ����Ϣ���ͣ�ERROR_MESSAGE����ʾ����ͼ�꣩��INFORMATION_MESSAGE��
					 * WARNING_MESSAGE��QUESTION_MESSAGE ��PLAIN_MESSAGE(��ͼ��)
					 */
					webAbsolutePath = null;
				}
			}
		}

		else if (e.getSource() == b2) {		//����ı�ѡȡ
			JFileChooser window = new JFileChooser();
			window.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = window.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				adsAbsolutePath = window.getSelectedFile().getAbsolutePath();
				text2.setText(adsAbsolutePath);
				text2.setEditable(false);
				if (Str_check(adsAbsolutePath) == null) {
					JOptionPane.showMessageDialog(this, "�в�����Ҫ����ĵ����ڣ�", "����",JOptionPane.ERROR_MESSAGE);
					adsAbsolutePath = null;
				}
			}
		}

		else if (e.getSource() == b3) {		//������λ��
			JFileChooser window = new JFileChooser();
			window.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = window.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				resAbsolutePath = window.getSelectedFile().getAbsolutePath();
				text3.setText(resAbsolutePath);
				text3.setEditable(false);
			}
		}
		
		else {	//������ҳƥ�����������
			long time = System.currentTimeMillis();		//�����Ժ���Ϊ��λ�ĵ�ǰʱ�䣬���ڼ�����ƥ���ʱ��

			int numKInt;		//���÷�Χ������
			numk = textK.getText();		// String getText()���ش��ı������ʾ���ı�(�� TextComponent ��ֵ)��Ĭ����һ�����ַ����� 
			numKInt = Integer.parseInt(numk);
			File[] temp = Str_check(adsAbsolutePath);		//��Ϊ���򷵻ع���ı�·���ڵ��ļ�
			if (!numk.matches("\\d+")) {	//��Ч��Pattern.matches(regex, str)
				JOptionPane.showMessageDialog(this, "�������ָ�ʽ", "����",JOptionPane.ERROR_MESSAGE);
			} else if (numKInt>= temp.length) {	//���ַ���������Ϊ�з��ŵ�ʮ�����������н���
				JOptionPane.showMessageDialog(this, "��͹���������ܹ����", "����",JOptionPane.ERROR_MESSAGE);
			} else {
				// ����
				File[] webfiles = null, adsfiles = null;
				
				if ((webAbsolutePath == null) || (adsAbsolutePath == null))	//&&��Ϊ��||
					JOptionPane.showMessageDialog(this, "��ҳ�͹��·������ȷ", "����",	JOptionPane.ERROR_MESSAGE);
				else {
					webfiles = Str_check(webAbsolutePath);
					adsfiles = Str_check(adsAbsolutePath);
				}
				
				if (webfiles != null && adsfiles != null) {
					int i, j;
					
					// ���������ʵ�	
					Dictionary dic = new Dictionary();
					// String[] dicWord=dic.loadDictionaryInv("dic/dictionary.txt");
					FeatureSelection fs = new FeatureSelection();
					String fDicPath = dic.constructFeatureDictionnary("dic/dictionary.txt",
							fs.FeatureCalculatemap(webfiles, adsfiles));
					
					// �����ĵ�ģ�� 
					Doc[] webdocs = fs.BuildWordVec(webfiles, fDicPath);
					Doc[] adsdocs = fs.BuildWordVec(adsfiles, fDicPath);

					WriteDoc wdoc = new WriteDoc();//��vsmģ�ͼ�TF-IDFȨ��д��txt�ļ��洢��ѡ��ļ�����·����
					
					// ��vsmģ��д���ĵ�������Ӳ����
					wdoc.writeFile(webdocs, resAbsolutePath + "/webDocVec.txt");//���е�webdocs��ж��ͬһ��txt�ļ���
					wdoc.writeFile(adsdocs, resAbsolutePath + "/adsDocVec.txt");
					int[][] webmatrix = wdoc.loadDoc2Vec(webdocs);//�����д����i���ĵ���������j����ÿ���ĵ��������ʵĸ���
					int[][] adsmatrix = wdoc.loadDoc2Vec(adsdocs);

					//��web��ads��������һ������������
					int[][] matrix = new int[webmatrix.length+ adsmatrix.length][adsmatrix[0].length];//��λ����ĳ��ȼ�����
					for (i = 0; i < matrix.length; i++)
						for (j = 0; j < matrix[0].length; j++) {//matrix[i]��Ϊ��matrix[0]
							if (i < webmatrix.length)
								matrix[i][j] = webmatrix[i][j];
							else
								matrix[i][j] = adsmatrix[i - webmatrix.length][j];
						}
					
					// TF-IDFȨ�ؼ���
					TF_IDF enty = new TF_IDF();
					double[][] tf_idf = enty.TF_IDF_Calcu(matrix);
					wdoc.writeFile(tf_idf, resAbsolutePath + "/TF_IDF.txt");
					
					DocAlike docAlike = new DocAlike();

					/*��result���ı��͹������ƶȷ������*/
					//���������ĵ����ƶ�
					double[][] result = docAlike.CosCalculateWholeMatrix_weight(tf_idf);
					wdoc.writeFile(result, resAbsolutePath + "/DocAlike.txt");
					//��ȡ���洢ads������ƶ�
					double[][] result_ads = new double[adsfiles.length][adsfiles.length];
					for (i = 0; i < adsfiles.length; i++)
						for (j = 0; j < adsfiles.length; j++) {
							if (i >= j)//i > j��Ϊi >= j
								result_ads[i][j] = result[i + webfiles.length][j + webfiles.length];
							else
								result_ads[i][j] = result[j + webfiles.length][i+ webfiles.length];
						}
					wdoc.writeFile(result_ads, resAbsolutePath + "/result_ads.txt");
					//��ȡVSMģ�ͽ�� ��web��ads�ı����ƶȣ�
					double[][] VSM_MATRIX = new double[webfiles.length][adsfiles.length];
					for (i = 0; i < webfiles.length; i++)
						for (j = 0; j < adsfiles.length; j++)
							VSM_MATRIX[i][j] = result[webfiles.length + j][i];

					// DBSCAN������adsdocs�ĵ����࣬�õ��������k
					ArrayList<Doc> doc = wdoc.loadDoc2ArrayList(adsdocs);
					DBSCAN data = new DBSCAN();
					data.dbscan(doc, result_ads);

					int numWeb = webfiles.length, numAds = adsfiles.length, 
							numWord = tf_idf[0].length, K = data.K;//kΪ����ĸ���
					PLSA one = new PLSA(tf_idf, numAds, numWeb, K, numWord);
					one.PLSA_TEMStep();

					wdoc.writeFile(one.Pz_d_out, resAbsolutePath + "/Pz_d_out.txt");
					double[][] result_plsa = docAlike.CosCalculateWholeMatrix_weight(one.Pz_d_out);
					wdoc.writeFile(result_plsa, resAbsolutePath + "/DocAlike_plsa.txt");
					/* ��ȡPLSAģ�ͽ��*/

					double[][] PLSA_MATRIX = new double[webfiles.length][adsfiles.length];
					for (i = 0; i < webfiles.length; i++)
						for (j = 0; j < adsfiles.length; j++) {
							PLSA_MATRIX[i][j] = result[webfiles.length + j][i];
						}
					double[][] PLSA_VSM = new double[webfiles.length][adsfiles.length];
					// ���ϴ���
					PLSA_VSM(PLSA_MATRIX, VSM_MATRIX, PLSA_VSM, 0.10);

					// wdoc.write_vsm(resAbsolutePath, webfiles, adsfiles, VSM_MATRIX, numK);
					wdoc.writeFile(PLSA_VSM, resAbsolutePath + "/PLSA_VSM.txt");
					// ��ҳ�������

					// ���д��Ӳ��
					wdoc.Write_result(resAbsolutePath, webfiles, adsfiles, PLSA_VSM, 0.45,
							numKInt);

					time = System.currentTimeMillis() - time;
					System.out.println((time / 1000) + "��");
					JOptionPane.showMessageDialog(this, "�������", "��ʾ�Ի���",JOptionPane.CLOSED_OPTION);
				}
			}
		}
	}

	/* �ж�·���Ƿ���ϱ�׼��������Ŀ¼�����ļ� */
	File[] Str_check(String s) {
		if (s == null)
			return null;
		ReadFile readfile = new ReadFile();
		File[] file = readfile.GetSubPath(s); // �õ�·��Ϊs��Ŀ¼�е��ļ���Ŀ¼�����򷵻�null
		for (int i = 0; i < file.length; i++)
			// file.length
			if (!(getsuffix(file[i].getName()).equals("txt")))
				/* getName()�����ļ���Ŀ¼�����ơ�·�������������е����һ������;
				 * equals(ObjectanObject)�����ַ�����ָ���Ķ���Ƚ�
				 */
				return null;
		return file;
	}

	/* �õ��ļ���׺ */// string...filename???
	String getsuffix(String s) {
		int a = s.lastIndexOf("."); // ����ָ�����ַ����ڴ��ַ��������ұ߳��ִ�������
		return s.substring(a + 1); // �����ַ�����ָ�����������ַ���ʼ��ֱ�����ַ���ĩβ
	}

	/*
	 * ��PLSA�������ĵ����ƶȺ���VSM�������ĵ����ƶȽ������ϴ���
	 */
	void PLSA_VSM(double[][] PLSA_MATRIX, double[][] VSM_MATRIX,
			double[][] PLSA_VSM, double Threshold) {
		int i, j;
		double temp;// �м����
		for (i = 0; i < PLSA_MATRIX.length; i++)
			for (j = 0; j < PLSA_MATRIX[i].length; j++) {
				if (VSM_MATRIX[i][j] > Threshold) {
					if (PLSA_MATRIX[i][j] < 0.5)// 0.5����ֵ
						PLSA_VSM[i][j] = PLSA_MATRIX[i][j] + 0.5;
					else
						PLSA_VSM[i][j] = PLSA_MATRIX[i][j];
				} else {
					temp = VSM_MATRIX[i][j] / Threshold;// ��VSMģ�Ͳ���������ֵ����������һ��
					PLSA_VSM[i][j] = PLSA_MATRIX[i][j] * temp;
				}
			}
	}
}