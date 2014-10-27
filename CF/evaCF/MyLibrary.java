package evaCF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * よく使うメソッド集
 * @author H_Sato
 */
public class MyLibrary {

	/**
	 * 高速版split
	 * @param str splitする文字列
	 * @param delimiter splitのデリミタ
	 * @return split後の文字列配列
	 */
	public static String[] split(String str, String delimiter) {
		StringTokenizer tokenizer = new StringTokenizer(str, delimiter);
		String[] resultStr = new String[tokenizer.countTokens()];

		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			resultStr[i++] = tokenizer.nextToken();
		}
		return resultStr;
	}

	/**
	 * ディレクトリを新規作成する
	 * @param directory 作成するディレクトリパス
	 */
	public static void makeDirectory(String directory) {
		File newfile = new File(directory);
	    if (!newfile.mkdirs()){
	    	System.out.println(directory);
	    	System.out.println("このディレクトリは既存 あるいは ディレクトリの作成に失敗しました.");
	    }
	}

	/**
	 * 実行時間を算出する
	 * @param start 実行開始時間
	 * @return 実行時間情報
	 */
	public static String getExecutionTime(long start) {
		long stop = System.currentTimeMillis();
		long time = (stop - start) / 1000;
		long hour = time / 3600;
		long minute = time % 3600 / 60;
		long second = time % 3600 % 60;
		long millisec = (stop - start) % 1000;
		String executionTimeInformation = "実行時間：  " + hour + " hours " + minute + " minutes " + second + " seconds " + millisec + " milliseconds";
		return executionTimeInformation;
	}

	/**
	 * Mapをvalue(Double)で昇順ソートして返す
	 * @param map ソートするMap
	 * @return ソート済のTreeMap
	 */
	public static Map<String, Double> sortAscendingMap(Map<String, Double> map) {
		List<Map.Entry<String, Double>> entries = new LinkedList<Map.Entry<String, Double>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				Map.Entry<String, Double> entry1 = (Map.Entry<String, Double>) o1;
				Map.Entry<String, Double> entry2 = (Map.Entry<String, Double>) o2;
				Double d1 = entry1.getValue(), d2 = entry2.getValue();

				if(d2 > d1)
					return -1;
				else if(d2 < d1)
					return 1;
				else
					return 0;
			}
		});

		//return用
		Map<String, Double> ranking = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : entries) {
			ranking.put(entry.getKey(), entry.getValue());
		}
		return ranking;
	}

	/**
	 * Mapをvalue(Double)で降順ソートして返す
	 * @param map ソートするMap
	 * @return ソート済のTreeMap
	 */
	public static Map<String, Double> sortDescendingMap(Map<String, Double> map) {
		List<Map.Entry<String, Double>> entries = new LinkedList<Map.Entry<String, Double>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				Map.Entry<String, Double> entry1 = (Map.Entry<String, Double>) o1;
				Map.Entry<String, Double> entry2 = (Map.Entry<String, Double>) o2;
				Double d1 = entry1.getValue(), d2 = entry2.getValue();

				if(d2 > d1)
					return 1;
				else if(d2 < d1)
					return -1;
				else
					return 0;
			}
		});

		//return用
		Map<String, Double> ranking = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : entries) {
			ranking.put(entry.getKey(), entry.getValue());
		}
		return ranking;
	}

	/**
	 * Map(String, Double)をファイルに出力(形式：key[split]value[\r\n])
	 * @param Map 出力するMap
	 * @param directory ディレクトリ
	 * @param filename ファイルネーム
	 * @param split 1行をStringで分割する
	 */
	public static void outFile(Map<String, Double> Map, String directory, String filename, String split, String charaCode) {
		try {
			FileOutputStream fos = new FileOutputStream(directory + filename);
			OutputStreamWriter osw = new OutputStreamWriter(fos, charaCode);
			BufferedWriter bw = new BufferedWriter(osw);

			String temp = "";
			for(Map.Entry<String, Double> ent: Map.entrySet()) {
				String key = ent.getKey();
				Double value = ent.getValue();
				temp = key + split + value.doubleValue() + "\r\n";
				bw.write(temp);
			}

			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
