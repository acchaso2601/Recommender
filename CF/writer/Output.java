package writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import resource.Ini;
import cfMain.CFExe;

public class Output {

	public static void outputSimList(Integer UserID,List<Entry<Integer, Double>> simList){
		String outputPath = Ini.outputSim;
		File f = new File(outputPath);
		f.mkdir();
		File dir = new File(outputPath+"/"+Ini.pNUM[CFExe.pat]+"/");
		dir.mkdir();
		File result = new File(dir,String.format("%05d", UserID)+".txt");
		try {
			result.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result),"UTF-8"));
			int cut = simList.size();
//			System.err.println(cut);
			if(cut>Ini.cutNum)cut=Ini.cutNum;
			for(int i = 0;i<cut;i++){


				bw.write(simList.get(i).getKey()+"\t"+simList.get(i).getValue()+"\n");

			}
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public static void outputItemList(Integer targetID ,HashMap<Integer,Double>itemScoreMap){
		List<Map.Entry<Integer, Double>> sortItemList = new LinkedList<Map.Entry<Integer, Double>>(itemScoreMap.entrySet());
		Collections.sort(sortItemList, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				Map.Entry<Integer, Double> entry1 = (Map.Entry<Integer, Double>) o1;
				Map.Entry<Integer, Double> entry2 = (Map.Entry<Integer, Double>) o2;
				Double int1 = entry1.getValue();
				Double int2 = entry2.getValue();
				return int2.compareTo(int1);
			}
		});
		String outputPath = Ini.outputItemList;
		File f = new File(outputPath);
		f.mkdir();
		File dir = new File(outputPath+"/"+Ini.pNUM[CFExe.pat]+"/");
		dir.mkdir();
		File result = new File(dir,String.format("%05d", targetID)+".txt");
		try {
			result.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result),"UTF-8"));
			for(int i = 0;i<sortItemList.size();i++){

				if(Ini.rep==1){
					bw.write(sortItemList.get(i).getKey()+"\t"+sortItemList.get(i).getValue()+"\n");
				}
				else{
					if(!Ini.targetMap.get(targetID).containsKey(sortItemList.get(i).getKey())){

						//System.out.println(sortItemList.get(i).getKey());
						bw.write(sortItemList.get(i).getKey()+"\t"+sortItemList.get(i).getValue()+"\n");
					}
				}

			}
//			if(targetID==0){
//			System.out.println(targetID+"\t"+Ini.targetMap.get(targetID).size());
//			System.out.println(Ini.targetMap.get(targetID));
//			}
		bw.close();
		} catch (IOException e){
			e.printStackTrace();
		}

	}


}
