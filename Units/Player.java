package Units;

//import org.newdawn.slick.Color;

public class Player extends Unit {
	public int clientId;
public Player(String unitName,int mapNumber,float areaX,float areaY,float mapX,float mapY,int color,int clientId){
	
	super(unitName,mapNumber,areaX,areaY,mapX,mapY,color);
	this.clientId=clientId;
}
}
