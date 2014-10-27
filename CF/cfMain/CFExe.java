package cfMain;

import java.util.Date;
import java.util.HashMap;

import reader.ResReader;
import rec.Rec;
import resource.Ini;
import cal.CosSim;

public class CFExe {

	public static int pat;
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		

		for(int i = 0;i<Ini.Pattern;i++){
			System.err.println("---------Read User Log--------");
			System.out.println("["+new Date().toString()+"]");
			ResReader rr = new ResReader();
			rr.executor(Ini.pNUM[i]);
			System.out.println("["+new Date().toString()+"]");
			
			//TargetLogの読み込み
			System.out.println("------Target Read : Pattern"+i+"------");
			System.out.println("["+new Date().toString()+"]");
			pat = i;
			rr.targetReader(Ini.pNUM[i]);
			System.out.println("["+new Date().toString()+"]");


			//ユーザー間類似度
			System.out.println("---------Start CalSim----------");
			System.out.println("["+new Date().toString()+"]");
			CosSim task = new CosSim();
			task.executor();
			System.out.println("["+new Date().toString()+"]");

			//ItemScoreの計算/推薦
			System.out.println("---------Start Rec----------");
			System.out.println("["+new Date().toString()+"]");
			for(Integer _tID:Ini.targetMap.keySet()){
				System.out.println("targetID:"+_tID);
				HashMap<Integer,Double> simMap = new HashMap<Integer,Double>();
				simMap = Rec.makeSimMap(_tID);
				Rec.recEngine(_tID, simMap);
				simMap.clear();
			}
			System.out.println("["+new Date().toString()+"]");
			Ini.targetMap.clear();
			System.gc();
		}
		System.err.println("--------Rec Finish---------");
	}

}
