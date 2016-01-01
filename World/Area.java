package World;

import java.util.ArrayList;

import Actions.Action;

//import org.newdawn.slick.tiled.TiledMap;

import Units.Unit;

public class Area {
//TiledMap
	//TiledMap tmap;
	public int[][] mapData;
	Map motherMap;
	public int mapId;
	public int positionX;
	public int positionY;
	public ArrayList<Unit> units = new ArrayList<Unit>();
	public ArrayList<Action> actions= new ArrayList<Action>();
	
	
	public Area(int x,int y,int mapId){
		this.positionX=x;
		this.positionY=y;
		this.mapId=mapId;
	}
}
