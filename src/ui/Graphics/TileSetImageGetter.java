package ui.Graphics;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileSetImageGetter {

    private static TileSetImageGetter instance;
    private BufferedImage tileset;
    private final int tileWidth = 32;
    private final int tileHeight = 32;

    private TileSetImageGetter() {
        try {
            // Load the tileset image
            this.tileset = ImageIO.read(new File("src/assets/dungeonTileset.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TileSetImageGetter getInstance() {
        if (instance == null) {
            instance = new TileSetImageGetter();
        }
        return instance;
    }

    public BufferedImage getImage(int imageX, int imageY) {
        return this.tileset.getSubimage(imageX * tileWidth, imageY * tileHeight, tileWidth, tileHeight);
    }

    public BufferedImage getImageCustom(int imageX, int imageY, int sizeX, int sizeY) {
        return this.tileset.getSubimage(imageX, imageY, sizeX, sizeY);
    }

    public BufferedImage getFloorImage() {
        return this.tileset.getSubimage(96,0, 12, 12);
    }

}
