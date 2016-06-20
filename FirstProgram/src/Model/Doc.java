package Model;

class ID {
	public int cid = -2;// �ĵ��ر�ţ���ʼ��Ϊ-2���������κ�һ���ĵ���
	public ID next = null;//ͬһ���ĵ������в�ͬ��cid�����ڲ�ͬ���ĵĴ�
}

public class Doc {
	public int NumOfWords;// �ĵ��дʵĸ������������ʵĸ���
	public int Did;// ��ʼΪ-1 �ĵ����
	public WordFeature[] words;
	// �ĵ��е����������飬ֻ��¼��Ӧ�������ʿ��д��ڴ��ĵ���ĸ�������words�����СΪFwords.length
	public ID id = null;

	public void setNum(int n) {
		NumOfWords = n;
	}
	
	//�ĵ������ڴصĸ���
	public int getIDNum() {
		int count = 1;//��0��Ϊ1
		ID t = id;
		if (t == null) {
			return 0;
		} else {
			//ɾ��count++;
			while (t.next != null) {
				count++;
				t = t.next;
			}
			return count;
		}

	}
	
	//�õ��ĵ�����Ĵر�š����µ�һ���صı��
	public void setone() {
		id.next = null;
	}

	// ���ĵ�������id�������뵽������������ĵ����ڵ����дصı��
	public int[] load2array() {
		int[] array = new int[getIDNum()];//ֱ�ӵ���getIDNum()������
		ID t = id;
		for (int i = 0; i < getIDNum(); i++) {
			array[i] = t.cid;
			t = t.next;
		}
		return array;
	}
	
	//�ж��ĵ��Ƿ���ڴر��ccid
	public boolean exist(int ccid) {
		ID t = id;
		while (t != null) {
			if (t.cid == ccid)
				return true;
			t = t.next;
		}
		return false;
	}

	// ���ĵ���Ϊ����id=n�ļ�����
	public void changeID(int f, int n) {
		ID t = id;
		if (t.cid == f) {
			if (exist(n))
				id = id.next;
			else
				id.cid = n;// if��elseȥ����{}
			return;
		}
		while (t.next != null) {
			if (t.next.cid == f) {
				if (exist(n)) {
					if (t.next.next == null)
						t.next = null;
					else
						t = t.next.next;// if��elseȥ����{}
				} else
					t.next.cid = n;// elseȥ����{}
				break;
			} else
				t = t.next;// elseȥ����{}
		}
	}

	//�����ĵ��ر��
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