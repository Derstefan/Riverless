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
	public int positionX;
	public int positionY;
	ArrayList<Unit> units = new ArrayList<Unit>();
	ArrayList<Action> actions= new ArrayList<Action>();
}
