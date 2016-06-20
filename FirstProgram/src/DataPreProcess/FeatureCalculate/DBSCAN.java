package DataPreProcess.FeatureCalculate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import Model.Doc;

public class DBSCAN {
	public static double e = 0.2;//0.8��Ϊ0.2
	public int MinPts = 1;//���ĵ���е���С�ھ���Ŀ,�Ƿ�С�˵㣿
	public int K;//�������
	
	/* dbscanɨ���ĵ����� */
	public void dbscan(ArrayList<Doc> doc, double[][] matrix) {//data.dbscan(doc, result_ads);
		int clusterID = -1;
		Iterator<Doc> iter = doc.iterator();
		while (iter.hasNext()) {
			Doc p = iter.next();
			Vector<Doc> neighbors = getNeighbors(p, doc, matrix);
			if (neighbors.size() > MinPts) {// �Ƿ�Ϊ���ĵ�
				clusterID++;
				setCluster(clusterID, neighbors);//�ھӰ�������������Ҳ���뵽�˴���
			}
		}
		// �ϲ��ܶ�������
		Iterator<Doc> it = doc.iterator();
		while (it.hasNext()) {
			Doc p = it.next();
			if (p.getIDNum() >= 2)
				mergeCluster(p, doc);
		}
		// ��ȡ�������K
		K = 0;
		K = getK(doc);
	}
	
	/* ��ȡ�������K */
	private int getK(ArrayList<Doc> doc) {
		int i = 0;
		int count = 0;
		Vector<Doc> sum = new Vector<Doc>();
		Iterator<Doc> it = doc.iterator();
		while (it.hasNext()) {
			Doc p = it.next();
			if (p.getCid() == -2)
				count++;//��ɢ��ĸ���
			for (i = 0; i < sum.size(); i++)//sum.size()��ʼʱΪ0
				if (p.getCid() == sum.get(i).getCid())
					break;
			if (i == sum.size() && p.getCid() != -2)
				sum.add(p);
		}
		return sum.size() + count;
	}

	
	/* ���ֱ���ܶȿɴ�� */
	public static Vector<Doc> getNeighbors(Doc center, ArrayList<Doc> docs,double[][] matrix) {
		Vector<Doc> neighbors = new Vector<Doc>();
		neighbors.add(center);//�ھӰ�������
		Iterator<Doc> iter = docs.iterator();
		while (iter.hasNext()) {
			Doc t = iter.next();
			if ( matrix[center.Did][t.Did]>= e)
				//����vsmģ�ͼ�������ƶ���Ϊ���룻�޸�eֵ��((1 - matrix[center.Did][t.Did]) < e)�õ�
				neighbors.add(t);
		}
		return neighbors;
	}

	/* ��ü��ϣ�����ID */
	public static void setCluster(int clusterID, Vector<Doc> neighbors) {
		Iterator<Doc> iter = neighbors.iterator();
		while (iter.hasNext()) {
			Doc p = iter.next();
			p.setCid(clusterID);
		}
	}

	
	/* �ϲ��ܶ���������,�����󼯺� */
	public static void mergeCluster(Doc t, ArrayList<Doc> doc) {
		int[] id = t.load2array();//�����ĵ����ڵ����дصı��
		t.setone();//�õ��ĵ�����Ĵر�š����µ�һ���صı��id[0]
		for (int i = 1; i < id.length; i++) {
			Iterator<Doc> iter = doc.iterator();
			while (iter.hasNext()) {
				Doc p = iter.next();
				if (p.exist(id[i]))
					p.changeID(id[i], id[0]);
			}
		}
	}

}
