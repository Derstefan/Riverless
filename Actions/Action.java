package Actions;

import Units.Unit;

public abstract class Action {
	public long time;
	public Unit unit;
	public Action(long time,Unit unit){
		this.time = time;
		this.unit=unit;
	}
	
}
