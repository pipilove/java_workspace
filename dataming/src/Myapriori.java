import java.util.*;

public class Myapriori{
    double minsup=0.6;		//��С֧�ֶ�
    double minconf=0.2;		//��С���Ŷ�
    int item_counts=0;		//��ѡ1���С
    int frequent_top=1;		//����Ƶ������ＯԪ�ظ�����
    HashMap rule=new HashMap();		//?
    String trans_set[]={"abc","abc","acde","bcdf","abcd","abcdf"};		//����
    TreeSet[] frequent_set=new TreeSet[10];			//Ƶ������飬[0]:����1Ƶ����...
    TreeSet max_frequent=new TreeSet();				//���Ƶ����
    TreeSet item1_candidate=new TreeSet();			//1��ѡ��
    TreeSet candidate_set[]=new TreeSet[10];		//��ѡ������   
   
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
   
    //�����ѡ1�����&������ѡ1�
    int counts(){
                String item_set;//�洢ÿ���
                char item;//�洢ÿ����е���
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
                                //Ԫ����ӵ��� set�������Ԫ����δ������ set �У�������ѡ1�
                        }
                }
        return item1_candidate.size();
    }
   
    //������Ŀ��֧�ֶ�
    double count_sup(String x){
        int counts=0;	//֧�ֶȼ�������ʼΪ0
        for(int i=0;i<trans_set.length;i++){
        	//if(trans_set[i].contains(x))counts++;	//�ַ�������ָ���� char ֵ����ʱ������ true
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
   
    //����Ƶ��1�
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
   
    //��Ƶ��k-1���1Ƶ�������ɺ�ѡk�
    public void candidate_gen(int k){
        String y="",z="";
        char c1='a',c2='a';
        Iterator it1=frequent_set[k-2].iterator();		//Ƶ��k-1��ĵ�����
        Iterator it2=frequent_set[0].iterator();
        TreeSet h=new TreeSet();		//��ѡk�
        while(it1.hasNext()){
            y=(String)it1.next();		//Ϊʲô���Ƿ��ص�������һ��Ԫ�أ���Ҫǿ������ת��������
            c1=y.charAt(y.length()-1);
            //System.out.println("y="+y+"�ֻ�����"+c1);
            while(it2.hasNext()){
                z=(String)it2.next();		//Ϊʲô����ֱ�Ӵӵ���������ת��Ϊchar���󣿣���
                c2=z.charAt(0);   
                //System.out.println("z="+z+"�ֻ�������"+c2);
                if(c1>=c2)
                    continue;		//continue���������
                else
                    h.add(y+z);
            }
            it2=frequent_set[0].iterator();		//ע��������ı仯
        }
        candidate_set[k-1]=h;
    }
   
    //k��ѡ��=>kƵ����
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
   
    //�ж�Ƶ��k��Ƿ�Ϊ��
    public boolean is_frequent_empty(int k){
        if(frequent_set[k-1].isEmpty())
            return true;
        else
            return false;
    }

    //�ж�s2�Ƿ����s1
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
   
    //����frequent_top�������Ƶ������㷨�д��Ľ�//�����ӷ���������ȫ�������ɾ��
    public void maxfrequent_gen(){
        int i,j;
        Iterator iterator,iterator1,iterator2;
        String temp="",it1="",it2="";
        for(i=0;i<frequent_top;i++)
            max_frequent.addAll(frequent_set[i]);		//��ָ�� collection �е�����Ԫ����ӵ��� set ��
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
        System.out.print("���Ƶ�����");
        while(iterator.hasNext()){
            System.out.println((String)iterator.next());
        }
    }
   
    //����max_frequent���Ӽ���
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
                rule.put(x,y);		//put(K key, V value) �ڴ�ӳ���й���ָ��ֵ��ָ����
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
        double confidence=0;		//���Ŷ�
        Set hs=rule.keySet();		//���ش�ӳ�����������ļ��� Set ��ͼ
        Iterator iterator=hs.iterator();
        System.out.println("��������");
        while(iterator.hasNext()){
            x=(String)iterator.next();
            y=(String)rule.get(x);		//����ָ������ӳ���ֵ
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
            System.out.println("��ѡ"+(String)temp.next());
        while(it1.hasNext())
            System.out.println("Ƶ��"+(String)it1.next());
    }
 
    //��ӡ���ѡ���Ƶ����
    void print_candidate(){       
        for(int i=0;i<frequent_set[0].size();i++){
            Iterator ix=candidate_set[i].iterator();
            Iterator iy=frequent_set[i].iterator();
            System.out.print("��ѡ��"+(i+1)+":");
            while(ix.hasNext())
                System.out.print((String)ix.next()+"\t");
            System.out.print("\n"+"Ƶ����"+(i+1)+":");
            while(iy.hasNext())
                System.out.print((String)iy.next()+"\t");
            System.out.println();
        }
    }   



}