package MarioSDK.effects;

import java.awt.Graphics;

import MarioSDK.component.Effects;

public class Fireball extends Effects {
    public Fireball(float x, float y) {
        super(x, y, 0, 0, 0, 0, 32, 8);
    }

    @Override
    public void render(Graphics og, float cameraX, float cameraY) {
        this.graphics.index = this.startingIndex + (8 - this.life);
        super.render(og, cameraX, cameraY);
    }
}
