package ui;

import javax.swing.*;

import controllers.BuildingModeHandler;
import domain.level.GridDesign;
import domain.objects.ObjectType;
import domain.util.Coordinate;
import domain.Textures;
import listeners.BuildModeListener;
import ui.Graphics.TileSetImageGetter;
import ui.Swing.Panels.HallPanelHolder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class BuildModePage extends Page implements ActionListener, BuildModeListener {


	private BuildingModeHandler buildingModeHandler;

    private JButton exitButton;

    private JButton fillButton;

    private JButton contButton;

    private double scaleFactor = 0.86;

    private JPanel[] hallPanels;

    private JPanel objectChooserPanel;

    public BuildModePage(BuildingModeHandler buildingModeHandler) {
        super();

        buildingModeHandler.addListener(this);

        this.hallPanels = new JPanel[4];
        this.buildingModeHandler = buildingModeHandler;

        initUI();
    }

    protected void initUI() {

        this.setPreferredSize(new Dimension((int)(1235*scaleFactor), (int)(931*scaleFactor)));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setLayout(null);


        this.objectChooserPanel = new JPanel();
        this.objectChooserPanel.setBounds(746, 188, 123, 470);
        this.objectChooserPanel.setLayout(new GridLayout(0, 2, 5, 5)); // 0 rows, 2 columns, 5px horizontal and vertical gap
        this.objectChooserPanel.setOpaque(false);
        this.add(objectChooserPanel);

        contButton = new JButton("Start Game");
        contButton.setFont(new Font("Gongster",Font.BOLD,20));
        contButton.setBackground(new Color(123, 87, 126));;
        contButton.setForeground(Color.BLACK);
        contButton.setOpaque(true);
        contButton.setBounds(898, 330, 150, 40);
        contButton.addActionListener(this);
        contButton.setFocusable(true);
        this.add(contButton);


        fillButton = new JButton("Fill Random");
        fillButton.setFont(new Font("Gongster",Font.BOLD,20));
        fillButton.setBackground(new Color(123, 87, 126));;
        fillButton.setForeground(Color.BLACK);
        fillButton.setOpaque(true);
        fillButton.setBounds(898, 400, 150, 40);
        fillButton.addActionListener(this);
        fillButton.setFocusable(true);
        this.add(fillButton);

        ImageIcon exitImage = new ImageIcon("src/assets/exit.png");
        Image image1 = exitImage.getImage().getScaledInstance(30, 28, Image.SCALE_SMOOTH);
        ImageIcon resizedExitImage = new ImageIcon(image1);
        exitButton = new JButton(resizedExitImage);
        exitButton.setOpaque(true);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setBounds(730, 74, 150, 40);
        exitButton.addActionListener(this);
        exitButton.setFocusable(true);
        this.add(exitButton);

        //dynamically create object buttons
        createObjectButtons("src/assets/build_mode_assets");

        for(int i = 0; i < 4; i++) {
            this.hallPanels[i] = new HallPanel(this.buildingModeHandler, i);
            this.hallPanels[i].setFocusable(true);
            this.hallPanels[i].requestFocusInWindow();
            this.hallPanels[i].setOpaque(false);
            this.hallPanels[i].setSize(hallPanels[i].getPreferredSize());
            this.add(hallPanels[i]);
        }

        hallPanels[0].setLocation(421, 97);
        hallPanels[1].setLocation(91, 97);
        hallPanels[2].setLocation(91, 451);
        hallPanels[3].setLocation(421, 451);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage floor = TileSetImageGetter.getInstance().getFloorImage();
        g.drawImage(floor,0, 0,(int)(1265*scaleFactor), (int)(931*scaleFactor), null);
        BufferedImage buildM = Textures.getSprite("buildmode");
        g.drawImage(buildM,0, 0,(int)(1115*scaleFactor), (int)(931*scaleFactor), null);

        remainingCountText(g);
    }

    private void remainingCountText(Graphics g) {
        g.setColor(new Color(164, 167, 182));
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.drawString("Remaining Object: " + buildingModeHandler.getRemainingObject(0), 602, 388);
        g.drawString("Remaining Object: " + buildingModeHandler.getRemainingObject(1), 275, 388);
        g.drawString("Remaining Object: " + buildingModeHandler.getRemainingObject(2), 275, 745);
        g.drawString("Remaining Object: " + buildingModeHandler.getRemainingObject(3), 602, 745);
    }

    private void createObjectButtons(String folderPath){
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        int[] imageScaleArr = {-1, -1, -1, -1, 24, 24, 24, 16, 16, 16};
        int index = 0;
        for (File file : files)
        {
            String fileName = file.getName();
            String baseName = fileName.substring(2, fileName.lastIndexOf("."));
            String enumName = baseName.toUpperCase(); // Assuming ObjectType names match file names in uppercase

            ObjectType objectType = null;
            try {
                objectType = ObjectType.valueOf(enumName);
            } catch (IllegalArgumentException ex) {
                // If there's no matching enum, you may want to skip or log a warning
                System.err.println("No matching ObjectType for file: " + fileName);
                continue;
            }

            JButton objButton = new JButton();
            objButton.setAlignmentY(Component.TOP_ALIGNMENT);

            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image originalImage = icon.getImage();
            Image scaledImage = originalImage.getScaledInstance(
                    imageScaleArr[index] == -1 ? icon.getIconWidth() : imageScaleArr[index],
                    imageScaleArr[index] == -1 ? icon.getIconHeight() : imageScaleArr[index],
                    Image.SCALE_DEFAULT
            );
            objButton.setIcon(new ImageIcon(scaledImage));

            objButton.setContentAreaFilled(false); // Remove background color
            objButton.setBorderPainted(false);    // Remove the border

            objButton.setActionCommand(enumName);
            objButton.addActionListener(this);

            this.objectChooserPanel.add(objButton);
            index++;
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == exitButton){
            PageManager.getInstance().showMainMenuPage();
        }
        else if (e.getSource() == contButton) {
            buildingModeHandler.startGame();
        }

        else if (e.getSource() == fillButton) {
            buildingModeHandler.fillHallsRandomly();
            repaint();
        }
        else
        {
            String command = e.getActionCommand();
            if (command != null) {
                try {
                    ObjectType selectedObject = ObjectType.valueOf(command);
                    buildingModeHandler.setSelectedObject(selectedObject);
                    System.out.println(selectedObject + " selected.");
                } catch (IllegalArgumentException ex) {
                    // If for some reason we fail here, just ignore
                    System.err.println("Unknown object type selected: " + command);
                }
            }

        }
    }

    @Override
    public void onObjectPlacementOrRemovalEvent() {
        this.revalidate();
        this.repaint();
    }

    class HallPanel extends JPanel implements MouseListener {

        public final int tileSizeX = 18;
        public final int tileSizeY = 15;
        public final int maxScreenCol = 16;
        public final int maxScreenRow = 16;

        final int screenWidth = tileSizeX * maxScreenCol;
        final int screenHeight = tileSizeY * maxScreenRow;

        //private final double scale = 1.3;

        private final  BuildingModeHandler buildingModeHandler;

        //the coordinates of the tiles that the mouse is hovered on (these coordinates will be highlighted for ease of use, in build mode.)
        private int hoveredRow = -1;
        private int hoveredCol = -1;

        private int hallNumber;

        public HallPanel(BuildingModeHandler buildingModeHandler, int hallNumber) {
            this.buildingModeHandler = buildingModeHandler;
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            this.setBackground(Color.BLACK);
            this.setDoubleBuffered(true);
            this.setFocusable(true);
            addMouseListener(this);

            this.hallNumber = hallNumber;

            //highlighting hovered tiles.
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();

                    int newHoveredCol = x / tileSizeX;
                    int newHoveredRow = y / tileSizeY;

                    //only highlight when the mouse is within hall borders.
                    if (x >= 0 && x < screenWidth && y >= 0 && y < screenHeight &&
                            newHoveredRow >= 0 && newHoveredRow < maxScreenRow &&
                            newHoveredCol >= 0 && newHoveredCol < maxScreenCol) {

                        //don't need to repaint when the mouse is on the same tile.
                        if (newHoveredRow != hoveredRow || newHoveredCol != hoveredCol) {
                            hoveredRow = newHoveredRow;
                            hoveredCol = newHoveredCol;
                            repaint(); //repaint for highlighting
                        }
                    } else {
                        // If outside hall bounds, reset highlight and repaint
                        if (hoveredRow != -1 || hoveredCol != -1) {
                            hoveredRow = -1;
                            hoveredCol = -1;
                            repaint();
                        }
                    }
                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Ensures the panel is properly rendered
            GridDesign currentHall = buildingModeHandler.getHall(hallNumber);
            if(currentHall != null) {
                drawPlacedObjects(g,currentHall);
            }
            if (hoveredRow >= 0 && hoveredCol >= 0 && hoveredRow < maxScreenRow && hoveredCol < maxScreenCol) {
                Color prevColor = g.getColor();
                g.setColor(new Color(255, 255, 255, 64)); //transparent white (highlight color.)
                g.fillRect(hoveredCol * tileSizeX, hoveredRow * tileSizeY, tileSizeX, tileSizeY);
                g.setColor(prevColor); //back to original color.
            }

        }

        private void drawPlacedObjects(Graphics g, GridDesign hall) {
            ObjectType[][] grid = hall.getGrid();
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col] != null) {

                        String objName = grid[row][col].toString().toLowerCase();
                        BufferedImage objectSprite = Textures.getSprite(objName);

                        int h = 20;
                        int w = 20;

                        // different size for column
                        if(objName.equals("column")) {
                            h = 28;
                            w = 14;
                        }
                        else if(objName.equals("skull") || objName.equals("pot")) {
                            h = 12;
                            w = 12;
                        }

                        //int scaledWidth = (int) (w * scale);
                        //int scaledHeight = (int) (h * scale);

                        int offsetX = (tileSizeX - w) / 2;
                        int offsetY = (tileSizeY- h) / 2;
                        g. drawImage(objectSprite, row*tileSizeX+offsetX, col*tileSizeY+offsetY, w, h, null);
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int row = e.getX() / tileSizeX;
            int col = e.getY() / tileSizeY;
            System.out.println ("Clicked at" + col + ", " + row);

            if(SwingUtilities.isRightMouseButton(e)) {
                if(buildingModeHandler.isObjectPresent(row,col, hallNumber)) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem removeItem = new JMenuItem("Remove Object");
                    removeItem.addActionListener(e1 -> {
                        boolean removed = buildingModeHandler.removeObjectAt(row, col, hallNumber);
                        if (removed) {
                            System.out.println("Object removed.");
                            repaint();
                        }
                    });
                    menu.add(removeItem);
                    menu.show(e.getComponent(), e.getX(), e.getY());  // Show the menu at the mouse position
                }
                repaint();
                return;
            }

            boolean placed = buildingModeHandler.placeObjectAt(row,col, hallNumber);
            if(placed) {
                repaint();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {
            hoveredRow = -1;
            hoveredCol = -1;
            repaint(); // ensure the highlight is cleared
        }
    }

}
	
    	
    
    
    
    


