package World;

import java.util.ArrayList;

import Actions.Action;
import Units.Unit;

//import org.newdawn.slick.tiled.TiledMap;

public class Map {
//TiledMap
	//TiledMap tmap;
	public int[][] mapData;
	public int mapId;
	public ArrayList<Area> areas = new ArrayList<Area>();
	public ArrayList<Unit> units = new ArrayList<Unit>();
	public ArrayList<Action> actions = new ArrayList<Action>();
	
	public Map(int mapId){
		this.mapId=mapId;
	}
}
