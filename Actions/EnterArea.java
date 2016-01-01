package Actions;

import Units.Unit;
import World.Area;

public class EnterArea extends Action {
	public float mapX;
	public float mapY;
	
	public EnterArea(long time, Unit unit) {
		super(time, unit);

	}

	public EnterArea(Unit unit) {
		super(System.currentTimeMillis(), unit);

	}

}
