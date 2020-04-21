package grid.tiles;

import grid.Grid;
import grid.Tile;

public class Dirt extends Tile {
	public Dirt(int x, int y) {
		super(x, y);
		age = 0;
		vita = 0;
	}

	@Override
	public String getDisplay(Grid grid) {
		if (vita == 0) {
			return "d";
		}
		else {
			return "e";
		}
	}

	@Override
	public void tick(Grid grid) {
		age++;
		if (this.vita <= 0) {
			this.vita = 0;
		}
	}
	
}
