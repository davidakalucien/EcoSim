package grid;

import grid.fauna.Squirrel;
import grid.flora.PlantDetritus;
import grid.flora.Seed;
import grid.flora.Tree;
import grid.soil.Worms;
import grid.tiles.Dirt;

public class Grid {
	public int wormPopulation = 1;
	public int squirrelPop = 0;
	public int treePop = 0;
	public int seedCount = 0;
	public int detCount = 0;
	public int gridSize = 28;
	public int halfGrid;
	public int vita = 0;
	public String[][] mainGrid;
	public int[] facts;
	public Tile[][] tileGrid = new Tile[gridSize][gridSize];
	public Flora[][] floraGrid = new Flora[gridSize][gridSize];
	public Fauna[][] faunaGrid = new Fauna[gridSize][gridSize];
	public Soil[][] soilGrid = new Soil[gridSize][gridSize];
	
	public Grid() {
		if (gridSize % 2 == 0) {
			halfGrid = gridSize/2;
		}
		else {
			halfGrid = (gridSize+1)/2;
		}
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				tileGrid[i][j] = new Dirt(i,j);
				if ((i+j) % 2 == 1) {
					floraGrid[i][j] = new PlantDetritus(i, j, 100);
					soilGrid[i][j] = new Worms(i,j,10);
				}
				if ((i+j) % 3 == 0) {
					floraGrid[i][j] = new Seed(i, j);
				}
			}
		}

		floraGrid[halfGrid][halfGrid] = new Tree(halfGrid,halfGrid);
		soilGrid[halfGrid][halfGrid] = new Worms(halfGrid,halfGrid,100);
		faunaGrid[halfGrid][halfGrid] = new Squirrel(halfGrid,halfGrid);
	}
	
	public String[][] displayGrid() {
		mainGrid = new String[gridSize*2][gridSize];

		for (int rowIndex = 0; rowIndex < tileGrid.length; rowIndex++) {
			String printRow = "";
			for (int columnIndex = 0; columnIndex < tileGrid[rowIndex].length; columnIndex++) {
				//Do the displaying
				if (this.faunaGrid[rowIndex][columnIndex] == null) {
					if (this.floraGrid[rowIndex][columnIndex] == null) {
						mainGrid[rowIndex][columnIndex] = String.valueOf(tileGrid[rowIndex][columnIndex].getDisplay(this));
					} else {
						mainGrid[rowIndex][columnIndex] = String.valueOf(floraGrid[rowIndex][columnIndex].getDisplay(this));
						
					}
				}
				else {
					mainGrid[rowIndex][columnIndex] = String.valueOf(faunaGrid[rowIndex][columnIndex].getDisplay(this));
				}
			}
			for (int columnIndex=0; columnIndex<this.soilGrid[rowIndex].length; columnIndex++) {
				if (this.soilGrid[rowIndex][columnIndex] == null) {
					mainGrid[rowIndex+gridSize][columnIndex] = "--";
				} else {
					mainGrid[rowIndex+gridSize][columnIndex] = soilGrid[rowIndex][columnIndex].getDisplay();
				}
			}
		}
		return mainGrid;
	}

	public int[] getFacts() {
		facts = new int[6];
		wormPopulation = 0;
		squirrelPop = 0;
		treePop = 0;
		vita = 0;
		seedCount = 0;
		detCount = 0;
		for (int rowIndex = 0; rowIndex < this.tileGrid.length; rowIndex++) {
			String printRow = "";
			for (int columnIndex = 0; columnIndex < this.tileGrid[rowIndex].length; columnIndex++) {
				//get all the numbers
				vita += this.tileGrid[rowIndex][columnIndex].vita;
				//ANIMALS
				if (this.faunaGrid[rowIndex][columnIndex] != null) {
					vita += this.faunaGrid[rowIndex][columnIndex].vita;
					if (this.faunaGrid[rowIndex][columnIndex].getDisplay(this) == "S" || this.faunaGrid[rowIndex][columnIndex].getDisplay(this) == "$") {
						squirrelPop++;
					}
					else if (this.faunaGrid[rowIndex][columnIndex].getDisplay(this) == "D") {
						detCount++;
				}
				}
				//PLANTS
				if (this.floraGrid[rowIndex][columnIndex] != null) {
					vita += this.floraGrid[rowIndex][columnIndex].vita;
					if (this.floraGrid[rowIndex][columnIndex].getDisplay(this) == "T" || this.floraGrid[rowIndex][columnIndex].getDisplay(this) == "H") {
						treePop++;
					}
					else if (this.floraGrid[rowIndex][columnIndex].getDisplay(this) == "s") {
						seedCount++;
					}
					else if (this.floraGrid[rowIndex][columnIndex].getDisplay(this) == "D") {
						detCount++;
					}
				}
				//WORMS
				if (this.soilGrid[rowIndex][columnIndex] != null) {
					vita += this.soilGrid[rowIndex][columnIndex].vita;
					wormPopulation += Integer.parseInt(this.soilGrid[rowIndex][columnIndex].getDisplay().trim());
				}
			}
		}
		facts[0] = vita;
		facts[1] = squirrelPop;
		facts[2] = treePop;
		facts[3] = wormPopulation;
		facts[4] = seedCount;
		facts[5] = detCount;
		return facts;
	}
	
	public void tick() {
		for (int rowIndex=0; rowIndex<this.tileGrid.length; rowIndex++) {
			for (int columnIndex=0; columnIndex<this.tileGrid[rowIndex].length; columnIndex++) {
				tileGrid[rowIndex][columnIndex].tick(this);
				if (floraGrid[rowIndex][columnIndex] != null) {
					floraGrid[rowIndex][columnIndex].tick(this);
				}
				if (soilGrid[rowIndex][columnIndex] != null) {
					soilGrid[rowIndex][columnIndex].tick(this);
				}
				if (faunaGrid[rowIndex][columnIndex] != null) {
					faunaGrid[rowIndex][columnIndex].tick(this);
				}
			}
		}
	};
}
