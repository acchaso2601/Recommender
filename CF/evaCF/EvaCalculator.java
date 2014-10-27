package evaCF;

import java.util.List;

/**
 * 予測結果と正解データを使って評価を行う
 */
public class EvaCalculator {

	private List<String> recommendItemList;
	private List<String> answerItemList;

	private double precision;
	private double recall;

	/**
	 * コンストラクタ
	 * @param recommend 推薦アイテムリスト
	 * @param answer 正解アイテムリスト
	 */
	public EvaCalculator(List<String> recommend, List<String> answer) {
		recommendItemList = recommend;
		answerItemList = answer;
	}

	/**
	 * Precision/Recallを計算し，記憶する
	 */
	public void calculate() {
		int count = 0;
		for(int i = 0; i < recommendItemList.size(); i++) {
			//System.out.println("answerlist: " + answerItemList.toString());
			//System.out.println("reclist: " + recommendItemList.toString());
			if(answerItemList.contains(recommendItemList.get(i)))
				count++;
		}
		precision = (double)count / (double)recommendItemList.size();
		recall = (double)count / (double)answerItemList.size();
	}

	/**
	 * calculate メソッドで計算したPrecisionの値を取得する
	 * @return Precision
	 */
	public double getPrecision() {
		return precision;
	}

	/**
	 * calculate メソッドで計算したRecallの値を取得する
	 * @return Recall
	 */
	public double getRecall() {
		return recall;
	}
}
