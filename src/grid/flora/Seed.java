package grid.flora;

import grid.Flora;
import grid.Grid;

public class Seed extends Flora {
	public int quantity;
	public Seed(int x, int y) {
		super(x, y);
		age = 0;
		vita = 5;
	}

	@Override
	public String getDisplay(Grid grid) {
		return "s";
	}

	@Override
	public void tick(Grid grid) {
		//pull vita from the soil
		if (grid.tileGrid[x][y].vita > 0) {
			vita++;
			grid.tileGrid[x][y].vita--;
		}
		//if vita == 10, sprout into tree
		if (vita == 10) {
			grid.floraGrid[x][y] = new Tree(x, y);
		}
		//if age is greater than vita, seed rots
		else if (age > vita) {
			grid.floraGrid[x][y] = new PlantDetritus(x, y, vita);
		}
		age++;
	}
	
}
