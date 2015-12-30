package Server;

import Units.Player;

public class User {
	
public int ClientID;
public String userName;
public Player player;
public String pw;

public User(String userName,Player player,String pw){
	this.userName = userName;
	this.pw = pw;
	
}
}
