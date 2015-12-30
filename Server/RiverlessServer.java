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
import Actions.Enter;
import NetObjects.ClientActions;
import NetObjects.LoginRequest;
import NetObjects.LoginResponse;
import NetObjects.RegisterRequest;
import NetObjects.RegisterResponse;
import Units.Player;
import Units.Unit;
import World.Area;
import World.Map;
import World.World;

public class RiverlessServer {
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
		RiverlessServer testServer = new RiverlessServer();
		
		//testServer.world.maps.add();
		
		
		//Schleife....
	}
	
	
	public RiverlessServer() {
		
		Log.set(Log.LEVEL_DEBUG);

		server = new Server();
		KryoUtil.registerServerClasses(server);
		
		Player p1 = new Player("Stefan",0,10,20,50,80,772351);
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
					Player p = new Player(((RegisterRequest) object).name,0,10,20,50,80,(int)(Math.random()*200000));
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
										for(int j=0;j<maps.size();j++){
											if(player.mapNumber ==maps.get(j).mapId){
												//füge player in map-j hinzu
												maps.get(j).units.add(player);
												maps.get(j).actions.add(new Enter(player));
												
											} else {
												Map map = new Map(player.mapNumber);
												maps.add(map);
												map.actions.add(new Enter(player));
												
											}
										}
										
										logedInUsers.add(users.get(i));
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
