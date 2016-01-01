package Server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.function.LongToIntFunction;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import Actions.Action;
import Actions.EnterArea;
import Actions.EnterMap;
import Actions.Movement;
import NetObjects.ClientActions;
import NetObjects.LoginRequest;
import NetObjects.LoginResponse;
import NetObjects.RegisterRequest;
import NetObjects.RegisterResponse;
import NetObjects.Synch;
import Units.Player;
import Units.Unit;
import World.Area;
import World.Map;
import World.World;

public class RiverlessServer {
	public static int TILE_HIGHT;
	public static int TILE_WIDTH;
	
	
	public Server server;
	public ArrayList<String> clients = new ArrayList<String>();
	public ArrayList<String> logedInClients = new ArrayList<String>();
	public ArrayList<User> users = new ArrayList<User>();
	public ArrayList<User> logedInUsers = new ArrayList<User>();
	
	

	World world = new World();
	ArrayList<Map> maps = new ArrayList<Map>();
	ArrayList<Area> areas = new ArrayList<Area>();
	
	
	
	
	public static void main(String[] args){
		System.out.println("Server start");
		RiverlessServer server = new RiverlessServer();
		
	}
	
	
	public void serverGo(){
		long time,delta;
		time = System.currentTimeMillis();
		while(true){
		delta = System.currentTimeMillis()-time;
		time = System.currentTimeMillis();
		//Map
			for(int i=0;i<maps.size();i++){
				
				//Synch object erstellen
				Synch synch = new Synch();
				for(int j = 0;j<maps.get(i).actions.size();j++){
					//während einer Scleifenausführung muss die Liste KOnstant sein ...
					goAction(maps.get(i).actions.get(j));
					synch.actions.add(maps.get(i).actions.get(j));
					maps.get(i).actions.remove(j);
				}
				
				
				for(int j =0;j<maps.get(i).units.size();j++){
					if(maps.get(i).units.get(j) instanceof Player){
						server.sendToUDP(((Player)maps.get(i).units.get(j)).clientId, synch);
					}
				}
				
				
			}
			//Area
			for(int i=0;i<areas.size();i++){

				Synch synch = new Synch();
				for(int j = 0;j<areas.get(i).actions.size();j++){
					goAction(areas.get(i).actions.get(j));
					synch.actions.add(areas.get(i).actions.get(j));
					areas.get(i).actions.remove(j);
				}
				
				
				for(int j =0;j<areas.get(i).units.size();j++){
					if(areas.get(i).units.get(j) instanceof Player){
						server.sendToUDP(((Player)areas.get(i).units.get(j)).clientId, synch);
					}
				}
			}
		}
	}
	
	


	private void goAction(Action action) {
		if(action instanceof EnterMap){
			Unit unit = action.unit;
			if(unit.inArea){
				unit.area.units.remove(unit);
				boolean exists = false;
			for(int i=0;i<maps.size();i++){
				if(unit.mapNumber==maps.get(i).mapId){
					maps.get(i).units.add(unit);
					exists = true;
					break;
					
				}
			}
			if(!exists){
				unit.map.units.remove(unit);
				Map map = new Map(unit.mapNumber);
				maps.add(map);
				map.actions.add(new EnterMap(unit));
				unit.map = map;			
			}
			}else {
				unit.map.units.remove(unit);
				if(((EnterMap) action).from=="Login"){
					//wird nicht benötigt
				}else if(((EnterMap) action).from=="Area"){
					//wird nicht benötigt...
				}else if(((EnterMap) action).from=="Top"){
					//map von oben wird unit gelöscht	
				}else{
					
				}
				
			}
			
			
			
		} else if(action instanceof EnterArea){
			Unit unit = action.unit;
			if(unit.inArea){
			//im moment noch nit möglich zwischen areas zu bewegen...
			
			}else {
				int x = (int)unit.mapX/this.TILE_WIDTH;
				int y = (int)unit.mapY/this.TILE_HIGHT;
				Area area;
				
				for(int i=0;i<maps.size();i++){
					if(unit.mapNumber==maps.get(i).mapId){
						boolean existsArea = false;
						for(int j =0;j<maps.get(i).areas.size();i++){
							if(maps.get(i).areas.get(j).positionX == (int)unit.mapX/this.TILE_WIDTH && maps.get(i).areas.get(j).positionY == (int)unit.mapY/this.TILE_HIGHT){
								existsArea = true;
								area = maps.get(i).areas.get(j);
								maps.get(i).areas.get(j).units.add(unit);
								unit.area=area;
								break;
							}
							
						}
						maps.get(i).units.remove(unit);
						if(!existsArea){
							area = new Area(x,y,unit.mapNumber);
							area.units.add(unit);
							maps.get(i).areas.add(area);
							unit.area=area;
						}
				
						if(maps.get(i).areas.isEmpty()){
							maps.remove(i);
						}
						
						break;	
					}	
				}
					
				
			}

		}else if(action instanceof Movement){
			
				action.unit.goAction(action, System.currentTimeMillis());
			
		}else {
			System.out.println("Error no Action definition of this type");
		}
		
	}




	public RiverlessServer() {
		
		Log.set(Log.LEVEL_DEBUG);

		server = new Server();
		KryoUtil.registerServerClasses(server);
		
		Player p1 = new Player("Stefan",0,10,20,50,80,772351,-1);
		users.add(new User("Stefan",p1,"pups"));
		
	
		
		
		
		server.addListener(new Listener() {
			
			public void connected(Connection connection) {
				connection.updateReturnTripTime();
				clients.add(String.valueOf(connection.getID()));
				System.out.println(showList(clients));
				System.out.println("connected " + connection.getID());
				
			}
			public void disconnected(Connection connection) {
				clients.remove(String.valueOf(connection.getID()));
				System.out.println(showList(clients));
				if(logedInClients.contains(String.valueOf(connection.getID()))){
				
					logedInClients.remove(String.valueOf(connection.getID()));
				}
				System.out.println("disconnected " + connection.getID());
			}
			public void received(Connection connection, Object object) {
				
				if (object instanceof RegisterRequest) {
					
					System.out.println("Registerierung von Client nr.: " + connection.getID() + " mit ");
					System.out.println("name : " + ((RegisterRequest) object).name + " .");
					System.out.println("pw: " + ((RegisterRequest) object).pw + " .");
					
					RegisterResponse rrp = new RegisterResponse();
					rrp.check = true;
					
					for(int i=0;i<users.size();i++){
						if(users.get(i).userName.equals(((RegisterRequest) object).name)){
							rrp.check = false;
							rrp.log="Der Name ist leider schon vorhanden...";
							break;
						}
					}
					if(rrp.check){
					Player p = new Player(((RegisterRequest) object).name,0,10,20,50,80,(int)(Math.random()*200000),connection.getID());
					User u = new User(((RegisterRequest) object).name,p,((RegisterRequest) object).pw);
					u.ClientID = connection.getID();
					users.add(u);
					rrp.log = "Registrierung erfolgreich";
					}
					connection.sendUDP(rrp);
				}
				else if (object instanceof LoginRequest) {
				System.out.println("Login von Client nr.: " + connection.getID() + " mit ");
						System.out.println("name : " + ((LoginRequest) object).name + " .");
						System.out.println("pw : " + ((LoginRequest) object).pw);
						LoginResponse lrp = new LoginResponse();
						lrp.check = false;
						lrp.log="Login leider fehlegschlagen";
						//connection.sendTCP(lrp);
						for(int i=0;i<users.size();i++){
							if(users.get(i).userName.equals(((LoginRequest) object).name)){
								if(users.get(i).pw.equals(((LoginRequest) object).pw)){
									if(!logedInUsers.contains(users.get(i))){
										Player player = users.get(i).player;
										player.clientId=connection.getID();
										boolean exists = false;
										for(int j=0;j<maps.size();j++){
											if(player.mapNumber ==maps.get(j).mapId){
												//füge player in map-j hinzu
												maps.get(j).units.add(player);
												EnterMap e = new EnterMap();
												maps.get(j).actions.add(new EnterMap(player));
												player.map =maps.get(j);
												exists = true;
											}
										}
										if(!exists){
											Map map = new Map(player.mapNumber);
											maps.add(map);
											map.actions.add(new EnterMap(player));
											player.map = map; 
										
										}
										
										logedInUsers.add(users.get(i));
										lrp.player = player;
										lrp.check = true;
										lrp.log="Login erfolgreich";
										break;
										}
									}
							}
						}
						connection.sendTCP(lrp);
						
						
				}
				else if (object instanceof ClientActions) {
					//optimierungsmöglichkeit....
					for(int i=0;i<logedInUsers.size();i++){
						if(logedInUsers.get(i).ClientID == 	connection.getID()){
							Player player = logedInUsers.get(i).player;
							if(player.inArea){
								//Area
								Area area = player.area;
								ArrayList<Action> tempList = ((ClientActions)object).actions;
								for(int j=0;j<tempList.size();j++){
									area.actions.add(tempList.get(j));
									tempList.get(j).unit = player;
								}
								
							} else {
								//Map
								Map map = player.map;
								map.actions.addAll(((ClientActions)object).actions);
								ArrayList<Action> tempList = ((ClientActions)object).actions;
								for(int j=0;j<tempList.size();j++){
									map.actions.add(tempList.get(j));
									tempList.get(j).unit = player;
								}
							}
						}
					}

				}
				
			}
		});

		try {
			server.bind(KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
		} catch (IOException ex) {
			System.out.println(ex);
		}

		server.start();
	}

	
	
	public static String showList(ArrayList<String> list){
		if(list!=null && !list.isEmpty()){
		String str = "";
		for(int i=0;i<list.size();i++){
			str+=list.get(i) + "\n";
		}
		return str;
		}
		return "";
	}
	
	
	
	
	
}
