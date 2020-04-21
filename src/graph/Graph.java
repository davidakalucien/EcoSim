package graph;

import grid.Grid;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import main.Main;

public class Graph extends Application {
	private String y;
	private int tickCount;
	private int depth;
	private int[][] stats;
	private String title;
	private LineChart linechart;
	private ComboBox dropdown;
	private static GridPane graph = new GridPane();
	private Scene scene;
	private static Grid grid;
	private String[] titles = {"Squirrels", "Trees", "Worms", "Seeds", "Detritus"};
	private int statSelector = 0;
	private Group group;

	public Graph(int tickCount, int depth, int[][] stats, Grid grid){
		super();
		this.tickCount = tickCount;
		this.depth = depth;
		this.stats = stats;
		this.title = "Squirrels";
		this.grid = grid;
	}
	
	private void setGraph() {
		//Defining X axis  
		NumberAxis xAxis = new NumberAxis(tickCount-depth, tickCount, 10); 
		xAxis.setLabel("Tick"); 
		        
		//Defining y axis 
		NumberAxis yAxis = new NumberAxis(0, getMaxValue(stats[statSelector]), 20); 
		yAxis.setLabel(title);
		
		linechart = new LineChart(xAxis, yAxis);
		
		XYChart.Series series = new XYChart.Series(); 
		series.setName(title);
		
		for (int tick = depth; tick > 0; tick--) {
			series.getData().add(new XYChart.Data(tickCount-tick, stats[statSelector][depth-tick]));
		}
		
		//Setting the data to Line chart    
		linechart.getData().add(series);
		
		group = new Group(linechart);
		group.getChildren().add(Main.game);
		
		System.out.println("graph set");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		int thisBig;
		if (grid.gridSize < 20) {
			thisBig = 20;
		} else {
			thisBig = grid.gridSize;
		}
		graph = new GridPane();
        graph.setMinSize(Main.displaySize*thisBig+100, Main.displaySize*thisBig+100);
        graph.setAlignment(Pos.CENTER);
        graph.setPadding(new Insets(10,10,10,10));
		graph.getRowConstraints().add(new RowConstraints(300));
		graph.getRowConstraints().add(new RowConstraints(50));
		graph.getRowConstraints().add(new RowConstraints(50));
		

		//Creating ComboBox
		dropdown = new ComboBox(FXCollections.observableArrayList(titles));
		// Create action event 
        EventHandler<ActionEvent> newSelection = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                title = (String) dropdown.getValue();
                switch (title) {
                case "Squirrels":
                	statSelector = 0;
                	break;
                case "Trees":
                	statSelector = 1;
                	break;
                case "Worms":
                	statSelector = 2;
                	break;
                case "Seeds":
                	statSelector = 3;
                	break;
                case "Detritus":
                	statSelector = 4;
                	break;
                default:
                	System.out.println("graph dropdown not working");
                	break;
                }
            	System.out.println(title);
        		graph.getChildren().remove(group);
                setGraph();
        		graph.add(group, 0, 0);
            } 
        }; 
  
        // Set on action 
        dropdown.setOnAction(newSelection);

		setGraph();

		graph.add(Main.game, 0, 1);
		graph.add(dropdown, 0, 2);

		scene = new Scene(graph, 600, 700);
		
		primaryStage.setTitle(title+" over time");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private static int getMaxValue(int[] numbers){
		  int maxValue = numbers[0];
		  for(int i=1;i < numbers.length;i++){
		    if(numbers[i] > maxValue){
		      maxValue = numbers[i];
		    }
		  }
		  return maxValue;
		}
}
