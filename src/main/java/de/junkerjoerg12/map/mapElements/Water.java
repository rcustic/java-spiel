package de.junkerjoerg12.map.mapElements;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.junkerjoerg12.Game;

public class Water extends MapElement {
    public static ArrayList<BufferedImage> images = new ArrayList<>();//müssen immer und auch in allen anderen Klassen, die MapElement extenden public sein, weil Mapelement auch drauf zugreift
    public  static BufferedImage imageToDisplay;
    public static int imageIndex;// the index of the Image currently displayed

    public Water(Game game) {
        super(game);
        if (Water.images.size() == 0) {
            try {
                Water.images.add(ImageIO
                        .read(new File(Paths.get("src", "main", "resources", "assets", "wasser1.png").toString())));
                Water.images.add(ImageIO
                        .read(new File(Paths.get("src", "main", "resources", "assets", "wasser2.png").toString())));
                Water.images.add(ImageIO
                        .read(new File(Paths.get("src", "main", "resources", "assets", "wasser3.png").toString())));
                Water.imageToDisplay = images.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public static void switchImage() {
        if (imageIndex == 3) {
            Water.imageToDisplay = images.get(imageIndex = 0);
        }
        else {
            Water.imageToDisplay = images.get(imageIndex++);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g, this.getClass());
    }

}
