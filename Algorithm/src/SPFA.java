
public class SPFA {
	const int INF = 999999;  

	// ����n��ʾ�������s��ʾԴ��  
	int SPFAAlgorithm(int n, int s){
	    int i, pri, end, p, t;		// pri�Ƕ���ͷ��㣬end�Ƕ���β���  
	    char vst[]=new char[n];  	//���������¼ÿ�����Ƿ��ڶ�����
		int  dis[n];
		int map[n][n]; //��ʼ�����i��j�ľ��룬δ֪��map[i,j]=INF;maxnΪ��ĸ���?
	    for(int i=0; i<n; ++i)  
	        Q[i] = 0;  		//???
        for (i=0; i<n; i++)  	//��ʼ������
	        dis[i] = INF;  
	    dis[s] = 0;  
	    vst[s] = 1;  
	    Q[0] = s; pri = 0; end = 1;  
	    while (pri < end){  
        p = Q[pri];
        for (i=0; i<n; ++i){		//����dis  
            if (dis[p]+map[p][i] < dis[i]){  
	                dis[i] = dis[p]+map[p][i];  
	                if (!vst[i]){       //��iδ�ڶ������������в�����Ѽ���
                    Q[end++] = i;  
	                vst[i] = 1;  
	                }  
	            }  
        }  
	        vst[p] = 0;   // �ó��ӵĵ�Ϊδ���  
        pri++;  
	    }  
	    return 1;  
}

}
