package grid.flora;

import grid.Flora;
import grid.Grid;

public class Tree extends Flora {
	public int seedsDropped = 0;
	public Tree(int x, int y) {
		super(x, y);
		age = 0;
		vita = 10;
	}

	@Override
	public String getDisplay(Grid grid) {
		if (isHome) {
			return "H";
		}
		else {
			return "T";
		}
	}

	@Override
	public void tick(Grid grid) {
		age++;
		//suck vita from the soil to grow
		for (int i = 0; i < 2; i++) {
			if (grid.tileGrid[x][y].vita > 0) {
				grid.tileGrid[x][y].vita--;
				vita++;
			}
		}
		//drop a seed randomly, if vita > 20
		if (vita > 15) {
			for (int i = -2; i < 3; i++) {
				for (int j = -2; j<3; j++) {
					if (i == j || (i+x)<0 || (j+y)<0 || (i+x)>(grid.tileGrid.length - 1) || (j+y)>(grid.tileGrid.length - 1)) {
						continue;
					}
					if (i != 0 || j != 0) {
						if (grid.floraGrid[x+i][y+j] == null) {
							double testnum = Math.random();
							if (testnum < 0.0625) {
								grid.floraGrid[x+i][y+j] = new Seed(x+i,y+j);
								seedsDropped++;
								vita -= 5;
							}
						}
					}
				}
			}
	}
		//Tree dies if it's vita/age ratio drops below 2
		if (((vita + seedsDropped*5) / age) < 2) {
			grid.floraGrid[x][y] = new PlantDetritus(x,y,vita);
		}
	}
	
}
