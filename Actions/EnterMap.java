
package Actions;

import Units.Unit;
import World.Map;

public class EnterMap extends Action {
	public static String FROM_AREA = "Area";
	public static String FROM_TOP = "Top";
	public static String FROM_BOT = "Bot";
	public static String FROM_LEFT = "Left";
	public static String FROM_RIGHT_ = "Right";
	public static String FROM_LOGIN = "Login";
	
	public float mapX;
	public float mapY;
	public String from;	
	public EnterMap(long time,Unit unit) {
		super(time,unit);
	
	}
	
	public EnterMap(Unit unit) {
		super(System.currentTimeMillis(),unit);
	}
	
	
	public EnterMap(){
		super(System.currentTimeMillis());
	}

}
