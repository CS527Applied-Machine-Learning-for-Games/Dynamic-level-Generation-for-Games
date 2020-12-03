package MarioSDK.graphics;

import java.awt.Graphics;
import java.awt.Image;

public class Images extends Visuals {
    public Image[][] sheet;
    public int index;

    public Images(Image[][] sheet, int index) {
        super();
        this.sheet = sheet;
        this.index = index;
    }

    @Override
    public void render(Graphics og, int x, int y) {
        if (!visible) return;

        int xPixel = x - originX;
        int yPixel = y - originY;
        Image image = this.sheet[index % sheet.length][index / sheet.length];

        og.drawImage(image, xPixel + (flipX ? width : 0), yPixel + (flipY ? height : 0), flipX ? -width : width, flipY ? -height : height, null);
    }

}
