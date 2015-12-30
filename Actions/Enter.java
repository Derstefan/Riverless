package Actions;

import Units.Unit;

public class Enter extends Action {
public float mapX;
public float mapY;
 
	public Enter(long time, Unit unit) {
		super(time,unit);
		
	}
	public Enter(Unit unit) {
		super(System.currentTimeMillis(),unit);
		
	}

}
