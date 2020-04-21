package grid.fauna;

import grid.Fauna;
import grid.Grid;

public class Squirrel extends Fauna {
	public boolean seesFood;
	public boolean seesPredator;
	public boolean atHome;
	public double distanceToFood = 20;
	public int[] nearestFood = new int[2];
	public int[] nearestPredator = new int[2];
	public boolean movedUp = false;
	public boolean halted = true;
	public boolean pregnant = false;
	public boolean inLabour = false;
	public int birthVita = 15;
	//newborn
	public Squirrel(int x, int y) {
		super(x, y, 4, 15);
		cellAge = 0;
	}
	//not newborn
	public Squirrel(int x, int y, int age, int vita, int[] nearestFood, int[] home, int[] plan, boolean movedUp) {
		super(x, y, 4, age, vita, plan);
		this.nearestFood = nearestFood;
		this.home = home;
		this.movedUp = movedUp;
	}

	@Override
	public String getDisplay(Grid grid) {
		return "S";
	}

	@Override
	public void tick(Grid grid) {
		if (cellAge > 0 || movedUp) {
			if (x==home[0] && y==home[1]) {
//				System.out.println("I'm at home");
				atHome = true;
			} else {
//				System.out.println("I'm not at home");
				atHome = false;
			}
			
			if (home[0] < 0) {
				System.out.println("fuuuuuuuuuuck0 "+home[0]);
			}
			if (home[1] < 0) {
				System.out.println("fuuuuuuuuuuck1 "+home[1]);
			}
			
	//		System.out.println("I am at "+x+','+y);
	//		System.out.println("My home is at "+home[0]+','+home[1]);
	//		System.out.println("!Homeless?: "+!homeless);
	//		System.out.println("!Predator?: "+!seesPredator);
	//		System.out.println("My hunger is "+hunger);
//			printPlan();
	//		System.out.println("My cellAge is "+cellAge);
	//		System.out.println("It is "+movedUp+" that I have moved up or left");
			
	//		Make sure that home knows it's home
			if (grid.floraGrid[x][y] != null && grid.floraGrid[x][y].getDisplay(grid) == "T" && home[0] == x && home[1] == y) {
				grid.floraGrid[x][y].isHome = true;
	//			System.out.println("I have confirmed my home");
			}
			
			seesPredator = false;
			seesFood = false;
			see(grid);
			
			//Look for food (s)
			int[] nearestFoodInfo = lookForNearest(grid, fieldOfView, "s");
//			System.out.println(nearestFoodInfo[2]);
			if (nearestFoodInfo[2] != 0 && nearestFoodInfo[2] != 100) {
				seesFood = true;
				nearestFood[0] = nearestFoodInfo[0];
				nearestFood[1] = nearestFoodInfo[1];
			}
	
//			System.out.println("SeesFood?: "+seesFood);
			
	//		System.out.println("I see food at "+nearestFood[0]+','+nearestFood[1]);
	//		int[] nearestPredatorInfo = lookForNearest(grid, fieldOfView, x, y, 'F');
	//		if (nearestPredatorInfo[2] != 0) {
	//			seesPredator = true;
	//			nearestPredator[0] = nearestPredatorInfo[0];
	//			nearestPredator[1] = nearestPredatorInfo[1];
	//		}
			//Check if Home Tree is still standing and if it isn't, choose the nearest tree that isn't a home tree as the new home tree
			if (grid.floraGrid[home[0]][home[1]] == null || (grid.floraGrid[home[0]][home[1]] != null && grid.floraGrid[home[0]][home[1]].getDisplay(grid) != "H")) {
	//			System.out.println("I need a new home");
				home = findNewHome(grid, fieldOfView, x, y, "T");
				if (home[0] == x && home[1] == y) {
					homeless = true;
				}
				else {
					planTripHome();
				}
			}
			
			// if we see a predator and we are not homeless we are going to decide whether we are closer to our home, or the predator is.
			//In the former case we run home. In the latter case, we find a new home or become homeless.
			if (seesPredator && !homeless) {
				if (judgeDistance(x,y,home[0],home[1]) >= judgeDistance(nearestPredator[0], nearestPredator[1], home[0], home[1])) {
					home = findNewHome(grid, fieldOfView, x, y, "T");
					//if no home can be found, stress causes increase in hunger
					if (home[0] == x && home[1] == y) {
						homeless = true;
					}
					else {
						int[] planTrip = planTrip(x, y, home[0], home[1]);
						int measure = planTrip.length;
						for (int i = 0; i < measure; i++) {
							plan[i] = planTrip[i];
						}
						int emptySteps = plan.length - measure;
						for (int i = 0; i < emptySteps; i++) {
							plan[i+2*measure] = 5;
						}
	//					printPlan();
					}
				}
			}
			
			//Do a poop
			if (age % 5 == 0) {
				if (vita > 5) {
					vita -= 5;
					grid.tileGrid[x][y].vita += 5;
				}
				else {
					System.out.println("I have starved to death1");
					System.out.println(vita);
					if (!homeless) {
						grid.floraGrid[home[0]][home[1]].isHome = false;
					}
					grid.faunaGrid[x][y] = new AnimalDetritus(x, y, vita);
					return;
				}
			}
			
			
	//		THE PLAN
	//		if we are home and we see food and (we're at least 10 hungry, and we don't see a predator) OR (we are at least 90 hungry), we formulate a plan to get food and return home
	//		we follow this plan unless we become homeless or we see a predator
	//		if we see a predator we run home
	//		if we are homeless, we simply go from food to food irrespective of predators
			
	//		//if we're home and we're over 7 vita, or we're less than 90 hungry and we see a predator, we plan to stay home
			if ((!pregnant && vita > 30) || (!homeless && atHome && (vita > 7 && seesPredator))) {
	//			System.out.println("I'm going to stay home");
				for (int i = 0; i < plan.length; i++) {
					plan[i] = 5;
				}
	//			printPlan();
			}
			
			//if we are home and we see food and (we don't see a predator) OR (we are at least 90 hungry), we formulate a plan to get food
			if (!homeless && atHome && seesFood && vita < 30 && (!seesPredator || vita < 7)) {
	//			System.out.println("I'm going to go get food");
				int[] planTrip = planTrip(x, y, nearestFood[0], nearestFood[1]);
				int measure = planTrip.length;
				for (int i = 0; i < measure; i++) {
					plan[i] = planTrip[i];
					plan[i+measure] = (planTrip[i]+2)%4;
				}
				int emptySteps = plan.length - 2*measure;
				for (int i = 0; i < emptySteps; i++) {
					plan[i+2*measure] = 5;
				}
	//			printPlan();
			}
			
			//if we see a predator we run home
			if (!homeless && seesPredator) {
				planTripHome();
			}
			
			//if we are homeless, we simply go from food to food irrespective of predators
			if (homeless) {
	//			System.out.println("I'm homeless so I'm going to go get food");
				int[] planTrip = planTrip(x, y, nearestFood[0], nearestFood[1]);
				int measure = planTrip.length;
				for (int i = 0; i < measure; i++) {
					plan[i] = planTrip[i];
				}
				int emptySteps = plan.length - measure;
				for (int i = 0; i < emptySteps; i++) {
					plan[i+measure] = 5;
				}
	//			printPlan();
			}
			
			//if we're not home, but our plan is all 5s, then we should head home
			if (!homeless && !atHome) {
				int count = 0;
				for (int i = 0; i < plan.length; i++) {
					if (plan[i] == 5) {
						count++;
					}
				}
				if (count == plan.length) {
					System.out.println("I'm stuck"+vita);
					planTripHome();
				}
			}
			
			//If our vita > 35, we will make a new squirrel, and our vita will decrease by birthVita
			//find new tree to give birth
			if (pregnant || vita > 35) {
				pregnant = true;
				System.out.println("I'm pregnant");
				int[] nearestTreeInfo = lookForNearest(grid, fieldOfView, "T");
				if (nearestTreeInfo[2] != 100 && nearestTreeInfo[2] != 0) {
					System.out.println("I'm going to a tree");
					int[] planTrip = planTrip(x, y, nearestTreeInfo[0], nearestTreeInfo[1]);
					int measure = planTrip.length;
					for (int i = 0; i < measure; i++) {
						plan[i] = planTrip[i];
					}
					int emptySteps = plan.length - measure;
					for (int i = 0; i < emptySteps; i++) {
						plan[i+measure] = 5;
					}
				}
			}
			//once in new tree, give birth
			if (pregnant && grid.floraGrid[x][y] != null && grid.floraGrid[x][y].getDisplay(grid) == "T") {
				inLabour = true;
				planTripHome();
			}
			
			age++;
			//Have we starved to death?
			if (vita <= 5) {
				System.out.println("I have starved to death2");
				System.out.println(vita);
				if (!homeless) {
					grid.floraGrid[home[0]][home[1]].isHome = false;
				}
				grid.faunaGrid[x][y] = new AnimalDetritus(x, y, vita);
				return;
			}
			
			//Execute the plan by making one step and then shifting the other steps forward
			switch (plan[0]) {
			case 0:
//				System.out.println("I'm moving left");
				moveTo(grid, x, y, x, y-1);
				break;
			case 1:
//				System.out.println("I'm moving up");
				moveTo(grid, x, y, x-1, y);
				break;
			case 2:
//				System.out.println("I'm moving right");
				moveTo(grid, x, y, x, y+1);
				break;
			case 3:
//				System.out.println("I'm moving down");
				moveTo(grid, x, y, x+1, y);
				break;
			case 5:
//				System.out.println("I'm staying put");
				updatePlan();
				break;
			default:
				System.out.println("plan broke");
				break;
			}
		}
		cellAge++;

//		printPlan();

//		System.out.println(vita);
	}
	private void printPlan() {
		System.out.println("My plan is:");
		for (int i = 0; i < plan.length; i++) {
			System.out.println(plan[i]);
		}
	}
	private void planTripHome() {
		int[] planTrip = planTrip(x, y, home[0], home[1]);
		int measure = planTrip.length;
		for (int i = 0; i < measure; i++) {
			plan[i] = planTrip[i];
		}
		int emptySteps = plan.length - measure;
		for (int i = 0; i < emptySteps; i++) {
			plan[i+measure] = 5;
		}
		System.out.println("I am going home now");
	}
	
	private void moveTo(Grid grid, int x, int y, int destX, int destY) {
		boolean dirshn = false;
		if (destX < x || destY < y) {
			dirshn = true;
		}
		if (destX < 0 || destY < 0) {
			System.out.println("LET ME OOOUT OF HEEEERRRRREEEE!!!!!!!!!");
			updatePlan();
			return;
		}
		if (grid.faunaGrid[destX][destY] != null && (grid.faunaGrid[destX][destY].getDisplay(grid) == "S" || grid.faunaGrid[destX][destY].getDisplay(grid) == "$" || grid.faunaGrid[destX][destY].getDisplay(grid) == "D")) {
			if (halted) {
				planTrip(x, y, home[0], home[1]);
			}
			halted = true;
			return ;
		}
		if (grid.floraGrid[destX][destY] != null && grid.floraGrid[destX][destY].getDisplay(grid) == "s") {
//			System.out.println("I'm eating");
			if (inLabour) {
				grid.faunaGrid[destX][destY] = new Squirrel(destX, destY, age, (vita - birthVita + grid.floraGrid[destX][destY].vita), nearestFood, home, plan, dirshn);
				grid.faunaGrid[x][y] = new Squirrel(x, y);
				inLabour = false;
				pregnant = false;
			}
			else {
				grid.faunaGrid[destX][destY] = new Squirrel(destX, destY, age, (vita + grid.floraGrid[destX][destY].vita), nearestFood, home, plan, dirshn);
				grid.faunaGrid[x][y] = null;
			}
			grid.floraGrid[destX][destY] = null;
			updatePlan();
			halted = false;
		}
		else {
			if (inLabour) {
				grid.faunaGrid[destX][destY] = new Squirrel(destX, destY, age, (vita - birthVita), nearestFood, home, plan, dirshn);
				grid.faunaGrid[x][y] = new Squirrel(x, y);
				inLabour = false;
				pregnant = false;
			}
			else {
				grid.faunaGrid[destX][destY] = new Squirrel(destX, destY, age, vita, nearestFood, home, plan, dirshn);
				grid.faunaGrid[x][y] = null;
			}
			updatePlan();
			halted = false;
		}
	}
	private void updatePlan() {
		for (int i = 0; i < plan.length-1; i++) {
			plan[i] = plan[i+1];
		}
		plan[plan.length-1] = 5;
	}
	
}
