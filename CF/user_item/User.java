package user_item;

import java.util.concurrent.ConcurrentHashMap;

public class User {

	public int userID ;
	public ConcurrentHashMap<Integer, Double> itemMap;

	public User(int _uID,ConcurrentHashMap<Integer,Double> _iMap){
		this.userID = _uID;
		this.itemMap = _iMap;
	}
}
