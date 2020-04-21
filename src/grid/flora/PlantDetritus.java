package grid.flora;

import grid.Flora;
import grid.Grid;

public class PlantDetritus extends Flora {
	public PlantDetritus(int x, int y,int quantity) {
		super(x, y);
		age = 0;
		vita = quantity;
		isHome = false;
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
			grid.floraGrid[x][y] = null;
		}
	}
	
}
