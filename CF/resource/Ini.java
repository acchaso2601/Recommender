package resource;

import java.util.concurrent.ConcurrentHashMap;

public class Ini {
	
	//UserMap
	public static ConcurrentHashMap<Integer,Double> itemMap;
	public static ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,Double>> userMap;
	public static ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,Double>> targetMap;


	//Target Pattern
	public static final int Pattern =4;
	public static String[] pNUM={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19"};

	//input path
	public static final String log = "G:/eclipseData/Resource/CF/DummyData_cross3/LearningData/noize100/";
	public static final String target = "G:/eclipseData/Resource/CF/DummyData_cross3/TargetLogData/";


	//output path
	public static final String outputSim = "G:/eclipseData/Output/CF/Dummy/cross/noize100/SimList/";
	public static final String outputItemList="G:/eclipseData/Output/CF/Dummy/cross/noize100/ItemList/";

	//Thread ini
	public static final int threadRead = 6;
	public static final int threadCal = 6;

	//cutLine
	public static double cutSim = 0.0;
	public static double cutScore = 0.0;
	public static int cutNum=20;
	public static int ItemCutNum = 30;

	//flag
	//rep=1:驥崎､�≠繧�
	//rep=0:驥崎､�↑縺�
	public static int rep = 0;
}
