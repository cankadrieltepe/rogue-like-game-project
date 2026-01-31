package ui;

import domain.Game;
import domain.GameSaveLoader;
import domain.level.GridDesign;

import javax.swing.JFrame;

import controllers.BuildingModeHandler;

//creates buildingmodehandler
//creates pages

public class PageManager {
	
	private static PageManager instance;
	private JFrame mainFrame;
	
	public PageManager() {
		
		mainFrame = new JFrame("Rokue-like Game");
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        
        
        }
	
	public static PageManager getInstance() {
		if (instance == null) {
            instance = new PageManager();
        }
        return instance;
	}
 	
	public void showPage(Page page) {
		
		mainFrame.setContentPane(page);
		mainFrame.pack();
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public void showMainMenuPage() {showPage(new MainMenuPage());}

	public void showGameSelectionPage() {
		int games = GameSaveLoader.getInstance().readGameSaves();
		showPage(new GameSelectionPage(games));
	}

	public void showGameOverPage() {showPage(new GameOverPage());}

	public void showWinGamePage() {showPage(new WinGamePage());}
	
    public void showBuildingModePage() {
		BuildingModeHandler bmh = BuildingModeHandler.recreateBuildingModeHandler();
    	showPage(new BuildModePage(bmh)); }

	public void showPlayModePage(GridDesign[] gridDesigns) {
		Game.getInstance().initPlayMode(gridDesigns);
		showPage(new PlayModePage(true));
	}

	public void continuePlayModePage() {
		showPage(new PlayModePage(false));
	}

    public void showFrame() {
        mainFrame.setVisible(true);
    }

 	

}
