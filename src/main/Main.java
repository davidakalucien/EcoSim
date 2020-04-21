package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import graph.Graph;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import grid.Grid;
import grid.GridRecord;

public class Main extends Application {
    
	public static Grid grid = new Grid();
	private static Scanner scanner = new Scanner(System.in);
	public static GridPane board1;
	public static GridPane board2;
	public static Scene land;
	public static Scene worms;
	public static Button play;
	public static Button viewSwitch;
	public static Button pause;
	public static Button graph;
	public static Button game;
	public static TextField repeatNum;
	public static boolean showWorms = false;
	public static int displaySize = 20;
	public static GridRecord history = new GridRecord();
	public static int tickCount = 0;
	public static Timeline playFunction;
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		Imager.initImager();
		playFunction = new Timeline(new KeyFrame(Duration.seconds(0.1f), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
            	grid.tick();
				history.addTick(grid);
        		tickCount++;
		    	try {
					setGridText();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}));
		playFunction.setCycleCount(Timeline.INDEFINITE);
		Application.launch(args);
		
	}
	
	public void start(Stage stage) throws FileNotFoundException {
		int thisBig;
		if (grid.gridSize < 20) {
			thisBig = 20;
		} else {
			thisBig = grid.gridSize;
		}
		board1 = new GridPane();
        board1.setMinSize(displaySize*thisBig+100, displaySize*thisBig+100);
//        board1.setVgap(5);
//        board1.setHgap(5);
        board1.setAlignment(Pos.CENTER);
        board1.setPadding(new Insets(10,10,10,10));
        for (int i = 0; i < grid.gridSize; i++) {
        	board1.getColumnConstraints().add(new ColumnConstraints(displaySize));
    		board1.getRowConstraints().add(new RowConstraints(displaySize));
        }
        board1.getColumnConstraints().add(new ColumnConstraints(100));
        
		board2 = new GridPane();
        board2.setMinSize(displaySize*thisBig+100, displaySize*thisBig+100);
//        board2.setVgap(5);
//        board2.setHgap(5);
        board2.setAlignment(Pos.CENTER);
        board2.setPadding(new Insets(10,10,10,10));
        for (int i = 0; i < grid.gridSize; i++) {
        	board2.getColumnConstraints().add(new ColumnConstraints(displaySize));
    		board2.getRowConstraints().add(new RowConstraints(displaySize));
        }
        board2.getColumnConstraints().add(new ColumnConstraints(100));
        
        //BUTTON FOR SWITCHING BETWEEN WORMS AND LAND
        viewSwitch = new Button("Switch view");
        
        EventHandler<ActionEvent> switchViews = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
        		if (showWorms) {
        			showWorms = false;
        			stage.setScene(land);
        		}
        		else {
        			showWorms = true;
        			stage.setScene(worms);
        		}
        		try {
					setGridText();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	stage.show();
            } 
        }; 
  
        // when button is pressed 
        viewSwitch.setOnAction(switchViews); 
        
      //BUTTONS FOR PLAY AND PAUSE
        play = new Button("Play");
        
        EventHandler<ActionEvent> loopAhead = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
				playFunction.play();
            } 
        }; 
        pause = new Button("Pause");
        
        EventHandler<ActionEvent> stopLooping = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
				playFunction.stop();
            } 
        }; 
  
        // when buttons are pressed 
        play.setOnAction(loopAhead); 
        pause.setOnAction(stopLooping);
        
        //GRAPH BUTTONS 
        graph = new Button("Graph");
        game = new Button("Return");
        
        EventHandler<ActionEvent> showGraph = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
				Graph chart = new Graph(tickCount, tickCount, history.returnStats(tickCount, tickCount), grid);
            	try {
					chart.start(stage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        };
        EventHandler<ActionEvent> showGame = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
    			stage.setScene(land);
    			stage.show();
            } 
        }; 
  
        // when buttons are pressed
        graph.setOnAction(showGraph);
        game.setOnAction(showGame);
        
        setGridText();
		
		land = new Scene(board1);
		worms = new Scene(board2);
		if (showWorms) {
			stage.setScene(worms);
		}
		else {
			stage.setScene(land);			
		}
		stage.setTitle("The BEST game evar");
		stage.show();
	}
	
	public static void setGridText() throws FileNotFoundException {
		
		board1.getChildren().clear();
		board2.getChildren().clear();
		String[][] mainGrid = grid.displayGrid();
		for (int rowIndex = 0; rowIndex < grid.gridSize; rowIndex++) {
			
			for (int columnIndex = 0; columnIndex < grid.gridSize; columnIndex++) {
				if (showWorms) {
					Label label = new Label(mainGrid[rowIndex+grid.gridSize][columnIndex]);
					board2.add(label, columnIndex, rowIndex);
				}
				else {
//					Label label = new Label(mainGrid[rowIndex][columnIndex]);
//				    Setting the image view
				    ImageView imageView = Imager.imageView(grid, columnIndex, rowIndex); 
					board1.add(imageView, columnIndex, rowIndex);
				}
			}
		}
		if (showWorms) {
			board2.add(viewSwitch, grid.gridSize, 0);
			Label ticks = new Label("Ticks: "+tickCount);
			board2.add(ticks,  grid.gridSize,  1);
			for (int i = 0; i < grid.getFacts().length; i++) {
				switch (i) {
				case 0:
					Label label0 = new Label("Vita:");
					Label value0 = new Label(String.valueOf(grid.facts[i]));
					board2.add(label0, grid.gridSize, 2);
					board2.add(value0, grid.gridSize, 3);
					break;
				case 1:
					Label label1 = new Label("Squirrels:");
					Label value1 = new Label(String.valueOf(grid.facts[i]));
					board2.add(label1, grid.gridSize, 4);
					board2.add(value1, grid.gridSize, 5);
					break;
				case 2:
					Label label2 = new Label("Trees:");
					Label value2 = new Label(String.valueOf(grid.facts[i]));
					board2.add(label2, grid.gridSize, 6);
					board2.add(value2, grid.gridSize, 7);
					break;
				case 3:
					Label label3 = new Label("Worms:");
					Label value3 = new Label(String.valueOf(grid.facts[i]));
					board2.add(label3, grid.gridSize, 8);
					board2.add(value3, grid.gridSize, 9);
					break;
				case 4:
					Label label4 = new Label("Seeds:");
					Label value4 = new Label(String.valueOf(grid.facts[i]));
					board2.add(label4, grid.gridSize, 10);
					board2.add(value4, grid.gridSize, 11);
					break;
				case 5:
					Label label5 = new Label("Detritus:");
					Label value5 = new Label(String.valueOf(grid.facts[i]));
					board2.add(label5, grid.gridSize, 12);
					board2.add(value5, grid.gridSize, 13);
					break;
				default:
					System.out.println("Facts not printing right");
				}
			}
			board2.add(play, grid.gridSize, 14);
			board2.add(pause, grid.gridSize, 16);
		}
		else {
			board1.add(viewSwitch, grid.gridSize, 0);
			Label ticks = new Label("Ticks: "+tickCount);
			board1.add(ticks,  grid.gridSize,  1);
			for (int i = 0; i < grid.getFacts().length; i++) {
				switch (i) {
				case 0:
					Label label0 = new Label("Vita:");
					Label value0 = new Label(String.valueOf(grid.facts[i]));
					board1.add(label0, grid.gridSize, 2);
					board1.add(value0, grid.gridSize, 3);
					break;
				case 1:
					Label label1 = new Label("Squirrels:");
					Label value1 = new Label(String.valueOf(grid.facts[i]));
					board1.add(label1, grid.gridSize, 4);
					board1.add(value1, grid.gridSize, 5);
					break;
				case 2:
					Label label2 = new Label("Trees::");
					Label value2 = new Label(String.valueOf(grid.facts[i]));
					board1.add(label2, grid.gridSize, 6);
					board1.add(value2, grid.gridSize, 7);
					break;
				case 3:
					Label label3 = new Label("Worms:");
					Label value3 = new Label(String.valueOf(grid.facts[i]));
					board1.add(label3, grid.gridSize, 8);
					board1.add(value3, grid.gridSize, 9);
					break;
				case 4:
					Label label4 = new Label("Seeds:");
					Label value4 = new Label(String.valueOf(grid.facts[i]));
					board1.add(label4, grid.gridSize, 10);
					board1.add(value4, grid.gridSize, 11);
					break;
				case 5:
					Label label5 = new Label("Detritus:");
					Label value5 = new Label(String.valueOf(grid.facts[i]));
					board1.add(label5, grid.gridSize, 12);
					board1.add(value5, grid.gridSize, 13);
					break;
				default:
					System.out.println("Facts not printing right");
				}
			}
			board1.add(play, grid.gridSize, 14);
			board1.add(pause, grid.gridSize, 16);
			board1.add(graph, grid.gridSize, 18);
		}
		
		
	}

}
