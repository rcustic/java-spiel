package de.junkerjoerg12.character;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.junkerjoerg12.Game;

public class Player extends Entity {

    public Player(double acceleration, Game game) {
        super(acceleration, game);
        this.setBounds(300, 300, 50, 50);
        try {
            images.add(ImageIO.read(new File("src\\main\\resources\\assets\\rsz_character.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Player(Game game) {
        this(2, game);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.drawImage(images.get(0), x, y, null);
    }

}
