import java.util.HashMap; 
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;


public class Apriori {
	  private Map<Integer,Set<String>>	txDatabase;
	 
	  private Float minSup;
	  private Float minConf;
	  private Integer txDatabaseCount;
	 
	  private Map<Integer,Set<Set<String>>>	freqItemSet; //频繁项集集合
	  private Map<Set<String>,Set<Set<String>>>	assiciationRules; //频繁关联规则集合

	private Object getFreq1ItemSet;
	  
	  //构造函数初始化
	  public APRIORI(Map<Integer , Set<String>> txDatabase, Float minSup,Float minConf )
	  {
		  this.txDatabase = txDatabase;
		  this.minSup = minSup;
		  this.minConf = minConf;
		  this.txDatabaseCount = this.txDatabase.size();
		  freqItemSet = new TreeMap<Integer,Set<Set<String>>>();
		  assiciationRules = new HashMap<Set<String>,Set<Set<String>>>();
	  }
	  public Map<Set<String>, Float> getFreq1ItemSet() 
	  {
		 Map<Set<String>, Float> freq1ItemSetMap = new HashMap<Set<String>, Float>();
		 Map<Set<String>, Float> candFreq1ItemSet = this.getFreq1ItemSet();
		 Iterator<Entry<Set<String>, Float>> it = candFreq1ItemSet.entrySet().iterator();
		 while(it.hasNext())
		 {
			 Entry<Set<String>, Float> entry = it.next();
			 //.toString 不是很理解
			 Float supported = new Float(entry.getValue().toString())/ new Float(txDatabaseCount);
			 if(supported >=minSup)
				 freq1ItemSetMap.put(entry.getKey(),supported);
		 }
		 return freq1ItemSetMap;
			
	 }
		 
	  
	 public Map<Set<String>, Integer>  getCandFreq1ItemSet()
	 {
		Map<Set<String>, Integer> candFreq1ItemSetMap = new HashMap<Set<String>,Integer>();
		Iterator<Map.Entry<Integer, Set<String>>> it = txDatabase.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer,Set<String>> entry = it.next();
			Set<String> itemSet = entry.getValue();
			for(String item: itemSet)
			{
				Set<String> key = new HashSet<String>();
				key.add(item.trim());
				if(!candFreq1ItemSetMap.containsKey(key)) 
	 	 		{
      	 			Integer value = 1;
         			candFreq1ItemSetMap.put(key, value);
    		 	}
    		    else 
    		    {
      				Integer value = 1+candFreq1ItemSetMap.get(key);
      				candFreq1ItemSetMap.put(key, value);
     	 		}
    		}
  		 }
  		 return candFreq1ItemSetMap;
	}

	private Iterator<Set<String>> getIterator(Set<String> itemSet, Set<Set<String>> freqkItemSet)
	{
		Iterator<Set<String>> it = freqkItemSet.iterator();
		while(it.hasNext())
		{
			if(itemSet.equals(it.next()))
				break;
		}
		return it;
	}
	 
	 
	public Set<Set<String>> aprioriGen(int m, Set<Set<String>> freqMItemSet)
	{
		Set<Set<String>> candFreqKItemSet = new HashSet<Set<String>>();
   		Iterator<Set<String>> it = freqMItemSet.iterator();
   		Set<String> originalItemSet = null;
   		while(it.hasNext())
   		{
   			originalItemSet = it.next();
   			Iterator<Set<String>> itr = this.getIterator(originalItemSet,freqMItemSet);
   			while(itr.hasNext())
   			{
   				Set<String> identicalSet = new HashSet<String>();
   				identicalSet.addAll(originalItemSet);
   				Set<String> set = itr.next();
   				identicalSet.retainAll(set);
   				if(identicalSet.size()== m-1);
   				{
   					Set<String> differentSet = new HashSet<String>();
   					differentSet.addAll(originalItemSet);
   					differentSet.removeAll(set);
   					differentSet.addAll(set);
   					candFreqKItemSet.add(differentSet);
   					
   				}
   			}
   			
   		}
   		
		 return candFreqKItemSet;
	}
	
	public Map<Set<String>, Float> support(Map<Set<String>, Integer> candFreqKItemSetMap) 
	{
		Map<Set<String>, Float> freqKItemSetMap = new HashMap<Set<String>,Float>();
		Iterator<Map.Entry<Set<String>, Integer>> it = candFreqKItemSetMap.entrySet().iterator();
		while(it.hasNext()) 
		{
			Map.Entry<Set<String>, Integer> entry = it.next();
			
			Float supportRate = new Float(entry.getValue().toString())/ new Float (txDatabaseCount);
			if(supportRate < minSup)
			{
				it.remove();
				
			}
			else 
			{
				freqKItemSetMap.put(entry.getKey(),supportRate);
			}
			
		}
		return freqKItemSetMap;
	}
	
	public Map<Set<String>,Float> getFreqKItemSet(int k,Set<Set<String>> freqMItemSet)
	
	{
		Map<Set<String>, Integer> candFreqKItemSetMap = new HashMap<Set<String>, Integer>();
		
		Set<Set<String>> candFreqKItemSet = this.aprioriGen(k-1, freqMItemSet);
		
		Iterator<Map.Entry<Integer,Set<String>>> it = txDatabase.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry<Integer,Set<String>> entry = it.next();
			Iterator<Set<String>> kit = candFreqKItemSet.iterator();
			
			while(kit.hasNext())
			{
				Set<String> kSet = kit.next();
				Set<String> set = new HashSet<String>();
				set.addAll(kSet);
				set.removeAll(entry.getValue());
				if(set.isEmpty())
				{
					if(candFreqKItemSetMap.get(kSet) == null) 
					{
       						Integer value = 1;
       						candFreqKItemSetMap.put(kSet, value);
					}
      					else 
					{
       						Integer value = 1+candFreqKItemSetMap.get(kSet);
       						candFreqKItemSetMap.put(kSet, value);
      					}
     				}
   			 }
  		 }  
		 return support(candFreqKItemSetMap);
	}
	
			
	public void mineFreqItemSet()
	{
		Set<Set<String>>  freqKItemSet = this.getFreq1ItemSet().keySet();
		freqItemSet.put(1,freqKItemSet);
		
		int k = 2;
		while(true)
		{
			Map<Set<String>, Float> freqKItemSetMap = this.getFreqKItemSet(k, freqKItemSet);
			
			if(!freqKItemSetMap.isEmpty()) 
			{
    				 this.freqItemSet.put(k, freqKItemSetMap.keySet());
     				 freqKItemSet = freqKItemSetMap.keySet();
   		 	}
    		else 
			{
     				break;
    		}
    		k++;
			
		}
	}
	
	public void mineAssociationRules()
	{
		freqItemSet.remove(1);
		Iterator<Map.Entry<Integer,Set<Set<String>>>> it = freqItemSet.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer, Set<Set<String>>> entry =it.next();
			for(Set<String> itemSet: entry.getValue())
			{
				mine(itemSet);
			}
			
		}
		
	}
	
	public void mine(Set<String> itemSet)
	{
		int n = itemSet.size()/2;
		for(int i=1; i <=n; i ++)
		{
			Set<Set<String>> properSubset = ProperSubsetCombination.getProperSubset(i, itemSet);
			
			for(Set<String> conditionSet:properSubset)
			{
					Set<String> conclusionSet = new HashSet<String>();
					conclusionSet.addAll(itemSet);
					conclusionSet.removeAll(conditionSet);
					confide(conditionSet,conclusionSet);
					
			}
		}
	}
	public void confide(Set<String> conditionSet,Set<String> conclusionSet)
	{
		Iterator<Map.Entry<Integer, Set<String>>> it = txDatabase.entrySet().iterator();
		int conditionToConclusionCnt = 0; // 关联规则(条件项集推出结论项集)计数
	   	int conclusionToConditionCnt = 0; // 关联规则(结论项集推出条件项集)计数
	   	int supCnt = 0;
	   	while(it.hasNext())
	   	{
	   		Map.Entry<Integer, Set<String>> entry = it.next();
	   		Set<String> txSet = entry.getValue();
    		Set<String> set1 = new HashSet<String>();
    		Set<String> set2 = new HashSet<String>();
    		
	   	}
	}

}
