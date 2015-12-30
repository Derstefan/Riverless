package Units;

//import org.newdawn.slick.Color;

import Actions.Action;
import Actions.Movement;
import World.Map;

public class Unit {

public String unitName;
public int mapNumber;
public Map map;
public float mapX;
public float mapY;
public float areaX;
public float areaY;
public boolean inArea = false;

public boolean moving;
public float dx;
public float dy;



//Keysimulation (nur ein teil der actionssimulation, aber für movement vll sehr praktisch)
//public boolean keyW;
//public boolean keyA;
//public boolean keyS;
//public boolean keyD;







public int yourOrgColor;
public int yourColor;
	public Unit(String name,int mapNumber,float areaX,float areaY,float mapX,float mapY,int color){
		this.unitName = name;
		this.mapNumber=mapNumber;
		this.areaX = areaX;
		this.areaY = areaY;
		this.mapX = mapX;
		this.mapY = mapY;
		this.yourOrgColor = color;
		this.yourColor = color;
	}
	
	public void walkArea(float dx,float dy,int delta){
		
			areaX+=dx*delta;
			areaY+=dy*delta;
			//System.out.println(dx);
		
	}
	
	public void walkMap(float dx,float dy,int delta){
		
			mapX+=dx*delta;
			mapY+=dy*delta;
		
	}
	
	public void goAction(Action action,long serverTime){
		if(action instanceof Movement){
			Movement m = (Movement) action;
			this.dx=m.dx;
			this.dy=m.dy;
			int difference = (int) (serverTime - m.time);
			
			//System.out.println("diff: " + difference);
			if(inArea){
				areaX=m.x;
				areaY=m.y;
				walkArea(m.dx,m.dy,difference);
				
			} else {
				mapX=m.x;
				mapY=m.y;
				walkMap(m.dx,m.dy,difference);
			}
			if(m.x == 0.0f && m.y == 0.0f){
				moving=false;
			} else {
				moving=true;
			}
			
		}
	}
	
	
	
}
