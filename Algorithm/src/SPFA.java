
public class SPFA {
	const int INF = 999999;  

	// 参数n表示结点数，s表示源点  
	int SPFAAlgorithm(int n, int s){
	    int i, pri, end, p, t;		// pri是队列头结点，end是队列尾结点  
	    char vst[]=new char[n];  	//布尔数组记录每个点是否处在队列中
		int  dis[n];
		int map[n][n]; //初始输入的i到j的距离，未知的map[i,j]=INF;maxn为点的个数?
	    for(int i=0; i<n; ++i)  
	        Q[i] = 0;  		//???
        for (i=0; i<n; i++)  	//初始化距离
	        dis[i] = INF;  
	    dis[s] = 0;  
	    vst[s] = 1;  
	    Q[0] = s; pri = 0; end = 1;  
	    while (pri < end){  
        p = Q[pri];
        for (i=0; i<n; ++i){		//更新dis  
            if (dis[p]+map[p][i] < dis[i]){  
	                dis[i] = dis[p]+map[p][i];  
	                if (!vst[i]){       //点i未在队列中则加入队列并标记已加入
                    Q[end++] = i;  
	                vst[i] = 1;  
	                }  
	            }  
        }  
	        vst[p] = 0;   // 置出队的点为未标记  
        pri++;  
	    }  
	    return 1;  
}

}
