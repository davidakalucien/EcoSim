package grid.fauna;

import grid.Fauna;
import grid.Grid;

public class AnimalDetritus extends Fauna {
	public int quantity;
	public AnimalDetritus(int x, int y, int vita) {
		super(x, y, 0, vita);
		age = 0;
	}

	@Override
	public String getDisplay(Grid grid) {
		return "D";
	}

	@Override
	public void tick(Grid grid) {
		age++;
		if (age%3 == 0) {
			vita--;
			grid.tileGrid[x][y].vita++;
		}
		if (vita == 0) {
			grid.faunaGrid[x][y] = null;
		}
	}
	
}
