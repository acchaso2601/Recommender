package cal;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import resource.Ini;
public class CosSim {


	public double inner(ConcurrentHashMap<Integer,Double> mapA,ConcurrentHashMap<Integer,Double> mapB){
		double n=0.0;

		Set<Integer> set = mapA.keySet();
		Iterator<Integer> iter =set.iterator();
		while(iter.hasNext()){
			Integer key = iter.next();
			if(mapB.containsKey(key)){
				n +=(double)(mapA.get(key)*mapB.get(key));
			}

		}
		return n;
	}

	public double norm(ConcurrentHashMap<Integer,Double> map){
		double size=0.0;
		Set<Integer> set = map.keySet();
		Iterator<Integer> iter = set.iterator();

		while(iter.hasNext()){
			Integer key = iter.next();
			size += (double)(map.get(key) * map.get(key));
		}
		size = Math.sqrt(size);
		return size;
	}

	public double cos(int t,int u){

		double cosSim=0.0;

		double Inner = inner(Ini.targetMap.get(t),Ini.userMap.get(u));
		double NormT = norm(Ini.targetMap.get(t));
		double NormU = norm(Ini.userMap.get(u));

		cosSim = Inner/(NormT*NormU);

		return cosSim;
	}


	/****************スレッド実装********************/

	public void executor(){

		//スレッドプール設定
		ExecutorService task = Executors.newFixedThreadPool(Ini.threadCal);

		for(Integer ID:Ini.targetMap.keySet()){
			task.execute(new calSim(ID));

		}
		task.shutdown();
		while(!task.isTerminated()){

		}
		System.out.println("["+new Date().toString()+"]");
	}

	private class calSim implements Runnable{
		double sim=0.0;
		Integer _tID;

		public calSim(Integer targetID ){
			this._tID=targetID;

		}
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			ConcurrentHashMap<Integer,Double> simMap = new ConcurrentHashMap<Integer,Double>();
			System.out.println("target:"+_tID);
			//			System.out.println(Ini.userMap.size());
			for(Integer ID:Ini.userMap.keySet()){
				//				System.out.println("\t"+ID);
				sim = cos(_tID,ID);
				if(sim>0.0){
					simMap.put(ID, sim);
				}
			}
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
			writer.Output.outputSimList(_tID, sortsimList);
			simMap.clear();
			sortsimList.clear();
			System.gc();
		}
	}



}
