package controllers;
import domain.level.GridDesign;
import domain.objects.ObjectType;
import domain.util.Coordinate;
import listeners.BuildModeListener;
import ui.BuildModePage;
import domain.*;
import ui.PageManager;

import java.util.List;

//singleton pattern is used

public class BuildingModeHandler {

	private static BuildingModeHandler instance;

	private Game game;

	private int gameHallCount = 4;

	private GridDesign[] gridDesigns = new GridDesign[gameHallCount];

	public final int[] limits = {6, 9, 13, 17};

	private ObjectType selectedObject;

	private List<BuildModeListener> listeners;

	private BuildingModeHandler() {
		this.game = Game.getInstance();
		Textures.createSprites();
		for(int i = 0; i < gridDesigns.length; i++){
			gridDesigns[i] = new GridDesign(16,16,limits[i]);
		}
		listeners = new java.util.ArrayList<>();
	}

	public static BuildingModeHandler recreateBuildingModeHandler() {
		instance = new BuildingModeHandler();
		return instance;
	}

	public static BuildingModeHandler getInstance() {
		if (instance == null) {
			instance = new BuildingModeHandler();
		}
		return instance;
	}


	public void setSelectedObject(ObjectType object) {
		this.selectedObject = object;
	}

	public ObjectType getSelectedObject(){
		return this.selectedObject;
	}

	/**
	 * Requires:
	 *  - selectedObject is not null.
	 *  - 0 <= row < gridDesigns[currentGameHall].getRowCount()
	 *  - 0 <= col < gridDesigns[currentGameHall].getColumnCount()
	 *  - grid[row][col] needs to be null.
	 * Modifies:
	 *  - gridDesigns[currentGameHall] (the current hall's grid)
	 * Effects:
	 *  - If selectedObject is null, returns false (no placement).
	 *  - If row or col are out of valid bounds or place is not empty,
	 *    placeObject() method will return false and not be placed.
	 *  - Returns true if placement is successful, false otherwise.
	 */
	public  boolean placeObjectAt(int row, int col, int currentDesign) {
		if(selectedObject == null) return false;
		GridDesign currentHall = gridDesigns[currentDesign];
		if (currentHall.placeObject(row,col,selectedObject))
		{
			notifyListeners();
			return true;
		}
		return false;
	}

	// Goes to next hall and returns if that hall is last or not.
	public void startGame() {
		if(areAllHallsComplete())
			PageManager.getInstance().showPlayModePage(gridDesigns);
	}

	public int getRemainingObject(int hallIndex) {
		int remaining = limits[hallIndex] - gridDesigns[hallIndex].getPlacedObjectCount();
		return Math.max(0, remaining);
	}

	public void fillHallsRandomly() {
		for(int i = 0; i < 4; i++) {
			int limit = limits[i] - gridDesigns[i].getPlacedObjectCount();
			if (limit <= 0) {
				continue;
			}
			int doubleBoxCount = Game.random.nextInt(limit/3 + 1);
			int chestCount = Game.random.nextInt(limit/3 + 1);
			int chestFullCount = Game.random.nextInt(limit/3 + 1);
			int boxCount = limit - doubleBoxCount - chestCount - chestFullCount;

			fillWithObjectRandomly(doubleBoxCount, i , ObjectType.DOUBLE_BOX);
			fillWithObjectRandomly(chestCount, i , ObjectType.CHEST_CLOSED);
			fillWithObjectRandomly(chestFullCount, i , ObjectType.CHEST_FULL_GOLD);
			fillWithObjectRandomly(boxCount, i , ObjectType.BOX);
		}
		notifyListeners();
	}

	private void fillWithObjectRandomly(int count, int hallIndex, ObjectType type) {
		for(int i = 0; i < count; i++) {
			int row = Game.random.nextInt(1, 15);
			int column = Game.random.nextInt(1, 15);
			while(isObjectPresent(row, column, hallIndex)) {
				row = Game.random.nextInt(1, 15);
				column = Game.random.nextInt(1, 15);
			}
			gridDesigns[hallIndex].placeObject(row, column, type);
		}
	}

	public boolean removeObjectAt(int row, int col, int currentDesign){
		GridDesign currentHall = gridDesigns[currentDesign];
		boolean res =  currentHall.removeObject(row, col);
		notifyListeners();
		return res;
	}

	public boolean areAllHallsComplete() {
		for(GridDesign hall : gridDesigns){
			if(!hall.isPlacementComplete()){
				return false;
			}
		}
		return true;
	}

	public GridDesign getHall(int hallNumber) {
		return gridDesigns[hallNumber];
	}

	public boolean isObjectPresent (int row, int col, int hallNumber) {
		GridDesign currentHall = getHall(hallNumber);
		return currentHall.isObjectPresent(row,col);
	}

	private void notifyListeners() {
		for(BuildModeListener listener : listeners) {
			listener.onObjectPlacementOrRemovalEvent();
		}
	}

	public void addListener(BuildModeListener listener) {
		listeners.add(listener);
	}

	/*
	
	public Coordinate placeObject(Coordinate mouseCoordinates, ObjectType type) {
		
		
	}

	
	public boolean validatePlacement(Coordinate coord) {
		
	}
	
	public boolean isReady() {
		
	}
	
	public Hall getNextHall() {
		
	}
	
	public void initializePlayMode() {
		
	}*/




}
