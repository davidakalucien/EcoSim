package grid;

public abstract class Flora extends Entity implements IDisplayable {
	public boolean isHome = false;
	public Flora(int x,int y) {
		super(x,y);
	}
}
