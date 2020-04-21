package grid;

import java.util.ArrayList;

public abstract class Fauna extends Entity implements IDisplayable {
	public int vita;
	public int[] plan;
	public int cellAge;
	public int[] home = new int[2];
	public boolean homeless = false;
	public Entity[][] fieldOfView;
	public int sightRange;
	
	//newborn
	public Fauna(int x,int y, int sightRange, int birthVita) {
		super(x,y);
		home[0] = x;
		home[1] = y;
		this.sightRange = sightRange;
		fieldOfView = new Entity [2*sightRange+1][2*sightRange+1];
		plan = new int[4*sightRange+1];
		for(int i = 0; i < 4*sightRange+1; i++) {
			plan[i] = 5;
		}
		vita = birthVita;
	}
	
	public Fauna(int x,int y,int sightRange, int age, int vita, int[] plan) {
		super(x,y);
		this.age = age;
		this.vita = vita;
		this.sightRange = sightRange;
		fieldOfView = new Entity [2*sightRange+1][2*sightRange+1];
		this.plan = plan;
	}
	
	public Entity[][] see(Grid grid) {
//		System.out.println("I am at "+x+","+y);
//		System.out.println(sightRange);
		for (int i = -sightRange; i < (sightRange + 1); i++) {
			for (int j = -sightRange; j < (sightRange + 1); j++) {
				if (i == 0 && j == 0 ) {
					continue;
				}
				if ((i+x)<0 || (j+y)<0 || (i+x)>(grid.tileGrid.length - 1) || (j+y)>(grid.tileGrid.length - 1)) {
					fieldOfView[sightRange + i][sightRange + j] = null;
					continue;
				}
				fieldOfView[sightRange + i][sightRange + j] = grid.floraGrid[x+i][y+j];
			}
		}
		//Print the Field of View
//		for (int i = 0; i < fieldOfView.length; i++) {
//			String thisLine = "";
//			for (int j = 0; j < fieldOfView.length; j++) {
//				if (fieldOfView[i][j] == null) {
//					thisLine += " ";
//				}
//				else {
//					thisLine += fieldOfView[i][j].getDisplay(grid);
//				}
//			}
//			System.out.println(thisLine);
//		}
		
		return fieldOfView;
	}
	
	public int judgeDistance(int x1, int y1, int x2, int y2) {
		int distance = Math.abs(x2-x1) + Math.abs(y2-y1);
		return distance;
	}
	
	public int[] lookForNearest(Grid grid, Entity[][] fieldOfView, String thing) {
		ArrayList<int[]> equidistantThings = new ArrayList<int[]>();
		int[] nearestThing = {x,y};
		int range = (fieldOfView.length - 1)/2;
		double distanceToThing = 100;
		int[] arr = new int[3];
		for (int i = -range; i < range; i++) {
			for (int j = -range; j < range; j++) {
				if (fieldOfView[range+i][range+j] != null) {
					if (fieldOfView[range+i][range+j].getDisplay(grid) == thing) {
						if (distanceToThing == judgeDistance(x,y,x+i,y+j)) {
							int[] location = {x+i, y+j};
							equidistantThings.add(location);
						}
//						System.out.println("there's one!");
//						System.out.println(distanceToThing);
//						System.out.println(judgeDistance(x,y,x+i,y+j));
						//if the new object is closer than the running count, make the new object the closest. If it's equidistant, then flip a coin on whether to keep or change
						if ( (distanceToThing > judgeDistance(x,y,x+i,y+j))) {
							equidistantThings.clear();
							int[] location = {x+i, y+j};
							equidistantThings.add(location);
							distanceToThing = judgeDistance(x,y,x+i,y+j);
//							System.out.println(nearestThing[0]);
//							System.out.println(fieldOfView[range+i][range+j].x);
//							System.out.println(nearestThing[1]);
//							System.out.println(fieldOfView[range+i][range+j].y);
							if (nearestThing[0] != fieldOfView[range+i][range+j].x || nearestThing[1] != fieldOfView[4+i][4+j].y) {
								nearestThing[0] = fieldOfView[range+i][range+j].x;
								nearestThing[1] = fieldOfView[range+i][range+j].y;
//								System.out.println("New nearest "+thing+" is at "+nearestThing[0]+'.'+nearestThing[1]);
							}
						}
					}
				}
			}
		}
		int size = equidistantThings.size();
		if (size > 1) {
			double decision = Math.floor(size*Math.random());
			int decisionInt = ((int) decision);
			arr[0] = equidistantThings.get(decisionInt)[0];
			arr[1] = equidistantThings.get(decisionInt)[1];
			arr[2] = (int) distanceToThing;
		} else if (nearestThing[0] == x && nearestThing[1] == y) {
			arr[0] = nearestThing[0];
			arr[1] = nearestThing[1];
			arr[2] = 0;
//			System.out.println("I don't see it");
		} else {
			arr[0] = nearestThing[0];
			arr[1] = nearestThing[1];
			arr[2] = (int) distanceToThing;
		}
		return arr;
	}

	public int[] findNewHome(Grid grid, Entity[][] fieldOfView, int x, int y, String home) {
		int[] currHome = {x,y};
		int[] nearestHomeInfo = lookForNearest(grid, fieldOfView, home);
		//If a home is visible, make it the new home
		if (nearestHomeInfo[2] != 0 && nearestHomeInfo[2] != 100) {
			currHome[0] = nearestHomeInfo[0];
			currHome[1] = nearestHomeInfo[1];
			grid.floraGrid[currHome[0]][currHome[1]].isHome = true;
			homeless = false;
		}
		return currHome;
	}
	
//	  1
//	0 5 2
//	  3
//	BE CAREFUL THE X IS VERTICAL AND Y IS HORIZONTAL!!! SMALLER VALUES MEAN UP AND TO THE LEFT!
	public int[] planTrip(int x, int y, int destX, int destY) {
		int planIndex = 0;
		int[] plan = new int[judgeDistance(x, y, destX, destY)];
		if (destY < 0) {
			System.out.println("fuuuuuuuuuuckY "+destY);
		}
		if (destX < 0) {
			System.out.println("fuuuuuuuuuuckX "+destY);
		}
		if (x > destX) {
			for (int i = 0; i < x - destX; i++) {
				plan[planIndex] = 1;
				planIndex++;
			}
		}
		else if (x < destX) {
			for (int i = 0; i < destX - x; i++) {
				plan[planIndex] = 3;
				planIndex++;
			}
		}
		if (y > destY) {
			for (int i = 0; i < y - destY; i++) {
				plan[planIndex] = 0;
				planIndex++;
			}
		}
		else if (y < destY) {
			for (int i = 0; i < destY - y; i++) {
				plan[planIndex] = 2;
				planIndex++;
			}
		}
		return plan;
	}
}
