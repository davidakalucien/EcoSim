package grid;

public abstract class Entity implements IDisplayable {
	public int x;
	public int y;
	public int age;
	public int vita;
	
	public Entity(int x,int y) {
		this.x = x;
		this.y = y;
		this.age = 0;
	}
	
	public abstract void tick(Grid grid);
}
