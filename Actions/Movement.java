package Actions;

import Units.Unit;

public class Movement extends Action {
	public float dx;
	public float dy;
	public float x;
	public float y;
	public Movement(float x,float y,float dx,float dy,long time,Unit unit) {
		super(time,unit);
		//System.out.println("time in movement " + time);
		this.dx=dx;
		this.dy=dy;
		this.x=x;
		this.y=y;
	}
}
