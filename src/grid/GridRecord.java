package grid;

import java.util.ArrayList;

public class GridRecord {
	public ArrayList<Grid> boardStates = new ArrayList<Grid>();
	public ArrayList<Integer> squirrels = new ArrayList<Integer>();
	public ArrayList<Integer> trees = new ArrayList<Integer>();
	public ArrayList<Integer> worms = new ArrayList<Integer>();
	public ArrayList<Integer> seeds = new ArrayList<Integer>();
	public ArrayList<Integer> detritus = new ArrayList<Integer>();
	
//	facts[0] = vita;
//	facts[1] = squirrelPop;
//	facts[2] = treePop;
//	facts[3] = wormPopulation;
//	facts[4] = seedCount;
//	facts[5] = detCount;
	
	public void addTick(Grid grid) {
		squirrels.add(grid.getFacts()[1]);
		trees.add(grid.getFacts()[2]);
		worms.add(grid.getFacts()[3]);
		seeds.add(grid.getFacts()[4]);
		detritus.add(grid.getFacts()[5]);
		boardStates.add(grid);
	}
	
	public Grid returnBoard(Grid grid, int tickCount) {
		return boardStates.get(tickCount);
	}
	
	public int[] returnPops(int tickCount) {
		int[] pops = {squirrels.get(tickCount), trees.get(tickCount), worms.get(tickCount), seeds.get(tickCount), detritus.get(tickCount)};
		return pops;
	}
	
	public int[][] returnStats(int tickCount, int depth) {
		int[][] stats = new int[5][depth];
		for (int i = 0; i < depth; i++) {
			stats[0][i] = squirrels.get(tickCount - (depth-i));
			stats[1][i] = trees.get(tickCount - (depth-i));
			stats[2][i] = worms.get(tickCount - (depth-i));
			stats[3][i] = seeds.get(tickCount - (depth-i));
			stats[4][i] = detritus.get(tickCount - (depth-i));
		}
		return stats;
	}
}
