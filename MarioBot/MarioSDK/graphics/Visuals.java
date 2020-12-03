package MarioSDK.graphics;

import java.awt.Graphics;

public abstract class Visuals {
    public boolean visible;
    public float alpha;
    public int originX, originY;
    public boolean flipX, flipY;
    public int width, height;

    public Visuals() {
        this.visible = true;
        this.alpha = 1;
        this.originX = this.originY = 0;
        this.flipX = this.flipY = false;
        this.width = this.height = 32;
    }

    public abstract void render(Graphics og, int x, int y);
}
