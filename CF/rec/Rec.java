package rec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import resource.Ini;
import cfMain.CFExe;

public class Rec {

	public static HashMap<Integer,Double> makeSimMap(Integer targetID){
		File[] f = new File(Ini.outputSim+"/"+Ini.pNUM[CFExe.pat]+"/").listFiles();
		HashMap<Integer,Double> simMap = null;
		for(File ID:f){
			int  name=Integer.parseInt(ID.getName().replace(".txt", ""));


			if(name == targetID){
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ID),"UTF-8"));
					simMap = new HashMap<Integer,Double>();
					String line="";
					while((line = br.readLine())!=null){
						String[] s = line.split("\t");
						int UserID = Integer.parseInt(s[0]);
						double score = Double.parseDouble(s[1]);
						simMap.put(UserID, score);

					}
					br.close();
				}catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				break;
			}

		}

		return simMap;
	}
	
	
	public static void recEngine(Integer targetID,HashMap<Integer,Double > simMap){
		List<Map.Entry<Integer, Double>> sortsimList = new LinkedList<Map.Entry<Integer, Double>>(simMap.entrySet());
		Collections.sort(sortsimList, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				Map.Entry<Integer, Double> entry1 = (Map.Entry<Integer, Double>) o1;
				Map.Entry<Integer, Double> entry2 = (Map.Entry<Integer, Double>) o2;
				Double int1 = entry1.getValue();
				Double int2 = entry2.getValue();
				return int2.compareTo(int1);
			}
		});
//		System.out.println(sortsimList);

		HashMap<Integer,Double> itemScoreMap  = new HashMap<Integer,Double>();
		int cut = sortsimList.size();

		for(int i =0;i<cut;i++){
			int _uID = sortsimList.get(i).getKey();
			double sim = sortsimList.get(i).getValue();
			for(int ItemID: Ini.userMap.get(_uID).keySet()){
				double score = Ini.userMap.get(_uID).get(ItemID);
				if(!itemScoreMap.containsKey(ItemID)){
					itemScoreMap.put(ItemID,sim*score);
				}else{
					itemScoreMap.put(ItemID, itemScoreMap.get(ItemID)+sim*score);
				}
			}

		}

		writer.Output.outputItemList(targetID, itemScoreMap);
		sortsimList.clear();

	}

}
