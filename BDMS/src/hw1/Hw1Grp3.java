package hw1;
/*
 * Make sure that the classpath contains all the hbase libraries
 *
 * Compile:
 *  javac HBaseTest.java
 *
 * Run: 
 *  java HBaseTest
 */
import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.CollationKey;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.log4j.*;

public class Hw1Grp3 {

	/**
	 * @param args
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws MasterNotRunningException,
			ZooKeeperConnectionException, IOException {
		Logger.getRootLogger().setLevel(Level.WARN);

		/**
		 * Parse the command : e.g. java Hw1GrpX R=<file> groupby:R2
		 * res:count,avg(R3),max(R4)
		 */
		String hdfs_filename = args[0].substring(2);// /hw1/orders.tbl
		final int group_col_no = Integer.valueOf(args[1].split(":R")[1]);// Group
																			// by
		// key
		String cf_name = args[2].split(":")[0];// column family:res
		String rs[] = args[2].split(":")[1].split(",");

		/**
		 * read data from hdfs
		 */
		String DIR = "hdfs://localhost:9000";// /hw1/README.txt
		// String file = Paths.get(DIR, hdfs_filename).toString();
		String file = DIR + hdfs_filename;

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(file), conf);
		Path path = new Path(file);
		FSDataInputStream in_stream = fs.open(path);
		BufferedReader in = new BufferedReader(new InputStreamReader(in_stream));

		final ArrayList<ArrayList<String>> LineColList = new ArrayList<ArrayList<String>>();
		String line;
		while ((line = in.readLine()) != null) {
			// System.out.println(line);
			// order.tbl :
			// 7|862|F|131896.49|1992-06-03|3-MEDIUM|Clerk#000000456|0|kly
			// regular pinto beans. carefully unusual water cajole never|
			ArrayList<String> Cols = new ArrayList<String>();
			for (String s : line.split("\\|"))
				Cols.add(s);
			LineColList.add(Cols);
		}
		in.close();
		fs.close();

		/**
		 * HBase configuration
		 */
		// create table descriptor
		String tableName = "Result";
		HTableDescriptor htd = new HTableDescriptor(
				TableName.valueOf(tableName));

		// create column descriptor
		HColumnDescriptor cf = new HColumnDescriptor(cf_name);
		htd.addFamily(cf);

		// configure HBase
		Configuration configuration = HBaseConfiguration.create();
		HBaseAdmin hAdmin = new HBaseAdmin(configuration);
		if (hAdmin.tableExists(tableName)) {
			System.out.println("Table already exists");
		} else {
			hAdmin.createTable(htd);
			System.out.println("table " + tableName + " created successfully");
		}
		hAdmin.close();

		HTable table = new HTable(configuration, tableName);

		/**
		 * Comparator for Group-by sorting
		 */
		class DataComparator implements Comparator {
			Collator collator = Collator.getInstance();// solve chinese sort

			public int compare(Object o1, Object o2) {
				@SuppressWarnings("unchecked")
				ArrayList<String> l1 = (ArrayList<String>) o1;
				@SuppressWarnings("unchecked")
				ArrayList<String> l2 = (ArrayList<String>) o2;
				final String g1 = (String) l1.get(group_col_no);
				final String g2 = (String) l2.get(group_col_no);
				CollationKey k1 = collator.getCollationKey(g1);
				CollationKey k2 = collator.getCollationKey(g2);
				return k1.compareTo(k2);
			}
		}
		;
		Collections.sort(LineColList, new DataComparator());
		// System.out.println("sorted linecollist");
		// for(ArrayList<String> s:LineColList)
		// System.out.println(s);

		/**
		 * 
		 * show count function for group-by
		 * 
		 * @author pi
		 *
		 */
		class GroupCounter {
			public HashMap<String, String> count() {
				HashMap<String, String> hm = new HashMap<String, String>();
				String old_col = (String) LineColList.get(0).get(group_col_no);
				Iterator<ArrayList<String>> it = LineColList.iterator();
				for (String col = (String) it.next().get(group_col_no);;) {
					int count_tmp = 0;
					while (col.equals(old_col)) {
						count_tmp++;
						if (!it.hasNext()) {
							col = null;
							break;
						}
						col = (String) (it.next().get(group_col_no));
					}
					// System.out.println(old_col + ": count="
					// + String.valueOf(count_tmp));
					hm.put(old_col, "count=" + String.valueOf(count_tmp));
					if (col == null)
						break;
					old_col = col;
				}
				return hm;
			}
		}
		;

		/**
		 * 
		 * show average function for group-by
		 * 
		 * @author pi
		 *
		 */
		class GroupAvg {
			public HashMap<String, String> avg(int col_index) {
				HashMap<String, String> hm = new HashMap<String, String>();
				ArrayList<Integer> count1;
				String old_col1 = (String) LineColList.get(0).get(group_col_no);
				String col1 = (String) LineColList.get(0).get(group_col_no);
				Iterator<ArrayList<String>> it = LineColList.iterator();
				for (ArrayList<String> al_tmp = it.next();;) {
					double avg_ri_group = 0.0;
					int count_tmp = 0;
					while (col1.equals(old_col1)) {
						count_tmp++;
						avg_ri_group += Double.valueOf((String) al_tmp
								.get(col_index));
						if (!it.hasNext()) {
							al_tmp = null;
							break;
						}
						al_tmp = it.next();
						col1 = (String) al_tmp.get(group_col_no);
					}
					// System.out.println(old_col1
					// + "avg(R"
					// + String.valueOf(col_index)
					// + ")="
					// + new DecimalFormat("######0.00")
					// .format(avg_ri_group / count_tmp));
					hm.put(old_col1,
							"avg(R"
									+ String.valueOf(col_index)
									+ ")="
									+ new DecimalFormat("######0.00")
											.format(avg_ri_group / count_tmp));
					if (al_tmp == null)
						break;
					old_col1 = col1;
				}
				return hm;
			}
		}
		;

		/**
		 * 
		 * show get group max function for group-by
		 * 
		 * @author pi
		 *
		 */
		class GroupMax {
			public HashMap<String, String> max(int col_index) {
				HashMap<String, String> hm = new HashMap<String, String>();
				String old_col2 = (String) LineColList.get(0).get(group_col_no);
				String col2 = (String) LineColList.get(0).get(group_col_no);
				Iterator<ArrayList<String>> it = LineColList.iterator();
				for (ArrayList<String> al_tmp = it.next();;) {
					try {
						double max = Double.MIN_VALUE;
						while (col2.equals(old_col2)) {
							double max_tmp = Double.valueOf((String) al_tmp
									.get(col_index));
							if (max < max_tmp)
								max = max_tmp;
							if (!it.hasNext()) {
								al_tmp = null;
								break;
							}
							al_tmp = it.next();
							col2 = (String) al_tmp.get(group_col_no);
							// System.out.println(old_col2 + "max(R"
							// + String.valueOf(col_index) + ")="
							// + String.valueOf(max));
						}
						hm.put(old_col2, "max(R" + String.valueOf(col_index)
								+ ")=" + String.valueOf(max));
					} catch (Exception e) {
						String max = (String) al_tmp.get(col_index);
						while (col2.equals(old_col2)) {
							String max_tmp = (String) al_tmp.get(col_index);
							if (max.compareTo(max_tmp) < 0)
								max = max_tmp;
							if (!it.hasNext())
								break;
							al_tmp = it.next();
							col2 = (String) al_tmp.get(group_col_no);
						}
						// System.out.println(old_col2 + "max(R"
						// + String.valueOf(col_index) + ")="
						// + String.valueOf(max));
						hm.put(old_col2, "max(R" + String.valueOf(col_index)
								+ ")=" + String.valueOf(max));
					}
					if (al_tmp == null)
						break;
					old_col2 = col2;
				}
				return hm;
			}
		}
		;

		/**
		 * 
		 * show get group min function for group-by
		 * 
		 * @author pi
		 *
		 */
		class GroupMin {
			public HashMap<String, String> min(int col_index) {
				HashMap<String, String> hm = new HashMap<String, String>();
				String old_col3 = (String) LineColList.get(0).get(group_col_no);
				String col3 = (String) LineColList.get(0).get(group_col_no);
				Iterator<ArrayList<String>> it = LineColList.iterator();
				for (ArrayList<String> al_tmp = it.next();;) {
					try {
						double min = Double.MAX_VALUE;
						while (col3.equals(old_col3)) {
							double min_tmp = Double.valueOf((String) al_tmp
									.get(col_index));
							if (min > min_tmp)
								min = min_tmp;
							if (!it.hasNext()) {
								al_tmp = null;
								break;
							}
							al_tmp = it.next();
							col3 = (String) al_tmp.get(group_col_no);
						}
						// System.out.println(old_col3 + "min(R"
						// + String.valueOf(col_index) + ")="
						// + String.valueOf(min));
						hm.put(old_col3, "min(R" + String.valueOf(col_index)
								+ ")=" + String.valueOf(min));
					} catch (Exception e) {
						String min = (String) al_tmp.get(col_index);
						while (col3.equals(old_col3)) {
							String min_tmp = (String) al_tmp.get(col_index);
							if (min.compareTo(min_tmp) > 0)
								min = min_tmp;
							if (!it.hasNext())
								break;
							al_tmp = it.next();
							col3 = (String) al_tmp.get(group_col_no);
						}
						// System.out.println(old_col3 + "min(R"
						// + String.valueOf(col_index) + ")="
						// + String.valueOf(min));
						hm.put(old_col3, "min(R" + String.valueOf(col_index)
								+ ")=" + String.valueOf(min));
					}
					if (al_tmp == null)
						break;
					old_col3 = col3;
				}
				return hm;
			}
		}
		;

		/**
		 * handle the res calculation
		 */
		HashMap<String, ArrayList<String>> result_hm = new HashMap<String, ArrayList<String>>();
		for (String r : rs) { // r is avg(R3)
			int col_index = 0;
			String cmd = null;
			Pattern pattern = Pattern.compile("(\\w+)(\\(R)*(\\d)*\\)*");
			Matcher matcher = pattern.matcher(r);
			if (matcher.find()) {
				cmd = matcher.group(1);
				if ((matcher.group(3)) != null)
					col_index = Integer.valueOf(matcher.group(3));
				// System.out.println(cmd);
				// System.out.println(col_index);
			}

			switch (cmd) {
			case "count":
				HashMap<String, String> hm = (new GroupCounter()).count();
				for (String k : hm.keySet()) {
					ArrayList<String> as = new ArrayList<String>();
					if (result_hm.get(k) != null)
						as = result_hm.get(k);
					as.add(hm.get(k));
					result_hm.put(k, as);
				}
				break;

			case "avg":
				HashMap<String, String> hm1 = (new GroupAvg()).avg(col_index);
				for (String k : hm1.keySet()) {
					ArrayList<String> as = new ArrayList<String>();
					if (result_hm.get(k) != null)
						as = result_hm.get(k);
					as.add(hm1.get(k));
					result_hm.put(k, as);
				}
				break;

			case "max":
				HashMap<String, String> hm2 = (new GroupMax()).max(col_index);
				for (String k : hm2.keySet()) {
					ArrayList<String> as = new ArrayList<String>();
					if (result_hm.get(k) != null)
						as = result_hm.get(k);
					as.add(hm2.get(k));
					result_hm.put(k, as);
				}
				break;

			case "min":
				HashMap<String, String> hm3 = (new GroupMin()).min(col_index);
				for (String k : hm3.keySet()) {
					ArrayList<String> as = new ArrayList<String>();
					if (result_hm.get(k) != null)
						as = result_hm.get(k);
					as.add(hm3.get(k));
					result_hm.put(k, as);
				}
				break;

			default:
				System.out.println("command not found!!!");
				break;
			}

		}

		/**
		 * put all the result data into hbase (row key=abc, res:count=3)
		 */
		for (String row_key : result_hm.keySet()) {
			for (String re : result_hm.get(row_key)) {
				Put put = new Put((row_key).getBytes());
				put.add(cf_name.getBytes(), re.split("=")[0].getBytes(),
						re.split("=")[1].getBytes());
				table.put(put);
			}
		}
		table.close();
		System.out.println("put successfully");
	}
}
