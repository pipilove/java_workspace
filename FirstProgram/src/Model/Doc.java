package Model;

class ID {
	public int cid = -2;// 文档簇编号，初始化为-2代表不属于任何一个文档簇
	public ID next = null;//同一个文档可能有不同的cid，属于不同核心的簇
}

public class Doc {
	public int NumOfWords;// 文档中词的个数，即特征词的个数
	public int Did;// 初始为-1 文档编号
	public WordFeature[] words;
	// 文档中的特征词数组，只记录对应于特征词库中词在此文档里的个数；且words数组大小为Fwords.length
	public ID id = null;

	public void setNum(int n) {
		NumOfWords = n;
	}
	
	//文档存在于簇的个数
	public int getIDNum() {
		int count = 1;//由0改为1
		ID t = id;
		if (t == null) {
			return 0;
		} else {
			//删除count++;
			while (t.next != null) {
				count++;
				t = t.next;
			}
			return count;
		}

	}
	
	//裁掉文档其余的簇编号。留下第一个簇的编号
	public void setone() {
		id.next = null;
	}

	// 将文档所属的id队列输入到数组里。即返回文档所在的所有簇的编号
	public int[] load2array() {
		int[] array = new int[getIDNum()];//直接调用getIDNum()！！！
		ID t = id;
		for (int i = 0; i < getIDNum(); i++) {
			array[i] = t.cid;
			t = t.next;
		}
		return array;
	}
	
	//判断文档是否存在簇编号ccid
	public boolean exist(int ccid) {
		ID t = id;
		while (t != null) {
			if (t.cid == ccid)
				return true;
			t = t.next;
		}
		return false;
	}

	// 将文档改为属于id=n的集合中
	public void changeID(int f, int n) {
		ID t = id;
		if (t.cid == f) {
			if (exist(n))
				id = id.next;
			else
				id.cid = n;// if，else去掉了{}
			return;
		}
		while (t.next != null) {
			if (t.next.cid == f) {
				if (exist(n)) {
					if (t.next.next == null)
						t.next = null;
					else
						t = t.next.next;// if，else去掉了{}
				} else
					t.next.cid = n;// else去掉了{}
				break;
			} else
				t = t.next;// else去掉了{}
		}
	}

	//设置文档簇编号
	public void setCid(int t) {
		if (id == null) {
			id = new ID();
			id.cid = t;
		} else {
			ID tt = id;
			while (tt.next != null)
				tt = tt.next;
			tt.next = new ID();
			tt.next.cid = t;
		}
	}

	public int getCid() {
		if (id == null) {
			return -2;
		}

		else
			return id.cid;
	}
}