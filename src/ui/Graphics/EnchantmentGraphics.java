package ui.Graphics;

import domain.collectables.Enchantment;
import domain.collectables.EnchantmentType;
import domain.factories.EnchantmentFactory;
import listeners.EnchantmentListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class EnchantmentGraphics extends EntityGraphics implements EnchantmentListener {

    private static EnchantmentGraphics instance;
    private int size;
    private HashMap<EnchantmentType, BufferedImage> enchantmentImages;
    private LinkedList<Enchantment> enchantments;

    public EnchantmentGraphics(int size){
        initalizeSpriteMap();
        this.size = size;
        this.enchantments = new LinkedList<>();
        this.subscribe(EnchantmentFactory.getInstance());
    }

    public static EnchantmentGraphics getInstance(int size) {
        if (instance == null) {
            instance = new EnchantmentGraphics(size);
        }
        return instance;
    }

    public void draw(Graphics g) {
        for (Enchantment enchantment: enchantments) {
            g.drawImage(enchantmentImages.get(enchantment.getType()), enchantment.getLocation().getX()*36,(15 - enchantment.getLocation().getY())*36 , size, size,null);
        }
    }

    public void subscribe(EnchantmentFactory ef) {
        ef.addListener(this);
    }

    @Override
    public void onCreationEvent(Enchantment enchantment) {
        enchantments.add(enchantment);
    }

    @Override
    public void onRemovalEvent(Enchantment enchantment) {
        enchantments.remove(enchantment);
    }

    @Override
    public void onClearEvent() {
        this.enchantments.clear();
    }

    @Override
    public void onNewGameEvent(){
        this.enchantments = new LinkedList<>();
    }

    @Override
    public void onGameOverEvent(){
        enchantments = null;
    }

    private void initalizeSpriteMap()
    {
        enchantmentImages = new HashMap<>();
        try {
            enchantmentImages.put(EnchantmentType.Life, ImageIO.read(new File("src/assets/extraLife.png")));
            enchantmentImages.put(EnchantmentType.Cloak, ImageIO.read(new File("src/assets/cloak.png")));
            enchantmentImages.put(EnchantmentType.Reveal, ImageIO.read(new File("src/assets/reveal.png")));
            enchantmentImages.put(EnchantmentType.Luring, ImageIO.read(new File("src/assets/lure.png")));
            enchantmentImages.put(EnchantmentType.Time, ImageIO.read(new File("src/assets/extraTime.png")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
