package evaCF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * ユーザの購入ログと推薦結果から，Precision/Recallに基づいて精度評価を行う
 * @author H_Sato
 */
public class Evaluation {
	// 読み込むDataファイルの文字コード
	private String characterCode;
	// 入出力ファイルデータのパス
	private String answerDataPath;
	private String predictionFolderPath;
	private String outputFilePath;

	// 精度を出力するP@N一覧
//	private int[] outputNList = {1,2,3,4,5,6,7,8,9,10};
	private int[] outputNList = {1,2,3,4,5,6,7,8};
	// 正解itemの数
	private int answerN;

	// 評価対象のuserIDと各userの正解データ・予測データを保存
	Map<String, List<String>> user_AnswerItemListMap;
	Map<String, List<String>> user_RecommendItemListMap;

	static Map<Integer,Double> mapPre = new HashMap<Integer,Double>();
	static Map<Integer,Double> mapRec = new HashMap<Integer,Double>();

	private final String splitSpace = " ";
	private final String splitTab = "\t";
	private final String splitComma = ",";


	static int countAll = 0;
	// コンストラクタ
	public Evaluation(String characterCode, String predictionPath, String answerPath, int answerN) {
		this.characterCode = characterCode;
		this.answerN = answerN;

		answerDataPath = answerPath;
		predictionFolderPath = predictionPath;
		outputFilePath = "G:/eclipseData/Resource/TF/resultverTF_Pattern17.txt";
		user_AnswerItemListMap = new HashMap<String, List<String>>();
		user_RecommendItemListMap = new HashMap<String, List<String>>();
	}

	/**
	 * 正解データファイルを読み込み，各テストユーザのIDと正解アイテムを憶える
	 *
	 * ファイルフォーマットは以下：
	 * [userID]\t[itemID]\s[itemID]\s...\s[itemID]
	 * [userID]\t[itemID]\s...
	 * ...
	 */
	private void readAnswerItemIDs(int num) {
		try {
			File f = new File(answerDataPath + String.format("%02d", num) + ".txt");
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader ir = new InputStreamReader(fis, characterCode);
			BufferedReader br = new BufferedReader(ir);

			String line = "";
			while ((line = br.readLine()) != null) {
				String userID = MyLibrary.split(line, splitTab)[0];
				line = MyLibrary.split(line, splitTab)[1];
				String[] split = MyLibrary.split(line, splitComma);

				List<String> answerItemList = new ArrayList<String>();
//				for (int i = 0; i < answerN && i < split.length; i++) {
				for(int i = 0;i < answerN;i++){
				answerItemList.add(split[i]);
				}

				user_AnswerItemListMap.put(userID, answerItemList);
			}
			br.close();
			ir.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * テストユーザの推薦リストを読み，各テストユーザのIDと推薦リストを憶える
	 * predictionFolderPath 直下に各ユーザの推薦リストが格納されているものとする．
	 *
	 * 推薦リストのファイルフォーマットは以下：
	 * [userID].txt
	 * 	[itemID]\t[score]
	 * 	[itemID]\t[score]
	 * 	...
	 * （ただし，itemIDはscore降順にソート済みとする）
	 */
	private void readRecommendationList(int num) {
		Set<String> uselessUsers = new HashSet<String>();
		File f;
		for(Entry<String, List<String>> ent : user_AnswerItemListMap.entrySet()) {
			String userID = ent.getKey();
			try {
				f = new File(predictionFolderPath + String.format("%02d", num) + "/" + userID + ".txt");
				if(!f.isFile()) {
					//System.out.println("flag");
					uselessUsers.add(userID);
					continue;
				}
				FileInputStream fis = new FileInputStream(predictionFolderPath + String.format("%02d", num) + "/" + userID + ".txt");
				InputStreamReader ir = new InputStreamReader(fis, characterCode);
				BufferedReader br = new BufferedReader(ir);

				List<String> predictItemList = new ArrayList<String>();
				String line;
//				if(!br.ready()) continue; // 推薦リストが空のユーザを考慮から外す場合
				while((line = br.readLine()) != null && predictItemList.size() < outputNList[outputNList.length-1]) {
					String itemID = MyLibrary.split(line, splitTab)[0];
					predictItemList.add(itemID);
				}
				br.close();
				ir.close();
				fis.close();
				user_RecommendItemListMap.put(userID, predictItemList);
			} catch(IOException e) {
				e.printStackTrace();
				uselessUsers.add(userID);
			}
		}
		for(String s: uselessUsers)
			user_AnswerItemListMap.remove(s);
	}

	/**
	 * 各テストユーザの推薦リストと正解を比較し，P@N別に評価値(Precision/Recall/F-measure)を出力する
	 * 但し，出力する各評価値は全ユーザの評価値の平均とする
	 */
	private void execute() {
		try {
			FileWriter fw = new FileWriter(outputFilePath);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			//System.out.println("P@N" +"\t" + "precision" + "\t" + "recall" + "\t" + "F-measure");
			pw.println("P@N" +"\t" + "precision" + "\t" + "recall" + "\t" + "F-measure");
			for(int i = 0; i < outputNList.length; i++) {
				evaluation(outputNList[i], pw);
			}
			pw.close();
			bw.close();
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * execute の下請けメソッド
	 * P@Nを指定して呼び出し
	 * @param outputN P@Nの値
	 * @param pw 出力用Writer
	 */
	private void evaluation(int outputN, PrintWriter pw) {
		double precisionSum = 0.0;
		double recallSum = 0.0;
		double userN = (double)user_RecommendItemListMap.size();
		int count = 0;
		//System.out.println(userN);
		for(Entry<String, List<String>> ent : user_RecommendItemListMap.entrySet()) {
			List<String> predictList = ent.getValue();

			if(predictList.size() > outputN)
				predictList = predictList.subList(0, outputN);
			List<String> answerList = user_AnswerItemListMap.get(ent.getKey());
			EvaCalculator ecal = new EvaCalculator(predictList, answerList);



			ecal.calculate();
			double precision = ecal.getPrecision();
			double recall = ecal.getRecall();

			if(!Double.isNaN(precision)) {
				precisionSum += precision;
				if(precision > 0){
					count++;

				}
			}
			if(!Double.isNaN(recall))
				recallSum += recall;
		}
//		System.out.println(presicionSum);
		double avePrecision = precisionSum/userN;
		double aveRecall = recallSum/userN;
		if(!mapPre.containsKey(outputN)){
			mapPre.put(outputN,avePrecision);
		}else{
			mapPre.put(outputN, mapPre.get(outputN)+avePrecision);
		}

		if(!mapRec.containsKey(outputN)){
			mapRec.put(outputN,aveRecall);
		}else{
			mapRec.put(outputN, mapRec.get(outputN)+aveRecall);
		}
		countAll = countAll+count;
		System.err.println(outputN + "\t" + avePrecision + "\t" + aveRecall + "\t" + 2*avePrecision*aveRecall/(avePrecision+aveRecall) + "\t" + count);
		pw.println(outputN + "\t" + avePrecision + "\t" + aveRecall);
	}

	public static void main(String[] args) throws IOException {
		String[] arg = new String[3];

		String noize = "noize0";
//		文字コードを指定
		arg[0] = "UTF-8";
//		推薦リストの存在するフォルダを指定
		arg[1] = "G:/eclipseData/Output/CF/Dummy/cross/"+noize+"/ItemList/";
//		arg[1] = "G:/eclipseData/Output/hgs/";
//		arg[1] = "G:/eclipseData/Resource/WI2/Data/Result/RecList_7/";
//		正解データの存在するフォルダを指定
		arg[2] = "G:/eclipseData/Resource/TF/DummyData_cross/Answer/";
//		正解アイテム数を指定
		int answerN = 8;
		int FolderN = 20;
		Evaluation eva = new Evaluation(arg[0], arg[1], arg[2], answerN);
		for(int i = 0; i < FolderN; i++){
			eva.readAnswerItemIDs(i);
			eva.readRecommendationList(i);
			System.out.println(i);
			eva.execute();
			eva.user_AnswerItemListMap.clear();
			eva.user_RecommendItemListMap.clear();
			}


//		/******結果出力処理*****/
		String out = "G:/eclipseData/Output/CF/Dummy/result/";
		File o = new File(out);
		o.mkdir();
		String file =noize+".csv";
		File outPut = new File(out+file);
		outPut.createNewFile();

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPut),"UTF-8"));
//		/***********************/
//
		System.out.println("result:");
		double p,r;

		for(int i =0;i<mapPre.size();i++){
			p=mapPre.get(i+1)/(double)FolderN;
			r=mapRec.get(i+1)/(double)FolderN;
			System.err.println(i+1);
		System.out.println("\tPrecision Ave = "+p);
		System.out.println("\tRecall Ave = "+r);
		System.out.println("\tF値 = "+2*p*r/(p+r));
//
		bw.write((i+1)+","+p+","+r+","+2*p*r/(p+r)+"\n");
//
		}

		bw.close();
		System.out.println("Count = "+countAll);


	}
}
