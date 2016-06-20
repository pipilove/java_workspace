package fim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nlp.Topic;
import nlp.Topics;
import utility.ItemWithValue;

/**
 * The transactions for frequent itemset mining.
 * 
 * Each transaction is a list of word string that ranks on the top of a topic.
 */
public class Transactions {
	public ArrayList<ArrayList<Integer>> transactionList = null;
	public Map<String, Integer> mpWordToItem = null;
	public Map<Integer, String> mpItemToWord = null;
	public Map<Integer, Integer> mpItemToCount = null;

	public Transactions() {
		transactionList = new ArrayList<ArrayList<Integer>>();
		mpWordToItem = new HashMap<String, Integer>();
		mpItemToWord = new HashMap<Integer, String>();
		mpItemToCount = new HashMap<Integer, Integer>();
	}

	/**
	 * Convert topics into transactions. Each transaction is a list of top words
	 * under a topic.
	 */
	public Transactions(Topics topics) {
		transactionList = new ArrayList<ArrayList<Integer>>();
		mpWordToItem = new HashMap<String, Integer>();
		mpItemToWord = new HashMap<Integer, String>();
		mpItemToCount = new HashMap<Integer, Integer>();

		for (Topic topic : topics) {
			ArrayList<Integer> transaction = new ArrayList<Integer>();
			for (ItemWithValue iwv : topic.topWordList) {
				String word = iwv.getIterm().toString();
				if (!mpWordToItem.containsKey(word)) {
					mpWordToItem.put(word, mpWordToItem.size());
				}
				int id = mpWordToItem.get(word);
				if (!mpItemToWord.containsKey(id)) {
					mpItemToWord.put(id, word);
				}
				transaction.add(id);
			}
			// We need to sort the transaction in order to have a more efficient
			// itemset look up algorithm.
			// Note that in MS-Apriori, each transaction will be re-sorted by
			// MIS.
			Collections.sort(transaction);
			this.addTransaction(transaction);
		}
	}

	public void addTransaction(ArrayList<Integer> transaction) {
		transactionList.add(transaction);
		for (int id : transaction) {
			if (!mpItemToCount.containsKey(id)) {
				mpItemToCount.put(id, 0);
			}
			mpItemToCount.put(id, mpItemToCount.get(id) + 1);
		}
	}

	public int size() {
		return transactionList.size();
	}

	@Override
	public String toString() {
		StringBuilder sbTransactions = new StringBuilder();
		for (ArrayList<Integer> transaction : transactionList) {
			StringBuilder sbLine = new StringBuilder();
			for (int id : transaction) {
				String word = mpItemToWord.get(id);
				sbLine.append(word);
				sbLine.append(' ');
			}
			sbTransactions.append(sbLine.toString().trim());
			sbTransactions.append(System.getProperty("line.separator"));
		}
		return sbTransactions.toString();
	}
}
