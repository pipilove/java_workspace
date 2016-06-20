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
		super(s); // 新建窗口的名称
		b1 = new JButton("网络文本选取");
		b1.addActionListener(this);
		b2 = new JButton("广告文本选取");
		b2.addActionListener(this);
		b3 = new JButton("结果存放位置");
		b3.addActionListener(this);
		b4 = new JButton("开始计算");
		b4.addActionListener(this);

		text1 = new TextField(30); // 构造具有指定列数的新空文本字段
		text2 = new TextField(30);
		text3 = new TextField(30);
		textK = new TextField(30);
		textK.setText("此处设置网页匹配广告最低数量");
		box1 = Box.createVerticalBox(); // 创建一个从上到下显示其组件的 Box！
		box2 = Box.createVerticalBox();

		box1.add(Box.createVerticalStrut(30)); // 创建一个不可见的、固定高度的组件;相当于增加了一个固定空隙
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

		baseBox = Box.createHorizontalBox(); // 创建一个从左到右显示其组件的 Box
		baseBox.add(Box.createHorizontalStrut(20));
		baseBox.add(box1);
		baseBox.add(Box.createHorizontalStrut(8));
		baseBox.add(box2);
		// baseBox.add(Box.createHorizontalStrut(20));

		setLayout(new FlowLayout());
		add(baseBox);
		setBackground();
		setBounds(333, 333, 500, 250); // setBounds(x,y,width,height) 移动组件(x 和 y
										// 指定左上角显示器上的新位置)并调整其大小
		setVisible(true);
		// setResizable(false);

		this.addWindowListener(new WindowAdapter() { // 关闭按钮监听？？
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void setBackground() {
		((JPanel) this.getContentPane()).setOpaque(false); // 将面板的内容设置为透明,显示最底部的背景？？

		ImageIcon img = new ImageIcon("Pic/界面.png"); // 添加图片，根据图像对象创建一个
														// ImageIcon
		JLabel background = new JLabel(img); // 创建具有指定图像的 JLabel 实例
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());

		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		// 从类 java.awt.Container 继承的方法，将背景添加到最底层中
	}

	// TODO Auto-generated method stub存根类，存根代码
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) { // 网页文本选取；java.util.EventObject 继承的方法,返回最初发生 Evente的对象
			JFileChooser window = new JFileChooser(); // JFileChooser 为用户选择文件提供了一种简单的机制
			window.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// 设置 JFileChooser，以允许用户FILES_ONLY、DIRECTORIES_ONLY，或者FILES_AND_DIRECTORIES
			int returnVal = window.showOpenDialog(null);
			/* showOpenDialog(Component parent)throws HeadlessException弹出一个 "Open File" 文件选择器对话框;
			 * parent- 该对话框的父组件，可以为 null
			 * 返回状态： JFileChooser.CANCEL_OPTION JFileChooser.APPROVE_OPTION JFileChooser.ERROR_OPTION 
			 */
			if (returnVal == JFileChooser.APPROVE_OPTION) { // 选择确认（yes、ok）后返回该值
				webAbsolutePath = window.getSelectedFile().getAbsolutePath();
				text1.setText(webAbsolutePath);
				text1.setEditable(false);
				if (Str_check(webAbsolutePath) == null) {
					// 使用过滤器代替？？？参见JFileChoser API;如果目录中含有不是txt文件的时候则输出错误提示
					JOptionPane.showMessageDialog(this, "有不符合要求的文档存在！", "错误！",JOptionPane.ERROR_MESSAGE);
					/*
					 * 确定在其中显示对话框的 Frame,如果为 null 或者 parentComponent 不具有 Frame，则使用默认的 Frame;
					 * ...;对话框的标题字符串;
					 * 要显示的消息类型：ERROR_MESSAGE（显示错误图标）、INFORMATION_MESSAGE、
					 * WARNING_MESSAGE、QUESTION_MESSAGE 或PLAIN_MESSAGE(无图标)
					 */
					webAbsolutePath = null;
				}
			}
		}

		else if (e.getSource() == b2) {		//广告文本选取
			JFileChooser window = new JFileChooser();
			window.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = window.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				adsAbsolutePath = window.getSelectedFile().getAbsolutePath();
				text2.setText(adsAbsolutePath);
				text2.setEditable(false);
				if (Str_check(adsAbsolutePath) == null) {
					JOptionPane.showMessageDialog(this, "有不符合要求的文档存在！", "错误！",JOptionPane.ERROR_MESSAGE);
					adsAbsolutePath = null;
				}
			}
		}

		else if (e.getSource() == b3) {		//结果存放位置
			JFileChooser window = new JFileChooser();
			window.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = window.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				resAbsolutePath = window.getSelectedFile().getAbsolutePath();
				text3.setText(resAbsolutePath);
				text3.setEditable(false);
			}
		}
		
		else {	//设置网页匹配广告最低数量
			long time = System.currentTimeMillis();		//返回以毫秒为单位的当前时间，用于计算广告匹配的时间

			int numKInt;		//作用范围？？？
			numk = textK.getText();		// String getText()返回此文本组件表示的文本(此 TextComponent 的值)。默认是一个空字符串。 
			numKInt = Integer.parseInt(numk);
			File[] temp = Str_check(adsAbsolutePath);		//不为空则返回广告文本路径内的文件
			if (!numk.matches("\\d+")) {	//等效于Pattern.matches(regex, str)
				JOptionPane.showMessageDialog(this, "不是数字格式", "错误！",JOptionPane.ERROR_MESSAGE);
			} else if (numKInt>= temp.length) {	//将字符串参数作为有符号的十进制整数进行解析
				JOptionPane.showMessageDialog(this, "最低广告数超过总广告数", "错误！",JOptionPane.ERROR_MESSAGE);
			} else {
				// 计算
				File[] webfiles = null, adsfiles = null;
				
				if ((webAbsolutePath == null) || (adsAbsolutePath == null))	//&&改为了||
					JOptionPane.showMessageDialog(this, "网页和广告路径不正确", "错误！",	JOptionPane.ERROR_MESSAGE);
				else {
					webfiles = Str_check(webAbsolutePath);
					adsfiles = Str_check(adsAbsolutePath);
				}
				
				if (webfiles != null && adsfiles != null) {
					int i, j;
					
					// 构建特征词典	
					Dictionary dic = new Dictionary();
					// String[] dicWord=dic.loadDictionaryInv("dic/dictionary.txt");
					FeatureSelection fs = new FeatureSelection();
					String fDicPath = dic.constructFeatureDictionnary("dic/dictionary.txt",
							fs.FeatureCalculatemap(webfiles, adsfiles));
					
					// 建立文档模型 
					Doc[] webdocs = fs.BuildWordVec(webfiles, fDicPath);
					Doc[] adsdocs = fs.BuildWordVec(adsfiles, fDicPath);

					WriteDoc wdoc = new WriteDoc();//将vsm模型及TF-IDF权重写进txt文件存储在选择的计算结果路径上
					
					// 将vsm模型写入文档，存在硬盘里
					wdoc.writeFile(webdocs, resAbsolutePath + "/webDocVec.txt");//所有的webdocs都卸载同一个txt文件里
					wdoc.writeFile(adsdocs, resAbsolutePath + "/adsDocVec.txt");
					int[][] webmatrix = wdoc.loadDoc2Vec(webdocs);//矩阵行代表第i个文档，矩阵列j代表每个文档中特征词的个数
					int[][] adsmatrix = wdoc.loadDoc2Vec(adsdocs);

					//将web和ads数组存放于一个矩阵数组中
					int[][] matrix = new int[webmatrix.length+ adsmatrix.length][adsmatrix[0].length];//二位数组的长度即行数
					for (i = 0; i < matrix.length; i++)
						for (j = 0; j < matrix[0].length; j++) {//matrix[i]改为了matrix[0]
							if (i < webmatrix.length)
								matrix[i][j] = webmatrix[i][j];
							else
								matrix[i][j] = adsmatrix[i - webmatrix.length][j];
						}
					
					// TF-IDF权重计算
					TF_IDF enty = new TF_IDF();
					double[][] tf_idf = enty.TF_IDF_Calcu(matrix);
					wdoc.writeFile(tf_idf, resAbsolutePath + "/TF_IDF.txt");
					
					DocAlike docAlike = new DocAlike();

					/*将result中文本和广告的相似度分离出来*/
					//计算所有文档相似度
					double[][] result = docAlike.CosCalculateWholeMatrix_weight(tf_idf);
					wdoc.writeFile(result, resAbsolutePath + "/DocAlike.txt");
					//提取并存储ads间的相似度
					double[][] result_ads = new double[adsfiles.length][adsfiles.length];
					for (i = 0; i < adsfiles.length; i++)
						for (j = 0; j < adsfiles.length; j++) {
							if (i >= j)//i > j改为i >= j
								result_ads[i][j] = result[i + webfiles.length][j + webfiles.length];
							else
								result_ads[i][j] = result[j + webfiles.length][i+ webfiles.length];
						}
					wdoc.writeFile(result_ads, resAbsolutePath + "/result_ads.txt");
					//提取VSM模型结果 （web和ads文本相似度）
					double[][] VSM_MATRIX = new double[webfiles.length][adsfiles.length];
					for (i = 0; i < webfiles.length; i++)
						for (j = 0; j < adsfiles.length; j++)
							VSM_MATRIX[i][j] = result[webfiles.length + j][i];

					// DBSCAN处理，将adsdocs文档聚类，得到簇类个数k
					ArrayList<Doc> doc = wdoc.loadDoc2ArrayList(adsdocs);
					DBSCAN data = new DBSCAN();
					data.dbscan(doc, result_ads);

					int numWeb = webfiles.length, numAds = adsfiles.length, 
							numWord = tf_idf[0].length, K = data.K;//k为簇类的个数
					PLSA one = new PLSA(tf_idf, numAds, numWeb, K, numWord);
					one.PLSA_TEMStep();

					wdoc.writeFile(one.Pz_d_out, resAbsolutePath + "/Pz_d_out.txt");
					double[][] result_plsa = docAlike.CosCalculateWholeMatrix_weight(one.Pz_d_out);
					wdoc.writeFile(result_plsa, resAbsolutePath + "/DocAlike_plsa.txt");
					/* 提取PLSA模型结果*/

					double[][] PLSA_MATRIX = new double[webfiles.length][adsfiles.length];
					for (i = 0; i < webfiles.length; i++)
						for (j = 0; j < adsfiles.length; j++) {
							PLSA_MATRIX[i][j] = result[webfiles.length + j][i];
						}
					double[][] PLSA_VSM = new double[webfiles.length][adsfiles.length];
					// 整合处理
					PLSA_VSM(PLSA_MATRIX, VSM_MATRIX, PLSA_VSM, 0.10);

					// wdoc.write_vsm(resAbsolutePath, webfiles, adsfiles, VSM_MATRIX, numK);
					wdoc.writeFile(PLSA_VSM, resAbsolutePath + "/PLSA_VSM.txt");
					// 网页广告推送

					// 结果写入硬盘
					wdoc.Write_result(resAbsolutePath, webfiles, adsfiles, PLSA_VSM, 0.45,
							numKInt);

					time = System.currentTimeMillis() - time;
					System.out.println((time / 1000) + "秒");
					JOptionPane.showMessageDialog(this, "运算完成", "提示对话框",JOptionPane.CLOSED_OPTION);
				}
			}
		}
	}

	/* 判断路径是否符合标准，并返回目录下子文件 */
	File[] Str_check(String s) {
		if (s == null)
			return null;
		ReadFile readfile = new ReadFile();
		File[] file = readfile.GetSubPath(s); // 得到路径为s的目录中的文件和目录，空则返回null
		for (int i = 0; i < file.length; i++)
			// file.length
			if (!(getsuffix(file[i].getName()).equals("txt")))
				/* getName()返回文件或目录的名称。路径名名称序列中的最后一个名称;
				 * equals(ObjectanObject)将此字符串与指定的对象比较
				 */
				return null;
		return file;
	}

	/* 得到文件后缀 */// string...filename???
	String getsuffix(String s) {
		int a = s.lastIndexOf("."); // 返回指定子字符串在此字符串中最右边出现处的索引
		return s.substring(a + 1); // 该子字符串从指定索引处的字符开始，直到此字符串末尾
	}

	/*
	 * 由PLSA处理后的文档相似度和由VSM处理后的文档相似度进行联合处理
	 */
	void PLSA_VSM(double[][] PLSA_MATRIX, double[][] VSM_MATRIX,
			double[][] PLSA_VSM, double Threshold) {
		int i, j;
		double temp;// 中间变量
		for (i = 0; i < PLSA_MATRIX.length; i++)
			for (j = 0; j < PLSA_MATRIX[i].length; j++) {
				if (VSM_MATRIX[i][j] > Threshold) {
					if (PLSA_MATRIX[i][j] < 0.5)// 0.5是阈值
						PLSA_VSM[i][j] = PLSA_MATRIX[i][j] + 0.5;
					else
						PLSA_VSM[i][j] = PLSA_MATRIX[i][j];
				} else {
					temp = VSM_MATRIX[i][j] / Threshold;// 讲VSM模型参数除以阈值，将参数归一化
					PLSA_VSM[i][j] = PLSA_MATRIX[i][j] * temp;
				}
			}
	}
}