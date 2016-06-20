import java.util.*;

public class Myapriori{
    double minsup=0.6;		//最小支持度
    double minconf=0.2;		//最小置信度
    int item_counts=0;		//候选1项集大小
    int frequent_top=1;		//最大的频繁项集事物集元素个数？
    HashMap rule=new HashMap();		//?
    String trans_set[]={"abc","abc","acde","bcdf","abcd","abcdf"};		//事务集
    TreeSet[] frequent_set=new TreeSet[10];			//频繁项集数组，[0]:代表1频繁集...
    TreeSet max_frequent=new TreeSet();				//最大频繁集
    TreeSet item1_candidate=new TreeSet();			//1候选集
    TreeSet candidate_set[]=new TreeSet[10];		//候选集数组   
   
    public Myapriori(){
        item_counts=counts();
       // frequent_set=new TreeSet[3];
       // frequent_set=new TreeSet[item_counts];
        for(int i=0;i<item_counts;i++){   
            frequent_set[i]=new TreeSet();
            candidate_set[i]=new TreeSet();
        }
        //System.out.println(frequent_set[0].size());
        candidate_set[0]=item1_candidate;
    }
   
    //计算候选1项集个数&产生候选1项集
    int counts(){
                String item_set;//存储每个项集
                char item;//存储每个项集中的项
                int i,j;
                for(i=0;i<trans_set.length;i++){
                        item_set=trans_set[i];
                        //System.out.println(item_set);
                        //System.out.println(item_set.length());
                        for(j=0;j<item_set.length();j++){
                                item=item_set.charAt(j);
                                //System.out.println(i);
                                //System.out.println(j);
                                //System.out.println(item);
                                item1_candidate.add(String.valueOf(item));
                                //元素添加到此 set（如果该元素尚未存在于 set 中）产生候选1项集
                        }
                }
        return item1_candidate.size();
    }
   
    //计算项目集支持度
    double count_sup(String x){
        int counts=0;	//支持度计数，初始为0
        for(int i=0;i<trans_set.length;i++){
        	//if(trans_set[i].contains(x))counts++;	//字符串包含指定的 char 值序列时，返回 true
            for(int j=0;j<x.length();j++){
                if(trans_set[i].indexOf(x.charAt(j))==0)
                    break;
                else
                    if(j==(x.length()-1))
                    	counts++;
            }
        }
        return counts;
    }
   
    //构造频繁1项集
    public void item1_gen(){
        String str="";
        double m=0;
        Iterator it=candidate_set[0].iterator();
        while(it.hasNext()){
            str=(String)it.next();
            m=count_sup(str);
            if(m>=minsup*trans_set.length){
                frequent_set[0].add(str);
            }   
        }
    }
   
    //由频繁k-1项集和1频繁集生成候选k项集
    public void candidate_gen(int k){
        String y="",z="";
        char c1='a',c2='a';
        Iterator it1=frequent_set[k-2].iterator();		//频繁k-1项集的迭代器
        Iterator it2=frequent_set[0].iterator();
        TreeSet h=new TreeSet();		//候选k项集
        while(it1.hasNext()){
            y=(String)it1.next();		//为什么不是返回迭代的下一个元素，而要强制类型转换？？？
            c1=y.charAt(y.length()-1);
            //System.out.println("y="+y+"手机给我"+c1);
            while(it2.hasNext()){
                z=(String)it2.next();		//为什么不能直接从迭代器对象转换为char对象？？？
                c2=z.charAt(0);   
                //System.out.println("z="+z+"手机还给我"+c2);
                if(c1>=c2)
                    continue;		//continue到哪里？？？
                else
                    h.add(y+z);
            }
            it2=frequent_set[0].iterator();		//注意迭代器的变化
        }
        candidate_set[k-1]=h;
    }
   
    //k候选集=>k频繁集
    public void frequent_gen(int k){
        String s1="";
        Iterator it=candidate_set[k-1].iterator();
        while(it.hasNext()){
            s1=(String)it.next();
            if(count_sup(s1)>=(minsup*trans_set.length)){
                frequent_set[k-1].add(s1);
            }
        }
    }
   
    //判断频繁k项集是否为空
    public boolean is_frequent_empty(int k){
        if(frequent_set[k-1].isEmpty())
            return true;
        else
            return false;
    }

    //判断s2是否包含s1
    public boolean included(String s1,String s2){
        for(int i=0;i<s1.length();i++){
            if(s2.indexOf(s1.charAt(i))==-1)
                return false;
            else
                if(i==s1.length()-1)
                    return true;
        }
        return true;
    }
   
    //根据frequent_top产生最大频繁项集，算法有待改进//逐个添加法，而不是全部添加再删除
    public void maxfrequent_gen(){
        int i,j;
        Iterator iterator,iterator1,iterator2;
        String temp="",it1="",it2="";
        for(i=0;i<frequent_top;i++)
            max_frequent.addAll(frequent_set[i]);		//将指定 collection 中的所有元素添加到此 set 中
        for(i=0;i<frequent_top;i++){
            iterator1=frequent_set[i].iterator();
            while(iterator1.hasNext()){
                it1=(String)iterator1.next();
                for(j=i+1;j<frequent_top;j++){
                    //System.out.println("i="+i+"\tj="+j);
                    //System.out.println(s[j+1]);
                    iterator2=frequent_set[j].iterator();
                    while(iterator2.hasNext()){
                        it2=(String)iterator2.next();
                        if(included(it1,it2))
                            max_frequent.remove(it1);
                    }
                }   
            }   
        }
    }

    public void print_maxfrequent(){
        Iterator iterator=max_frequent.iterator();
        System.out.print("最大频繁项集：");
        while(iterator.hasNext()){
            System.out.println((String)iterator.next());
        }
    }
   
    //产生max_frequent的子集？
    public void subGen(String s){
        String x="",y="";
        for(int i=1;i< (1<<s.length())-1;i++){		//?
            for(int j=0;j<s.length();j++){
                if(((1<<j)&i)!=0){
                    //System.out.print(s.charAt(j));
                    x+=s.charAt(j);
                }
            }
            //System.out.print("\t");
            for(int j=0;j<s.length();j++){
                if(((1<<j)&(~i))!=0){
                    //System.out.print(s.charAt(j));
                    y+=s.charAt(j);
                //System.out.println(x.trim()+"\t"+y.trim());
                }
            }   
            //System.out.println(x+"\t"+y);
            if(count_sup(x+y)/count_sup(x)>=minconf){
                rule.put(x,y);		//put(K key, V value) 在此映射中关联指定值与指定键
            }
            x="";y="";
           
        }
    }
   
    public void ruleGen(){
        String s;
        Iterator iterator=max_frequent.iterator();
        while(iterator.hasNext()){
            s=(String)iterator.next();
            subGen(s);
        }
    }
   
    public void rulePrint(){
        String x,y;
        double confidence=0;		//置信度
        Set hs=rule.keySet();		//返回此映射中所包含的键的 Set 视图
        Iterator iterator=hs.iterator();
        System.out.println("关联规则：");
        while(iterator.hasNext()){
            x=(String)iterator.next();
            y=(String)rule.get(x);		//返回指定键所映射的值
            confidence=(count_sup(x+y)/count_sup(x));
            System.out.println(x+"==>"+y+"\t"+confidence);
        }
    }
   
    public void run(){
                //Myapriori run=new Myapriori();
                item1_gen();
                do{
                	frequent_top++;
                    candidate_gen(frequent_top);
                    frequent_gen(frequent_top);
                }while(!is_frequent_empty(frequent_top));
                frequent_top--;
        print_candidate();
        maxfrequent_gen();
        print_maxfrequent();
        ruleGen();
        rulePrint();
   
    }
   
    public static void main(String [] args){
        Myapriori x=new Myapriori();
        x.run();
    }

    //for test
    void print1(){
        Iterator temp=candidate_set[0].iterator();
        while(temp.hasNext())
            System.out.println(temp.next());   
    }
    //for test
    void print2(){
        Iterator temp=frequent_set[0].iterator();
        while(temp.hasNext())
            System.out.println((String)temp.next());
    }
    //for test
    void print3(){
        candidate_gen(1);
        frequent_gen(2);
        Iterator temp=candidate_set[1].iterator();
        Iterator it1=frequent_set[1].iterator();
        while(temp.hasNext())
            System.out.println("候选"+(String)temp.next());
        while(it1.hasNext())
            System.out.println("频繁"+(String)it1.next());
    }
 
    //打印后候选项集和频繁集
    void print_candidate(){       
        for(int i=0;i<frequent_set[0].size();i++){
            Iterator ix=candidate_set[i].iterator();
            Iterator iy=frequent_set[i].iterator();
            System.out.print("候选集"+(i+1)+":");
            while(ix.hasNext())
                System.out.print((String)ix.next()+"\t");
            System.out.print("\n"+"频繁集"+(i+1)+":");
            while(iy.hasNext())
                System.out.print((String)iy.next()+"\t");
            System.out.println();
        }
    }   



}