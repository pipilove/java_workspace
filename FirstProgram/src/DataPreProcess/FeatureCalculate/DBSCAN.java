package DataPreProcess.FeatureCalculate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import Model.Doc;

public class DBSCAN {
	public static double e = 0.2;//0.8改为0.2
	public int MinPts = 1;//核心点具有的最小邻居数目,是否小了点？
	public int K;//簇类个数
	
	/* dbscan扫描文档集合 */
	public void dbscan(ArrayList<Doc> doc, double[][] matrix) {//data.dbscan(doc, result_ads);
		int clusterID = -1;
		Iterator<Doc> iter = doc.iterator();
		while (iter.hasNext()) {
			Doc p = iter.next();
			Vector<Doc> neighbors = getNeighbors(p, doc, matrix);
			if (neighbors.size() > MinPts) {// 是否为核心点
				clusterID++;
				setCluster(clusterID, neighbors);//邻居包括本身，即本身也加入到此簇中
			}
		}
		// 合并密度相连类
		Iterator<Doc> it = doc.iterator();
		while (it.hasNext()) {
			Doc p = it.next();
			if (p.getIDNum() >= 2)
				mergeCluster(p, doc);
		}
		// 获取簇类个数K
		K = 0;
		K = getK(doc);
	}
	
	/* 获取簇类个数K */
	private int getK(ArrayList<Doc> doc) {
		int i = 0;
		int count = 0;
		Vector<Doc> sum = new Vector<Doc>();
		Iterator<Doc> it = doc.iterator();
		while (it.hasNext()) {
			Doc p = it.next();
			if (p.getCid() == -2)
				count++;//离散点的个数
			for (i = 0; i < sum.size(); i++)//sum.size()初始时为0
				if (p.getCid() == sum.get(i).getCid())
					break;
			if (i == sum.size() && p.getCid() != -2)
				sum.add(p);
		}
		return sum.size() + count;
	}

	
	/* 获得直接密度可达点 */
	public static Vector<Doc> getNeighbors(Doc center, ArrayList<Doc> docs,double[][] matrix) {
		Vector<Doc> neighbors = new Vector<Doc>();
		neighbors.add(center);//邻居包括本身
		Iterator<Doc> iter = docs.iterator();
		while (iter.hasNext()) {
			Doc t = iter.next();
			if ( matrix[center.Did][t.Did]>= e)
				//利用vsm模型计算的相似度作为距离；修改e值和((1 - matrix[center.Did][t.Did]) < e)得到
				neighbors.add(t);
		}
		return neighbors;
	}

	/* 获得集合，设置ID */
	public static void setCluster(int clusterID, Vector<Doc> neighbors) {
		Iterator<Doc> iter = neighbors.iterator();
		while (iter.hasNext()) {
			Doc p = iter.next();
			p.setCid(clusterID);
		}
	}

	
	/* 合并密度相连集合,获得最大集合 */
	public static void mergeCluster(Doc t, ArrayList<Doc> doc) {
		int[] id = t.load2array();//返回文档所在的所有簇的编号
		t.setone();//裁掉文档其余的簇编号。留下第一个簇的编号id[0]
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
