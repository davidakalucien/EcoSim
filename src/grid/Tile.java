package grid;

public abstract class Tile implements IDisplayable {
	public int x;
	public int y;
	public int age;
	public int vita;
	
	public Tile(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public abstract void tick(Grid grid);
}
