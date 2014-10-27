package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import resource.Ini;


public class ResReader {

	/**************スレッド実装***************/
	public void executor(String pattern){
		ExecutorService task = Executors.newFixedThreadPool(Ini.threadRead);

//		パス設定
		String path = Ini.log+pattern+"/";
		File[] f = new File(path).listFiles();
		System.out.println(f.length);
		Ini.userMap = new ConcurrentHashMap<Integer,ConcurrentHashMap<Integer, Double>>();
		for(File file:f){
			//System.out.println(f[i].getName());
			task.execute(new logReader(file));
		}
		task.shutdown();
		while(!task.isTerminated()){

		}
		System.out.println("["+new Date().toString()+"]");
	}

	private class logReader implements Runnable{
		private File _f;

		public logReader(File f){
			this._f = f;
		}

		public void run(){
			File[] in = new File(Ini.log).listFiles();
			BufferedReader br;


				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(_f),"UTF-8"));
					int UserID = Integer.parseInt(_f.getName().replace(".txt", ""));
//					System.out.println(UserID);
					int ItemID;
					double Score;
					String line ="";
					Ini.itemMap = new ConcurrentHashMap<Integer,Double>();
					while((line=br.readLine())!=null){

						String[] s = line.split("\t");
						ItemID = Integer.parseInt(s[0]);
						Score = Double.parseDouble(s[2]);
//						System.out.println(ItemID);
						if(!Ini.itemMap.contains(ItemID)&&Score>0.0){
							Ini.itemMap.put(ItemID,Score);
						}else{
							if(Ini.itemMap.contains(ItemID)&&Ini.itemMap.get(ItemID)<Score){
								Ini.itemMap.put(ItemID,Score);
							}
						}

//						System.out.println(UserID+"\t"+Ini.itemMap.size());
					}
					Ini.userMap.put(UserID,Ini.itemMap);
					//				System.out.println(Ini.userMap.size());
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}

	}

	/****TargetLog読み込み****/
	public void targetReader(String p){
		File[] in = new File(Ini.target+p+"/").listFiles();
		System.out.println("length;"+in.length);
		BufferedReader br ;
		Ini.targetMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Double>>();
		for(int i = 0;i<in.length;i++){

			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(in[i]),"UTF-8"));
				int UserID = Integer.parseInt(in[i].getName().replace(".txt", ""));
//				System.out.println("UserID:"+UserID);
				String line ="";
				int ItemID;
				double Score;
				Ini.itemMap = new ConcurrentHashMap<Integer,Double>();
				while((line=br.readLine())!=null){

					String[] s = line.split("\t");
					ItemID = Integer.parseInt(s[0]);
					Score = Double.parseDouble(s[3]);
					//System.out.println(ItemID);
					if(!Ini.itemMap.contains(ItemID)&&Score>0.0){
						Ini.itemMap.put(ItemID,Score);
					}else{
						if(Ini.itemMap.contains(ItemID)&&Ini.itemMap.get(ItemID)<Score){
							Ini.itemMap.put(ItemID,Score);
						}
					}
					//					System.out.println(UserID+"\t"+Ini.itemMap.size());
				}
//				System.out.println(UserID+"\t"+Ini.itemMap.size());
//				System.out.println(Ini.itemMap);
//				System.out.println("map:"+Ini.itemMap.size());

				Ini.targetMap.put(UserID,Ini.itemMap);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

	}


}
