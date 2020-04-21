package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import grid.Grid;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Imager {
//	Initialising the images
	private static Image dirtImage;
	private static Image dirtAnimalDetritusImage;
	private static Image dirtPlantDetritusImage;
	private static Image dirtPlantDetritusSquirrelImage;
	private static Image dirtSeedImage;
	private static Image dirtSquirrelImage;
	private static Image earthImage;
	private static Image earthAnimalDetritusImage;
	private static Image earthPlantDetritusImage;
	private static Image earthPlantDetritusSquirrelImage;
	private static Image earthSeedImage;
	private static Image earthSquirrelImage;
	private static Image treeImage;
	private static Image treeHomeImage;
	private static Image treeHomeSquirrelImage;
	private static Image treeSquirrelImage;
	public static void initImager() throws FileNotFoundException {
//		Connecting images to sprites
	    dirtImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/dirt.jpg"));  
	    dirtAnimalDetritusImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/dirtAnimalDetritus.jpg"));  
	    dirtPlantDetritusImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/dirtPlantDetritus.jpg"));  
	    dirtPlantDetritusSquirrelImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/dirtPlantDetritusSquirrel.jpg"));  
	    dirtSeedImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/dirtSeed.jpg"));  
	    dirtSquirrelImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/dirtSquirrel.jpg"));  
	    earthImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/earth.jpg"));  
	    earthAnimalDetritusImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/earthAnimalDetritus.jpg"));  
	    earthPlantDetritusImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/earthPlantDetritus.jpg"));  
	    earthPlantDetritusSquirrelImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/earthPlantDetritusSquirrel.jpg"));  
	    earthSeedImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/earthSeed.jpg"));  
	    earthSquirrelImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/earthSquirrel.jpg"));  
	    treeImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/tree.jpg"));  
	    treeHomeImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/treeHome.jpg"));  
	    treeHomeSquirrelImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/treeHomeSquirrel.jpg"));  
	    treeSquirrelImage = new Image(new FileInputStream("C://Users/David Lucien Cooper/eclipse-workspace/BoardGameGUI/src/assets/treeSquirrel.jpg"));
	}
	private static Image image(Grid grid, int x, int y) {
//		boolean dirt;
		boolean fauna;
		boolean flora;
		String plant;
//		String animal;
		
//		if (grid.tileGrid[x][y].getDisplay(grid) == "d") {
//			dirt = true;
//		} else {
//			dirt = false;
//		}
		
		if (grid.faunaGrid[x][y] != null) {
			fauna = true;
//			animal = grid.faunaGrid[x][y].getDisplay(grid);
		} else {
			fauna = false;
//			animal = null;
		}
		
		if (grid.floraGrid[x][y] != null) {
			flora = true;
			plant = grid.floraGrid[x][y].getDisplay(grid);
		} else {
			flora = false;
			plant = null;
		}
		
		if (flora) {
			//Cases of trees
			if (plant == "T") {
				if (fauna) {
					return squirrelOrNo(grid, x, y, treeSquirrelImage, treeImage);
				} else {
					return treeImage;
				}
			} else if (plant == "H") {
				if (fauna) {
					return squirrelOrNo(grid, x, y, treeHomeSquirrelImage, treeHomeImage);
				} else {
					return treeHomeImage;
				}
			} else
			//Cases of plant detritus
			if (plant == "D") {
				if (fauna) {
					return squirrelOrNo(grid, x, y, dirtOrEarth(grid, x, y, dirtPlantDetritusSquirrelImage, earthPlantDetritusSquirrelImage), dirtOrEarth(grid, x, y, dirtPlantDetritusImage, earthPlantDetritusImage));
				} else {
					return dirtOrEarth(grid, x, y, dirtPlantDetritusImage, earthPlantDetritusImage);
				}
			} else
			//Cases of seeds
			if (plant == "s") {
				return dirtOrEarth(grid, x, y, dirtSeedImage, earthSeedImage);
			}
		} else if (fauna) {
			return squirrelOrNo(grid, x, y, dirtOrEarth(grid, x, y, dirtSquirrelImage, earthSquirrelImage), dirtOrEarth(grid, x, y, dirtAnimalDetritusImage, earthAnimalDetritusImage));
		} else {
//			System.out.println("returning dirt or earth");
			return dirtOrEarth(grid, x, y, dirtImage, earthImage);
		}
		System.out.println("I found no images");
		return null;
	}
	
	public static ImageView imageView (Grid grid, int x, int y) {
		ImageView picture = new ImageView(image(grid, x, y));
		return picture;
	}
	
	private static Image dirtOrEarth(Grid grid, int x, int y, Image dirt, Image earth) {
		if (grid.tileGrid[x][y].getDisplay(grid) == "d") {
			return dirt;
		} else if (grid.tileGrid[x][y].getDisplay(grid) == "e") {
			return earth;
		} else {
			System.out.println("Not dirt and not Earth");
			return null;
		}
	}
	
	private static Image squirrelOrNo(Grid grid, int x, int y, Image squirrel, Image noSquirrel) {
		if (grid.faunaGrid[x][y].getDisplay(grid) == "S") {
			return squirrel;
		} else {
			return noSquirrel;
		}
	}
}
