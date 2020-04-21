package grid.soil;

import grid.Grid;
import grid.Soil;
import grid.flora.PlantDetritus;

public class Worms extends Soil {
	int quantity;
	public Worms(int x, int y) {
		super(x, y);
		age = 0;
		this.quantity = 1;
		vita = 1;
	}
	

	public Worms(int x, int y, int quantity) {
		super(x, y);
		age = 0;
		this.quantity = quantity;
		vita = quantity;
	}

	@Override
	public void tick(Grid grid) {
		//If the soil is barren, 1 or 2 worms die, adding 1 or 2 vita to the soil
		if (grid.tileGrid[x][y].vita == 0) {
			if (quantity >= 2) {
				quantity -= 2;
				vita -= 2;
				grid.tileGrid[x][y].vita += 2;
				if (quantity == 0) {
					grid.soilGrid[x][y] = null;
					return;
				}
			}
			else {
				grid.tileGrid[x][y].vita += this.vita;
				grid.soilGrid[x][y] = null;
				return;
			}
		}
		//If there's detritus, the worms consume the vita of the detritus. They consume 1 and add one to the soil
		//flora grid
		if (grid.floraGrid[x][y] != null && grid.floraGrid[x][y].vita > 0) {
			//one for the worms
			if (grid.floraGrid[x][y].getDisplay(grid) == "D") {
				grid.floraGrid[x][y].vita--;
				this.quantity++;
				this.vita++;
				//one for the soil
				if (grid.floraGrid[x][y].vita > 0) {
					grid.floraGrid[x][y].vita--;
					grid.tileGrid[x][y].vita++;
				}
				//if detritus is depleted, remove from grid
				if ((grid.floraGrid[x][y]).vita == 0) {
					grid.floraGrid[x][y] = null;
				}
				if (grid.floraGrid[x][y] != null && (grid.floraGrid[x][y]).vita < 0) {
					System.out.println("Houston, we have negative vita");
				}
			}
		}
		else if (grid.floraGrid[x][y] != null && grid.floraGrid[x][y].vita == 0) {
			grid.floraGrid[x][y] = null;
		}
		//fauna grid
		if (grid.faunaGrid[x][y] != null && grid.faunaGrid[x][y].vita > 0) {
			//one for the worms
			if (grid.faunaGrid[x][y].getDisplay(grid) == "D") {
				grid.faunaGrid[x][y].vita--;
				this.quantity++;
				this.vita++;
				//one for the soil
				if (grid.faunaGrid[x][y].vita > 0) {
					(grid.faunaGrid[x][y]).vita--;
					grid.tileGrid[x][y].vita++;
				}
				//if detritus is depleted, remove from grid
				if (grid.faunaGrid[x][y].vita == 0) {
					grid.faunaGrid[x][y] = null;
				}
				if (grid.faunaGrid[x][y] != null && grid.faunaGrid[x][y].vita < 0) {
					System.out.println("Houston, we have negative vita");
				}
			}
		}
		else if (grid.faunaGrid[x][y] != null && grid.faunaGrid[x][y].vita == 0) {
			grid.faunaGrid[x][y] = null;
		}
		
		//A wormstack that has decreased to one worm will die out
		if (this.quantity == 1 && this.age > 1) {
			grid.tileGrid[x][y].vita++;
			grid.soilGrid[x][y] = null;
			return;
		}
		//A wormstack will send one worm to a random adjacent tile
		else if (age > 0 && quantity > 0) {
			double numd = 8*Math.random();
			int num = (int) numd;
			switch(num) {
			case 0:
				if ((x-1)>=0 && (y-1)>=0) {
					moveWorms(grid, x-1, y-1);
				}
				break;
			case 1:
				if ((x-1)>=0) {
					moveWorms(grid, x-1, y);
				}
				break;
			case 2:
				if ((x-1)>=0 && (y+1)<=(grid.tileGrid.length - 1)) {
					moveWorms(grid, x-1, y+1);
				}
				break;
			case 3:
				if ((y-1)>=0) {
					moveWorms(grid, x, y-1);
				}
				break;
			case 4:
				if ((y+1)<=(grid.tileGrid.length - 1)) {
					moveWorms(grid, x, y+1);
				}
				break;
			case 5:
				if ((x+1)<=(grid.tileGrid.length - 1) && (y-1)>=0) {
					moveWorms(grid, x+1, y-1);
				}
				break;
			case 6:
				if ((x+1)<=(grid.tileGrid.length - 1)) {
					moveWorms(grid, x+1, y);
				}
				break;
			case 7:
				if ((x+1)<=(grid.tileGrid.length - 1) && (y+1)<=(grid.tileGrid.length - 1)) {
					moveWorms(grid, x+1, y+1);
				}
				break;
			default:
				System.out.println("oh uh");
				break;
			}
		}
		
		//A wormstack with over 10 worms, will have a random number of worms, 0-5 die
		if (quantity > 10) {
			int rand = ((int) Math.floor(Math.random()*(quantity - 10)));
			quantity -= rand;
			vita -= rand;
			grid.tileGrid[x][y].vita += rand;
		}
		//catch any negative tiles
		if (this.quantity < 0) {
			System.out.println("We had negative worms at "+x+','+y);
			this.quantity = 0;
			grid.soilGrid[x][y] = null;
		}
		age++;
	}

	private void moveWorms(Grid grid, int x, int y) {
		if (grid.soilGrid[x][y] == null) {
			grid.soilGrid[x][y] = new Worms(x,y);
			decrementWorms(grid);
		}
		else
		{
			((Worms)grid.soilGrid[x][y]).quantity++;
			grid.soilGrid[x][y].vita++;
			decrementWorms(grid);
		}
	}

	private void decrementWorms(Grid grid) {
		this.quantity--;
		this.vita--;
		if (this.quantity == 0) {
			grid.soilGrid[x][y] = null;
			return;
		}
	}

	@Override
	public String getDisplay() {
		if (quantity >= 10) {
			return Integer.toString(quantity);
		}
		else {
			return ' ' + Integer.toString(quantity);
		}
	}
	
}
