package grid;

public abstract class Soil implements IDisplayableInt {
	public int x;
	public int y;
	public int age;
	public int vita;
	public Soil(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public abstract void tick(Grid grid);
}
